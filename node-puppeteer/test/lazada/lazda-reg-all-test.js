const LazdaRobot = require("../../src/lazada/LazdaRobot");
const LazdaDataSource = require("../../src/lazada/LazdaDataSource");
const logger = require("tracer").colorConsole();
const DataDbService = require("../../src/lazada/DataDbService");
const ProxyService = require("../../src/lazada/ProxyService");
async function main() {
  const skipList = ["LareecGarren32816@gmail.com"];
  const datas = LazdaDataSource.readRegData();
  logger.info("read data size=%s", datas.length);
  const optionsDefult = {
    needClearCache: false,
    PROXY: "127.0.0.1:7890",
  };
  for (let index = 0; index < datas.length; index++) {
    let data = datas[index];
    if (skipList.includes(data[0])) {
      logger.info("skip handler account:%s", data[0]);
      continue;
    }
    const options = {regData:data};
    if (DataDbService.getRunCount() % 4 == 0) {
      logger.info("need Clear cache!");
      options.needClearCache = true;
    }

    let robot = new LazdaRobot(options);
    //重试3次
    let result;
    let finished=false;
    for (let index = 0; index <=3 ; index++) {
      result = await robot.doReg();
      if (result.success) {
        //除了重试的其他直接过就好了
        if (result.retry) {
          continue;
        }
        if(result.finished){
          finished=true;
        }
        break;
      }else {
        // 记录失败数据邮箱
        if (index === 3) {
          DataDbService.regError(data[0])
        }
      }
      //失敗了換ip
      // proxy = await ProxyService.getProxy();
      // options.PROXY = proxy;
    }
    if(!finished){
      DataDbService.incrmentRunCount();
    }
    logger.info("end handler index:%s account:%s result=%j", index,data[0], result);
  }
}

main();
