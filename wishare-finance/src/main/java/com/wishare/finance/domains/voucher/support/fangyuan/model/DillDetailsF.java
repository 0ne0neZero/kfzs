package com.wishare.finance.domains.voucher.support.fangyuan.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@XStreamAlias("item")
public class DillDetailsF {
    private String pk_org;
    private String pk_pcorg;
    private String pk_fiorg;
    private String sett_org;
    private String so_deptid;
    private String so_psndoc;
    private String so_ordertype;
    private String so_transtype;
    private String so_org;
    private String material;
    private String customer;
    private String postunit;
    private String postpricenotax;
    private String pk_subjcode;
    private String postquantity;
    private String postprice;
    private String task;
    private Integer coordflag;
    private String equipmentcode;
    private String productline;
    private String cashitem;
    private String bankrollprojet;
    private String pausetransact = "N";
    private String billdate;
    private String pk_group = "0001";
    private String pk_billtype = "F0";
    private String billclass = "ys";
    private String pk_tradetype = "F0-Cxx-02";
    private String pk_recitem;
    private String busidate;
    private String pk_recbill;
    private String billno;
    private Integer objtype = 0;
    private Integer rowno = 0;
    private Integer rowtype = 0;
    private Integer direction = 1;
    private String pk_ssitem;
    private String scomment;
    private String subjcode;
    private String pk_currtype = "CNY";
    private String rate = "1.00000000";
    private String pk_deptid;
    private String pk_psndoc;
    private String money_de;
    private String local_money_de;
    private String quantity_de;
    private String money_bal;
    private String local_money_bal;
    private String quantity_bal = "0.00000000";
    private String local_tax_de;
    private String notax_de;
    private String local_notax_de;
    private String price = "0.00000000";
    private String taxprice = "0.00000000";
    private BigDecimal taxrate;
    private Integer taxtype = 1;
    private String pk_payterm;
    private String payaccount;
    private String recaccount;
    private String assetpactno;
    private String invoiceno;
    private String pk_jobphase;
    private String pk_job;
    private String checkelement;
    private String grouprate;
    private String globalrate;
    private Integer groupdebit;
    private Integer globaldebit;
    private String groupbalance;
    private String globalbalance;
    private Integer groupnotax_de;
    private Integer globalnotax_de;
    private String occupationmny;
    private String project_task;
    private String settleno;
    private String costcenter;
    private String confernum;
    private String rececountryid;
    private Integer buysellflag = 2;
    /**
     * 结算方式
     */
    private String pk_balatype;
    private String def1 = "";
    private String def2 = "";
    private String def3;
    private String def4;
    private String def5;
    private String def6;
    private String def7;
    private String def8;
    private String def9;
    private String def10;
    private Integer def11;
    private String def12;
    private String def13;
    private Integer def14;
    private String def15;
    private String def16;
    private Integer def17;
    private String def20;
    private String def27;
    private String def29;
}
