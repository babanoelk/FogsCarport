package app.entities;

public class Carport {

    private int id;
    private int width;
    private int length;
    private int height;
    private Shed shed;

    public Carport(int width, int length, int height) {
        this.width = width;
        this.length = length;
        this.height = height;
    }

    public Carport(int id, int width, int length, int height) {
        this.id = id;
        this.width = width;
        this.length = length;
        this.height = height;
    }


    public Carport(int id, int width, int length, int height, Shed shed) {
        this.id = id;
        this.width = width;
        this.length = length;
        this.height = height;
        this.shed = shed;
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

    public Shed getShed() {
        return shed;
    }

    public void setShed(Shed shed) {
        this.shed = shed;
    }
}
