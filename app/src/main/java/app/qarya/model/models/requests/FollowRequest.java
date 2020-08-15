package app.qarya.model.models.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FollowRequest {

    public FollowRequest(Integer id, Boolean follow, String key) {
        this.id = id;
        this.follow = follow;
        this.key = key+id;
        this.value = key+id;
    }

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("follow")
    @Expose
    private Boolean follow;


    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("value")
    @Expose
    private String value;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getFollow() {
        return follow;
    }

    public void setFollow(Boolean follow) {
        this.follow = follow;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}