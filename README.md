# Game Sales API

## 项目简介

Game Sales API 是一个基于 Spring Boot 和 JdbcTemplate 的 RESTful 服务，用于管理和查询游戏销售数据。该 API 支持批量导入游戏销售数据、分页查询以及按条件统计销售数据。

## 目录

- [项目简介](#项目简介)
- [技术栈](#技术栈)
- [功能](#功能)
- [环境配置](#环境配置)
- [构建和运行](#构建和运行)
- [API 文档](#api-文档)
- [示例请求](#示例请求)

## 技术栈

- Java 8
- Spring Boot 2.1.5
- Spring JDBC
- MySQL
- HikariCP
- Maven

## 功能

1. 批量导入游戏销售数据
2. 分页查询游戏销售数据
3. 按条件查询游戏销售数据
4. 统计销售数据

## 环境配置

### 必要条件

- Java 8 或更高版本
- MySQL 数据库
- Maven 3.6 或更高版本

### 数据库配置

1. 创建数据库：
    ```sql
    CREATE DATABASE db_demo;
    ```

2. 创建数据表：
    ```sql
    CREATE TABLE `db_demo`.`game_sales`  (
      `id` int(11) NOT NULL AUTO_INCREMENT,
      `game_no` int(11) NOT NULL,
      `game_name` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
      `game_code` varchar(5) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
      `type` tinyint(2) NOT NULL,
      `cost_price` decimal(5, 2) NOT NULL,
      `tax` decimal(5, 2) NOT NULL,
      `sale_price` decimal(5, 2) NOT NULL,
      `date_of_sale` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
      PRIMARY KEY (`id`) USING BTREE,
      INDEX `index_date_of_sale`(`date_of_sale`) USING BTREE,
      INDEX `index_sale_price`(`sale_price`) USING BTREE,
      INDEX `index_game_no`(`game_no`) USING BTREE
    ) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;    


   CREATE TABLE `db_demo`.`csv_imports`  (
     `id` bigint(20) NOT NULL AUTO_INCREMENT,
     `file_name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
     `status` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
     `total_rows` int(11) NULL DEFAULT 0,
     `success_rows` int(11) NULL DEFAULT 0,
     `error_rows` int(11) NULL DEFAULT 0,
     `start_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
     `end_time` timestamp(0) NULL DEFAULT NULL,
     PRIMARY KEY (`id`) USING BTREE
   ) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;
 
 
   CREATE TABLE `db_demo`.`csv_import_errors`  (
     `id` bigint(20) NOT NULL AUTO_INCREMENT,
     `import_id` bigint(20) NOT NULL,
     `row_number` int(11) NOT NULL,
     `error_message` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
     PRIMARY KEY (`id`) USING BTREE,
     INDEX `import_id`(`import_id`) USING BTREE
   ) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic; 
 
    ```

### 配置文件

在 `src/main/resources/application.properties` 中配置数据库连接信息：对应自己的数据库监听端口号，用户名和密码

```properties
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/db_demo?useUnicode=true&characterEncoding=utf-8
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.driver-class-name=com.mysql.jdbc.Driver

spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB
```

## 构建和运行
用IDEA导入Maven工程，使用Maven构建，安装依赖和构建环境
进入`src/main/java/com/cn/demo 运行DemoApplication`，启动服务，默认监听端口8000

## API 文档
生成符合条件的CSV文件
- 进入`src/main/java/com/cn/demo/controler/GenerateCsv.java`,运行main方法生成1000000行的csv文件

导入游戏销售数据
- URL: `http://127.0.0.1:8000/import`
- 方法: `POST`
- 请求体: `multipart/form-data`，参数file, 包含一个 CSV 文件
- 描述: 批量导入游戏销售数据

分页查询游戏销售数据
- URL: `http://127.0.0.1:8000/getGameSales`
- 方法: `GET`
- 请求体: `form-data`
- 参数:
    - `startDate` (可选): 开始日期，格式为 `yyyy-MM-dd HH:mm:ss`
    - `endDate` (可选): 开始日期，格式为 `yyyy-MM-dd HH:mm:ss`
    - `minSalePrice` (可选): 低于该销售价格
    - `maxSalePrice` (可选): 高于该销售价格
    - `page` (可选): 页码，默认为 1
    - `size` (可选): 每页记录数，默认为 100  
- 描述: 按条件分页查询游戏销售数据

统计销售数据
- URL: `http://127.0.0.1:8000/getTotalSales`
- 方法: `GET`
- 请求体: `form-data`
- 参数:
    - `startDate` (可选): 开始日期，格式为 `yyyy-MM-dd HH:mm:ss`
    - `endDate` (可选): 开始日期，格式为 `yyyy-MM-dd HH:mm:ss`
    - `gameNo` (可选): 游戏编号
- 描述: 按条件统计销售数据







