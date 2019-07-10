package cxa.overclockedtoaster.hawkpay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CreateAccountDB  extends AsyncTask<String, Void, Boolean> {

    Context context;
    ProgressDialog p;
    double amount;

    CreateAccountDB(Context context) {
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

    @Override
    protected Boolean doInBackground(String... args) {
        Connection conn = null;
        String username = args[0];
        String password = args[1];
        String salt = args[2];
        String partnerstr = args[3];
        Boolean partner = false;

        if (partnerstr.equals("true")) {
            partner = true;
        } else {
            partner = false;
        }


        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://18.223.22.246/hawkfast","cxa19","cxa19");

            PreparedStatement stmt= conn.prepareStatement("insert into hawkfast.users (username, password, salt, hawkfastpartner) values (?, ?, ?, ?)");

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, salt);
            stmt.setBoolean(4, partner);

            int row = stmt.executeUpdate();

            return true;

        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        p.hide();

        if (result == true) {
            Intent startIntent = new Intent(context.getApplicationContext(), MainActivity.class);
            Boolean created = true;
            startIntent.putExtra("com.example.hawkfastpaylah.createaccount", created);
            context.startActivity(startIntent);
        } else {
            Toast toast = Toast.makeText(context, "Error Creating Account", Toast.LENGTH_SHORT);
            toast.show();
        }


    }

}
