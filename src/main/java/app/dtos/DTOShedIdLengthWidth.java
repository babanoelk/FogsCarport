package app.dtos;

public class DTOShedIdLengthWidth {
    private int id;
    private int length;
    private int width;

    public DTOShedIdLengthWidth(int id, int length, int width) {
        this.id = id;
        this.length = length;
        this.width = width;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getId() {
        return id;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }
}
