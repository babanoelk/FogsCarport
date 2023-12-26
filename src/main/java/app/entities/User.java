package app.entities;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private String address;
    private int mobile;
    private int role;
    private int zipcode;

    public User(String name, String email, String password, String address, int mobile, int zipcode) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.mobile = mobile;
        this.role = role;
        this.zipcode = zipcode;
    }

    public User(int id, String name, String email, String password, String address, int mobile, int role, int zipcode) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.mobile = mobile;
        this.role = role;
        this.zipcode = zipcode;
    }

    /*
    public User(String name, String email, String password, String address, int mobile, int zipcode) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.mobile = mobile;
        this.zipcode = zipcode;
        //this.consent = consent;
    }*/

    public User(String name, String email, String password, String address, int mobile, int zipcode, boolean consent, int role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.mobile = mobile;
        this.zipcode = zipcode;
        this.role = role;
    }
    public User(int id, String name, String email, String address, int mobile, int zipcode) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.mobile = mobile;
        this.zipcode = zipcode;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
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
