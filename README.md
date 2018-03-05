# spider
爬虫项目

#使用的主要技术：
JDK1.8 使用1.8新特性。
SpringBoot1.5.6 可以快速开发web项目。简化配置，方便开发和部署。
Mybatis3.4.4 作为持久化框架，更加灵活查询。使用druid数据库连接池，配置多数据源做到读写分离，Mybatis配置分页插件，更灵活分页的查询。
Thymeleaf 2.1.5 是个XML/XHTML/HTML5模板引擎,支持html原型的自然引擎,它在html标签增加额外的属性来达到模板+数据的展示方式，由于浏览器解释html时，忽略未定义的标签属性，因此thymeleaf的模板可以静态运行,并且提供了丰富的表达式，方便处理数据。
Webmagic 0.7.3， WebMagic是一个简单灵活的Java爬虫框架。特性：简单的API，可快速上手；模块化的结构，可轻松扩展；提供多线程和分布式支持
工具包使用，google guava,apache-commons包，fastjson

#核心方案
梳理当前日历导出，导入功能，梳理代理IP爬取和使用流程；
新建项目，新建库，迁移日历和代理ip相关功能；
提供接口让日志同步通能正常使用；
爬取Airbnb房源日历等数据分析；
提供查询界面，设置界面；

