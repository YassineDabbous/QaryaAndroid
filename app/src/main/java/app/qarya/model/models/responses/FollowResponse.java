package app.qarya.model.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FollowResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("followed")
    @Expose
    private Boolean followed;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Boolean getFollowed() { return followed; }
    public void setFollowed(Boolean followed) { this.followed = followed; }
}