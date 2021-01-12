---
title: Gitee图床+typora&notion+snipaste
date: 2020-12-26  12:40:11
categories:
- 杂项
tags:
- 工具
- 图床
- git
---
## 摘要
* 软件的下载地址
* 配置PicGo图床，把gitee作为图床
* markdown软件typora，notion1简介
* 截图软件snipaste快速配合PicGo

 <!--more--> 

> tips: 以下{$xxx}均为用户自定义

## 软件下载

> [1].[图床软件PicGo](https://molunerfinn.com/PicGo/)
>
> [2].[typota](https://typora.io/)
>
> [3].[notion](https://www.notion.so/)
>
> [4].[snipaste](https://www.snipaste.com/)

## 使用

### typora,notion

typora和notion都是支持Markdown语法的文档编辑器，但是notion支持更多的功能，比如生成如下形式的文档

![](https://gitee.com/BothSavage/PicGo/raw/master/image/20201226130027.png)

### picgo

所谓图床工具，就是自动把本地图片转换成链接的一款工具，网络上有很多图床工具，就目前使用种类而言，PicGo 算得上一款比较优秀的图床工具。它是一款用 `Electron-vue` 开发的软件，可以支持微博，七牛云，腾讯云COS，又拍云，GitHub，阿里云OSS，SM.MS，imgur 等8种常用图床，功能强大，简单易用

> 推荐使用gitee，因为github访问不稳定

- 直接搜索gitee在PicG的插件界面，安装picgo-plugin-gitee-uploader
- 对应填好如下属性

- - 其中repo为**用户名/仓库名**
  - branch默认为master
  - token的获取方法在下面
  - path为子文件为目录

- ![](https://gitee.com/BothSavage/PicGo/raw/master/image/1602233669972-05e582dc-5379-4b2b-ac8d-4ecb1789093c.png)

### snipaste

这是一款强大的截图工具，默认热键为F1，截图完成后点击右下角复制到剪切板，再使用图床的默认热键，快速完成图片上传

![](https://gitee.com/BothSavage/PicGo/raw/master/image/20201226131235.png)

## 参考

[1].[Typora 完全使用详解](https://sspai.com/post/54912/)

[2].[PicGo图床使用](https://www.yuque.com/u2063760/xbfae1/mipufs)





