const XLSX = require("xlsx");
const LazdaDataSource = require("../../src/lazada/LazdaDataSource");
const logger = require("tracer").colorConsole();
const DataDbService = require("../../src/lazada/DataDbService");
function writeRegSuccess(){
  let datas = LazdaDataSource.readRegData();
  logger.info("read data size=%s", datas.length);
  let results=[];
  for (let index = 0; index < datas.length; index++) {
    let data = datas[index];
    if(DataDbService.checkAccountStatus(data[0],"skip_")){
      logger.info("%s skip...",data[0])
      continue;
    }
    results.push(data);
  }

  let wb = XLSX.utils.book_new();
  let ws = XLSX.utils.aoa_to_sheet(results);
  /* add worksheet to workbook */
  XLSX.utils.book_append_sheet(wb, ws);
  XLSX.writeFile(wb, "D:/注册可用2020-10-23_2.xlsx");
}

writeRegSuccess()
