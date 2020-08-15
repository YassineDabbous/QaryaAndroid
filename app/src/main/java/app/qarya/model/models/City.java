package app.qarya.model.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class City implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("followed")
    @Expose
    private Boolean followed;

    public Boolean getFollowed() {
        return followed;
    }

    public void setFollowed(Boolean followed) {
        this.followed = followed;
    }


    public Boolean getCanPost() {
        return canPost;
    }

    public void setCanPost(Boolean canPost) {
        this.canPost = canPost;
    }

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("canPost")
    @Expose
    private Boolean canPost;
    @SerializedName("parent_id")
    @Expose
    private Integer parentId;
    @SerializedName("followersCount")
    @Expose
    private Integer followersCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }




}
