package cxa.overclockedtoaster.hawkpay;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent().hasExtra("com.example.hawkfastpaylah.createaccount")) {
            Boolean created = getIntent().getExtras().getBoolean("com.example.hawkfastpaylah.createaccount");
            if (created == true) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Account Created");
                alertDialog.setMessage("Account Created. You may login now.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Mock-up Warning");
            alertDialog.setMessage("This is a mock-up application of DBS PayLah! for CXA 2019.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Understood",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

        Button loginbutton = (Button) findViewById(R.id.loginbutton);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText username = (EditText) findViewById(R.id.username);
                EditText password = (EditText) findViewById(R.id.password);

                String usernamestr = username.getText().toString();
                String passwordstr = password.getText().toString();

                String[] creds = new String[2];
                creds[0] = usernamestr;
                creds[1] = passwordstr;

                LoginDB dbconnect = new LoginDB(MainActivity.this);
                dbconnect.execute(creds);
            }
        });

        TextView createaccountbutton = (TextView) findViewById(R.id.redirecttocreateaccount);

        createaccountbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createaccountintent = new Intent(getApplicationContext(), CreateAccount.class);
                startActivity(createaccountintent);
            }
        });

    }


}

