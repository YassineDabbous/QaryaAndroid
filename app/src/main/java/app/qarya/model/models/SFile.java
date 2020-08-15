package app.qarya.model.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SFile {

    public Integer positionInLocalList;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("type")
    @Expose
    private Integer type;

    @SerializedName("url")
    @Expose
    private String url;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}