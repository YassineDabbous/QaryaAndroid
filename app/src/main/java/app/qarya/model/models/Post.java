package app.qarya.model.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

import app.qarya.model.ModelType;

public class Post extends Commun{


    @SerializedName("type")
    @Expose
    private Integer type;
    public Integer getType() {
        return type == null ? ModelType.POST : type;
    }
    public void setType(Integer type) {
        this.type = type;
    }

    @SerializedName("files")
    @Expose
    private List<SFile> files;

    @SerializedName("category_id")
    @Expose
    private Integer categoryId;

    @SerializedName("can_comment")
    @Expose
    private Integer canComment;
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("short_description")
    @Expose
    private String short_description;

    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }


    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("solution")
    @Expose
    private Integer solution;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("viewsCount")
    @Expose
    private Integer viewsCount;
    @SerializedName("commentsCount")
    @Expose
    private Integer commentsCount;
    @SerializedName("reactionsCount")
    @Expose
    private Integer likesCount;
    @SerializedName("reacted")
    @Expose
    private Boolean liked;
    @SerializedName("marked")
    @Expose
    private Boolean marked;







    @SerializedName("price_type")
    @Expose
    private String priceType;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("mapLink")
    @Expose
    private String mapLink;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;

    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("shipping")
    @Expose
    private String shipping;


    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public Integer getCommentsCount() {
        return commentsCount != null ? commentsCount : 0;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Boolean getMarked() {
        return marked;
    }

    public void setMarked(Boolean marked) {
        this.marked = marked;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getCanComment() {
        return canComment;
    }

    public void setCanComment(Integer canComment) {
        this.canComment = canComment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description==null? short_description : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getSolution() {
        return solution;
    }

    public void setSolution(Integer solution) {
        this.solution = solution;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Integer getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(Integer viewsCount) {
        this.viewsCount = viewsCount;
    }

    public Integer getLikesCount() {
        return likesCount != null ? likesCount : 0;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }




    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMapLink() {
        return mapLink;
    }

    public void setMapLink(String mapLink) {
        this.mapLink = mapLink;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public List<SFile> getFiles() {
        return files;
    }

    public void setFiles(List<SFile> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("categoryId", categoryId).append("canComment", canComment).append("title", title).append("description", description).append("photo", photo).append("solution", solution).append("deletedAt", deletedAt).append("viewsCount", viewsCount).append("reactionsCount", likesCount).append("reacted", liked).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(title).append(canComment).append(categoryId).append(description).append(liked).append(likesCount).append(deletedAt).append(viewsCount).append(solution).append(photo).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Post) == false) {
            return false;
        }
        Post rhs = ((Post) other);
        return new EqualsBuilder().append(title, rhs.title).append(canComment, rhs.canComment).append(categoryId, rhs.categoryId).append(description, rhs.description).append(liked, rhs.liked).append(likesCount, rhs.likesCount).append(deletedAt, rhs.deletedAt).append(viewsCount, rhs.viewsCount).append(solution, rhs.solution).append(photo, rhs.photo).isEquals();
    }

}