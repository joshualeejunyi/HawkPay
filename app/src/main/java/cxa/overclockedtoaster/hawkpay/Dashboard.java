package cxa.overclockedtoaster.hawkpay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.DecimalFormat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Dashboard extends AppCompatActivity {
    Integer userid = null;
    Double amount = null;
    String username = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.amount")) {
            amount = getIntent().getExtras().getDouble("cxa.overclockedtoaster.hawkpay.amount");
            DecimalFormat formatter = new DecimalFormat( "#.00" );
            String amountstr = formatter.format(amount);

            TextView amounttv = (TextView) findViewById(R.id.amount);
            String displayamount = "S$".concat(amountstr);
            amounttv.setText(displayamount);
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.userid")) {
            userid = getIntent().getExtras().getInt("cxa.overclockedtoaster.hawkpay.userid");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.username")) {
            username = getIntent().getExtras().getString("cxa.overclockedtoaster.hawkpay.username");
        }
//
//        ImageView paybutton = (ImageView) findViewById(R.id.payicon);
//        paybutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent gotopayment = new Intent(getApplicationContext(), PayDetails.class);
//                startActivity(gotopayment);
//            }
//        });

        ImageButton hawkfastbutton = (ImageButton) findViewById(R.id.hawkfastbutton);
        hawkfastbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotohawkfastform = new Intent(getApplicationContext(), AvailableHawkers.class);
                gotohawkfastform.putExtra("cxa.overclockedtoaster.hawkpay.userid", userid);
                gotohawkfastform.putExtra("cxa.overclockedtoaster.hawkpay.amount", amount);
                gotohawkfastform.putExtra("cxa.overclockedtoaster.hawkpay.username", username);
                startActivity(gotohawkfastform);
            }
        });

        Button uploadbutton = (Button) findViewById(R.id.rewardbutton);
        uploadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotorewards = new Intent(getApplicationContext(), PhotoReward.class);
                gotorewards.putExtra("cxa.overclockedtoaster.hawkpay.userid", userid);
                gotorewards.putExtra("cxa.overclockedtoaster.hawkpay.amount", amount);
                gotorewards.putExtra("cxa.overclockedtoaster.hawkpay.username", username);
                startActivity(gotorewards);
            }
        });
    }
}
