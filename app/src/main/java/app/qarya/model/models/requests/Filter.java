package app.qarya.model.models.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Filter implements Serializable {

    public Filter(String word, Integer type, Integer uid, Integer category) {
        this.word = word;
        this.type = type;
        this.uid = uid;
        this.categoryId = category;
    }

    @SerializedName("word")
    @Expose
    private String word;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("uid")
    @Expose
    private Integer uid;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}