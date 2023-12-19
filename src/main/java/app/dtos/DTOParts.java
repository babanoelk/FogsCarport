package app.dtos;

public class DTOParts {

    public final static int POST_MATERIAL_ID = 19;
    public final static int RAFT_MATERIAL_ID = 10;
    public final static int REM_MATERIAL_ID = 25;


    private int post;
    private int raft;
    private int rem;

    public DTOParts(int post, int raft, int rem) {
        this.post = post;
        this.raft = raft;
        this.rem = rem;
    }

    public int getPost() {
        return post;
    }

    public int getRaft() {
        return raft;
    }

    public int getRem() {
        return rem;
    }
}
