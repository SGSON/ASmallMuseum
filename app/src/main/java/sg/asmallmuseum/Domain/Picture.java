package sg.asmallmuseum.Domain;

public class Picture extends Artwork {
    public Picture(){
        super();
    }

    public Picture(String pid, String pauthor, String pdate, String pdesc) {
        super(pid, pauthor, pdate, pdesc);
    }
}
