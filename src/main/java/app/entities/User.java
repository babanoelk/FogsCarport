package app.entities;

public class User {
    private int primaryKey;
    private String name;
    private String eMail;
    private String password;
    private String address;
    private int mobile;
    private int role;
    private int zipcode;

    public User(String name, String eMail, String password, String address, int mobile, int zipcode) {
        this.name = name;
        this.eMail = eMail;
        this.password = password;
        this.address = address;
        this.mobile = mobile;
        this.role = role;
        this.zipcode = zipcode;
    }

    public User(int primaryKey, String name, String eMail, String password, String address, int mobile, int role, int zipcode) {
        this.primaryKey = primaryKey;
        this.name = name;
        this.eMail = eMail;
        this.password = password;
        this.address = address;
        this.mobile = mobile;
        this.role = role;
        this.zipcode = zipcode;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setMobile(int mobile) {
        this.mobile = mobile;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public String getName() {
        return name;
    }

    public String geteMail() {
        return eMail;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public int getMobile() {
        return mobile;
    }

    public int getRole() {
        return role;
    }

    public int getZipcode() {
        return zipcode;
    }
}
