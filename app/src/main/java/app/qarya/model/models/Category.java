package app.qarya.model.models;


import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.Expose;


import java.io.Serializable;

import app.qarya.model.ModelType;


public class Category extends Commun implements Serializable {



    @SerializedName("type")
    @Expose
    private Integer type;

    @SerializedName("followed")
    @Expose
    private Boolean followed;

    public Boolean getFollowed() {
        return followed;
    }

    public void setFollowed(Boolean followed) {
        this.followed = followed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCanPost() {
        return canPost;
    }

    public void setCanPost(Boolean canPost) {
        this.canPost = canPost;
    }


    @SerializedName("followable")
    @Expose
    private Integer followable;

    @SerializedName("count")
    @Expose
    private Integer count;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("canPost")
    @Expose
    private Boolean canPost;
    @SerializedName("parent_id")
    @Expose
    private Integer parentId;
    @SerializedName("followersCount")
    @Expose
    private Integer followersCount;


    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getFollowable() {
        return followable;
    }

    public void setFollowable(Integer followable) {
        this.followable = followable;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
