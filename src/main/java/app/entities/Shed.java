package app.entities;

public class Shed {

    private int id;
    private int carportID;
    private int width;
    private int length;

    public Shed(int id, int carportID, int width, int length) {
        this.id = id;
        this.carportID = carportID;
        this.width = width;
        this.length = length;
    }

    public Shed(int id, int width, int length) {
        this.id = id;
        this.width = width;
        this.length = length;
    }

    public Shed(int width, int length) {
        this.width = width;
        this.length = length;
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

}
