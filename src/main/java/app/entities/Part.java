package app.entities;

public class Part {
    private int id;
    private int materialId;
    private int amount;
    private int orderId;

    public Part(int id, int materialId, int amount, int orderId) {
        this.id = id;
        this.materialId = materialId;
        this.amount = amount;
        this.orderId = orderId;
    }

    public Part(int materialId, int amount, int orderId) {
        this.materialId = materialId;
        this.amount = amount;
        this.orderId = orderId;
    }


    public int getId() {
        return id;
    }

    public int getMaterialId() {
        return materialId;
    }

    public int getAmount() {
        return amount;
    }

    public int getOrderId() {
        return orderId;
    }
}
