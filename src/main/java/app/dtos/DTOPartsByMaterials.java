package app.dtos;

public class DTOPartsByMaterials {

    private String name;
    private int length;
    private int amount;
    private String description;


    public DTOPartsByMaterials(String name, int length, int amount, String description) {
        this.name = name;
        this.length = length;
        this.amount = amount;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public int getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
