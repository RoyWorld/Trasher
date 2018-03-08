# Project Description
这个工具是用于对数据库中的表产生大量数据，方便开发和测试功能

# About Project
## project structure
![Project Structure](https://raw.githubusercontent.com/RoyWorld/Trasher/master/src/main/resources/images/projectStructure.png)

## how to use
### about dependency
由于这里使用了`Dolphins`来读取database, 所以table的DDL要满足以下这种格式:
```sql
CREATE TABLE `tb_xx` (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `creator` varchar(36) DEFAULT NULL COMMENT '创建人',
  `lastModifier` varchar(36) DEFAULT NULL COMMENT '最后修改人',
  `createDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新建时间',
  `lastModify` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='XX表|CreateBaseDomain\r\n系统XX表';
```
用了Spring来管理公共组件，用logback来打印日志信息

### the order of data generation
通过将表间的依赖关系转化成AOV网
对AOV网进行拓扑排序
每个表的数据生成顺序是由拓扑排序的逆序所确定的

### data generation
这里运用了工厂方法模式并配合Spring来做配置，每个表既可以配置一个special data generator，也可以使用generic data generator。 
如果要配置special data generator， 则要在`table_data.xml`中声明相关配置，否则将默认使用generic data generator来生成数据

# project version
## v1.0.1
目前已实现的功能
* 根据数据库中的表生成相关数据
* 可配置数据的生成类
* 能够并发生成数据

待优化和增加的功能
* 优化配置数据生成类的设计
* 优化并发生成数据的速度
* 增加生成数据的进度日志打印功能