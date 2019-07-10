package cxa.overclockedtoaster.hawkpay;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class OrderFood extends AppCompatActivity {
    Integer userid = null;
    Double amount = null;
    String username = null;
    Integer storeid = null;
    Integer foodid;
    String foodname;
    String ingredient1;
    Double ingredient1price;
    String ingredient2;
    Double ingredient2price;
    String ingredient3;
    Double ingredient3price;
    String ingredient4;
    Double ingredient4price;
    Double totalprice;
    Bitmap storeimage;
    Double calculatedbaseprice = 0.0;
    String storename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_food);

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.amount")) {
            amount = getIntent().getExtras().getDouble("cxa.overclockedtoaster.hawkpay.amount");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.userid")) {
            userid = getIntent().getExtras().getInt("cxa.overclockedtoaster.hawkpay.userid");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.username")) {
            username = getIntent().getExtras().getString("cxa.overclockedtoaster.hawkpay.username");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.storeid")) {
            storeid = getIntent().getExtras().getInt("cxa.overclockedtoaster.hawkpay.storeid");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.foodid")) {
            foodid = getIntent().getExtras().getInt("cxa.overclockedtoaster.hawkpay.foodid");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.foodname")) {
            foodname = getIntent().getExtras().getString("cxa.overclockedtoaster.hawkpay.foodname");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.storename")) {
            storename = getIntent().getExtras().getString("cxa.overclockedtoaster.hawkpay.storename");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.ingredient1")) {
            ingredient1 = getIntent().getExtras().getString("cxa.overclockedtoaster.hawkpay.ingredient1");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.ingredient1price")) {
            ingredient1price = getIntent().getExtras().getDouble("cxa.overclockedtoaster.hawkpay.ingredient1price");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.ingredient2")) {
            ingredient2 = getIntent().getExtras().getString("cxa.overclockedtoaster.hawkpay.ingredient2");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.ingredient2price")) {
            ingredient2price = getIntent().getExtras().getDouble("cxa.overclockedtoaster.hawkpay.ingredient2price");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.ingredient3")) {
            ingredient3 = getIntent().getExtras().getString("cxa.overclockedtoaster.hawkpay.ingredient3");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.ingredient3price")) {
            ingredient3price = getIntent().getExtras().getDouble("cxa.overclockedtoaster.hawkpay.ingredient3price");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.ingredient4")) {
            ingredient4 = getIntent().getExtras().getString("cxa.overclockedtoaster.hawkpay.ingredient4");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.ingredient4price")) {
            ingredient4price = getIntent().getExtras().getDouble("cxa.overclockedtoaster.hawkpay.ingredient4price");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.totalprice")) {
            totalprice = getIntent().getExtras().getDouble("cxa.overclockedtoaster.hawkpay.totalprice");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.storeimage")) {
            storeimage = (Bitmap) getIntent().getParcelableExtra("cxa.overclockedtoaster.hawkpay.storeimage");
        }

        System.out.println("1");
        System.out.println(ingredient1price);
        System.out.println("2");
        System.out.println(ingredient2price);
        System.out.println("3");
        System.out.println(ingredient3price);
        System.out.println("4");
        System.out.println(ingredient4price);

        TextView foodnameheader = (TextView) findViewById(R.id.foodnameheader);
        foodnameheader.setText(foodname);

        ImageView imagetv = (ImageView) findViewById(R.id.image);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), storeimage);
        imagetv.setBackgroundDrawable(bitmapDrawable);

        calculatedbaseprice = totalprice - ingredient1price - ingredient2price - ingredient3price - ingredient4price;

        TextView baseprice = (TextView) findViewById(R.id.baseprice);
        String basepricestring = "Base Price: $" + calculatedbaseprice;
        baseprice.setText(basepricestring);

        TextView ingredient1text = (TextView) findViewById(R.id.ingredient1);
        ingredient1text.setText(ingredient1);

        TextView ingredient1pricetv = (TextView) findViewById(R.id.ingredient1price);
        String ingredient1pricetext = "+$" + Double.toString(ingredient1price);
        ingredient1pricetv.setText(ingredient1pricetext);

        TextView ingredient2text = (TextView) findViewById(R.id.ingredient2);
        ingredient2text.setText(ingredient2);


        TextView ingredient2pricetv = (TextView) findViewById(R.id.ingredient2price);
        String ingredient2pricetext = "+$" + Double.toString(ingredient2price);
        ingredient2pricetv.setText(ingredient2pricetext);

        TextView ingredient3text = (TextView) findViewById(R.id.ingredient3);
        ingredient3text.setText(ingredient3);

        TextView ingredient3pricetv = (TextView) findViewById(R.id.ingredient3price);
        String ingredient3pricetext = "+$" + Double.toString(ingredient3price);
        ingredient3pricetv.setText(ingredient3pricetext);

        TextView ingredient4text = (TextView) findViewById(R.id.ingredient4);
        ingredient4text.setText(ingredient4);

        TextView ingredient4pricetv = (TextView) findViewById(R.id.ingredient4price);
        String ingredient4pricetext = "+$" + Double.toString(ingredient4price);
        ingredient4pricetv.setText(ingredient4pricetext);

        TextView total = (TextView) findViewById(R.id.total);
        total.setText("$" + Double.toString(calculatedbaseprice));

        CheckBox cb1 = (CheckBox) findViewById(R.id.ingredient1checkbox);
        CheckBox cb2 = (CheckBox) findViewById(R.id.ingredient2checkbox);
        CheckBox cb3 = (CheckBox) findViewById(R.id.ingredient3checkbox);
        CheckBox cb4 = (CheckBox) findViewById(R.id.ingredient4checkbox);

        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb1.isChecked()) {
                    updateTotal(true, 1);
                } else {
                    updateTotal(false, 1);
                }
            }
        });

        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb2.isChecked()) {
                    updateTotal(true, 2);
                } else {
                    updateTotal(false, 2);
                }
            }
        });

        cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb3.isChecked()) {
                    updateTotal(true, 3);
                } else {
                    updateTotal(false, 3);
                }
            }
        });

        cb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb4.isChecked()) {
                    updateTotal(true, 4);
                } else {
                    updateTotal(false, 4);
                }
            }
        });

        ImageView backbuton = (ImageView) findViewById(R.id.backbutton);
        backbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button submitbutton = (Button) findViewById(R.id.submitbutton);
        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean checked1 = false;
                Boolean checked2 = false;
                Boolean checked3 = false;
                Boolean checked4 = false;

                System.out.println("HI");

                JSONObject json = new JSONObject();
                StringBuilder sb = new StringBuilder("");
                String orderinfo = "";

                if (cb1.isChecked()) {
                    checked1 = true;
                    sb.append("1,");
                    orderinfo = orderinfo + ingredient1 + " (" + ingredient1pricetext + ")\n";
                }

                if (cb2.isChecked()) {
                    checked2 = true;
                    sb.append("2,");
                    orderinfo = orderinfo + ingredient2 + " (" + ingredient2pricetext + ")\n";
                }

                if (cb3.isChecked()) {
                    checked3 = true;
                    sb.append("3,");
                    orderinfo = orderinfo + ingredient3 + " (" + ingredient3pricetext + ")\n";
                }

                if (cb4.isChecked()) {
                    checked4 = true;
                    sb.append("4,");
                    orderinfo = orderinfo + ingredient4 + " (" + ingredient4pricetext + ")\n";

                }

                System.out.println(sb);

                String numbers = sb.toString();
                System.out.println(orderinfo);


                if (numbers != null && numbers.length() > 0 && numbers.charAt(numbers.length() - 1) == ',') {
                    numbers = numbers.substring(0, numbers.length() - 1);
                }

                System.out.println(numbers);

                if (numbers.equals("")) {
                    miningredient();
                } else {

                    Intent gotosummary = new Intent(getApplicationContext(), OrderSummary.class);
                    gotosummary.putExtra("cxa.overclockedtoaster.hawkpay.userid", userid);
                    gotosummary.putExtra("cxa.overclockedtoaster.hawkpay.amount", amount);
                    gotosummary.putExtra("cxa.overclockedtoaster.hawkpay.username", username);
                    gotosummary.putExtra("cxa.overclockedtoaster.hawkpay.storename", storename);
                    gotosummary.putExtra("cxa.overclockedtoaster.hawkpay.storeid", storeid);
                    gotosummary.putExtra("cxa.overclockedtoaster.hawkpay.foodname", foodname);
                    gotosummary.putExtra("cxa.overclockedtoaster.hawkpay.orderinfo", orderinfo);
                    gotosummary.putExtra("cxa.overclockedtoaster.hawkpay.price", calculatedbaseprice);
                    gotosummary.putExtra("cxa.overclockedtoaster.hawkpay.foodid", foodid);
                    gotosummary.putExtra("cxa.overclockedtoaster.hawkpay.payload", numbers);

                    startActivity(gotosummary);
                }




            }
        });

    }

    private void updateTotal(Boolean ticked, Integer id) {
        Double newprice = calculatedbaseprice;

        if (ticked == true) {
            if (id == 1) {
                newprice += ingredient1price;
            } else if (id == 2) {
                newprice += ingredient2price;
            } else if (id == 3) {
                newprice += ingredient3price;
            } else if (id == 4) {
                newprice += ingredient4price;
            }
        } else {
            if (id == 1) {
                newprice -= ingredient1price;
            } else if (id == 2) {
                newprice -= ingredient2price;
            } else if (id == 3) {
                newprice -= ingredient3price;
            } else if (id == 4) {
                newprice -= ingredient4price;
            }
        }


        calculatedbaseprice = newprice;

        TextView total = (TextView) findViewById(R.id.total);
        String output = "$" + Double.toString(newprice);
        total.setText(output);
    }

    public void miningredient() {
        AlertDialog alertDialog = new AlertDialog.Builder(OrderFood.this).create();
        alertDialog.setTitle("Select At Least One Ingredient");
        alertDialog.setMessage("Please select at least one ingredient.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
