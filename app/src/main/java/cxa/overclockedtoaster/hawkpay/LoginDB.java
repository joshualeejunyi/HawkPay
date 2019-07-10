package cxa.overclockedtoaster.hawkpay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;

public class LoginDB extends AsyncTask<String, Void, Boolean> {
    Context context;
    ProgressDialog p;
    double amount = 0;
    Boolean dbpartner = null;
    Integer userid = null;

    LoginDB(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        p = new ProgressDialog(context);
        p.setMessage("Please wait...");
        p.setIndeterminate(false);
        p.setCancelable(false);
        p.show();
    }

    protected Boolean doInBackground(String... args) {
        boolean result = false;
        String dbuser = "";
        String dbpass = "";
        String dbsalt = "";
        Connection conn = null;
        String username = args[0];
        String password = args[1];

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://18.223.22.246/hawkfast","cxa19","cxa19");

            PreparedStatement stmt= conn.prepareStatement("select id, username, password, salt, hawkfastpartner, amount from hawkfast.users");

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                userid = rs.getInt(1);
                dbuser = rs.getString(2);
                dbpass = rs.getString(3);
                dbsalt = rs.getString(4);
                dbpartner = rs.getBoolean(5);
                amount = rs.getDouble(6);

                try {
                    byte[] salt = Base64.getDecoder().decode(dbsalt);
                    String hashed = getSecurePassword(password, salt);

                    if (dbuser.equals(username) && dbpass.equals(hashed)) {
                        result = true;
                        break;
                    }

                } catch (Exception e) {
                    System.out.println("Error" + e.toString());
                }


            }


        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(result);
        return result;
    }

    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        p.hide();

        if (result == true) {
            Intent startIntent = new Intent(context.getApplicationContext(), Dashboard.class);
            EditText username = (EditText) ((Activity) context).findViewById(R.id.username);
            String usernamestr = username.getText().toString();
            startIntent.putExtra("cxa.overclockedtoaster.hawkpay.userid", userid);
            startIntent.putExtra("cxa.overclockedtoaster.hawkpay.username", usernamestr);
            startIntent.putExtra("cxa.overclockedtoaster.hawkpay.amount", amount);
            context.startActivity(startIntent);
        } else {
            Toast toast = Toast.makeText(context, "Invalid Login Credentials", Toast.LENGTH_SHORT);
            toast.show();
        }


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

}
