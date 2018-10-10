package ru.yandex.dunaev.mick.photoinstance;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DiskInfo {
    @SerializedName("total_space")
    @Expose
    private long total_space;

    @SerializedName("used_space")
    @Expose
    private long used_space;

    public long getTotal_space() {
        return total_space;
    }

    public long getUsed_space() {
        return used_space;
    }
}
