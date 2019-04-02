package com.example.consumer.bean;

import java.util.Date;

/**
 * @author shenzm
 * @date 2019-4-2
 * @description 作用
 */
public class SecuMain {

    //ID,InnerCode,CompanyCode,SecuCode,ChiName,ChiNameAbbr,SecuAbbr,ChiSpelling,SecuMarket,SecuCategory,ListedDate

    private Long ID;

    /**
     * 证券内部编码
     */
    private int InnerCode;

    /**
     * 公司代码
     */
    private int CompanyCode;

    /**
     * 证券代码
     */
    private String SecuCode;

    /**
     * 中文名称
     */
    private String ChiName;

    /**
     * 中文名称缩写
     */
    private String ChiNameAbbr;


    /**
     * 证券简称
     */
    private String SecuAbbr;

    /**
     * 拼音证券简称
     */
    private String ChiSpelling;

    /**
     * 证券市场
     */
    private int SecuMarket;

    /**
     * 证券类别
     */
    private int SecuCategory;

    /**
     * 上市日期
     */
    private Date ListedDate;

    /**
     * 上市板块
     */
    private int ListedSector;

    /**
     * 上市状态
     */
    private int ListedState;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public int getInnerCode() {
        return InnerCode;
    }

    public void setInnerCode(int innerCode) {
        InnerCode = innerCode;
    }

    public int getCompanyCode() {
        return CompanyCode;
    }

    public void setCompanyCode(int companyCode) {
        CompanyCode = companyCode;
    }

    public String getSecuCode() {
        return SecuCode;
    }

    public void setSecuCode(String secuCode) {
        SecuCode = secuCode;
    }

    public String getChiName() {
        return ChiName;
    }

    public void setChiName(String chiName) {
        ChiName = chiName;
    }

    public String getChiNameAbbr() {
        return ChiNameAbbr;
    }

    public void setChiNameAbbr(String chiNameAbbr) {
        ChiNameAbbr = chiNameAbbr;
    }

    public String getSecuAbbr() {
        return SecuAbbr;
    }

    public void setSecuAbbr(String secuAbbr) {
        SecuAbbr = secuAbbr;
    }

    public String getChiSpelling() {
        return ChiSpelling;
    }

    public void setChiSpelling(String chiSpelling) {
        ChiSpelling = chiSpelling;
    }

    public int getSecuMarket() {
        return SecuMarket;
    }

    public void setSecuMarket(int secuMarket) {
        SecuMarket = secuMarket;
    }

    public int getSecuCategory() {
        return SecuCategory;
    }

    public void setSecuCategory(int secuCategory) {
        SecuCategory = secuCategory;
    }

    public Date getListedDate() {
        return ListedDate;
    }

    public void setListedDate(Date listedDate) {
        ListedDate = listedDate;
    }

    public int getListedSector() {
        return ListedSector;
    }

    public void setListedSector(int listedSector) {
        ListedSector = listedSector;
    }

    public int getListedState() {
        return ListedState;
    }

    public void setListedState(int listedState) {
        ListedState = listedState;
    }
}
