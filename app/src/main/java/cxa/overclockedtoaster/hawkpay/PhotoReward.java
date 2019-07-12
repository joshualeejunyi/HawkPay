package cxa.overclockedtoaster.hawkpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PhotoReward extends AppCompatActivity {
    Integer userid = null;
    Double amount = null;
    String username = null;
    public static final int REQUEST_IMAGE = 100;
    public static final int REQUEST_PERMISSION = 200;
    String imageFilePath = "";
    Uri mMakePhotoUri;

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

        Button rewardbutton = (Button) findViewById(R.id.uploadphotobutton);
        rewardbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCameraIntent();
            }
        });
    }
//    private void selectImage() {
//        final CharSequence[] options = { "Take Photo","Cancel" };
//        AlertDialog.Builder builder = new AlertDialog.Builder(PhotoReward.this);
//        builder.setTitle("Add Photo!");
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                if (options[item].equals("Take Photo"))
//                {
//                    openCameraIntent();
//                }
//                else if (options[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
//    }

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


                    String[] stuff = new String[3];
                    stuff[0] = userid.toString();
                    stuff[1] = imagestr;
                    stuff[2] = imageFilePath;

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
}
