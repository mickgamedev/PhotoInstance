package ru.yandex.dunaev.mick.photoinstance;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IYandexDiskApi {

    @GET("/v1/disk")
    Call<DiskInfo> getDiskInfo();
}
