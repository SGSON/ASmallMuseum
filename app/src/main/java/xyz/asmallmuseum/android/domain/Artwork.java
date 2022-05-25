package xyz.asmallmuseum.android.domain;

import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;
import java.util.List;

public abstract class Artwork implements Serializable {
    private DocumentReference aID;
    private int aPostNum;
    private String aCategory;
    private String aType;
    private String aTitle;
    private String aAuthor;
    private String aDate;
    private String aDesc;
    private List<String> aFileLoc;
    private int aRating;
    private String aUserID;
    private int aLike;
    private String aTime;

    public Artwork(){

    }

    public Artwork(String aCategory, String aType, String aTitle, String aAuthor, String aDate, String aTime, String aDesc, String aUserID){
        this.aCategory = aCategory;
        this.aType = aType;
        this.aTitle = aTitle;
        this.aAuthor = aAuthor;
        this.aDate = aDate;
        this.aDesc = aDesc;
        this.aRating = 0;
        this.aUserID = aUserID;
        this.aLike = 0;
        this.aTime = aTime;
    }

    public DocumentReference getaID() {
        return aID;
    }

    public void setaID(DocumentReference aID) {
        this.aID = aID;
    }

    public String getaCategory() {
        return aCategory;
    }

    public void setaCategory(String aCategory) {
        this.aCategory = aCategory;
    }

    public String getaType() {
        return aType;
    }

    public void setaType(String aType) {
        this.aType = aType;
    }

    public String getaTitle() {
        return aTitle;
    }

    public void setaTitle(String aTitle) {
        this.aTitle = aTitle;
    }

    public String getaAuthor() {
        return aAuthor;
    }

    public void setaAuthor(String aAuthor) {
        this.aAuthor = aAuthor;
    }

    public String getaDate() {
        return aDate;
    }

    public void setaDate(String aDate) {
        this.aDate = aDate;
    }

    public String getaDesc() {
        return aDesc;
    }

    public void setaDesc(String aDesc) {
        this.aDesc = aDesc;
    }

    public List<String> getaFileLoc() {
        return aFileLoc;
    }

    public void setaFileLoc(List<String> aFileLoc) {
        this.aFileLoc = aFileLoc;
    }

    public int getaRating() {
        return aRating;
    }

    public void setaRating(int aRating) {
        this.aRating = aRating;
    }

    public int getaPostNum() {
        return aPostNum;
    }

    public void setaPostNum(int aPostNum) {
        this.aPostNum = aPostNum;
    }

    public String getaUserID() {
        return aUserID;
    }

    public void setaUserID(String aUserID) {
        this.aUserID = aUserID;
    }

    public int getaLike() {
        return aLike;
    }

    public void setaLike(int aLike) {
        this.aLike = aLike;
    }

    public String getaTime() {
        return aTime;
    }

    public void setaTime(String aTime) {
        this.aTime = aTime;
    }
}
