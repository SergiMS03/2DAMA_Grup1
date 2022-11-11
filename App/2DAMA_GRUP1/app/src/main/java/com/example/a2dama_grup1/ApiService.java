package com.example.a2dama_grup1;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
        @Multipart
        @POST("http://192.168.1.45:3000/uploadFile")
        Call<ResponseBody> postImage(@Part MultipartBody.Part image, @Part("myFile") RequestBody name);
}
