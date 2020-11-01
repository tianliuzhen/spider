const puppeteer = require("puppeteer-extra");
const StealthPlugin = require("puppeteer-extra-plugin-stealth");
puppeteer.use(StealthPlugin());
const TimeoutError = require("puppeteer").errors.TimeoutError;
const logger = require("tracer").colorConsole();
const util = require("util");
const TimeSleep = require("./TimeSleep");
const AbstractRobot = require("./AbstractRobot");

async function _waitLoading(page) {
  try {
    for (let index = 0; index < 15; index++) {
      //如果有正在加載的情況，則等待加載完成
      try {
        await page.waitForSelector("span.v1", {
          visible: true,
          timeout: 1000,
        });
        TimeSleep.sleep(1);
      } catch (e) {
        if (e instanceof TimeoutError) {
          return;
        }
      }
    }
  } catch (e) {}
}

async function _checkAccount(page) {
  try {
    await page.waitForSelector("#headingText", {
      visible: true,
      timeout: 15000,
    });
    TimeSleep.sleep(1);
    let accountTextEl = await page.$("#headingText span");
    let accountText = await page.evaluate((el) => el.innerText, accountTextEl);
    logger.warn("account is not used!message=%s", accountText);
    if (accountText.indexOf("帐号已停用") != -1) {
      return {
        success: false,
        stopCode: "skip_gmail_account_disable",
      };
    }
    return null;
  } catch (e) {
    if (e instanceof TimeoutError) {
      try {
        let imgEl = await page.$(
          'img[src="//ssl.gstatic.com/accounts/idv-reenable-phone.png"]'
        );
        if (imgEl != null) {
          return {
            success: false,
            stopCode: "skip_gmail_phone_check",
          };
        }
      } catch (e) {}
      return null;
    }
    throw e;
  }
}
/**
 * gmail机器人 主要用于登录后收取验证码
 */
class GMailRobot extends AbstractRobot {
  constructor(options) {
    super();
    this.options = options;
  }

  /**
   * 打开浏览器
   */
  async _createBrowser() {
    var appDir = process.cwd();
    let userDir = util.format("%s/chromedata/gmail", appDir);
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
      headless: true,
      // headless: false,
      devtools: false,
      userDataDir: userDir,
    });
  }

  /**
   * 登录
   */
  async _login() {
    let page = null;
      page = await this.browser.newPage();
      await page.setRequestInterception(true);
      page.on("request", (request) => {
        if (["image", "font"].indexOf(request.resourceType()) !== -1) {
          request.abort();
        } else {
          request.continue();
        }
      });

      this.page = page;
      await page._client.send("Network.clearBrowserCookies");

      if (this.options.needClearCache) {
        await page._client.send("Network.clearBrowserCache");
      }
      await page.goto(
        "https://accounts.google.com/signin/v2/identifier?continue=https%3A%2F%2Fmail.google.com%2Fmail%2F&osid=1&service=mail&ss=1&ltmpl=default&rm=false&hl=zh-CN&flowName=GlifWebSignIn&flowEntry=ServiceLogin",
        { waitUntil: "domcontentloaded" }
      );
      await page.waitForSelector("#identifierId", { visible: true });
      await page.type("#identifierId", this.options.gmailId, {
        delay: this.getRandomInputTime(),
      });
      await page.click("#identifierNext");

      try {
        //如果有验证码的直接报错
        await page.waitForSelector("#ca", {
          visible: true,
          timeout: 8000,
        });
        return {
          success: false,
          msg: "need captchaimg!",
        };
      } catch (e) {
        //非超时异常需要抛出
        if (!(e instanceof TimeoutError)) {
          throw e;
        }
      }
      await page.waitForSelector("#password input", { visible: true });
      await page.type("#password input", this.options.passWord, {
        delay: this.getRandomInputTime(),
      });
      await page.click("#passwordNext");
      try {
        await  page.waitForXPath("//span[contains(text(),'发生了更改')] | //span[contains(text(),'密码错误')]", {
          timeout: 5000,
          visible: true,
        });
        return {
          success: false,
          stopCode: "skip_gmail_account_pwd_error",
        };
      } catch (e) {
        if(!(e instanceof TimeoutError)){
          throw e
        }
      }
      try {
        await page.waitForXPath('//div[text()="确认您的辅助邮箱"]', {
          timeout: 10000,
          visible: true,
        });
        TimeSleep.sleep(2);
        await this.click_by_xpath(page, '//div[text()="确认您的辅助邮箱"]');
        await page.waitForSelector("#knowledge-preregistered-email-response", {
          visible: true,
        });
        await page.type(
          "#knowledge-preregistered-email-response",
          this.options.fzEmail,
          {
            delay: this.getRandomInputTime(),
          }
        );
        await Promise.all([
          page.waitForNavigation(), // The promise resolves after navigation has finished
          this.click_by_xpath(page, "//span[text()='下一步']/.."), // Clicking the link will indirectly cause a navigation
        ]);
      } catch (e) {}
      TimeSleep.sleep(3);
      let accountError = await _checkAccount(page);
      if (accountError != null) {
        return accountError;
      }

      await this._handlerTips(page, "//span[text()='以后再说']");
      await this._handlerTips(page, "//span[text()='确认']");
      await this._handlerTips(page, "//a[text()='Try Again']");
      await this._handlerTips(page, "//span[text()='我同意']", {
        clickCount: 3,
        delay: 300,
      });
  }

  async _handlerTips(page, xpathExp, options) {
    try {
      await page.waitForXPath(xpathExp, {
        visible: true,
        timeout: 2000,
      });
      await this.click_by_xpath(page, xpathExp, options);
    } catch (e) {
      if (!(e instanceof TimeoutError)) {
        throw e;
      }
    }
  }

  //获取lazda的验证码
  async _getLazdaVerifyCode() {
    let page = this.page;
    await page.waitForSelector("div[role='complementary']", {
      visible: true,
      timeout: 120000,
    });
    await _waitLoading(page);
    try {
      //等待3s 關閉下提示div
      TimeSleep.sleep(3);
      await page.click("div.YU");
    } catch (e) {}

    let trs = await page.$x('//table[@role="grid"]/tbody/tr');
    for (let tr of trs) {
      let text = await page.evaluate((el) => el.innerText, tr);
      //提取一下包含Lazda的验证邮件
      if (
        text.indexOf("Lazada") != -1 &&
        text.indexOf("Verify your email") != -1
      ) {
        await tr.click();
        break;
      }
    }
    TimeSleep.sleep(3);
    await page.waitForXPath('//div[@role="listitem"]', { timeout: 10000 });
    let listitems = await page.$x('//div[@role="listitem"]');
    try {
      let trimmedBtns = await page.$x(
        "//div[@data-tooltip='Show trimmed content']"
      );
      await trimmedBtns[trimmedBtns.length - 1].click();
      TimeSleep.sleep(1);
    } catch (e) {}
    let codeEl = listitems[listitems.length - 1];
    let t = await page.evaluate((el) => el.innerText, codeEl);
    let start = t.indexOf("Email Verification Page:");
    let end = t.indexOf("Do not share this code");
    start = start + "Email Verification Page:".length;
    let code = t.substring(start, end).replace(/\n/g, "");

    return { success: true, code: code };
  }

  async getCode() {
    try {
      await this._createBrowser();
      let loginResult = await this._login();
      if(loginResult!=null){
        return loginResult;
      }
      return await this._getLazdaVerifyCode();
    } catch (e) {
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

module.exports = GMailRobot;
