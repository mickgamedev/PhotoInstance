package ru.yandex.dunaev.mick.photoinstance;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DiskHelper {

    private static IYandexDiskApi sIYandexDiskApi = null;

    public static IYandexDiskApi getInstance(final String token){
        if(sIYandexDiskApi != null) return sIYandexDiskApi;

        Log.v("DiskHelper","OkHttpClient");
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                final Request request = chain.request().newBuilder()
                        .addHeader("Authorization", "OAuth " + token)
                        .build();
                return chain.proceed(request);
            }
        });
        Log.v("DiskHelper","Попытка получить интерфейс");

        sIYandexDiskApi = new Retrofit.Builder()
                .baseUrl("https://cloud-api.yandex.net:443")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
                .create(IYandexDiskApi.class);
        Log.v("DiskHelper","Интерфейс создан");

        return sIYandexDiskApi;
    }
}
