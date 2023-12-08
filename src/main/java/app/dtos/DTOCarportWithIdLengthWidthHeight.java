package app.dtos;

public class DTOCarportWithIdLengthWidthHeight {

    private int Id;
    private int length;
    private int width;
    private int height;

    public DTOCarportWithIdLengthWidthHeight(int Id, int length, int width, int height) {
        this.Id = Id;
        this.length = length;
        this.width = width;
        this.height = height;
    }

    public int getId() {
        return Id;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
