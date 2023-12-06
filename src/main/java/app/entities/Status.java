package app.entities;

import java.util.List;

public class Status {
    private int id;
    private String text;
    public Status(int id, String text){
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
