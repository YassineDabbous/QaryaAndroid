package app.qarya.model.models;

import app.qarya.MyApplication;
import app.qarya.R;

import java.io.Serializable;

public class PostsOwner implements Serializable {
    public Integer id = 0, followersCount = 0, postsCount = 0;
    public OwnerType type = OwnerType.CATEGORY;
    public Boolean canPost = true;
    public Boolean followable = false, followed = false;
    public String name = "", description = "", image = "", path = "";

    public enum OwnerType {
        PATH,
        USER,
        CATEGORY,
        CITY,
        SEARCH,
        TAG
    }

    public static PostsOwner newInstance(APath item){
        PostsOwner p = new PostsOwner();
        p.name = item.getName();
        p.image = item.getIcon();
        p.postsCount = item.getCount();
        p.type = OwnerType.PATH;
        if (item.getPath()!=null && !item.getPath().isEmpty()){
            p.path =  item.getPath();
        } else {
            p.type = OwnerType.CATEGORY;
            p.id = item.getCategoryId();
            p.followable = true;
        }
        return p;
    }
    public static PostsOwner newInstance(String item){
        PostsOwner p = new PostsOwner();
        p.name = item;
        p.description = MyApplication.get().getString(R.string.search_results_for)+" "+item;
        if(item.contains("#"))
            p.type = OwnerType.TAG;
        else
            p.type = OwnerType.SEARCH;
        return p;
    }
    public static PostsOwner newInstance(Category item){
        PostsOwner p = new PostsOwner();
        p.id = item.getId();
        p.name = item.getName();
        if(item.getIcon()!=null) p.image = item.getIcon();
        p.description = item.getDescription();
        p.followersCount = item.getFollowersCount();
        p.followable = item.getFollowable()>0;
        p.followed = item.getFollowed();
        p.canPost = item.getCanPost();
        p.type = OwnerType.CATEGORY;
        return p;
    }
    public static PostsOwner newInstance(User item){
        PostsOwner p = new PostsOwner();
        p.id = item.getUid();
        p.name = item.getName();
        p.image = item.getPhoto();
        p.type = OwnerType.USER;
        return p;
    }
    public static PostsOwner newInstance(City item){
        PostsOwner p = new PostsOwner();
        p.id = item.getId();
        p.name = item.getName();
        p.followersCount = item.getFollowersCount();
        p.followable = true;
        p.followed = item.getFollowed();
        p.canPost = item.getCanPost();
        p.type = OwnerType.CITY;
        return p;
    }
}