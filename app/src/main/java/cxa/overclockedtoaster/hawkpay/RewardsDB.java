package cxa.overclockedtoaster.hawkpay;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.util.Base64;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class RewardsDB extends AsyncTask<String, Void, Boolean> {
    Context context;
    ProgressDialog p;
    double amount = 0;
    Boolean dbpartner = null;
    Double price = 0.0;
    String storename = "";
    Double finalbalance = 0.0;
    Integer userid;
    String username;
    Connection conn;
    Boolean result;
    String uristr;
    Bitmap imagebitmapthingbutactuallybitmap;
    File fileuri;
    FileInputStream fis = null;
    String frickresult;
    Bitmap bitmap;
    String imagefilepath;
    String mlresult;


    RewardsDB(Context context) {
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
        uristr = args[1];
        imagefilepath = args[2];
        userid = Integer.parseInt(args[3]);

//        Uri uri = Uri.parse(uristr);

//        System.out.println(uri);
//        try {
//            InputStream in =  context.getContentResolver().openInputStream(uri);
//            OutputStream out = new FileOutputStream(new File("test.jpg"));
//            byte[] buf = new byte[1024];
//            int len;
//            while((len=in.read(buf))>0){
//                out.write(buf,0,len);
//            }
//            out.close();
//            in.close();
//        } catch (Exception e) {
//            System.out.println(e);
//        }

//        try {
//            fis = new FileInputStream(fileuri);
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//
//        System.out.println("starting");

        try {
            byte [] encodeByte=Base64.decode(uristr,Base64.DEFAULT);
            bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            System.out.println(bitmap);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();

            actuallyml ml = new actuallyml();
            mlresult = ml.imageresult(bitmapdata, imagefilepath);
            System.out.println(mlresult);

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://18.223.22.246:1433/hawkfast","cxa19","cxa19");
            PreparedStatement stmt= conn.prepareStatement("insert into hawkfast.rewards (userid, timestamp, image, approved) values(?,?,?,?)");
            stmt.setInt(1, userid);
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(2, ts);
            stmt.setBytes(3, bitmapdata);
            stmt.setBoolean(4, true);

            Integer i = stmt.executeUpdate();
            System.out.println(i + "records inserted");

            PreparedStatement stmt1 = conn.prepareStatement("select rewardpoints from hawkfast.users where id = ?");
            stmt1.setInt(1, userid);

            ResultSet rs1 = stmt1.executeQuery();
            Integer dbrewardpoints = 0;

            while (rs1.next()) {
                dbrewardpoints = rs1.getInt(1);
            }

            dbrewardpoints += 10;

            PreparedStatement stmt2 = conn.prepareStatement("update hawkfast.users set rewardpoints = ? where id = ?");
            stmt2.setInt(1, dbrewardpoints);
            stmt2.setInt(2, userid);

            Integer o = stmt2.executeUpdate();
            System.out.println(o + " REWARD records updated");

            result = true;

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
            Toast toast = Toast.makeText(context, "ML RESULT:" + mlresult, Toast.LENGTH_LONG);
            toast.show();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(mlresult);
            String prettyJsonString = gson.toJson(je);

            prettyJsonString = prettyJsonString.replace("[", "");
            prettyJsonString = prettyJsonString.replace("]", "");
            prettyJsonString = prettyJsonString.replace("{", "");
            prettyJsonString = prettyJsonString.replace("}", "");
            prettyJsonString = prettyJsonString.replace("\"", "");
            prettyJsonString = prettyJsonString.replace(",\n", "");
            prettyJsonString = prettyJsonString.replace("    label", "Label");
            prettyJsonString = prettyJsonString.replace("score", "\nScore");



            Intent finish = new Intent(context.getApplicationContext(), RewardUploaded.class);
            finish.putExtra("cxa.overclockedtoaster.hawkpay.mlresult", prettyJsonString);
            context.startActivity(finish);

        } else {
            Toast toast = Toast.makeText(context, "Error", Toast.LENGTH_SHORT);
            toast.show();
        }


    }

}
