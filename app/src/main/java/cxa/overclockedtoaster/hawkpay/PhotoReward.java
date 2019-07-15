package cxa.overclockedtoaster.hawkpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PhotoReward extends AppCompatActivity {
    private ArrayList<Reward> itemArrayList;  //List items Array
    Integer userid = null;
    Double amount = null;
    String username = null;
    public static final int REQUEST_IMAGE = 100;
    public static final int REQUEST_PERMISSION = 200;
    String imageFilePath = "";
    Uri mMakePhotoUri;
    Boolean success;
    Timestamp rewardtimestamp;
    Boolean approved;
    private PhotoReward.MyAppAdapter myAppAdapter; //Array Adapter
    private RecyclerView recyclerView; //RecyclerView
    private RecyclerView.LayoutManager mLayoutManager;
    Integer rewardpoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_reward);

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.amount")) {
            amount = getIntent().getExtras().getDouble("cxa.overclockedtoaster.hawkpay.amount");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.userid")) {
            userid = getIntent().getExtras().getInt("cxa.overclockedtoaster.hawkpay.userid");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.username")) {
            username = getIntent().getExtras().getString("cxa.overclockedtoaster.hawkpay.username");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.rewardpoints")) {
            rewardpoints = getIntent().getExtras().getInt("cxa.overclockedtoaster.hawkpay.rewardpoints");
        }

        Button rewardbutton = (Button) findViewById(R.id.uploadphotobutton);
        rewardbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCameraIntent();
            }
        });

        TextView rewardtv = (TextView) findViewById(R.id.rewardpoints);
        rewardtv.setText("Reward Points: ".concat(rewardpoints.toString()));

        recyclerView = (RecyclerView) findViewById(R.id.rewardsrv); //Recyclerview Declaration
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        itemArrayList = new ArrayList<Reward>(); // Arraylist Initialization

        // Calling Async Task
        PhotoReward.SyncData orderData = new PhotoReward.SyncData();
        orderData.execute("");

    }

    private void openCameraIntent() {
        try {
            File f = createImageFile();
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            mMakePhotoUri = FileProvider.getUriForFile(PhotoReward.this, getApplicationContext().getPackageName() + ".provider", createImageFile());
            i.putExtra(MediaStore.EXTRA_OUTPUT, mMakePhotoUri);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(i, REQUEST_IMAGE);
        } catch (IOException e) {
//            Log.e(T?AG, "IO error", e);
//            Toast.makeText(getActivity(), R.string.error_writing_image, Toast.LENGTH_LONG).show();
            System.out.println(e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Thanks for granting Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                System.out.println(imageFilePath);
                System.out.println("grrrrr");

                try {
                    System.out.println(mMakePhotoUri);

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mMakePhotoUri);
                    ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
                    byte [] b=baos.toByteArray();
                    String imagestr=Base64.encodeToString(b, Base64.DEFAULT);


                    String[] stuff = new String[4];
                    stuff[0] = userid.toString();
                    stuff[1] = imagestr;
                    stuff[2] = imageFilePath;
                    stuff[3] = userid.toString();

                    System.out.println("IM HERE");

                    RewardsDB rewards = new RewardsDB(PhotoReward.this);
                    rewards.execute(stuff);

                } catch (Exception e) {
                    System.out.println(e);
                }


            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "You cancelled the operation", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", dir);

        imageFilePath = image.getAbsolutePath();
        return image;

    }


    private class SyncData extends AsyncTask<String, String, String>
    {
        String msg = "Internet/DB_Credentials/Windows_FireWall_TurnOn Error, See Android Monitor in the bottom For details!";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            progress = ProgressDialog.show(PhotoReward.this, "Good Choice!",
                    "Loading your available food!", true);
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
            try
            {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://18.223.22.246:1433/hawkfast","cxa19","cxa19");
                if (conn == null)
                {
                    success = false;
                }
                else {
                    // Change below query according to your own database.
                    PreparedStatement stmt= conn.prepareStatement("select timestamp, approved from hawkfast.rewards where userid = ?");
                    stmt.setInt(1, userid);
                    ResultSet rs = stmt.executeQuery();

                    if (rs != null) // if resultset not null, I add items to itemArraylist using class created
                    {
                        while (rs.next())
                        {
                            try {
                                rewardtimestamp = rs.getTimestamp(1);
                                approved = rs.getBoolean(2);

                                itemArrayList.add(new Reward(userid, rewardtimestamp, approved));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        msg = "Data Found";
                        success = true;
                    } else {
                        msg = "No Food???!";
                        success = false;
                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();
                success = false;
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) // disimissing progress dialoge, showing error and setting up my gridview
        {
            progress.dismiss();
            Toast.makeText(PhotoReward.this, msg + "", Toast.LENGTH_LONG).show();
            if (success == false)
            {
            }
            else {
                try {
                    myAppAdapter = new PhotoReward.MyAppAdapter(itemArrayList , PhotoReward.this);
                    recyclerView.setAdapter(myAppAdapter);
                } catch (Exception ex) {
                    System.out.println(ex);
                }

            }
        }
    }

    public class MyAppAdapter extends RecyclerView.Adapter<PhotoReward.MyAppAdapter.ViewHolder> {
        private List<Reward> values;
        public Context context;

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            // public image title and image url
            public TextView rewardnumber;
            public TextView rewardinfo;
            public View layout;

            public ViewHolder(View v)
            {
                super(v);
                layout = v;
                rewardnumber = (TextView) v.findViewById(R.id.rewardnumber);
                rewardinfo = (TextView) v.findViewById(R.id.rewardinfo);
            }
        }

        // Constructor
        public MyAppAdapter(List<Reward> myDataset, Context context)
        {
            values = myDataset;
            this.context = context;
        }

        // Create new views (invoked by the layout manager) and inflates
        @Override
        public PhotoReward.MyAppAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            // create a new view
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.layout_rewards, parent, false);
            PhotoReward.MyAppAdapter.ViewHolder vh = new PhotoReward.MyAppAdapter.ViewHolder(v);
            return vh;
        }

        // Binding items to the view
        @Override
        public void onBindViewHolder(PhotoReward.MyAppAdapter.ViewHolder holder, final int position) {

            final Reward classListItems = values.get(position);
            int number = 0;
            number = (int)((Math.random() * 9000000)+1000000);

            holder.rewardnumber.setText("Reward Request #".concat(String.valueOf(number)));
            String approvedstr;


            if (classListItems.getApproved() == false) {
                approvedstr = "No";
            } else {
                approvedstr = "Yes";
            }

            String info = "Timestamp: " + classListItems.getRewardtimestamp()
                    + "\nApproved: " + approvedstr;

            holder.rewardinfo.setText(info);

        }

        // get item count returns the list item count
        @Override
        public int getItemCount() {
            return values.size();
        }

    }

}