package cxa.overclockedtoaster.hawkpay;
import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

public class Store {

    @SerializedName("storeid")
    private Integer storeid;

    @SerializedName("storename")
    private String storename;

    @SerializedName("storeinfo")
    private String storeinfo;

    @SerializedName("storeimage")
    private Bitmap storeimage;


    public Store(Integer storeid, String storename, String storeinfo, Bitmap storeimage) {
        this.storeid = storeid;
        this.storename = storename;
        this.storeinfo = storeinfo;
        this.storeimage = storeimage;
    }

    public Integer getStoreid() {
        return storeid;
    }

    public void setStoreid() {
        this.storeid = storeid;
    }


    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getStoreinfo() {
        return storeinfo;
    }

    public void setStoreinfo(String storeinfo) {
        this.storeinfo = storeinfo;
    }

    public Bitmap getStoreimage() {
        return storeimage;
    }

    public void setStoreimage() {
        this.storeimage = storeimage;
    }






}
