const DataDbService = require("../../src/lazada/DataDbService");
const LazdaDataSource = require("../../src/lazada/LazdaDataSource");
const ProxyService = require("../../src/lazada/ProxyService");

async function main() {
  //   const id = 123123;
  //   DataDbService.addComment({ orderId: id, data: [1, 2, 3] });
  //   console.log(DataDbService.hasExistsByOrderId(id));
  // DataDbService.incrmentRunCount();
  // console.log(DataDbService.getRunCount());
  // await ProxyService.getProxy();
  // LazdaDataSource.readRegData();
  DataDbService.regOK({ account: "123", status: "skip_111" });
  DataDbService.checkAccountReg("123");
  DataDbService.checkAccountStatus("123", "skip");
}

main();
