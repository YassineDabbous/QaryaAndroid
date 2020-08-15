package app.qarya.model.models.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageSetter {

    public MessageSetter(Integer cid, Integer to, String message, Integer fid) {
        if(fid!=0)
            this.fid = fid;
        this.cid = cid;
        this.to = to;
        this.message = message;
    }

    @SerializedName("fid")
    @Expose
    private Integer fid;

    @SerializedName("cid")
    @Expose
    private Integer cid;
    @SerializedName("to")
    @Expose
    private Integer to;
    @SerializedName("message")
    @Expose
    private String message;


    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}