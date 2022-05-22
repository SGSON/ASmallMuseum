package xyz.asmallmuseum.android.domain;


import java.io.Serializable;

public class Comment implements Serializable {
    private String commIdx;
    private String ref;
    private String path;
    private String uEmail;
    private String content;
    private String commDate;

    public Comment() {
        //default constructor
        //do not delete this constructor
    }


    public Comment(String commIdx, String ref, String path, String uEmail, String content, String commDate) {
        this.commIdx = commIdx;
        this.ref = ref;
        this.path = path;
        this.uEmail = uEmail;
        this.content = content;
        this.commDate = commDate;
    }

    public String getCommIdx() {
        return commIdx;
    }

    public void setCommIdx(String commIdx) {
        this.commIdx = commIdx;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String path) {
        this.ref = ref;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommDate() {
        return commDate;
    }

    public void setCommDate(String commDate) {
        this.commDate = commDate;
    }

}
