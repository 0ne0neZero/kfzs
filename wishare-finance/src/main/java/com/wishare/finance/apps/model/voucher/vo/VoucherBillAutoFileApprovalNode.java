package com.wishare.finance.apps.model.voucher.vo;

import cn.hutool.core.date.DateUtil;
import com.wishare.finance.infrastructure.remote.vo.bpm.ProgressNode;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author longhuadmin
 */
@Data
public class VoucherBillAutoFileApprovalNode {

    private static final String START_ROLE_NAME = "发起人";
    private static final String AGREE = "agree";
    private static final String REFUSE = "refuse";

    private String nodeName;
    private String departName;
    private String userName;
    private String handleDate;
    private String handleComment;

    public static VoucherBillAutoFileApprovalNode transferFromProgressNode(ProgressNode progressNode){
        VoucherBillAutoFileApprovalNode node = new VoucherBillAutoFileApprovalNode();
        node.setNodeName(progressNode.getName());
        if (CollectionUtils.isNotEmpty(progressNode.getOrgNames())){
            node.setDepartName(StringUtils.join(progressNode.getOrgNames(), ","));
        }
        if (Objects.nonNull(progressNode.getUser())){
            node.setUserName(progressNode.getUser().getName());
        }
        if (Objects.nonNull(progressNode.getFinishTime())){
            node.setHandleDate(DateUtil.format(progressNode.getFinishTime(), "yyyy-MM-dd"));
        }
        if (StringUtils.equals(START_ROLE_NAME, progressNode.getName())){
            node.setHandleComment(StringUtils.EMPTY);
        } else {
            if (StringUtils.isBlank(progressNode.getResult())){
                node.setHandleComment("未审批");
            } else if (StringUtils.equals(AGREE, progressNode.getResult())){
                node.setHandleComment("同意");
            } else if (StringUtils.equals(REFUSE, progressNode.getResult())){
                node.setHandleComment("拒绝");
            }
        }
        return node;
    }

}
