package model;

public class UserDetail {
    private String userId;
    private String username;
    private String password;
    private String role;

    public UserDetail() {  }

    public UserDetail(String userId, String username, String password, String role) {
        this.setUserId(userId);
        this.setUsername(username);
        this.setPassword(password);
        this.setRole(role);
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserDetail{" +
                "userId='" + getUserId() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", role='" + getRole() + '\'' +
                '}';
    }
}
