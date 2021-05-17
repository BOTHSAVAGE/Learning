用户 网站 内容 



推荐系统的基本思想

* 用户和内容的特征信息
* 用户喜欢过的相似内容
* 相似用户喜欢过的内容

![image-20210517100445211](https://gitee.com/BothSavage/PicGo/raw/master//image/20210517100445.png)

![image-20210517100930456](https://gitee.com/BothSavage/PicGo/raw/master//image/20210517100930.png)

![image-20210517101141508](https://gitee.com/BothSavage/PicGo/raw/master//image/20210517101141.png)

![image-20210517101338787](https://gitee.com/BothSavage/PicGo/raw/master//image/20210517101338.png)

![image-20210517101435457](https://gitee.com/BothSavage/PicGo/raw/master//image/20210517101435.png)





协同：用户+内容 ，可以描述为用户产生的行为

![image-20210517101542481](https://gitee.com/BothSavage/PicGo/raw/master//image/20210517101542.png)





CF:最坏的情况下，只要拿到日志就可以做推荐 -> 根据内容马上做推荐

![image-20210517101843205](https://gitee.com/BothSavage/PicGo/raw/master//image/20210517101843.png)

如果是电影网站的打分或者是收藏的话，说明矩阵为稀疏矩阵









冷启动的时候不能使用CF推荐，一定要有操作日志什么的







![image-20210517102408129](https://gitee.com/BothSavage/PicGo/raw/master//image/20210517102408.png)

![image-20210517102705409](https://gitee.com/BothSavage/PicGo/raw/master//image/20210517102705.png)

![image-20210517102734832](https://gitee.com/BothSavage/PicGo/raw/master//image/20210517102734.png)







![image-20210517103230754](https://gitee.com/BothSavage/PicGo/raw/master//image/20210517103230.png)

![image-20210517103645363](https://gitee.com/BothSavage/PicGo/raw/master//image/20210517103645.png)

![image-20210517103859729](https://gitee.com/BothSavage/PicGo/raw/master//image/20210517103859.png)

![image-20210517103923779](https://gitee.com/BothSavage/PicGo/raw/master//image/20210517103923.png)