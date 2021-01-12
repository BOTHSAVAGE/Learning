---
title: Commit规范和Idea插件
date: 2021-01-02  12:40:11
categories:
- 杂项
tags:
- 工具
- idea
- git
---
## 摘要

* 在Idea中安装commit模板信息
* commit规范模板格式介绍
<!--more-->
## 简介

此commit规范是基于augular，规范的编写Commit可以在Idea或者命令行中快速定位修改，或者快速定位问题编号等。

## Idea安装插件

> 插件名称：Git Commit Template

安装完成后在commit框点击如下按钮

![image-20210105175612157](https://gitee.com/BothSavage/PicGo/raw/master/image-20210105175612157.png)

模板展示

![image-20210105181008428](https://gitee.com/BothSavage/PicGo/raw/master/image-20210105181008428.png)

## 格式

### 模板

```bash
<type>(<scope>): <subject>
// 空一行
<body>
// 空一行
<footer>
```

### Header

#### type

- **feat：新功能（feature）**
- **fix：修补bug**
- docs：文档（documentation）
- style： 格式（不影响代码运行的变动,空格,格式化,等等）
- refactor：重构（即不是新增功能，也不是修改bug的代码变动
- perf: 性能 (提高代码性能的改变)
- test：增加测试或者修改测试
- build: 影响构建系统或外部依赖项的更改(maven,gradle,npm 等等)
- ci: 对CI配置文件和脚本的更改
- chore：对非 src 和 test 目录的修改
- revert: Revert a commit

> 最常用的就是feat和fix

#### scope

scope用于说明 commit 影响的范围，比如数据层、控制层、视图层等等，视项目不同而不同

#### subject

subject是 commit 目的的简短描述，不超过50个字符，主要介绍此次代码变更的主要内容

### body

本次commit的详细描述

### footer

* Breaking Changes

  不兼容变动

* Closed issues

  关闭的问题号或者其他指示信息

## 例子

**新增功能** 

```

feat(xxx模块): 增加xx功能

新建了xxxx，增加了xxx功能

```

**修改BUG**

```

feat(xxx模块): 修复了xx问题

这个问题的原因是xxx，已经更改xxx文件中的xxx代码，本地测试通过

Closes #1234

```



## 参考

[1].[Commit message 和 Change log 编写指南](http://www.ruanyifeng.com/blog/2016/01/commit_message_change_log.html)

[2].[Git Commit提交规范和IDEA插件Git Commit Template的使用](https://blog.csdn.net/qq_35854212/article/details/103856299)

