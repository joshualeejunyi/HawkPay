package cxa.overclockedtoaster.hawkpay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RewardUploaded extends AppCompatActivity {
    String mlresult;
    String storename;
    Double price;
    Integer userid;
    String username;
    Double amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_uploaded);

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.mlresult")) {
            mlresult = getIntent().getExtras().getString("cxa.overclockedtoaster.hawkpay.mlresult");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.storename")) {
            storename = getIntent().getExtras().getString("cxa.overclockedtoaster.hawkpay.storename");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.price")) {
            price = getIntent().getExtras().getDouble("cxa.overclockedtoaster.hawkpay.price");
        }


        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.userid")) {
            userid = getIntent().getExtras().getInt("cxa.overclockedtoaster.hawkpay.userid");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.username")) {
            username = getIntent().getExtras().getString("cxa.overclockedtoaster.hawkpay.username");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.amount")) {
            amount = getIntent().getExtras().getDouble("cxa.overclockedtoaster.hawkpay.amount");
        }

        TextView mlresulttv = (TextView) findViewById(R.id.result);
        mlresulttv.setText(mlresult);

        Button homebutton = (Button) findViewById(R.id.backtohome);
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent finish = new Intent(getApplicationContext(), Dashboard.class);
                finish.putExtra("cxa.overclockedtoaster.hawkpay.price", price);
                finish.putExtra("cxa.overclockedtoaster.hawkpay.storename", storename);
                finish.putExtra("cxa.overclockedtoaster.hawkpay.amount", amount);
                finish.putExtra("cxa.overclockedtoaster.hawkpay.userid", userid);
                finish.putExtra("cxa.overclockedtoaster.hawkpay.username", username);
                startActivity(finish);
            }
        });
    }
}
