package com.infusiblecoder.myvideodownloaderv2.utils;

import com.infusiblecoder.myvideodownloaderv2.model.DataModel;
import com.infusiblecoder.myvideodownloaderv2.model.ModelFacebook;
import com.infusiblecoder.myvideodownloaderv2.model.ModelInstagram;
import com.infusiblecoder.myvideodownloaderv2.model.ModelLiveleak;
import com.infusiblecoder.myvideodownloaderv2.model.ModelTed;
import com.infusiblecoder.myvideodownloaderv2.model.ModelVimeo;
import com.infusiblecoder.myvideodownloaderv2.model.ModelYoutube;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface apiRest {
    @GET("demo.php")
    Call<DataModel> getdata(@Query("key") String str);

    @GET("demo-facebook.php")
    Call<ModelFacebook> getfacebook(@Query("facebook_url") String str, @Query("version") String str2);

    @GET("demo-instagram.php")
    Call<ModelInstagram> getinstagram(@Query("instagram_url") String str, @Query("version") String str2);

    @GET("demo-liveleak.php")
    Call<ModelLiveleak> getliveleak(@Query("liveleak_url") String str, @Query("version") String str2);

    @GET("demo-ted.php")
    Call<ModelTed> getted(@Query("ted_url") String str, @Query("version") String str2);

    @GET("demo-vimeo.php")
    Call<ModelVimeo> getviemo(@Query("vimeo_url") String str, @Query("version") String str2);

    @GET("demo-yotube.php")
    Call<ModelYoutube> getyoutube(@Query("youtube_url") String str, @Query("version") String str2);
}
