const puppeteer = require('puppeteer');
//puppeteer文件上传操作，适用原声控件：<input type=file/>
async function upload() {
    //创建一个Browser浏览器实例，并设置相关参数
    const browser=  await puppeteer.launch({
        headless: false,
    })
    //创建一个Page实例
    const page = await browser.newPage();
    //跳转打开的url
    await page.goto("http://127.0.0.1:8012/file")

    //点击上传图片按钮
    // let els = await page.$x("/html/body/form/input[1]");
    // els[0].click();

   

    //上传图片目录自定义
    const uploadPic = await page.waitForSelector("input[name='file']");
    //上传图片目录自定义
    await uploadPic.uploadFile("C:\\Users\\TLZ\\Desktop\\js\\待评价信息 图片-7\\10.jpg");

    //点击发送图片
    await  page.click("body > form > input[type=submit]:nth-child(5)")
    await page.waitFor(3000)
    //关闭浏览器
    browser.close()


}
upload() ;
