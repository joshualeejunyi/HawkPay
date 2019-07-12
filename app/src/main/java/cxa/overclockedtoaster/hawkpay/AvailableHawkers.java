package cxa.overclockedtoaster.hawkpay;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Blob;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.sql.Connection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.ButterKnife;



public class AvailableHawkers extends AppCompatActivity {
    private ArrayList<Store> itemArrayList;  //List items Array
    private MyAppAdapter myAppAdapter; //Array Adapter
    private RecyclerView recyclerView; //RecyclerView
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean success = false; // boolean
    Integer userid = null;
    Double amount = null;
    String username = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hawk_fast_form);

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.amount")) {
            amount = getIntent().getExtras().getDouble("cxa.overclockedtoaster.hawkpay.amount");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.userid")) {
            userid = getIntent().getExtras().getInt("cxa.overclockedtoaster.hawkpay.userid");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.username")) {
            username = getIntent().getExtras().getString("cxa.overclockedtoaster.hawkpay.username");
        }



        recyclerView = (RecyclerView) findViewById(R.id.rv); //REcyclerview Declaration
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        itemArrayList = new ArrayList<Store>(); // Arraylist Initialization

        // Calling Async Task
        SyncData orderData = new SyncData();
        orderData.execute("");

        ImageView backbuton = (ImageView) findViewById(R.id.backbutton);
        backbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private class SyncData extends AsyncTask<String, String, String>
    {
        String msg = "Error";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            progress = ProgressDialog.show(AvailableHawkers.this, "Getting hungry?",
                    "Loading your food options!", true);
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
                    PreparedStatement stmt= conn.prepareStatement("select storeid, storename, storeinfo, storeimage from hawkfast.stores");
                    ResultSet rs = stmt.executeQuery();

                    if (rs != null) // if resultset not null, I add items to itemArraylist using class created
                    {
                        while (rs.next())
                        {
                            try {
                                Integer dbstoreid = rs.getInt(1);
                                String dbstorename = rs.getString(2);
                                String dbstoreinfo = rs.getString(3);

                                Blob imageblob = rs.getBlob(4);
                                byte[] imageBytes = imageblob.getBytes(1, (int) imageblob.length());
                                Bitmap imagebm = BitmapFactory.decodeByteArray(imageBytes, 0 ,imageBytes.length);

                                itemArrayList.add(new Store(dbstoreid, dbstorename, dbstoreinfo, imagebm));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        msg = "Data Loaded - now it's your tummy's turn!";
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
            Toast.makeText(AvailableHawkers.this, msg + "", Toast.LENGTH_LONG).show();
            if (success == false)
            {
            }
            else {
                try {
                    myAppAdapter = new MyAppAdapter(itemArrayList , AvailableHawkers.this);
                    recyclerView.setAdapter(myAppAdapter);
                } catch (Exception ex) {
                    System.out.println(ex);
                }

            }
        }
    }

    public class MyAppAdapter extends RecyclerView.Adapter<MyAppAdapter.ViewHolder> {
        private List<Store> values;
        public Context context;

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            // public image title and image url
            public TextView textName;
            public ImageButton imageView;
            public View layout;

            public ViewHolder(View v)
            {
                super(v);
                layout = v;
                textName = (TextView) v.findViewById(R.id.storename);
                imageView = (ImageButton) v.findViewById(R.id.storeimage);
            }
        }

        // Constructor
        public MyAppAdapter(List<Store> myDataset, Context context)
        {
            values = myDataset;
            this.context = context;
        }

        // Create new views (invoked by the layout manager) and inflates
        @Override
        public MyAppAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            // create a new view
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.layout_stores, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Binding items to the view
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            final Store classListItems = values.get(position);
            holder.textName.setText(classListItems.getStorename());


            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), classListItems.getStoreimage());
            holder.imageView.setBackgroundDrawable(bitmapDrawable);

            Integer storeid = classListItems.getStoreid();
            String storename = classListItems.getStorename();
            String storeinfo = classListItems.getStoreinfo();
            Bitmap storeimage = classListItems.getStoreimage();

            Intent gotofood = new Intent(context.getApplicationContext(), AvailableFood.class);
            gotofood.putExtra("cxa.overclockedtoaster.hawkpay.storeid", storeid);
            gotofood.putExtra("cxa.overclockedtoaster.hawkpay.storename", storename);
            gotofood.putExtra("cxa.overclockedtoaster.hawkpay.storeinfo", storeinfo);
            gotofood.putExtra("cxa.overclockedtoaster.hawkpay.storeimage", storeimage);
            gotofood.putExtra("cxa.overclockedtoaster.hawkpay.userid", userid);
            gotofood.putExtra("cxa.overclockedtoaster.hawkpay.amount", amount);
            gotofood.putExtra("cxa.overclockedtoaster.hawkpay.username", username);

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(gotofood);
                }
            });

            holder.textName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(gotofood);
                }
            });

        }

        // get item count returns the list item count
        @Override
        public int getItemCount() {
            return values.size();
        }

    }

}

