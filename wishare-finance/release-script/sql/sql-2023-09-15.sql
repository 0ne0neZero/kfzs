-- 林修楠-start-处理历史票据中账单缴费日期
CREATE PROCEDURE updateInvoiceBillAfterPayment()
BEGIN
	DECLARE end_flag int DEFAULT 0;
	DECLARE invoiceReceiptId bigint;
	DECLARE hasPayTime int DEFAULT 0;
	DECLARE ird_result CURSOR FOR SELECT ird.invoice_receipt_id, COUNT(IFNULL(ird.bill_pay_time, null)) AS has_pay_time FROM wishare_finance.invoice_receipt_detail AS ird GROUP BY ird.invoice_receipt_id;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET end_flag = 1;
OPEN ird_result;
REPEAT FETCH ird_result INTO invoiceReceiptId, hasPayTime;
UPDATE invoice_receipt SET after_payment = CASE WHEN hasPayTime = 0 THEN 0 WHEN hasPayTime > 0 THEN 1 ELSE NULL	END	WHERE id = invoiceReceiptId;
UNTIL end_flag END REPEAT;
CLOSE ird_result;
END
-- -------
CREATE PROCEDURE updateInvoiceBillPayTime()
BEGIN
	DECLARE tableIndex int DEFAULT 0;
	WHILE tableIndex < 256 DO SET @receivableUpdateSql = CONCAT('UPDATE invoice_receipt_detail AS ird, receivable_bill_', tableIndex, ' AS rb SET ird.bill_pay_time = rb.pay_time WHERE ird.bill_id = rb.id AND ird.bill_type = rb.bill_type');
PREPARE stmt1 from @receivableUpdateSql;
EXECUTE stmt1;
DEALLOCATE PREPARE stmt1;
SET tableIndex = tableIndex + 1;
END WHILE;
	SET @advanceUpdateSql = CONCAT('UPDATE invoice_receipt_detail AS ird, advance_bill AS rb SET ird.bill_pay_time = rb.pay_time WHERE ird.bill_id = rb.id AND ird.bill_type = 2');
PREPARE stmt2 from @receivableUpdateSql;
EXECUTE stmt2;
DEALLOCATE PREPARE stmt2;
CALL updateInvoiceBillAfterPayment();
END
-- 林修楠-start-处理历史票据中账单缴费日期
-- 林修楠-start-初始化中交logo（中交正式环境需要）
INSERT INTO `wishare_cfg`.`cfg_dictionary_item`(`id`, `dictionaryCode`, `tenantId`, `orgId`, `name`, `code`, `parentId`, `sort`, `level`, `remarks`, `disabled`, `deleted`, `sourceSys`, `creator`, `gmtCreate`, `operator`, `gmtModify`) VALUES ('101296971825513', 'TENANT_ICON', '0', 0, '108314314140208/org/tenant/OrgTenantE/118092113353101/1668511262152101.png', 'invoice_logo', '0', 0, 1, NULL, 0, 0, 1, NULL, NULL, NULL, NULL);
-- 林修楠-start-初始化中交logo（中交正式环境需要）