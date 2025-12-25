package com.wishare.finance.domains.voucher.support.fangyuan.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


import java.util.Date;
import java.util.List;

@Getter
@Setter
@XStreamAlias("billhead")
public class BillHeadF {

    private String pk_group = "0001";
    private String pk_org;
    private String pk_fiorg;
    private String pk_pcorg;
    private String sett_org;
    private String isreded = "N";
    private String officialprintuser;
    private String officialprintdate;
    private String modifiedtime;
    private String creationtime;
    private String creator = "zhidr";
    private String pk_billtype = "F0";
    private String custdelegate;
    private String pk_corp;
    private String modifier;
    private String pk_tradetype = "F0-Cxx-02";
    private String billclass = "ys";
    private String pk_recbill;
    private Integer accessorynum = 0;
    private String subjcode;
    private String isflowbill = "N";
    private String confirmuser;
    private String isinit = "N";
    private String billno;
    private String billdate;
    private Integer src_syscode = 17;
    private Integer billstatus = 1;
    private Integer syscode = 0;
    private String billmaker = "WYYH";
    private String approver = "WYYH";
    private String approvedate;
    private String lastadjustuser;
    private String pk_busitype = "选择收款";
    private String money;
    private String local_money;
    private String rate = "1.00000000";
    private String billyear;
    private String billperiod;
    private String scomment;
    private Integer effectstatus = 10;
    private String effectuser;
    private String effectdate;
    private String lastapproveid;
    private String grouplocal;
    private String globallocal;
    private String grouprate = "1.00000000";
    private String globalrate = "1.00000000";
    private String checkelement;
    private String so_deptid;
    private String so_psndoc;
    private String so_org;
    private String cashitem;
    private String bankrollprojet;
    private String pk_deptid;
    private String pk_psndoc;
    private String customer;
    private String pk_subjcode;
    private Integer objtype;
    private String recaccount;
    private String payaccount;
    private String costcenter;
    private String def1;
    private String def13;
    private String def14;
    private String def15;
    private String def16;
    private String def28;
    private Long def29;
    private String def30 = "wy";
    private List<DillDetailsF> bodys;
    private String coordflag = "0";
    private String busidate;
}
