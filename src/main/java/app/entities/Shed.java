package app.entities;

public class Shed {

    private int id;
    private int carportID;
    private int width;
    private int length;
    private int height;

    public Shed(int id, int carportID, int width, int length, int height) {
        this.id = id;
        this.carportID = carportID;
        this.width = width;
        this.length = length;
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCarportID() {
        return carportID;
    }

    public void setCarportID(int carportID) {
        this.carportID = carportID;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
