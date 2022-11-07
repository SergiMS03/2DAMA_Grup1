package com.example.a2dama_grup1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.io.File;
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

    private static Retrofit retrofit;
    private static String BASE_URL =  "http://192.168.207.154:3000/pushImage";
    Date fecha = new Date();
    String filePath = fecha + ".jpeg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);
        uploadImage();
    }

    private void uploadImage() {
        File file = new File(filePath);

        Retrofit retrofit = getRetrofit();

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part parts = MultipartBody.Part.createFormData("newimage", file.getName(), requestBody);

        RequestBody someData = RequestBody.create(MediaType.parse("text/plain"), "This is a new image");

        UploadApis uploadApis = retrofit.create(UploadApis.class);
        Call call = uploadApis.uploadImage(parts, someData);
        call.enqueue(new Callback(){
            @Override
            public void onResponse (Call call, Response response){

            }
            @Override
            public void onFailure(Call call, Throwable t){

            }
        });
    }


    public static Retrofit getRetrofit(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        if(retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).
                    addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build();
        }
        return retrofit;
    }
}