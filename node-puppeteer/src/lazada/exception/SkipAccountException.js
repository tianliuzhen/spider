//跳过账号异常
class SkipAccountException extends Error {
  constructor(stopCode) {
    super();
    //跳过原因
    this.stopCode = stopCode;
  }

  getStopCode() {
    return this.stopCode;
  }
}

module.exports = SkipAccountException
