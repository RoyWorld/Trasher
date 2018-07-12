# Project Description
这个工具是用于对数据库中的表产生大量数据, 方便开发和测试功能

# About Project
## project structure
![Project Structure](https://raw.githubusercontent.com/RoyWorld/Trasher/master/core/src/main/resources/images/projectStructure.png)

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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='XX表|CreateBaseDomain\r\n系统XX表';
```
用Spring来管理公共组件, 用logback来打印日志信息

### the order of data generation
各个表的数据生成顺序是通过以下几步确定的
1 通过将表间的依赖关系转化成AOV网
2 对AOV网进行拓扑排序
3 最后由拓扑排序的逆序确定每个表的数据生成顺序

![Project Structure](https://raw.githubusercontent.com/RoyWorld/Trasher/master/core/src/main/resources/images/DBRelationshipModel.png)
上图的拓扑排序和逆序如下:
```xml
<!-- topological Sort -->
A->B->E->F->C->D
<!--reverse topological Sort -->
D->C->F->E->B->A
```

### data generation
这里运用了工厂方法模式并配合Spring来做配置，每个表既可以配置一个special data generator，也可以使用generic data generator。 
如果要配置special data generator， 则要在`table_data.xml`中声明相关配置，否则将默认使用generic data generator来生成数据

### test table
```sql
CREATE TABLE `account` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(64) DEFAULT NULL COMMENT 'code',
  `user_name` varchar(64) DEFAULT NULL COMMENT '用户名',
  `user_password` varchar(64) DEFAULT NULL COMMENT '用户密码',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态：1-可用；2-不可用',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建人',
  `modified_time` bigint(20) DEFAULT NULL COMMENT '修改时间',
  `modified` varchar(64) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='账号表|CreateBaseDomain\r\n系统账号表';

CREATE TABLE `custmor` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(64) DEFAULT NULL COMMENT 'code',
  `custmor_name` varchar(64) DEFAULT NULL COMMENT '用户名',
  `phone_number` varchar(64) DEFAULT NULL COMMENT '电话号码',
  `qq_number` varchar(64) DEFAULT NULL COMMENT 'QQ号码',
  `wechat` varchar(64) DEFAULT NULL COMMENT '微信',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态：1-可用；2-不可用',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建人',
  `modified_time` bigint(20) DEFAULT NULL COMMENT '修改时间',
  `modified` varchar(64) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100001 DEFAULT CHARSET=utf8 COMMENT='客户表|CreateBaseDomain\r\n系统客户表';

CREATE TABLE `order` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(64) DEFAULT NULL COMMENT 'code',
  `custmor_id` int(10) unsigned DEFAULT '1' COMMENT '客户id|custmor',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `total_price` decimal(9,2) DEFAULT NULL COMMENT '总价',
  `final_price` decimal(9,2) DEFAULT NULL COMMENT '成交价',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态：1-可用；2-不可用',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建人',
  `modified_time` bigint(20) DEFAULT NULL COMMENT '修改时间',
  `modified` varchar(64) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  KEY `custmor_id_index` (`custmor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100001 DEFAULT CHARSET=utf8 COMMENT='订单表|CreateBaseDomain\r\n系统订单表';

CREATE TABLE `order_detail` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(64) DEFAULT NULL COMMENT 'code',
  `order_id` int(10) unsigned NOT NULL COMMENT '订单id|order',
  `stock_id` int(10) unsigned NOT NULL COMMENT '库存id|stock',
  `amount` int(11) DEFAULT '0' COMMENT '数量',
  `price` decimal(9,2) DEFAULT NULL COMMENT '售价',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态：1-可用；2-不可用',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建人',
  `modified_time` bigint(20) DEFAULT NULL COMMENT '修改时间',
  `modified` varchar(64) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  KEY `order_id_index` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100001 DEFAULT CHARSET=utf8 COMMENT='订单明细表|CreateBaseDomain\r\n系统订单明细表';

CREATE TABLE `stock` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(64) DEFAULT NULL COMMENT 'code',
  `product_name` varchar(255) DEFAULT NULL COMMENT '产品名',
  `cost` decimal(9,2) DEFAULT NULL COMMENT '成本',
  `price` decimal(9,2) DEFAULT NULL COMMENT '售价',
  `amount` int(11) DEFAULT '0' COMMENT '数量',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态：1-可用；2-不可用',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建人',
  `modified_time` bigint(20) DEFAULT NULL COMMENT '修改时间',
  `modified` varchar(64) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100001 DEFAULT CHARSET=utf8 COMMENT='库存表|CreateBaseDomain\r\n系统库存表';
```


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