package uca.ruiz.antonio.jwtapp.data.mapping;

/**
 * Created by toni on 08/06/2018.
 */

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Authority implements Serializable
{

    @SerializedName("authority")
    @Expose
    private String authority;
    private final static long serialVersionUID = -7287981482110975325L;

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

}
