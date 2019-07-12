package cxa.overclockedtoaster.hawkpay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AvailableFood extends AppCompatActivity {
    private ArrayList<Food> itemArrayList;  //List items Array
    private MyAppAdapter myAppAdapter; //Array Adapter
    private RecyclerView recyclerView; //RecyclerView
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean success = false; // boolean
    private Integer storeid = null;
    private String storename = null;
    private String storeinfo = null;
    private Bitmap storeimage = null;
    Integer userid = null;
    Double amount = null;
    String username = null;
    Integer foodid = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_food);

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.storeid")) {
            storeid = getIntent().getExtras().getInt("cxa.overclockedtoaster.hawkpay.storeid");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.storename")) {
            storename = getIntent().getExtras().getString("cxa.overclockedtoaster.hawkpay.storename");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.storeinfo")) {
            storeinfo = getIntent().getExtras().getString("cxa.overclockedtoaster.hawkpay.storeinfo");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.storeimage")) {
            storeimage = (Bitmap) getIntent().getParcelableExtra("cxa.overclockedtoaster.hawkpay.storeimage");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.amount")) {
            amount = getIntent().getExtras().getDouble("cxa.overclockedtoaster.hawkpay.amount");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.userid")) {
            userid = getIntent().getExtras().getInt("cxa.overclockedtoaster.hawkpay.userid");
        }

        if (getIntent().hasExtra("cxa.overclockedtoaster.hawkpay.username")) {
            username = getIntent().getExtras().getString("cxa.overclockedtoaster.hawkpay.username");
        }

        ImageView backbuton = (ImageView) findViewById(R.id.backbutton);
        backbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ImageView imagetv = (ImageView) findViewById(R.id.image);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), storeimage);
        imagetv.setBackgroundDrawable(bitmapDrawable);

        TextView storenametv = (TextView) findViewById(R.id.storename);
        storenametv.setText(storename);

        TextView storeinfotv = (TextView) findViewById(R.id.storeinfo);
        storeinfotv.setText(storeinfo);

        recyclerView = (RecyclerView) findViewById(R.id.rv); //Recyclerview Declaration
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        itemArrayList = new ArrayList<Food>(); // Arraylist Initialization

        // Calling Async Task
        SyncData orderData = new SyncData();
        orderData.execute("");

    }

    private class SyncData extends AsyncTask<String, String, String>
    {
        String msg = "Internet/DB_Credentials/Windows_FireWall_TurnOn Error, See Android Monitor in the bottom For details!";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            progress = ProgressDialog.show(AvailableFood.this, "Good Choice!",
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
                    PreparedStatement stmt= conn.prepareStatement("select foodname, ingredient1, ingredient1price, ingredient2, ingredient2price, ingredient3, ingredient3price, ingredient4, ingredient4price, totalprice, foodid from hawkfast.food where storeid = ?");
                    stmt.setInt(1, storeid);
                    ResultSet rs = stmt.executeQuery();

                    if (rs != null) // if resultset not null, I add items to itemArraylist using class created
                    {
                        while (rs.next())
                        {
                            try {
                                String foodname = rs.getString(1);
                                String ingredient1 = rs.getString(2);
                                Double ingredient1price = rs.getDouble(3);
                                String ingredient2 = rs.getString(4);
                                Double ingredient2price = rs.getDouble(5);
                                String ingredient3 = rs.getString(6);
                                Double ingredient3price = rs.getDouble(7);
                                String ingredient4 = rs.getString(8);
                                Double ingredient4price = rs.getDouble(9);
                                Double totalprice = rs.getDouble(10);
                                foodid = rs.getInt(11);

                                itemArrayList.add(new Food(storeid, foodname, ingredient1, ingredient1price, ingredient2, ingredient2price, ingredient3, ingredient3price, ingredient4, ingredient4price, totalprice));
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
            Toast.makeText(AvailableFood.this, msg + "", Toast.LENGTH_LONG).show();
            if (success == false)
            {
            }
            else {
                try {
                    myAppAdapter = new AvailableFood.MyAppAdapter(itemArrayList , AvailableFood.this);
                    recyclerView.setAdapter(myAppAdapter);
                } catch (Exception ex) {
                    System.out.println(ex);
                }

            }
        }
    }

    public class MyAppAdapter extends RecyclerView.Adapter<AvailableFood.MyAppAdapter.ViewHolder> {
        private List<Food> values;
        public Context context;

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            // public image title and image url
            public TextView foodname;
            public TextView foodinfo;
            public View layout;

            public ViewHolder(View v)
            {
                super(v);
                layout = v;
                foodname = (TextView) v.findViewById(R.id.foodname);
                foodinfo = (TextView) v.findViewById(R.id.foodinfo);
            }
        }

        // Constructor
        public MyAppAdapter(List<Food> myDataset, Context context)
        {
            values = myDataset;
            this.context = context;
        }

        // Create new views (invoked by the layout manager) and inflates
        @Override
        public AvailableFood.MyAppAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            // create a new view
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.layout_food, parent, false);
            AvailableFood.MyAppAdapter.ViewHolder vh = new AvailableFood.MyAppAdapter.ViewHolder(v);
            return vh;
        }

        // Binding items to the view
        @Override
        public void onBindViewHolder(AvailableFood.MyAppAdapter.ViewHolder holder, final int position) {

            final Food classListItems = values.get(position);
            holder.foodname.setText(Html.fromHtml(classListItems.getUnderlinedFoodName()));
            holder.foodinfo.setText(Html.fromHtml(classListItems.getFoodInfo()));

            Intent orderfood = new Intent(context.getApplicationContext(), OrderFood.class);
            orderfood.putExtra("cxa.overclockedtoaster.hawkpay.userid", userid);
            orderfood.putExtra("cxa.overclockedtoaster.hawkpay.amount", amount);
            orderfood.putExtra("cxa.overclockedtoaster.hawkpay.username", username);
            orderfood.putExtra("cxa.overclockedtoaster.hawkpay.storeid", storeid);
            orderfood.putExtra("cxa.overclockedtoaster.hawkpay.storename", storename);
            orderfood.putExtra("cxa.overclockedtoaster.hawkpay.storeimage", storeimage);
            orderfood.putExtra("cxa.overclockedtoaster.hawkpay.foodname", classListItems.getFoodname());
            orderfood.putExtra("cxa.overclockedtoaster.hawkpay.ingredient1", classListItems.getIngredient1());
            orderfood.putExtra("cxa.overclockedtoaster.hawkpay.ingredient1price", classListItems.getIngredient1price());
            orderfood.putExtra("cxa.overclockedtoaster.hawkpay.ingredient2", classListItems.getIngredient2());
            orderfood.putExtra("cxa.overclockedtoaster.hawkpay.ingredient2price", classListItems.getIngredient2price());
            orderfood.putExtra("cxa.overclockedtoaster.hawkpay.ingredient3", classListItems.getIngredient3());
            orderfood.putExtra("cxa.overclockedtoaster.hawkpay.ingredient3price", classListItems.getIngredient3price());
            orderfood.putExtra("cxa.overclockedtoaster.hawkpay.ingredient4", classListItems.getIngredient4());
            orderfood.putExtra("cxa.overclockedtoaster.hawkpay.ingredient4price", classListItems.getIngredient4price());
            orderfood.putExtra("cxa.overclockedtoaster.hawkpay.totalprice", classListItems.getTotalprice());
            orderfood.putExtra("cxa.overclockedtoaster.hawkpay.foodid", foodid);

            holder.foodname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(orderfood);
                }
            });

            holder.foodinfo .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(orderfood);
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
