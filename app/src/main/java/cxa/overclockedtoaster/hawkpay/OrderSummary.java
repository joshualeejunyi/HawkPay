package cxa.overclockedtoaster.hawkpay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;
import org.w3c.dom.Text;

public class OrderSummary extends AppCompatActivity {
    Double amount = 0.0;
    Integer userid = 0;
    String payload = "";
    JSONObject json = new JSONObject();
    String username = "";
    Integer storeid = 0;
    String foodname = "";
    String orderinfo = "";
    Double price = 0.0;
    Integer foodid = 0;
    String storename = "";
    String newpayload = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.userid")) {
            userid = getIntent().getExtras().getInt("cxa.overclockedtoaster.hawkpay.userid");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.amount")) {
            amount = getIntent().getExtras().getDouble("cxa.overclockedtoaster.hawkpay.amount");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.username")) {
            username = getIntent().getExtras().getString("cxa.overclockedtoaster.hawkpay.username");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.storeid")) {
            storeid = getIntent().getExtras().getInt("cxa.overclockedtoaster.hawkpay.storeid");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.foodname")) {
            foodname = getIntent().getExtras().getString("cxa.overclockedtoaster.hawkpay.foodname");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.price")) {
            price = getIntent().getExtras().getDouble("cxa.overclockedtoaster.hawkpay.price");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.orderinfo")) {
            orderinfo = getIntent().getExtras().getString("cxa.overclockedtoaster.hawkpay.orderinfo");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.storename")) {
            storename = getIntent().getExtras().getString("cxa.overclockedtoaster.hawkpay.storename");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.foodid")) {
            foodid = getIntent().getExtras().getInt("cxa.overclockedtoaster.hawkpay.foodid");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.payload")) {
            payload = getIntent().getExtras().getString("cxa.overclockedtoaster.hawkpay.payload");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.newpayload")) {
            newpayload = getIntent().getExtras().getString("cxa.overclockedtoaster.hawkpay.newpayload");
        }

        TextView foodplacetv = (TextView) findViewById(R.id.foodplace);
        foodplacetv.setText(storename);

        TextView balancetv = (TextView) findViewById(R.id.balance);
        balancetv.setText("$".concat(amount.toString()));

        TextView useraccounttv = (TextView) findViewById(R.id.useraccount);
        useraccounttv.setText("(".concat(username).concat(")"));

        TextView foodnametv = (TextView) findViewById(R.id.foodname);
        foodnametv.setText(foodname);

        TextView foodinfotv = (TextView) findViewById(R.id.foodinfo);
        foodinfotv.setText(orderinfo);

        TextView pricetv = (TextView) findViewById(R.id.price);
        pricetv.setText("$".concat(price.toString()));

        ImageView backbuton = (ImageView) findViewById(R.id.backbutton);
        backbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button submitorder = (Button) findViewById(R.id.submitorder);
        submitorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // what do i need to submit
                // db:
                // userid
                // storeid
                // foodid
                // selectedingredients as of 1,2,3,4
                // cost

                // mqtt:
                // 1,2,3,4


                String[] stuff4db = new String[9];
                stuff4db[0] = userid.toString();
                stuff4db[1] = storeid.toString();
                stuff4db[2] = foodid.toString();
                stuff4db[3] = payload;
                stuff4db[4] = price.toString();
                stuff4db[5] = storename;
                stuff4db[6] = username;
                stuff4db[7] = newpayload;
                stuff4db[8] = foodname;

                OrderDB orderfood = new OrderDB (OrderSummary.this);
                orderfood.execute(stuff4db);

            }
        });

    }
}
