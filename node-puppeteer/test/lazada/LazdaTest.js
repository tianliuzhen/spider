const LazdaRobot = require("../../src/lazada/LazdaRobot");

async function main() {
  var options = {
    needClearCache: false,
    regData: [
      "noahron12@gmail.com",
      "Lmk52iop",
      "SadieSchaffer@outlook.com",
      "泰国",
      "15A1kgGF",
    ],
    PROXY: "192.168.100.205:7890",
    orderId: "330442989705148",
    items: [
      {
        itemId: 1579978694,
        comment: "ผลการควบคุมความมันดีมากและราคาถูกสามารถใช้ได้เมื่อผมมัน",
      },  {
        itemId: 1579954794,
        comment: "ชอบมากและราคาเหมาะสมมาก",
      }, {
        itemId: 1579968726,
        comment: "",
      },
    ],
  };
  let robot = new LazdaRobot(options);
  let result = await robot.doComment();
  console.log(result);
}

main();
