package app.qarya.model.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import app.qarya.model.shared.YDUserManager;

public class User extends Commun {

    public boolean isAdmin = false;

    public Boolean isMe(){
        int me = this.uid!=null ? this.uid : this.id;
        //MyActivity.log("ISSSSSSSSSSS MEEEEEEEEEEEEEEEEE", me+"=="+YDUserManager.auth().getId());
        return me == YDUserManager.auth().getId();
    }


    @SerializedName("type")
    @Expose
    private Integer type;

    public Integer getFriendship() {
        return friendship;
    }

    public void setFriendship(Integer friendship) {
        this.friendship = friendship;
    }

    @SerializedName("friendship")
    @Expose
    private Integer friendship;
    @SerializedName("friendshipMaker")
    @Expose
    private Integer friendshipMaker;

    public Integer getFriendshipMaker() {
        return friendshipMaker;
    }

    public void setFriendshipMaker(Integer friendshipMaker) {
        this.friendshipMaker = friendshipMaker;
    }

    @SerializedName("postsCount")
    @Expose
    private Integer postsCount;
    @SerializedName("friendsCount")
    @Expose
    private Integer friendsCount;
    @SerializedName("productsCount")
    @Expose
    private Integer productsCount;


    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("address")
    @Expose
    private String address;

    public Integer getPostsCount() {
        return postsCount;
    }

    public void setPostsCount(Integer postsCount) {
        this.postsCount = postsCount;
    }

    public Integer getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(Integer friendsCount) {
        this.friendsCount = friendsCount;
    }

    public Integer getProductsCount() {
        return productsCount;
    }

    public void setProductsCount(Integer productsCount) {
        this.productsCount = productsCount;
    }

    public String getSummary() {
        return summary!=null ? summary : "";
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAddress() {
        return address!=null ? address : "";
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWork() {
        return work!=null ? work : "";
    }

    public void setWork(String work) {
        this.work = work;
    }

    @SerializedName("gender")
    @Expose
    private Integer gender;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("work")
    @Expose
    private String work;

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("online")
    @Expose
    private Boolean online;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User(Integer uid, String name, String photo) {
        this.id = uid;
        this.uid = uid;
        this.name = name;
        this.photo = photo;
    }
    public User(User user) {
        //Integer i = user.uid!=null ? user.uid : user.id;
        this.id = user.id ;
        this.uid = user.id;
        this.name = user.name;
        this.photo = user.photo;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Boolean getOnline() {
        return online!=null ? online : false;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }



}