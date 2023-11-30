package app.entities;

public class Shed {

    private int primaryKey;
    private int carportID;
    private int width;
    private int length;
    private int high;

    public Shed(int primaryKey, int carportID, int width, int length, int high) {
        this.primaryKey = primaryKey;
        this.carportID = carportID;
        this.width = width;
        this.length = length;
        this.high = high;
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
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

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }
}
