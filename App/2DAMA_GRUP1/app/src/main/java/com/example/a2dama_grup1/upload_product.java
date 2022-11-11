package com.example.a2dama_grup1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class upload_product extends AppCompatActivity{

    private EditText product_name;
    private EditText price;
    private EditText stock;
    private EditText product_description;
    private ImageButton image;
    private static Retrofit retrofit;
    private static String url_pushImage =  "http://localhost:3000/uploadFile";
    Date fecha = new Date();
    String filePath = fecha + ".jpeg";

    ApiService apiService;
    Uri picUri;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;
    FloatingActionButton fabCamera, fabUpload;
    Bitmap mBitmap;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);

        product_name = (EditText) (findViewById(R.id.editText_nomProducte_upload));
        price = (EditText) (findViewById(R.id.editText_precio_upload));
        stock = (EditText) (findViewById(R.id.editText_stock_upload));
        product_description = (EditText) (findViewById(R.id.editText_descripcio_upload));
        image = (ImageButton) (findViewById(R.id.uploadImage_btn));

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
    }

    //NO FUNCIONA
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
    //////////////

    public void displayToast (String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }


    public void clickUploadProduct(View view){
        initRetrofitClient();
        if (mBitmap != null)
            multipartImageUpload();
        else {
            Toast.makeText(getApplicationContext(), "Bitmap is null. Try again", Toast.LENGTH_SHORT).show();
        }
        String HOST = "http://localhost:3000/uploadProduct/"+product_name.getText()+"/"+price.getText()+"/"
                +stock.getText()+"/"+product_description.getText();
        new productToServer().execute(HOST);
    }


/////////////////////////////////////////////////
/////////////////////////////////////////////////


    //**********UPLOAD IMAGE ***********
    public void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i);
    }

    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
        new ActivityResultContracts
                .StartActivityForResult(),
        result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null  && data.getData() != null) {
                    Uri selectedImageUri = data.getData();
                    mBitmap = null;
                    try {
                        mBitmap = MediaStore.Images.Media.getBitmap(
                                this.getContentResolver(),
                                selectedImageUri);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    image.setImageBitmap(mBitmap);
                }
            }
        });


    private void multipartImageUpload() {
        try {
            File filesDir = getApplicationContext().getFilesDir();
            File file = new File(filesDir, "image" + ".png");

            OutputStream os;
            try {
                os = new FileOutputStream(file);
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();


            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();


            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("myFile", file.getName(), reqFile);
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "myFile");

            Call<ResponseBody> req = apiService.postImage(body, name);
            req.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    Log.e("Upload", String.valueOf(response.body()));

                    if (response.code() == 200) {
                        textView.setText("Uploaded Successfully!");
                        textView.setTextColor(Color.BLUE);
                    }

                    Toast.makeText(getApplicationContext(), response.code() + " ", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    textView.setText("Uploaded Failed!");
                    textView.setTextColor(Color.RED);
                    Toast.makeText(getApplicationContext(), "Request failed", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                    Log.e("ERROR", t.toString());
                }
            });


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initRetrofitClient() {
        OkHttpClient client = new OkHttpClient.Builder().build();

        apiService = new Retrofit.Builder().baseUrl("http://localhost:3000").client(client).build().create(ApiService.class);
    }




///////////////////////////////////////////////
///////////////////////////////////////////////
///////////////////////////////////////////////

    //********UPLOAD PRODUCT********


    public class productToServer extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            Log.i("LOGINFO", "do in Background: !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

            return dades(strings[0]);
        }

        private String dades(String queryString){
            HttpURLConnection con = null;
            BufferedReader reader = null;
            String result = null;

            try{
                Log.i("LOGINFO", "dades: !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

                String url = queryString;
                Uri builtURI = Uri.parse(url).buildUpon().build();
                URL requestURL = new URL(builtURI.toString());
                con = (HttpURLConnection) requestURL.openConnection();
                con.setRequestMethod("GET");
                con.connect();

                InputStream inputStream = con.getInputStream();

                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder builder = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                    builder.append("\n");
                }
                if (builder.length() == 0) {
                    return null;
                }

                result = builder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                if (con != null)
                    con.disconnect();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return result;
        }

        protected void onPostExecute(String s){
            super.onPostExecute(s);
            try {
                if(s.equals("0\n")){
                    displayToast("Upload successful");
                }
                else{
                    displayToast("Alguna dada no és correcte");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}