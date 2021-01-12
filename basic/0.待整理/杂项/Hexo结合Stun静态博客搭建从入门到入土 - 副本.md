---
title: Hexo结合Stun静态博客搭建从入门到入土
date: 2020-12-19  20:40:11
categories:
- 杂项
tags:
- 前端
- 建站
---
## 摘要
* 安装npm，安装hexo相关依赖，安装主题stun
* 修改hexo配置，修改stun配置，部署到github，gitee实现静态访问
* 给博客加上全局搜索，访问量统计
* hexo博客编写模板

 <!--more--> 

> tips: 以下{$xxx}均为用户自定义

## 搭建环境

### 创建仓库

1.github

> 仓库命名为：{$你的ID}.github.io

2.gitee

> 仓库命名为：{$你的ID}  

***仓库名大小写一致，访问路径也是***

### 环境安装

1.安装npm

> https://nodejs.org/en/download/

2.安装cnpm

```
npm install cnpm
```

3.安装博客搭建框架hexo

```
cnpm install -g hexo-cli
```

4.新建一个文件夹，并且之后所有操作都基于该文件夹

```
mkdir {$xxx}
cd {$xxx}
```

5.安装依赖，下载主题

```
cnpm install --save hexo-deployer-git 
cnpm install --save hexo-render-pug
cnpm install --save hexo-generator-search 
git clone https://github.com/liuyib/hexo-theme-stun.git themes/stun
```

6.增加页面导航

```
hexo new page categories
hexo new page tags
```

> 项目目录/source/categories/index.md  加上
>
> ```
> type: categories
> ```
>
> 项目目录/source/tags/index.md  加上
>
> ```
> type: tags
> ```

### 修改配置文件

1.修改部署文件夹下/_config.yml（挑选重要部分修改）

```
#展示设置
title: {$自定义文字}
language: zh-CN 
url: {$博客地址}

#主题
theme: stun

#git相关
deploy:
  type: git
  repo: 
  	github: {$你的博客仓库}
  	gitee: {$你的博客仓库}
  branch: master

#搜索相关
search:
  path: search.json
  field: post
  content: true
```

2.修改部署文件夹下/themes/stun/_config.yml(挑选重要部分修改)

```
#取消导航栏注释（注意空格对齐）
menu:
  categories: /categories/ || fas fa-layer-group
  tags: /tags/ || fas fa-tags
  
#更改图标(最好使用链接地址，我这里使用的图床，图床使用见下一篇博客)
favicon:
  small: https://gitee.com/BothSavage/PicGo/raw/master/image/favicon-16x16.png
  medium: https://gitee.com/BothSavage/PicGo/raw/master/image/favicon-32x32.png

#更改背景大图
header:
  bg_image:
    enable: true
    url: https://gitee.com/BothSavage/PicGo/raw/master/image/背景.png

#更改头像大图，并设为圆形
author:
  avatar:
    url: https://gitee.com/BothSavage/PicGo/raw/master/image/头像.png
    rounded: true
    
#打开访问统计
busuanzi:
  enable: true
  
#打开搜索功能
local_search:
  enable: true

```

### 运行

1.基础命令

```
hexo c  #清理一下
hexo g  #生成
hexo s  #部署到本地4000端口
hexo d  #部署到远程Github仓库
```

2.推荐命令

```
#第一次尝试本地是否能访问
hexo c  && hexo g && hexo s
#以后每次添加文章
hexo c  && hexo g && hexo d
```

### 博客文件存放地点

> 项目目录/source/_posts

## markdown文件规范

1.文章元数据

```
#分别指定文章标题，时间，类别，标签
---
title: Hexo结合Stun静态博客搭建从入门到入土
date: 2020-12-19  20:40:11
categories:
- 杂项
tags:
- 前端
- 建站
---
```

2.主页不显示文章全部

```
 上面是显示内容
 <!--more--> 
```

## 其他高级功能

stun主题支持

* 评论系统
* 站长工具
* 数据公式
* 网站特效
* ................

详情请查看

https://theme-stun.github.io/docs/zh-CN/

## 参考

[1].[菜鸟教程：NodeJS安装配置](https://www.runoob.com/nodejs/nodejs-install-setup.html)

[2].[Stun主题指南](https://theme-stun.github.io/docs/zh-CN/)

[3].[静态博客搭建](https://www.yuque.com/u2063760/xbfae1/gf45k1)

[4].[_more截断文章_多标签添加](https://blog.csdn.net/Aoman_Hao/article/details/79291741)

[5].[在gitee上部署静态网站（或者个人博客）](https://blog.csdn.net/zhangyu4863/article/details/80473412)

