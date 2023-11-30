package app.entities;

public class Carport {

    private int primaryKey;
    private int width;
    private int length;
    private int high;
    private boolean hasShed;

    public Carport(int primaryKey, int width, int length, int high, boolean hasShed) {
        this.primaryKey = primaryKey;
        this.width = width;
        this.length = length;
        this.high = high;
        this.hasShed = hasShed;
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
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

    public boolean isHasShed() {
        return hasShed;
    }

    public void setHasShed(boolean hasShed) {
        this.hasShed = hasShed;
    }
}
