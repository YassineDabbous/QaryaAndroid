package app.qarya.model.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Relation extends Commun {

    public Relation(User user) {
        this.userOne = 0;
        this.userTwo = 0;
        this.relation = -1;
        this.uid = user.id;
        this.uname = user.getName();
        this.upicture = user.getPhoto();
    }

    @SerializedName("user_one")
    @Expose
    private Integer userOne;
    @SerializedName("user_two")
    @Expose
    private Integer userTwo;
    @SerializedName("relation")
    @Expose
    private Integer relation;

    /*
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("uname")
    @Expose
    private String uname;
    @SerializedName("upicture")
    @Expose
    private String upicture;
    @SerializedName("uid")
    @Expose
    private Integer uid;





    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUpicture() {
        return upicture;
    }

    public void setUpicture(String upicture) {
        this.upicture = upicture;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }


    */


    public Integer getUserOne() {
        return userOne;
    }

    public void setUserOne(Integer userOne) {
        this.userOne = userOne;
    }

    public Integer getUserTwo() {
        return userTwo;
    }

    public void setUserTwo(Integer userTwo) {
        this.userTwo = userTwo;
    }

    public Integer getRelation() {
        return relation;
    }

    public void setRelation(Integer relation) {
        this.relation = relation;
    }


}