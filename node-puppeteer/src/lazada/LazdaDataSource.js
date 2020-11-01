const XLSX = require("xlsx");

class LazdaDataSource {
  //读取评论数据
  readCommentData() {
    let path = "D:\\file\\";
    let imgPath = "D:\\file\\images\\";
    let workbook = XLSX.readFile(path + "待评价信息-7.xlsx");
    let sheet = workbook.Sheets[workbook.SheetNames[0]];
    let csv = XLSX.utils.sheet_to_csv(sheet);
    let lines = csv.split("\n");
    let datas = [];
    let lastRecord = null;
    let lastData = null;
    for (let index = 1; index < lines.length; index++) {
      let line = lines[index];
      if (line == "" || line.length < 10) {
        continue;
      }
      let dataArray = line.split(",");
      let data = {
        orderId: dataArray[1].trim(),
        regData: dataArray.slice(2, 7),
      };
      let itemId;
      if (dataArray[0] == "") {
        //这种是1个宝贝对应多个订单的，从上1个数据里面获取对应的宝贝ID
        itemId = lastRecord[0];
      } else {
        //分析出订单ID
        let url = dataArray[0];
        if (url.indexOf("info-i") != -1) {
          itemId = parseInt(
            url.substring(url.indexOf("info-i") + 6, url.indexOf(".html"))
          );
        } else {
          let startIndex = url.lastIndexOf("-i");
          itemId = parseInt(
            url.substring(startIndex + 2, url.indexOf("-s", startIndex))
          );
        }
        dataArray[0] = itemId;
        lastRecord = dataArray;
      }
      //这种是1个订单的需要合并数据
      if (dataArray[1] == "") {
        lastData.items.push({ itemId: itemId, comment: dataArray[7] ,images:this.extractedImages(dataArray[9],index+1,imgPath)});
      } else {
        data.items = [];
        data.items.push({ itemId: itemId, comment: dataArray[7] ,images:this.extractedImages(dataArray[9],index+1,imgPath)});
        datas.push(data);
        lastData = data;
      }
    }
    return datas;
  }
  //处理评论图片[]
  extractedImages(nums, index,imgPath) {
    let images = [];
    if (Number(nums) === 1) {
      images.push(imgPath+index + ".jpg")
    }
    if (Number(nums) > 1) {
      for (let i = 1; i <= Number(nums); i++) {
        images.push(imgPath+index+"." + i + ".jpg")
      }
    }

    return images;
  }
  //读取注册数据
  readRegData() {
    let workbook = XLSX.readFile("D:/last.xlsx");
    let sheet = workbook.Sheets[workbook.SheetNames[0]];
    let csv = XLSX.utils.sheet_to_csv(sheet);
    let lines = csv.split("\n");
    let datas = [];
    for (let index = 1; index < lines.length; index++) {
      let line = lines[index];
      if (line == "") {
        continue;
      }
      let dataArray = line.split(",");
      //如果第一列就是空的则直接跳过这一行;
      if (dataArray[0] == ""||dataArray.length<3) {
        continue;
      }
      for (let j = 0; j < dataArray.length; j++) {
        let colVal = dataArray[j];
        dataArray[j] = colVal.trim();
      }
      datas.push(dataArray);
    }
    return datas;
  }
}

module.exports = new LazdaDataSource();
