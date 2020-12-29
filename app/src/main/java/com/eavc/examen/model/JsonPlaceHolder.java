package com.eavc.examen.model;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface JsonPlaceHolder {
    @GET("getFile.json?dl=0")
    Call <NodoPrincipal> getInformacion();

    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);
}
