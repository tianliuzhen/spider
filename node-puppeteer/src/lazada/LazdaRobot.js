const puppeteer = require("puppeteer-extra");
// const puppeteer = require("puppeteer");
const StealthPlugin = require("puppeteer-extra-plugin-stealth");
puppeteer.use(StealthPlugin());
const TimeoutError = require("puppeteer").errors.TimeoutError;
const logger = require("tracer").colorConsole();
const util = require("util");
const TimeSleep = require("./TimeSleep");
const AbstractRobot = require("./AbstractRobot");
const GMailRobot = require("./GMailRobot");
const ProxyService = require("./ProxyService");
const SkipAccountException = require("./exception/SkipAccountException");
const {
  AlreadyRegException,
  RetryException,
} = require("./exception/Exceptions");
const DataDbService = require("./DataDbService");

async function clearLocalStorage(page) {
  await page.evaluate(() => {
    window.localStorage.clear();
  });
}

class LazdaRobot extends AbstractRobot {
  constructor(options) {
    super();
    this.options = options;
  }

  /**
   * 打开浏览器
   */
  async _createBrowser() {
    var appDir = process.cwd();
    let userDir = util.format("%s/chromedata/lazda", appDir);
    this.browser = await puppeteer.launch({
      args: [
        "--proxy-server=" + this.options.PROXY,
        "--lang=en-US",
        "--disable-extensions",
        "--hide-scrollbars",
        "--start-maximized",
        "--disable-bundled-ppapi-flash",
        "--mute-audio",
        "--no-sandbox",
        "--disable-setuid-sandbox",
        "--disable-gpu",
      ],
      defaultViewport: null,
      headless: false,
      devtools: false,
      userDataDir: userDir,
    });
  }

  /**
   * 获取验证码
   * @param {*} regData
   */
  async _getCode(regData, PROXY, needClearCache) {
    var options = {
      needClearCache: needClearCache,
      gmailId: regData[0],
      passWord: regData[1],
      fzEmail: regData[2],
      PROXY: PROXY,
    };
    let robot = new GMailRobot(options);
    for (let index = 0; index < 6; index++) {
      let result = await robot.getCode();
      if (result.success) {
        logger.info(`gmail=${regData[0]} code=${result.code}`);
        return result.code;
      } else {
        if (result.stopCode) {
          throw new SkipAccountException(result.stopCode);
        }
        logger.warn("gmail extract fail...", result);
        continue;
      }
    }
    return result;
  }

  /**
   * 打开新页面
   */
  async openNewPage() {
    let page = await this.browser.newPage();
    await page._client.send("Network.clearBrowserCookies");

    if (this.options.needClearCache) {
      await page._client.send("Network.clearBrowserCache");
    }
    await page.setRequestInterception(true);
    page.on("request", (request) => {
      if (["image", "font"].indexOf(request.resourceType()) !== -1) {
        request.abort();
      } else {
        request.continue();
      }
    });
    return page;
  }

  /**
   * 登录
   */
  async _login() {
    let regData = this.options.regData;
    let page = await this.openNewPage();
    this.page = page;
    await page.goto("https://member.lazada.co.th/user/profile#/", {
      waitUntil: "domcontentloaded",
      timeout: 600000,
    });
    await clearLocalStorage(page);
    //等待语言出来
    await page.waitForXPath('//*[@id ="topActionSwitchLang"]', {
      visible: true,
      timeout: 15000,
    });
    //切换到英语
    await this.click_by_xpath(page, '//*[@id ="topActionSwitchLang"]');
    await page.waitForXPath("//div[@data-lang='en']", {
      visible: true,
      timeout: 15000,
    });
    await this.click_by_xpath(page, "//div[@data-lang='en']");

    await page.waitForXPath("//button[text()='LOGIN']", {
      visible: true,
      timeout: 30000,
    });
    await this.type_by_selector(
      page,
      "div.mod-input-loginName input",
      regData[0],
      {
        delay: this.getRandomInputTime(),
      }
    );
    await this.type_by_selector(
      page,
      "div.mod-input-password input",
      regData[4],
      {
        delay: this.getRandomInputTime(),
      }
    );
    await this.click_by_xpath(page, "//button[text()='LOGIN']");
    await this._handleLoginCaptcha(page);
    try {
      //登录点击后 等待一下看看是否有严重邮箱
      await page.waitForSelector('button[data-spm="verify-eamil"]', {
        visible: true,
        timeout: 15000,
      });
      //如果有 点击下验证邮箱
      await this.click_by_selector(page, 'button[data-spm="verify-eamil"]');
      //等待發送驗證碼按鈕
      await page.waitForSelector("div.mod-sendcode", {
        visible: true,
        timeout: 15000,
      });
      //點擊發送驗證碼
      await this.click_by_selector(page, "div.mod-sendcode");
      //簡單等待
      TimeSleep.sleep(5);
      //打開郵箱去收驗證碼
      let verficationCode = await this._getCode(
        regData,
        this.options.PROXY,
        this.options.needClearCache
      );
      await this.type_by_selector(
        page,
        "div.mod-input-sms input",
        verficationCode,
        {
          delay: this.getRandomInputTime(),
        }
      );
      await this.click_by_xpath(page, "//button[text()='Verify Code']");
      // page.waitForNavigation(), // The promise resolves after navigation has finished
      await this._handleLoginCaptcha(page);
      await this.waitSubmitPass(page);
    } catch (e) {
      if (!(e instanceof TimeoutError)) {
        throw e;
      }
    }
  }

  //等待进度条过去
  async waitSubmitPass(page,isNavigation=true) {
    try {
      //等进度条出来
      await page.waitForSelector("div.lzd-loader", { timeout: 10000 });
      for (let index = 0; index < 30; index++) {
        let loaderEl = await page.$("div.lzd-loader");
        if (loaderEl == null) {
          return true;
        }
        TimeSleep.sleep(1);
      }
      if(isNavigation){
        await page.waitForNavigation();
      }
    } catch (e) {
      if (this.isThrowError(e)) {
        throw e;
      }
      return false;
    }
  }

  /**
   * 处理登录的滑动验证码
   */
  async _handleLoginCaptcha(page) {
    try {
      //验证码iframe
      await page.waitForSelector("#sufei-dialog-content", {
        visible: true,
        timeout: 15000,
      });
      const elementHandle = await page.$("#sufei-dialog-content");
      const frame = await elementHandle.contentFrame();
      //继续等待滑动验证码可用
      await frame.waitForSelector("#nc_2_n1z", { timeout: 30000 });
      //可用之后开始滑动
      for (let index = 0; index < 10; index++) {
        await this._handlerCode(frame, page);
        let flag = await this._codeRefresh(frame);
        if (!flag) {
          break;
        }
        TimeSleep.sleep(2);
      }
    } catch (e) {
      if (!(e instanceof TimeoutError)) {
        throw e;
      }
    }
  }

  async _handlerCode(page,frame) {
    //滑动验证码
    if(frame){
      await frame.waitForSelector("#nc_2_n1z", { timeout: 10000 });
      await frame.hover("#nc_2_n1z");
    }else{
      await page.waitForSelector("#nc_2_n1z", { timeout: 10000 });
      await page.hover("#nc_2_n1z");
    }
    await page.mouse.down();
    await page.mouse.move(2000, 0);
    TimeSleep.sleep(1);
    await page.mouse.up();
  }

  async _codeRefresh(page) {
    try {
      await page.waitForXPath(
        "//a[@href='javascript:noCaptcha.reset(2)']",
        { timeout: 10000 }
      );
      await this.click_by_xpath(
        page,
        "//a[@href='javascript:noCaptcha.reset(2)']"
      );
      return true;
    } catch (e) {
      //非超时异常需要抛出
      if (!(e instanceof TimeoutError)) {
        throw e;
      }
      return false;
    }
  }

  async _handlerReg() {
    let regData = this.options.regData;
    let page = await this.openNewPage();
    this.page = page;
    await page.goto(
      "https://member.lazada.co.th/user/register?signupType=email",
      {
        waitUntil: "domcontentloaded",
        timeout: 600000,
      }
    );
    await clearLocalStorage(page);
    //等待语言出来
    await page.waitForXPath('//*[@id ="topActionSwitchLang"]', {
      timeout: 15000,
    });
    //切换到英语
    await this.click_by_xpath(page, '//*[@id ="topActionSwitchLang"]');
    await page.waitForXPath("//div[@data-lang='en']", { timeout: 15000 });
    await this.click_by_xpath(page, "//div[@data-lang='en']");

    //等待加载出注册按钮
    try {
      await page.waitForSelector("#nc_2_n1z", { timeout: 15000 });
    } catch (e) {
      if (e instanceof TimeoutError) {
        await this._codeRefresh(page);
      }
    }

    //输出邮箱
    await this.type_by_xpath(
      page,
      "//input[@placeholder='Please enter your email']",
      regData[0],
      { delay: this.getRandomInputTime() }
    );
    //驗證碼是否刷出來了
    for (let index = 0; index < 10; index++) {
      await this._handlerCode(page);
      try {
        await page.waitForSelector('input[placeholder="6 digits"]', {
          visible: true,
          timeout: 10000,
        });
        break;
      } catch (e) {
        if (e instanceof TimeoutError) {
          let _crResult = await this._codeRefresh(page);
          if (!_crResult) {
            break;
          }
        }
      }
    }

    // 等待15s去邮箱里面查询一下
    TimeSleep.sleep(5);
    await page.waitForSelector('input[placeholder="6 digits"]', {
      visible: true,
      timeout: 3000,
    });

    let verficationCode = await this._getCode(regData,  this.options.PROXY,
      this.options.needClearCache);
    //输入验证码
    await this.type_by_xpath(
      page,
      "//div[contains(@class, 'mod-login-input-sms mod-input-sms')]/input",
      verficationCode,
      { delay: this.getRandomInputTime() }
    );

    await this.type_by_xpath(
      page,
      "//div[contains(@class, 'mod-login-input-password mod-input-password')]/input",
      regData[4],
      { delay: this.getRandomInputTime() }
    );

    await this.type_by_xpath(
      page,
      "//div[contains(@class, 'mod-login-input-name mod-input-name')]/input",
      regData[5],
      { delay: this.getRandomInputTime() }
    );

    TimeSleep.sleep(2);
    await this.click_by_xpath(page, "//button[text()='SIGN UP']");
    await this.waitSubmitPass(page,false);
    await this.checkSubmitResult(page);
    let inputError = await this.isInputError(page);
    if (inputError) {
      throw new SkipAccountException("skip_regdata_invaild");
    }

    //等待用戶名顯示出來表示注冊成功
    await page.waitForSelector("#lzd_current_logon_user_name", {
      timeout: 120000,
    });
  }

  //检查提交是否存在错误
  async checkSubmitResult(page) {
    try {
      //等待可能的报错
      await page.waitForSelector("div.next-feedback-content", {
        timeout: 3000,
      });
      let feedbackEl = await page.$("div.next-feedback-content");
      let feedback = await page.evaluate((el) => el.innerText, feedbackEl);
      logger.warn(`feedback message=${feedback}`);
      //TODO: 对注册错误信息进行分析
      //邮箱已被注册，需要登陆后在设置地址
      if (feedback.indexOf("The email is occupied") != -1) {
        throw new AlreadyRegException();
      }
      //验证码错误的 直接重试
      if (feedback.indexOf("Invalid verification code") != -1) {
        throw new RetryException();
      }
      //验证码过期的 直接重试
      if (feedback.indexOf("Verification code expired") != -1) {
        throw new RetryException();
      }
    } catch (e) {
      if (!(e instanceof TimeoutError)) {
        throw e;
      }
    }
  }

  //检查提交字段是否存在错误
  async isInputError(page) {
    try {
      await page.waitForSelector("input[error]", { timeout: 2000 });
      let errorEls = await page.$$("input[error]");
      if (errorEls.length == 0) {
        return false;
      } else {
        return true;
      }
    } catch (e) {
      return false;
    }
  }

  async _waitSelectClickable(page, css_selector) {
    //需要等待联动加载完成
    let isOk = false;
    for (let index = 0; index < 15; index++) {
        let waitEl = await page.$(css_selector);
        let classStr = await page.evaluate(
          (el) => el.getAttribute("class"),
          waitEl
        );
        if(classStr.indexOf("disable")==-1){
          isOk = true;
          break;
        }
        TimeSleep.sleep(1);
    }

    //如果指定時間沒有處理完成，則直接重試，基本上是網絡問題
    if (!isOk) {
      throw new RetryException();
    }
  }

  async _selectHandlerByName(page, name) {
    //等待列表加载 避免未加载完成 出错
    TimeSleep.sleep(2);
    let lis = await page.$$("ul.next-menu-content li");
    for (let index = 0; index < lis.length; index++) {
      let liEl = lis[index];
      let nameStr = await page.evaluate((el) => el.getAttribute("name"), liEl);
      if (nameStr == name) {
        await liEl.click();
        break;
      }
    }
  }

  async handlerAddress() {
    let regData = this.options.regData;
    let page = this.page;
    //開始設置其收穫地址
    await page.waitForSelector("#Address-book a", { visible: true, timeout: 30000 });
    //點擊收穫地址
    // await _stopLoading(page)
    TimeSleep.sleep(2);

    await this.click_by_selector(page, "#Address-book a");
    try {
      await page.waitForSelector("div.mod-address-book-empty", {
        visible: true,
        timeout: 30000,
      });
    } catch (e) {
      if (e instanceof TimeoutError) {
        await page.waitForSelector("div.mod-address-book table", {
          visible: true,
          timeout: 3000,
        });
        //如果已经有地址数据了直接返回
        return;
      }
    }

    //添加收穫地址
    await page.waitForSelector("div.mod-address-book-ft button", {
      timeout: 15000,
    });
    await this.click_by_selector(page, "div.mod-address-book-ft button");

    //等待頁面渲染
    await page.waitForSelector("div.mod-address-form-action", {
      timeout: 30000,
    });

    //开始填写表单
    //full name
    await this._waitSelectClickable(page, "div.mod-select-location-tree-1");
    await this.type_by_selector(page, "div.mod-input-name input", regData[5], {
      delay: this.getRandomInputTime(),
    });
    await this.type_by_selector(page, "div.mod-input-phone input", regData[6], {
      delay: this.getRandomInputTime(),
    });
    await this.type_by_selector(
      page,
      "div.mod-input-detailAddress input",
      regData[12],
      { delay: this.getRandomInputTime() }
    );

    await this.click_by_selector(page, "div.mod-select-location-tree-1 i");
    await this._selectHandlerByName(page, regData[9]);

    await this._waitSelectClickable(page, "div.mod-select-location-tree-2");

    await this.click_by_selector(page, "div.mod-select-location-tree-2 i");
    await this._selectHandlerByName(page, regData[10]);

    await this._waitSelectClickable(page, "div.mod-select-location-tree-3");

    await this.click_by_selector(page, "div.mod-select-location-tree-3 i");
    await this._selectHandlerByName(page, regData[11]);
    await this.waitSubmitPass(page,false)
    for (let index = 0; index < 3; index++) {
      let btnOk = await this.click_by_xpath(page, "//button[text()='SAVE']");
      if (!btnOk) {
        break;
      }
      let flag = await isInputError(page);
      if (flag) {
        throw new SkipAccountException("skip_addrdata_invaild");
      }
      flag = await waitSubmitPass(page);
      if (flag) {
        break;
      }
    }
    await page.waitForSelector("div.mod-address-book table", {
      timeout: 60000,
    });
  }

  //评论
  async _comment() {
    let page = this.page;
    // await this.page.waitForSelector("#abbb", { timeout: 0 });
    //等待菜单
    await this.page.waitForSelector("#My-Orders", {
      visible: true,
      timeout: 15000,
    });
    await Promise.all([
      page.waitForNavigation(), // The promise resolves after navigation has finished
      this.click_by_xpath(page, "//span[text()='My Orders']"), // Clicking the link will indirectly cause a navigation
    ]);
    //等待订单界面出来
    await this.page.waitForSelector("div.orders", {
      visible: true,
      timeout: 15000,
    });
    let orderDivs = await this.page.$$("div.orders>div");
    for (const orderDiv of orderDivs) {
      let linkEl = await orderDiv.$("a.link");
      let orderId = await page.evaluate((el) => el.innerText, linkEl);
      //匹配订单和检查对应的状态
      let statusEl = await orderDiv.$("p.capsule");
      //TODO:这个地方需要检测可能收货不完整的情况
      let status = await page.evaluate((el) => el.innerText, statusEl);
      if (status != "Delivered") {
        continue;
      }
      if (orderId != this.options.orderId) {
        continue;
      }

      let orderUrl =
        "https://my.lazada.co.th/customer/order/view/?tradeOrderId=" + orderId;
      await this.writeComment(page, orderUrl);
      //这里暂时退出去完善多订单处理
      return { success: true };
    }
  }

  /**
   * 写评论
   */
  async writeComment(commentPage, orderUrl) {
    //从新标签页打开订单管理页面进行操作
    await commentPage.goto(orderUrl, {
      waitUntil: "domcontentloaded",
      timeout: 600000,
    });
    //这里最好新开page来搞(失败。。。开新页面需要登录)
    // await this.page.waitForSelector("#abbb", { timeout: 0 });

    await Promise.all([
      commentPage.waitForNavigation(), // The promise resolves after navigation has finished
      this.click_by_xpath(commentPage, "//p[text()='WRITE A REVIEW']"), // Clicking the link will indirectly cause a navigation
    ]);

    await commentPage.waitForSelector("div.item-rating-container", {
      visible: true,
      timeout: 15000,
    });

    let itemMap = await this._getItemMap(commentPage);
    let itmes = this.options.items;

    for (const item of itmes) {
      //评论内容为空，直接提交
      if(!item.comment){
        break;
      }
      let itemTitle = itemMap.get(item.itemId);
      if (itemTitle == null) {
        logger.warn(`orderId=${this.options.orderId} skip_itemId_invaild!`);
        throw new SkipAccountException("skip_itemId_invaild");
      }
      let el = await this.find_element_by_xpath(
        commentPage,
        `//div[contains(text(),'${itemTitle}')]/..//textarea`
      );
      await el.click({ clickCount: 3 });
      await commentPage.keyboard.press("Backspace");
      await el.type(item.comment, { delay: this.getRandomInputTime() });
      // 图片的处理
      if(item.images){
        for (const imgOne of item.images) {
          //等待元素出现
          let uploadBtn = await this.find_element_by_xpath(
              commentPage, `//div[text()='Upload Photo']`
          );
          //点击上传图片按钮
          uploadBtn.click();
          //uploadFile上传图片
          const uploadPic = await commentPage.waitForSelector("input[name='imageToUpload']");
          //上传图片目录自定义
          await uploadPic.uploadFile(imgOne);
          await commentPage.waitFor(3000);
        }

      }
    }

    TimeSleep.sleep(1);

    try {
      //TODO:这个地方还需要检测提交tips，避免数据问题导致失败
      for (let index = 0; index < 3; index++) {
        let el = await this.find_element_by_xpath(
          commentPage,
          "//button[text()='SUBMIT']"
        );
        await el.click();
        TimeSleep.sleep(2);
        //1s后查询是否禁用，没有的就在点一次
        const isDisabled = await commentPage.evaluate((button) => {
          return button.disabled;
        }, el);
        if (isDisabled) {
          break;
        }
      }
      //成功后会跳转到评价历史界面，即认为成功
      await this.page.waitForSelector("div.my-reviews-container", {
        visible: true,
        timeout: 30000,
      });
    } catch (e) {
      if (!(e instanceof TimeoutError)) {
        throw e;
      }
    }
  }

  /**
   * 获取评价页面里面的宝贝数据Map，用于后续使用
   * @param {*} commentPage
   */
  async _getItemMap(commentPage) {
    let items = await commentPage.evaluate(() => {
      return window.__initData__.model.items;
    });
    let itemMap = new Map();
    for (let index = 0; index < items.length; index++) {
      const item = items[index];
      itemMap.set(item.itemId, item.itemTitle);
    }
    return itemMap;
  }

  async doReg() {
    let account = this.options.regData[0];
    try {
      let isReg = false;
      let isAddr = false;
      let isSkip = false;
      if (DataDbService.checkAccountReg(account)) {
        isReg = true;
        if (DataDbService.checkAccountStatus(account, "address_ok")) {
          isAddr = true;
        }
        if (DataDbService.checkAccountStatus(account, "skip")) {
          isSkip = true;
        }
      }
      if (isReg && (isAddr || isSkip)) {
        logger.info("account=%s is handler finished!", account);
        return {
          success: true,
          finished: true,
        };
      }
      if(!this.options.PROXY){
        let proxy = await ProxyService.getProxy();
        this.options.PROXY = proxy;
      }
      logger.info("start handler account:%s proxy=%s", account, this.options.PROXY);
      await this._createBrowser();
      if (!isReg) {
        logger.info("start handler reg account=%s", account);
        await this._handlerReg();
        DataDbService.regOK({ account: account, status: "reg_ok" });
      } else {
        logger.info("start handler log account=%s", account);
        await this._login();
      }
      if (!isAddr) {
        logger.info("start handler addr account=%s", account);
        await this.handlerAddress();
        DataDbService.updateAccountStatus(account, "address_ok");
      }
      return {
        success: true,
      };
    } catch (e) {
      if (e instanceof AlreadyRegException) {
        logger.warn("%s already reg...",account);
        DataDbService.regOK({ account: account, status: "reg_ok" });
        return {
          success: true,
          retry: true,
        };
      }
      if (e instanceof SkipAccountException) {
        logger.warn("%s skip!code=%s",account,e.stopCode);
        DataDbService.regOK({ account: account, status: e.stopCode });
        return {
          success: true,
          stopCode: e.stopCode,
        };
      }
      logger.error(e);
      return {
        success: false,
        retry: true,
      };
    } finally {
      //确保回收
      if (this.page != null) {
        await this.page.close();
      }
      if (this.browser != null) {
        await this.browser.close();
      }
    }
  }

  async doComment() {
    try {
      await this._createBrowser();
      await this._login();
      return await this._comment();
    } catch (e) {
      if (e instanceof SkipAccountException) {
        return {
          success: true,
          stopCode: e.stopCode,
        };
      }
      logger.error(e);
      return {
        success: false,
        error: true,
      };
    } finally {
      //确保回收
      if (this.page != null) {
        await this.page.close();
      }
      if (this.browser != null) {
        await this.browser.close();
      }
    }
  }
}

module.exports = LazdaRobot;
