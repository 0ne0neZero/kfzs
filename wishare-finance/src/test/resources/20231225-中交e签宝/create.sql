CREATE TABLE wishare_finance.invoice_flow_monitor (
	id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
	receipt_id BIGINT COMMENT '收据id',
	invoice_id BIGINT COMMENT '开票id',
	step_type TINYINT COMMENT '步骤类型',
	step_description VARCHAR(128) COMMENT '步骤描述(参考)',
	receipt_parameters JSON COMMENT '开收据入参',
	invoice_parameters JSON COMMENT '开发票入参',
	remark JSON COMMENT '错误信息',
	tenant_id VARCHAR(40) NOT NULL COMMENT '租户ID',
	creator VARCHAR(40) DEFAULT NULL COMMENT '创建人id',
	creator_name VARCHAR(40) DEFAULT NULL COMMENT '创建人名称',
	gmt_create DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	gmt_modify DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
	operator VARCHAR(40) DEFAULT NULL COMMENT '修改人id',
	operator_name VARCHAR(40) DEFAULT NULL COMMENT '修改人名称',
	deleted TINYINT DEFAULT 0 COMMENT '是否删除（0否1是）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='开票监控流程';