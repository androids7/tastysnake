# TastySnake

[![Build Status](https://travis-ci.org/stevennl/TastySnake.svg?branch=master)](https://travis-ci.org/stevennl/TastySnake)

这是一个基于蓝牙的重力感应贪吃蛇对战游戏。

[技术实现](./doc/implementation.md)

## 游戏规则

1. 请记住自己永远是红色的蛇。
2. 留意上方倒计时！每隔 10 秒攻守身份会对调。
3. 只要两条蛇互相碰撞，攻击者胜利。
4. 撞到自己的身体或者战场边界将会被判定为失败。
5. 吃到战场上的食物，会导致身体变长。
6. 蛇的移动方向使用重力感应控制。

## 效果演示

![](./doc/img/demo_light.gif)

![](./doc/img/demo_dark.gif)

## 发布

* [TastySnake_1.0.0.161220.apk](./apk/TastySnake_1.0.0.161220.apk)
    * 首次发布
* [TastySnake_1.0.1.161222.apk](./apk/TastySnake_1.0.1.161222.apk)
    * 修复蓝牙连接时的bug
    * 修复魅族系列提示框bug
* [TastySnake_1.0.2.161230.apk](./apk/TastySnake_1.0.2.161230.apk)
    * 修复Home键bug
    * 优化返回键功能

## License

See the [LICENSE](./LICENSE) file for license rights and limitations.