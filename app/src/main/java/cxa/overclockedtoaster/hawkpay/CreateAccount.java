package cxa.overclockedtoaster.hawkpay;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class CreateAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Button createaccount = (Button) findViewById(R.id.createaccountbutton);

        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText username = (EditText) findViewById(R.id.username);
                EditText password = (EditText) findViewById(R.id.password);
                EditText password2 = (EditText) findViewById(R.id.password2);
                CheckBox partner = (CheckBox) findViewById(R.id.partnercheckbox);

                String usernamestr = username.getText().toString();
                String passwordstr = password.getText().toString();
                String password2str = password2.getText().toString();
                String partnerstr = "";

                if (partner.isChecked()) {
                    partnerstr = "true";
                }

                Boolean emptycheck = checkempty(usernamestr, passwordstr);
                Boolean checkresult = comparepasswords(passwordstr, password2str);

                if (checkresult == true && emptycheck == true) {
                    try {
                        byte[] salt = getSalt();
                        String hashed = getSecurePassword(passwordstr, salt);
                        String stringsalt = Base64.getEncoder().encodeToString(salt);
                        String[] creds = new String[4];
                        creds[0] = usernamestr;
                        creds[1] = hashed;
                        creds[2] = stringsalt;
                        creds[3] = partnerstr;

                        CreateAccountDB createaccount = new CreateAccountDB(CreateAccount.this);
                        createaccount.execute(creds);

                    } catch (Exception e) {
                        Toast toast = Toast.makeText(CreateAccount.this, e.toString(), Toast.LENGTH_LONG);
                        toast.show();
                    }

                } else {
                    if (checkresult == false) {
                        wrongpassword();
                    } else if (emptycheck == true) {
                        emptyfields();
                    }

                }

            }
        });

    }

    public Boolean comparepasswords(String password, String password2) {
        if (password.equals(password2)) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkempty(String username, String password) {
        if (username.equals("") && password.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public void emptyfields() {
        AlertDialog alertDialog = new AlertDialog.Builder(CreateAccount.this).create();
        alertDialog.setTitle("Empty Fields");
        alertDialog.setMessage("Please Enter All Fields");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void wrongpassword() {
        AlertDialog alertDialog = new AlertDialog.Builder(CreateAccount.this).create();
        alertDialog.setTitle("Passwords Do Not Match");
        alertDialog.setMessage("Passwords given do not match.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private static String getSecurePassword(String passwordToHash, byte[] salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException {
        //Always use a SecureRandom generator
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        //Create array for salt
        byte[] salt = new byte[16];
        //Get a random salt
        sr.nextBytes(salt);
        //return salt
        return salt;
    }
}
