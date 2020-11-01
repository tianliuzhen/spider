const XLSX = require("xlsx");

async function main() {
  var workbook = XLSX.readFile("D:/111/马来泰国地址信息.xlsx");
  var sheet = workbook.Sheets[workbook.SheetNames[0]];
  var csv = XLSX.utils.sheet_to_csv(sheet);
  var lines = csv.split("\n");
  var datas = [];
  for (let index = 1; index < lines.length; index++) {
    const line = lines[index];
    const dataArray = line.split(",");
    if (dataArray[0] == "" || dataArray[4] != "Thailand") {
      continue;
    } else {
      for (let j = 0; j < dataArray.length; j++) {
        if (j == 0) {
          dataArray[j] = index;
        } else {
          let colVal = dataArray[j];
          dataArray[j] = colVal.trim();
        }
      }
      datas.push(dataArray);
    }
  }
  console.table(datas);
}

main();
