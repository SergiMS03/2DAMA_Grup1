package com.example.a2dama_grup1;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadApis {
    public interface uploadApis {
        @Multipart
        @POST("upload")
        Call<RequestBody> uploadImage(@Part MultipartBody.Part part, @Part("someData") RequestBody requestBody);
    }
}
