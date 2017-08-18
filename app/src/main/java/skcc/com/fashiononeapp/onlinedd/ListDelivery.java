package skcc.com.fashiononeapp.onlinedd;

import java.io.Serializable;

/**
 * Created by 07237 on 2017-07-20.
 */

public class ListDelivery implements Serializable {
    String seq;
    String reqDate;
    String ordNo;
    String styleCd;
    String clrCd;
    String sizeCd;
    String reqQty;
    String shopQty;
    String expFee;
    String status;
    String ordShopCd;
    String rtToShopCd;
    String tmCd;
    String shopZipCd;
    String shopAdrs1;
    String shopAdrs2;

    public ListDelivery(String seq, String reqDate, String ordNo, String styleCd, String clrCd, String sizeCd, String reqQty, String shopQty, String expFee, String status, String ordShopCd, String rtToShopCd, String tmCd, String shopZipCd, String shopAdrs1, String shopAdrs2) {
        this.seq = seq;
        this.reqDate = reqDate;
        this.ordNo = ordNo;
        this.styleCd = styleCd;
        this.clrCd = clrCd;
        this.sizeCd = sizeCd;
        this.reqQty = reqQty;
        this.shopQty = shopQty;
        this.expFee = expFee;
        this.status = status;
        this.ordShopCd = ordShopCd;
        this.rtToShopCd = rtToShopCd;
        this.tmCd = tmCd;
        this.shopZipCd = shopZipCd;
        this.shopAdrs1 = shopAdrs1;
        this.shopAdrs2 = shopAdrs2;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public String getOrdNo() {
        return ordNo;
    }

    public void setOrdNo(String ordNo) {
        this.ordNo = ordNo;
    }

    public String getStyleCd() {
        return styleCd;
    }

    public void setStyleCd(String styleCd) {
        this.styleCd = styleCd;
    }

    public String getClrCd() {
        return clrCd;
    }

    public void setClrCd(String clrCd) {
        this.clrCd = clrCd;
    }

    public String getSizeCd() {
        return sizeCd;
    }

    public void setSizeCd(String sizeCd) {
        this.sizeCd = sizeCd;
    }

    public String getReqQty() {
        return reqQty;
    }

    public void setReqQty(String reqQty) {
        this.reqQty = reqQty;
    }

    public String getShopQty() {
        return shopQty;
    }

    public void setShopQty(String shopQty) {
        this.shopQty = shopQty;
    }

    public String getExpFee() {
        return expFee;
    }

    public void setExpFee(String expFee) {
        this.expFee = expFee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrdShopCd() {
        return ordShopCd;
    }

    public void setOrdShopCd(String ordShopCd) {
        this.ordShopCd = ordShopCd;
    }

    public String getRtToShopCd() {
        return rtToShopCd;
    }

    public void setRtToShopCd(String rtToShopCd) {
        this.rtToShopCd = rtToShopCd;
    }

    public String getTmCd() {
        return tmCd;
    }

    public void setTmCd(String tmCd) {
        this.tmCd = tmCd;
    }

    public String getShopZipCd() {
        return shopZipCd;
    }

    public void setShopZipCd(String shopZipCd) {
        this.shopZipCd = shopZipCd;
    }

    public String getShopAdrs1() {
        return shopAdrs1;
    }

    public void setShopAdrs1(String shopAdrs1) {
        this.shopAdrs1 = shopAdrs1;
    }

    public String getShopAdrs2() {
        return shopAdrs2;
    }

    public void setShopAdrs2(String shopAdrs2) {
        this.shopAdrs2 = shopAdrs2;
    }
}
