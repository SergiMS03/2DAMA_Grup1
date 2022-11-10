package com.example.a2dama_grup1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class upload_product extends AppCompatActivity {

    private EditText product_name;
    private EditText price;
    private EditText stock;
    private EditText product_description;
    private ImageButton image;
    private int SELECT_PICTURE = 200;
    private static Retrofit retrofit;
    private static String url_pushImage =  "http://192.168.207.155:3000/pushImage";
    Date fecha = new Date();
    String filePath = fecha + ".jpeg";

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


    //**********UPLOAD IMAGE ***********
    private void imageChooser()
    {
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
                    Bitmap selectedImageBitmap = null;
                    try {
                        selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                                            this.getContentResolver(),
                                            selectedImageUri);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    image.setImageBitmap(selectedImageBitmap);
                }
            }
    });

    /*private void uploadImage() {


        File file = new File(filePath);

        Retrofit retrofit = getRetrofit();

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part parts = MultipartBody.Part.createFormData("newimage", file.getName(), requestBody);

        RequestBody someData = RequestBody.create(MediaType.parse("text/plain"), "This is a new image");

        UploadApis uploadApis = retrofit.create(UploadApis.class);
        Call call = uploadApis.uploadImage(parts, someData);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }


    public static Retrofit getRetrofit(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        if(retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(url_pushImage).
                    addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build();
        }
        return retrofit;
    }*/

    public static void postData(Bitmap imageToSend) {
        try
        {
            URL url = new URL("http://myserver/myapp/upload-image");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Cache-Control", "no-cache");

            conn.setReadTimeout(35000);
            conn.setConnectTimeout(35000);

            // directly let .compress write binary image data
            // to the output-stream
            OutputStream os = conn.getOutputStream();
            imageToSend.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();

            System.out.println("Response Code: " + conn.getResponseCode());

            InputStream in = new BufferedInputStream(conn.getInputStream());
            Log.d("sdfs", "sfsd");
            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = responseStreamReader.readLine()) != null)
                stringBuilder.append(line).append("\n");
            responseStreamReader.close();

            String response = stringBuilder.toString();
            System.out.println(response);

            conn.disconnect();
        }
        catch(MalformedURLException e) {
            e.printStackTrace();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    //********UPLOAD PRODUCT********

    public void clickUploadProduct(View view){
        //uploadImage();
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        postData(bitmap);
        String HOST = "http://192.168.207.155:3000/uploadProduct/"+product_name.getText()+"/"+price.getText()+"/"
                +stock.getText()+"/"+product_description.getText();
        new productToServer().execute(HOST);
    }


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