DROP TABLE IF EXISTS ACCOUNT;
CREATE TABLE ACCOUNT
(
    id            bigint       NOT NULL COMMENT 'id',
    created_by    bigint COMMENT '创建人',
    created_date  DATETIME DEFAULT  now() COMMENT '创建时间',
    updated_by    bigint COMMENT '更新人',
    updated_date  DATETIME DEFAULT now() COMMENT '更新时间',
    name          VARCHAR(255) NOT NULL COMMENT '账户名',
    nickname      VARCHAR(255) COMMENT '',
    three_account VARCHAR(255) COMMENT '',
    password      VARCHAR(255) NOT NULL COMMENT '密码',
    tel           VARCHAR(255) COMMENT '联系电话',
    email         VARCHAR(255) COMMENT '邮箱',
    head_img_url  VARCHAR(1024) COMMENT '头像地址',
    role_id       bigint       NOT NULL COMMENT '角色Id',
    enabled       INT      DEFAULT 1 COMMENT '',
    PRIMARY KEY (id)
) COMMENT = 'ACCOUNT';

DROP TABLE IF EXISTS ROLE;
CREATE TABLE ROLE
(
    id           bigint       NOT NULL COMMENT 'id',
    created_by   bigint COMMENT '创建人',
    created_date DATETIME DEFAULT now() COMMENT '创建时间',
    updated_by   bigint COMMENT '更新人',
    updated_date DATETIME DEFAULT now() COMMENT '更新时间',
    name         VARCHAR(255) NOT NULL COMMENT '角色名称',
    description  VARCHAR(255) COMMENT '描述信息',
    PRIMARY KEY (id)
) COMMENT = 'ROLE';

DROP TABLE IF EXISTS ACCOUNT_DEVICE_XREF;
CREATE TABLE ACCOUNT_DEVICE_XREF
(
    id           bigint NOT NULL COMMENT 'id',
    created_by   bigint COMMENT '创建人',
    created_date DATETIME DEFAULT now() COMMENT '创建时间',
    updated_by   bigint COMMENT '更新人',
    updated_date DATETIME DEFAULT now() COMMENT '更新时间',
    account_id   bigint NOT NULL COMMENT '',
    device_id    bigint NOT NULL COMMENT '',
    PRIMARY KEY (id)
) COMMENT = 'ACCOUNT_DEVICE_XREF';

DROP TABLE IF EXISTS SHARE_DEVICE;
CREATE TABLE SHARE_DEVICE
(
    id              bigint NOT NULL COMMENT 'id',
    created_by      bigint COMMENT '创建人',
    created_date    DATETIME DEFAULT now() COMMENT '创建时间',
    updated_by      bigint COMMENT '更新人',
    updated_date    DATETIME DEFAULT now() COMMENT '更新时间',
    from_account_id bigint NOT NULL COMMENT '',
    to_account_id   bigint NOT NULL COMMENT '',
    device_id       bigint NOT NULL COMMENT '',
    PRIMARY KEY (id)
) COMMENT = 'SHARE_DEVICE';

DROP TABLE IF EXISTS DEVICE;
CREATE TABLE DEVICE
(
    id           bigint       NOT NULL COMMENT 'id',
    created_by   bigint COMMENT '创建人',
    created_date DATETIME DEFAULT now() COMMENT '创建时间',
    updated_by   bigint COMMENT '更新人',
    updated_date DATETIME DEFAULT now() COMMENT '更新时间',
    device_id    VARCHAR(255) NOT NULL COMMENT '',
    name         VARCHAR(255) COMMENT '',
    imei         VARCHAR(255) COMMENT '',
    description  VARCHAR(1024) COMMENT '',
    PRIMARY KEY (id)
) COMMENT = 'DEVICE';

DROP TABLE IF EXISTS DEVICE_DATA_RECORD;
CREATE TABLE DEVICE_DATA_RECORD
(
    id           bigint       NOT NULL COMMENT 'id',
    created_by   bigint COMMENT '创建人',
    created_date DATETIME DEFAULT now() COMMENT '创建时间',
    updated_by   bigint COMMENT '更新人',
    updated_date DATETIME DEFAULT now() COMMENT '更新时间',
    device_id    bigint       NOT NULL COMMENT '',
    code         VARCHAR(255) NOT NULL COMMENT '',
    content      VARCHAR(1024) COMMENT '',
    ip_address   VARCHAR(255) COMMENT '',
    PRIMARY KEY (id)
) COMMENT = 'DEVICE_DATA_RECORD';

DROP TABLE IF EXISTS MESSAGE_CENTER;
CREATE TABLE MESSAGE_CENTER
(
    id           bigint        NOT NULL COMMENT 'id',
    created_by   bigint COMMENT '创建人',
    created_date DATETIME DEFAULT now() COMMENT '创建时间',
    updated_by   bigint COMMENT '更新人',
    updated_date DATETIME DEFAULT now() COMMENT '更新时间',
    account_id   VARCHAR(255)  NOT NULL COMMENT '',
    message      VARCHAR(1024) NOT NULL COMMENT '',
    unread       INT      DEFAULT 1 COMMENT '未读标记',
    PRIMARY KEY (id)
) COMMENT = 'MESSAGE_CENTER';