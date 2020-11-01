//生成随机密码
var generator = require("generate-password");
var count = 100;
var passwords = generator.generateMultiple(count, {
  length: 8,
  uppercase: true,
  numbers: true,
  symbols: false,
  strict: true,
});

// [ 'hnwulsekqn', 'qlioullgew', 'kosxwabgjv' ]
// console.log(passwords);
for (const pwd of passwords) {
  console.log(pwd);
}
