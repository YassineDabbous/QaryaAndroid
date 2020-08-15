package app.qarya.model.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable
{

    public Message(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    //0, mUsername, message, toId, toName
    public Message(Integer id, String message, String uname, String toName, Integer toID) {
        this.id = id;
        this.message = message;
        this.uname = uname;
        this.toName = toName;
        this.toID = toID;
        code = Message.TYPE_MY_MESSAGE;
    }

    public Message(Integer id, String message, String uname, SFile sFile) {
        this.id = id;
        this.message = message;
        this.uname = uname;
        code = Message.TYPE_MY_MESSAGE;
        if (sFile!=null){
            this.fileType = sFile.getType();
            this.fileUrl = sFile.getUrl();
        }
    }

    private final static long serialVersionUID = -6141396681658582096L;


    public static final int TYPE_MY_MESSAGE = -1;
    public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_LOG = 1;
    public static final int TYPE_ACTION = 2;
    public static final int TYPE_JOIN  = 3;
    public static final int TYPE_LEAVE = 4;
    public static final int TYPE_BLOCK = 5;
    public static final int TYPE_ALERT = 6;

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("uid")
    @Expose
    private Integer uid;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("uname")
    @Expose
    private String uname;
    @SerializedName("upicture")
    @Expose
    private String upicture;

    @SerializedName("time_ago")
    @Expose
    private String timeAgo;


    @SerializedName("file_url")
    @Expose
    private String fileUrl;
    @SerializedName("file_type")
    @Expose
    private Integer fileType;

    // ----------------------- < for socket msgs > -----------------------
    @SerializedName("code")
    @Expose
    private Integer code; // = Message.TYPE_MESSAGE;
    @SerializedName("users")
    @Expose
    private List<User> users = null;
    @SerializedName("toName")
    @Expose
    private String toName;
    @SerializedName("toID")
    @Expose
    private Integer toID;
    // ----------------------- </ for socket msgs > ----------------------

    public Integer getCode() {
        if (code==null)
            return Message.TYPE_MESSAGE;
        return code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        if (uid==null)
            return -1;
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }


    public void setCode(Integer code) {
        this.code = code;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public Integer getToID() {
        return toID;
    }

    public void setToID(Integer toID) {
        this.toID = toID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;

        Message message1 = (Message) o;

        if (id != null ? !id.equals(message1.id) : message1.id != null) return false;
        if (uid != null ? !uid.equals(message1.uid) : message1.uid != null) return false;
        if (message != null ? !message.equals(message1.message) : message1.message != null)
            return false;
        if (uname != null ? !uname.equals(message1.uname) : message1.uname != null) return false;
        if (upicture != null ? !upicture.equals(message1.upicture) : message1.upicture != null)
            return false;
        if (timeAgo != null ? !timeAgo.equals(message1.timeAgo) : message1.timeAgo != null)
            return false;
        if (fileUrl != null ? !fileUrl.equals(message1.fileUrl) : message1.fileUrl != null)
            return false;
        return fileType != null ? fileType.equals(message1.fileType) : message1.fileType == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (uname != null ? uname.hashCode() : 0);
        result = 31 * result + (upicture != null ? upicture.hashCode() : 0);
        result = 31 * result + (timeAgo != null ? timeAgo.hashCode() : 0);
        result = 31 * result + (fileUrl != null ? fileUrl.hashCode() : 0);
        result = 31 * result + (fileType != null ? fileType.hashCode() : 0);
        return result;
    }
}