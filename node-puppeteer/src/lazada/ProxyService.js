const axios = require("axios");
const logger = require("tracer").colorConsole();
module.exports = {
    getProxy: async function () {
        let resp = await axios.get("http://tiqu.linksocket.com:81/abroad?num=1&type=1&lb=1&sb=0&flow=1&regions=th&n=0", { responseType: "text" })
        let data = resp.data;
        try {
            let error = JSON.parse(data);
            logger.warn("get proxy failed!!!", error)
            // if (error.msg.indexOf("您的剩余流量不足") != -1) {
            process.exit()
            // }
        } catch (error) {
            let results = data.split("\r\n")
            return results[0]
        }
    }
}