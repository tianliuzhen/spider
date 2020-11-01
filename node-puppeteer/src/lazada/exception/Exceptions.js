//跳过账号异常
class AlreadyRegException extends Error {
  constructor() {
    super();
  }
}
//重试异常
class RetryException extends Error {
  constructor() {
    super();
  }
}

module.exports = { AlreadyRegException, RetryException };
