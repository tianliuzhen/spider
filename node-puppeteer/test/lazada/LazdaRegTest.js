const LazdaRobot = require("../../src/lazada/LazdaRobot");

async function main() {
  var options = {
    needClearCache: false,
    regData: [
      "TrevionvPfeilv75127@gmail.com",
      "oroo4thooFVD",
      "BuckMerkle29852@outlook.com",
      "泰国", "2WfxPlMV", "ปอ ออโต้เซลร์",
      "660959304961",
      "",
      "Thailand",
      "ประจวบคีรีขันธ์/ Prachuap Khiri Khan",
      "ปราณบุรี/ Pran Buri",
      "77220",
      "ต.ปากนำ้ปราน(สถานีจ่ายนำ้ปากนำ้ปรานบุรี)"
    ],
    PROXY: "192.168.100.205:7890",
  };
  let robot = new LazdaRobot(options);
  let result = await robot.doReg();
  console.log(result);
}

main();
