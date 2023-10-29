package au.edu.federation.itech3107.studentattendance30395626.bean;

public class User {
    //non-parameter constructor
    public User() {
    }
    //Parameterized constructor
    public User(String account, String password) {
        this.account = account;
        this.password = password;
    }

    //User account and password
    public String account;
    public String password;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
