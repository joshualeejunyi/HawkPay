package cxa.overclockedtoaster.hawkpay;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

public class Food {

    @SerializedName("storeid")
    private Integer storeid;

    @SerializedName("foodname")
    private String foodname;

    @SerializedName("ingredient1")
    private String ingredient1;

    @SerializedName("ingredient1price")
    private Double ingredient1price;

    @SerializedName("ingredient2")
    private String ingredient2;

    @SerializedName("ingredient2price")
    private Double ingredient2price;

    @SerializedName("ingredient3")
    private String ingredient3;

    @SerializedName("ingredient3price")
    private Double ingredient3price;

    @SerializedName("ingredient4")
    private String ingredient4;

    @SerializedName("ingredient4price")
    private Double ingredient4price;

    @SerializedName("totalprice")
    private Double totalprice;

    public Food(Integer storeid, String foodname, String ingredient1, Double ingredient1price, String ingredient2, Double ingredient2price, String ingredient3, Double ingredient3price, String ingredient4, Double ingredient4price, Double totalprice) {
        this.storeid = storeid;
        this.foodname = foodname;
        this.ingredient1 = ingredient1;
        this.ingredient1price = ingredient1price;
        this.ingredient2 = ingredient2;
        this.ingredient2price = ingredient2price;
        this.ingredient3 = ingredient3;
        this.ingredient3price = ingredient3price;
        this.ingredient4 = ingredient4;
        this.ingredient4price = ingredient4price;
        this.totalprice = totalprice;
    }

    public Integer getStoreid() {
        return storeid;
    }

    public void setStoreid() {
        this.storeid = storeid;
    }


    public String getUnderlinedFoodName(){
        String underlined = "<u>" + foodname + "</u>";
        return underlined;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getIngredient1() {
        return ingredient1;
    }

    public void setIngredient1(String ingredient1) {
        this.ingredient1 = ingredient1;
    }

    public Double getIngredient1price() {
        return ingredient1price;
    }

    public String getIngredient2() {
        return ingredient2;
    }

    public void setIngredient2(String ingredient2) {
        this.ingredient2 = ingredient2;
    }

    public Double getIngredient2price() {
        return ingredient2price;
    }

    public String getIngredient3() {
        return ingredient3;
    }

    public void setIngredient3(String ingredient3) {
        this.ingredient3 = ingredient3;
    }

    public Double getIngredient3price() {
        return ingredient3price;
    }

    public String getIngredient4() {
        return ingredient4;
    }

    public void setIngredient4(String ingredient4) {
        this.ingredient4 = ingredient4;
    }

    public Double getIngredient4price() {
        return ingredient4price;
    }

    public Double getTotalprice() {
        return totalprice;
    }

    public String getFoodInfo() {
        String result =
                "<b>Ingredients:</b> <br>"
                        + ingredient1 + " ($" + ingredient1price + "0) <br>"
                        + ingredient2 + " ($" + ingredient2price + "0) <br>"
                        + ingredient3 + " ($" + ingredient3price + "0) <br>"
                        + ingredient4 + " ($" + ingredient4price + "0) <br>"
                        + "<b>Total Price: $" + totalprice + "</b>";
        return result;
    }

    public void setTotalprice() {
        this.totalprice = totalprice;
    }



}
