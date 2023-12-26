package app.dtos;

public class DTOStatus {
    private int id;
    private String text;
    public DTOStatus(int id, String text){
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}
