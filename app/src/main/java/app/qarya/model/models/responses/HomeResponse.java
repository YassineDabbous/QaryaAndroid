package app.qarya.model.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import app.qarya.model.models.Fork;
import app.qarya.model.models.Post;
import app.qarya.model.models.User;


import java.util.List;

import tn.core.model.responses.PagingResponse;

public class HomeResponse {

    @SerializedName("code")
    @Expose
    private Integer code;

    @SerializedName("posts")
    @Expose
    private PagingResponse<Post> posts;

    @SerializedName("users")
    @Expose
    private PagingResponse<User> users;
    @SerializedName("forks")
    @Expose
    private List<Fork> forks;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<Fork> getForks() {
        return forks;
    }

    public void setForks(List<Fork> forks) {
        this.forks = forks;
    }


    public PagingResponse<Post> getPosts() {
        return posts;
    }

    public void setPosts(PagingResponse<Post> posts) {
        this.posts = posts;
    }


    public PagingResponse<User> getUsers() {
        return users;
    }

    public void setUsers(PagingResponse<User> users) {
        this.users = users;
    }




}