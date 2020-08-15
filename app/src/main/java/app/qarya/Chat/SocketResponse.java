package app.qarya.Chat;

import app.qarya.model.models.Message;

public class SocketResponse extends Message
{
    public SocketResponse(Integer code, String message) {
        super(code, message);
    }

    public SocketResponse(Integer id, String message, String uname, String toName, Integer toID) {
        super(id, message, uname, toName, toID);
    }
    /*

    private final static long serialVersionUID = -6141396681658582096L;


    @SerializedName("code")
    @Expose
    private Integer code;


    @SerializedName("user")
    @Expose
    private User user;


    @SerializedName("users")
    @Expose
    private List<User> users = null;


    @SerializedName("message")
    @Expose
    private SocketMessage message;




    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getUsersCount() {
        if(users == null)
            return 0;
        return users.size();
    }


    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public SocketMessage getMessage() {
        return message;
    }

    public void setMessage(SocketMessage message) {
        this.message = message;
    }
    * */
}
