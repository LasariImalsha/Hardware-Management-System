package model;

public class LoginDetail {
    private String userId;
    private String loginDate;
    private String loginTime;
    private String logoutDate;
    private String logoutTime;

    public LoginDetail() {  }

    public LoginDetail(String userId, String loginDate, String loginTime, String logoutDate, String logoutTime) {
        this.setUserId(userId);
        this.setLoginDate(loginDate);
        this.setLoginTime(loginTime);
        this.setLogoutDate(logoutDate);
        this.setLogoutTime(logoutTime);
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoginDate() {
        return loginDate;
    }
    public void setLoginDate(String loginDate) {
        this.loginDate = loginDate;
    }

    public String getLoginTime() {
        return loginTime;
    }
    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getLogoutDate() {
        return logoutDate;
    }
    public void setLogoutDate(String logoutDate) {
        this.logoutDate = logoutDate;
    }

    public String getLogoutTime() {
        return logoutTime;
    }
    public void setLogoutTime(String logoutTime) {
        this.logoutTime = logoutTime;
    }

    @Override
    public String toString() {
        return "LoginDetail{" +
                "userId='" + getUserId() + '\'' +
                ", loginDate='" + getLoginDate() + '\'' +
                ", loginTime='" + getLoginTime() + '\'' +
                ", logoutDate='" + getLogoutDate() + '\'' +
                ", logoutTime='" + getLogoutTime() + '\'' +
                '}';
    }
}
