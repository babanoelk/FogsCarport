package app.dtos;

public class DTOUserWithUserIdNameAddressZipcodeMobileEmail {


    private int userId;
    private String name;
    private String address;
    private int zipcode;
    private int mobile;
    private String email;

    public DTOUserWithUserIdNameAddressZipcodeMobileEmail(int userId, String name, String address, int zipcode, int mobile, String email) {
        this.userId = userId;
        this.name = name;
        this.address = address;
        this.zipcode = zipcode;
        this.mobile = mobile;
        this.email = email;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getZipcode() {
        return zipcode;
    }

    public int getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }
}
