package app.qarya.model.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class MarkResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("bookmarked")
    @Expose
    private Boolean bookmarked;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(Boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("bookmarked", bookmarked).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(bookmarked).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof MarkResponse) == false) {
            return false;
        }
        MarkResponse rhs = ((MarkResponse) other);
        return new EqualsBuilder().append(id, rhs.id).append(bookmarked, rhs.bookmarked).isEquals();
    }

}