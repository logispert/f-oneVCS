package skcc.com.fashiononeapp.util;

/**
 * Created by 05526 on 2017-06-20.
 */

public class UserInfo {

    private UserInfo() {}
    private static UserInfo userInfo;

    public static UserInfo getUser() {
        if (userInfo == null) {
            userInfo = new UserInfo();
        }
        return userInfo;
    }

    private String sessionId = "";

    private String userId = "";
    private String userNm = "";
    private String brandCode = "";
    private String shopCd = "";

    public boolean isLogin() {
        return userNm == null || !"".equals(userNm);
    }

    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNm() {
        return userNm;
    }
    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }

    public String getBrandCode() {
        return brandCode;
    }
    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getShopCd() {
        return shopCd;
    }
    public void setShopCd(String shopCd) {
        this.shopCd = shopCd;
    }
}
