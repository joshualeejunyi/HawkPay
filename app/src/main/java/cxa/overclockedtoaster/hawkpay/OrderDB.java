package cxa.overclockedtoaster.hawkpay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class OrderDB extends AsyncTask<String, Void, Boolean> {
    Context context;
    ProgressDialog p;
    double amount = 0;
    Boolean dbpartner = null;
    Double price = 0.0;
    String storename = "";
    Double finalbalance = 0.0;
    Integer userid;
    String username;
    String newpayload;
    String foodname;

    OrderDB(Context context) {
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
        userid = Integer.parseInt(args[0]);
        Integer storeid = Integer.parseInt(args[1]);
        Integer foodid = Integer.parseInt(args[2]);
        String payload = args[3];
        price = Double.parseDouble(args[4]);
        storename = args[5];
        username = args[6];
        newpayload = args[7];
        foodname = args[8];
        Connection conn;
        Boolean result = false;

        System.out.println(userid);
        System.out.println(storeid);
        System.out.println(foodid);
        System.out.println(payload);
        System.out.println(price);

        payload = payload.replace(",", "");

//        payload = "Instructions:".concat(payload).concat(";StalldId:");
        newpayload = "Instructions:".concat(newpayload).concat(";StallId:").concat(storeid.toString()).concat(";Item:").concat(foodname).concat(";");
        String payloadfinal = "Payload=".concat(newpayload);



        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://18.223.22.246:1433/hawkfast","cxa19","cxa19");

            PreparedStatement stmt1 = conn.prepareStatement("select amount from hawkfast.users where id = ?");
            stmt1.setInt(1, userid);

            ResultSet rs1 = stmt1.executeQuery();
            Double dbbalance = 0.0;

            while (rs1.next()) {
                dbbalance = rs1.getDouble(1);
            }

            System.out.println(dbbalance);

            if (dbbalance > price) {
                PreparedStatement stmt= conn.prepareStatement("insert into hawkfast.orders (userid, storeid, foodid, selectedingredients, cost) values(?,?,?,?,?)");
                stmt.setInt(1, userid);
                stmt.setInt(2, storeid);
                stmt.setInt(3, foodid);
                stmt.setString(4, payload);
                stmt.setDouble(5, price);

                Integer i = stmt.executeUpdate();
                System.out.println(i + "records inserted");

                finalbalance = dbbalance - price;

                PreparedStatement stmt2 = conn.prepareStatement("update hawkfast.users set amount = ? where id = ?");
                stmt2.setDouble(1, finalbalance);
                stmt2.setInt(2, userid);

                Integer o = stmt2.executeUpdate();
                System.out.println(o + "records updated");
                result = true;


            } else {
                return false;
            }

            String urlstr = "http://ec2-18-223-22-246.us-east-2.compute.amazonaws.com/api/MQTT";
            URL url = new URL(urlstr);

            HttpURLConnection urlconn = (HttpURLConnection) url.openConnection();
            urlconn.setRequestMethod("POST");
            urlconn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

            OutputStream os = urlconn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(payloadfinal);
            writer.flush();
            writer.close();
            os.close();

            urlconn.connect();

            String httpresult = urlconn.getResponseMessage()+"";
            System.out.println("PLSPLSPLSPLSPLS");
            System.out.println(httpresult);

        } catch (Exception e) {
            System.out.println(e);
            result = false;
        }

        return result;
    }

    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        p.hide();

        if (result == true) {
            Intent finish = new Intent(context.getApplicationContext(), PaymentSuccess.class);
            finish.putExtra("cxa.overclockedtoaster.hawkpay.price", price);
            finish.putExtra("cxa.overclockedtoaster.hawkpay.storename", storename);
            finish.putExtra("cxa.overclockedtoaster.hawkpay.amount", finalbalance);
            finish.putExtra("cxa.overclockedtoaster.hawkpay.userid", userid);
            finish.putExtra("cxa.overclockedtoaster.hawkpay.username", username);
            context.startActivity(finish);
        } else {
            Toast toast = Toast.makeText(context, "Error Ordering Food", Toast.LENGTH_SHORT);
            toast.show();
        }


    }

}
