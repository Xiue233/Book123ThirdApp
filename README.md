# 无名图书第三方App

本软件为[无名图书](http://www.book123.info/)的第三方App，仅供学习交流使用，本软件对本站内容不负任何责任。

## 功能

- [x] 书籍搜索
- [x] 书籍详情查看
- [x] 书籍分类浏览
- [x] 热门书籍推荐
- [ ] 书籍下载
- [ ] 下载配置
- [ ] 收藏书籍

## 相关API

### SimpleSearch

https://www.book123.info/api/simple_search?count=5&page=1&key={key}

### 搜索

https://www.book123.info/api/search

可选参数：
count: int 单次查询的图书数量
page: int 分页序数
type: string 排序方式(有:lastUpdate、pubDate、rate)
hasFile: 1或0 图片是否具有可下载的资源文件
isExact: 1或0 搜索时，是否要求书籍名与Key完全相同(仅在使用Key搜索时使用)

参数四选一:
tag: 标签
key: 书名
author: 作者
publisher: 出版商

### 最近热门

https://www.book123.info/api/recentHot?tag={tag}

### 推荐图书

https://www.book123.info/api/getRelatedBook?isbn={isbn}

### 查询电子书文件是否存在

https://static2.file123.info/api/checkHasFile?fileName={isbn}.{fileType}

# LICENSE

本软件遵循 **GNU AFFERO GENERAL PUBLIC LICENSE Version 3, 19 November 2007** 开源协议。

This opensource app obeys **GNU AFFERO GENERAL PUBLIC LICENSE Version 3, 19 November 2007** LICENSE.
