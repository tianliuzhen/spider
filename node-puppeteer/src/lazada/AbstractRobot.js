/**
 * 抽象机器人
 */
const TimeoutError = require("puppeteer").errors.TimeoutError;
function randomInt(min, max) {
  return Math.floor(Math.random() * (max - min)) + min;
}
class AbstractRobot {
  getRandomInputTime() {
    return randomInt(50, 100);
  }

  async find_element_by_xpath(page, xpath) {
    let els = await page.$x(xpath);
    return els[0];
  }

  async find_element_by_selector(page, selector) {
    let els = await page.$(selector);
    return els;
  }

  async click_by_xpath(page, xpath, options) {
    let el = await this.find_element_by_xpath(page, xpath);
    await el.click(options);
  }

  async click_by_selector(page, selector, options) {
    let el = await this.find_element_by_selector(page, selector);
    await el.click(options);
  }

  async type_by_xpath(page, xpath, text, options) {
    let el = await this.find_element_by_xpath(page, xpath);
    await el.type(text, options);
  }

  async type_by_selector(page, selector, text, options) {
    let el = await this.find_element_by_selector(page, selector);
    await el.type(text, options);
  }

  //是否是需要抛出的异常
  isThrowError(e){
    //超時異常
    if (e instanceof TimeoutError) {
      return false;
    }
    //檢查過程中頁面跳轉了
    if(e.message&&e.message.indexOf("most likely because of a navigation")!=-1){
      return false;
    }
    return true;
  }
}

module.exports = AbstractRobot;
