package app.qarya.model.shared;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Showcases implements Serializable
{

    @SerializedName("main")
    @Expose
    public Integer main = 0;

    @SerializedName("drawerLeft")
    @Expose
    public Integer drawerLeft = 0;

    public boolean done(){
        return main==1 && drawerLeft==1;
    }

    public Showcases() {
    }
}