package cxa.overclockedtoaster.hawkpay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;



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
        JSONObject json = new JSONObject();
        Connection conn;
        Boolean result = false;

        System.out.println(userid);
        System.out.println(storeid);
        System.out.println(foodid);
        System.out.println(payload);
        System.out.println(price);


        System.out.println(json);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://18.223.22.246/hawkfast","cxa19","cxa19");

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

            HttpURLConnection httpcon = null;
            String url = "http://ec2-18-223-22-246.us-east-2.compute.amazonaws.com/api/MQTT";
            String jsonresult = "";

            try {
                json.put("payload", payload);
            } catch (Exception e) {
                System.out.println(e);
            }

            String jsonstr = "{ \"payload\": \"1,2,3\"}";
            System.out.println(jsonstr);

            OrderDB orderting = new OrderDB(context);


            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println(response);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error);
                }
            }){
                @Override
                public byte[] getBody() throws AuthFailureError {
                    return jsonstr.getBytes();
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
            queue.start();

//            try {
//                httpcon = (HttpURLConnection) ((new URL (url).openConnection()));
//                httpcon.setDoOutput(true);
//                httpcon.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//                httpcon.setRequestProperty("Accept", "application/json");
//                httpcon.setRequestMethod("POST");
//                httpcon.connect();
//
//                //Write
//                OutputStream os = httpcon.getOutputStream();
//                os.write(jsonstr.getBytes("UTF-8"));
//                os.close();
//
//                //Read
//                BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(),"UTF-8"));
//
//                String line = null;
//                StringBuilder sb = new StringBuilder();
//
//                while ((line = br.readLine()) != null) {
//                    sb.append(line);
//                }
//
//                br.close();
//                jsonresult = sb.toString();
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                if (httpcon != null) {
//                    httpcon.disconnect();
//                }
//            }
//
//            System.out.println("GRR" + jsonresult);

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
