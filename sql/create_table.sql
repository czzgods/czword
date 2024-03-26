# 数据库初始化
create
    database if not exists word;

use word;
-- 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                         `userAccount` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账号',
                         `userPassword` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
                         `email` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
                         `userName` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
                         `userAvatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户头像',
                         `userRole` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'user' COMMENT '用户角色：user/admin/ban',
                         `accessKey` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'accessKey',
                         `secretKey` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'secretKey',
                         `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                         PRIMARY KEY (`id`) USING BTREE,
                         INDEX `idx_email`(`email` ASC) USING BTREE
) COMMENT = '用户表';

drop table log;
create
    database if not exists log;
create table log
(
    id           bigint auto_increment comment '主键'
        primary key,
    URL          varchar(200) not null comment '请求URL',
    BusinessName varchar(200) not null comment '描述信息',
    HttpMethod   varchar(200) not null comment '请求响应格式',
    TypeName     varchar(200) not null comment '调用方法的全路径名',
    MethodName   varchar(200) not null comment '调用的方法名',
    RemoteHost   varchar(200) not null comment '请求的IP',
    Args         varchar(200) not null comment '传入的参数',
    exception    varchar(256) comment '异常',
    ret          varchar(200)  comment '返回值',
    cost_time    bigint       null comment '方法执行耗时, 单位:ms',
    creat_time   datetime     null comment '操作时间'
)
    comment '操作日志表';
