package ru.yandex.dunaev.mick.photoinstance;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickAutorization(View view) {
        startActivityForResult(new Intent(this,AutorizationActivity.class),AutorizationActivity.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == AutorizationActivity.REQUEST_CODE && data != null && resultCode == RESULT_OK){
            String token = data.getStringExtra(AutorizationActivity.EXTRA_TOKEN_REQUEST);

            Log.v("Token in main",token);

            DiskHelper.getInstance(token).getDiskInfo().enqueue(new Callback<DiskInfo>() {
                @Override
                public void onResponse(Call<DiskInfo> call, Response<DiskInfo> response) {
                    if(response.body() == null) return;
                    Log.v("Response","Данные пришли");

                    Log.v("total_space"," " + response.body().getTotal_space());
                    Log.v("used_space"," " + response.body().getUsed_space());

                    float total_space = (float)response.body().getTotal_space();
                    float user_space = (float)response.body().getUsed_space();
                    user_space /= total_space;
                    user_space *= 100.0f;//to %
                    float free_space = 100.0f - user_space;

                    List<PieEntry> entries = new ArrayList<>();
                    entries.add(new PieEntry(user_space,"Used"));
                    entries.add(new PieEntry(free_space,"Free"));

                    PieDataSet pieDataSet = new PieDataSet(entries,"Disk");
                    pieDataSet.setColors(new int[] {R.color.usedSpace,R.color.freeSpace},MainActivity.this);

                    PieChart chart = (PieChart) findViewById(R.id.chart);
                    chart.setData(new PieData(pieDataSet));
                    chart.invalidate();
                }

                @Override
                public void onFailure(Call<DiskInfo> call, Throwable t) {

                }
            });
        }
    }
}
