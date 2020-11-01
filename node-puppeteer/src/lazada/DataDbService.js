const low = require("lowdb");
const FileSync = require("lowdb/adapters/FileSync");
var path = require("path");
const fs = require("fs");
class DataDbService {
  constructor() {
    const root = process.cwd();
    const filePath = path.join(root, "/_data/db.json");
    let directory = path.dirname(filePath);
    if (!fs.existsSync(directory)) {
      fs.mkdirSync(directory);
    }
    const adapter = new FileSync(filePath);
    this.db = low(adapter);
    // Set some defaults (required if your JSON file is empty)
    this.db.defaults({ comments: [],commentsError: [], regDatas: [],  regErrorDatas: [],runCount: 0 }).write();
  }

  incrmentRunCount() {
    this.db.update("runCount", (n) => n + 1).write();
  }

  getRunCount() {
    return this.db.get("runCount").value();
  }

  addComment(commnet) {
    // Add a post
    this.db.get("comments").push(commnet).write();
  }

  hasExistsByOrderId(orderId) {
    let val = this.db.get("comments").find({ orderId: orderId }).value();
    return val != null;
  }

  regOK(regData) {
    this.db.get("regDatas").push(regData).write();
  }

  regError(regErrorDatas) {
    this.db.get("regErrorDatas").push(regErrorDatas).write();
  }

  commentsError(commentsError) {
    this.db.get("commentsError").push(commentsError).write();
  }

  checkAccountReg(account) {
    let value = this.db
      .get("regDatas")
      .find((data) => {
        if (data.account != account) {
          return false;
        }
        return true;
      })
      .value();
    return value != null;
  }

  updateAccountStatus(account, status) {
    this.db
      .get("regDatas")
      .find({ account: account })
      .assign({ status: status })
      .write();
  }

  checkAccountStatus(account, status) {
    let value = this.db
      .get("regDatas")
      .find((data) => {
        if (data.account != account) {
          return false;
        }
        if (data.status.indexOf(status) != -1) {
          return true;
        }
        return false;
      })
      .value();
    return value != null;
  }
}

module.exports = new DataDbService();
