package app.entities;

public class Carport {

    private int id;
    private int width;
    private int length;
    private int height;
    private boolean hasShed;

    public Carport(int id, int width, int length, int height, boolean hasShed) {
        this.id = id;
        this.width = width;
        this.length = length;
        this.height = height;
        this.hasShed = hasShed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isHasShed() {
        return hasShed;
    }

    public void setHasShed(boolean hasShed) {
        this.hasShed = hasShed;
    }
}
