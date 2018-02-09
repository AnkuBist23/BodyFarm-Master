package aman.backender.com.bodyfarm.retrofit;

import java.io.Serializable;
import java.util.List;

import aman.backender.com.bodyfarm.bean.GymUserInfo;
import aman.backender.com.bodyfarm.bean.UserInfo;

/**
 * Created by abc on 1/23/2018.
 */

public class RetrofitResponse implements Serializable {
    private Boolean returnCode;
    private String strMessage;
    private UserInfo userInfo;
    private List<GymUserInfo> arrUsers;

    public Boolean getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(Boolean returnCode) {
        this.returnCode = returnCode;
    }

    public String getStrMessage() {
        return strMessage;
    }

    public void setStrMessage(String strMessage) {
        this.strMessage = strMessage;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<GymUserInfo> getArrUsers() {
        return arrUsers;
    }

    public void setArrUsers(List<GymUserInfo> arrUsers) {
        this.arrUsers = arrUsers;
    }
}

