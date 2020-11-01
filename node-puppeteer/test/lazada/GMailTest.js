const GMailRobot = require("../../src/lazada/GMailRobot");

async function main() {
  var options = {
    needClearCache: false,
    gmailId: "elsiebeth2013485@gmail.com",
    passWord: "FASK2034@54@58%",
    fzEmail: "AndreaOntiveros@outlook.com",
    PROXY: "127.0.0.1:7890",
  };
  let robot = new GMailRobot(options);
  let result = await robot.getCode();
  console.log(result);
}

main();
