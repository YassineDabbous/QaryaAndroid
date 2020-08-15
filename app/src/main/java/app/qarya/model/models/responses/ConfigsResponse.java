package app.qarya.model.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ConfigsResponse {

    @SerializedName("time")
    @Expose
    private Integer time;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("strict")
    @Expose
    private Boolean strict;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("background")
    @Expose
    private String background;
    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("ads_after")
    @Expose
    private Integer adsAfter;
    @SerializedName("alert")
    @Expose
    private String alert;
    @SerializedName("CACHE_TIME")
    @Expose
    private Integer cACHETIME;
    @SerializedName("CACHE_SIZE")
    @Expose
    private Integer cACHESIZE;


    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getStrict() {
        return strict;
    }

    public void setStrict(Boolean strict) {
        this.strict = strict;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getAdsAfter() {
        return adsAfter;
    }

    public void setAdsAfter(Integer adsAfter) {
        this.adsAfter = adsAfter;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public Integer getCACHETIME() {
        return cACHETIME;
    }

    public void setCACHETIME(Integer cACHETIME) {
        this.cACHETIME = cACHETIME;
    }

    public Integer getCACHESIZE() {
        return cACHESIZE;
    }

    public void setCACHESIZE(Integer cACHESIZE) {
        this.cACHESIZE = cACHESIZE;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("time", time).append("active", active).append("strict", strict).append("image", image).append("background", background).append("url", url).append("adsAfter", adsAfter).append("alert", alert).append("cACHETIME", cACHETIME).append("cACHESIZE", cACHESIZE).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(time).append(alert).append(background).append(image).append(active).append(cACHETIME).append(adsAfter).append(strict).append(cACHESIZE).append(url).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ConfigsResponse) == false) {
            return false;
        }
        ConfigsResponse rhs = ((ConfigsResponse) other);
        return new EqualsBuilder().append(time, rhs.time).append(alert, rhs.alert).append(background, rhs.background).append(image, rhs.image).append(active, rhs.active).append(cACHETIME, rhs.cACHETIME).append(adsAfter, rhs.adsAfter).append(strict, rhs.strict).append(cACHESIZE, rhs.cACHESIZE).append(url, rhs.url).isEquals();
    }

}