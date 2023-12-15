package app.entities;

public class Part {

    private int id;
    private int materialId;
    private int amount;
    private int order_id;

    public Part(int id, int materialId, int amount, int order_id) {
        this.id = id;
        this.materialId = materialId;
        this.amount = amount;
        this.order_id = order_id;
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

    public int getOrder_id() {
        return order_id;
    }
}
