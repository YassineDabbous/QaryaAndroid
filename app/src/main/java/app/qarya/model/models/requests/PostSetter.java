package app.qarya.model.models.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostSetter {
    public PostSetter(Integer categoryId, String title, String description) {
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
    }

    public PostSetter(Integer categoryId, String title, String description, List<Integer> files) {
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.files = files;
    }

    public PostSetter(Integer categoryId, String title, String description, String address, String longitude, String latitude, String phone, String price, Integer shipping, List<Integer> files) {
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.phone = phone;
        this.price = price;
        this.shipping = shipping;
        this.files = files;
    }
    public PostSetter(Integer categoryId, String title, String description, String address, String longitude, String latitude, String phone, String price, Integer shipping) {
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.shipping = shipping;
        this.phone = phone;
        this.price = price;
    }


    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("files")
    @Expose
    private List<Integer> files;






    @SerializedName("shipping")
    @Expose
    private Integer shipping;

    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("price")
    @Expose
    private String price;


    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("address")
    @Expose
    private String address;
}