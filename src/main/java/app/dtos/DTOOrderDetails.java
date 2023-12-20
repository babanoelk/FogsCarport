package app.dtos;

public class DTOOrderDetails {

    private int userId;
    private String name;
    private String address;
    private int zipcode;
    private int mobile;
    private String email;
    private int carportId;
    private int carportWidth;
    private int carportLength;
    private int carportHeight;
    private int shedId;
    private int shedWidth;
    private int shedLength;
    private String customerNote;

    public DTOOrderDetails(int userId, String name, String address, int zipcode, int mobile, String email, int carportId, int carportWidth, int carportLength, int carportHeight, int shedId, int shedWidth, int shedLength, String customerNote) {
        this.userId = userId;
        this.name = name;
        this.address = address;
        this.zipcode = zipcode;
        this.mobile = mobile;
        this.email = email;
        this.carportId = carportId;
        this.carportWidth = carportWidth;
        this.carportLength = carportLength;
        this.carportHeight = carportHeight;
        this.shedId = shedId;
        this.shedWidth = shedWidth;
        this.shedLength = shedLength;
        this.customerNote = customerNote;
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

    public int getCarportId() {
        return carportId;
    }

    public int getCarportWidth() {
        return carportWidth;
    }

    public int getCarportLength() {
        return carportLength;
    }

    public int getCarportHeight() {
        return carportHeight;
    }

    public int getShedId() {
        return shedId;
    }

    public int getShedWidth() {
        return shedWidth;
    }

    public int getShedLength() {
        return shedLength;
    }

    public String getCustomerNote() {
        return customerNote;
    }
}
