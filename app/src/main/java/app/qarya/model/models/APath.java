package app.qarya.model.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class APath implements Serializable {

    public APath(String path, Integer categoryId) {
        this.path = path;
        this.categoryId = categoryId;
    }

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;


    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("icon")
    @Expose
    private String icon;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("name", name).append("type", type).append("path", path).append("categoryId", categoryId).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(categoryId).append(name).append(path).append(type).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof APath) == false) {
            return false;
        }
        APath rhs = ((APath) other);
        return new EqualsBuilder().append(id, rhs.id).append(categoryId, rhs.categoryId).append(name, rhs.name).append(path, rhs.path).append(type, rhs.type).isEquals();
    }

}