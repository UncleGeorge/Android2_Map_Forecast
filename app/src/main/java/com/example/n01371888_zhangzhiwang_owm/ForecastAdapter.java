package com.example.n01371888_zhangzhiwang_owm;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    private List<Forecast> forecastList;

    public ForecastAdapter(List<Forecast> forecastList) {
        this.forecastList = forecastList;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_item, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        Forecast forecast = forecastList.get(position);
        holder.dateTime.setText(forecast.getDateTime());
        holder.temperature.setText(forecast.getTemperature());
        holder.weather.setText(forecast.getWeather());

        Picasso.get()
                .load(forecast.getIconUrl())
                .into(holder.weatherIcon);
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    static class ForecastViewHolder extends RecyclerView.ViewHolder {

        TextView dateTime;
        TextView temperature;
        TextView weather;
        ImageView weatherIcon;

        ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTime = itemView.findViewById(R.id.tv_date_time);
            temperature = itemView.findViewById(R.id.tv_temperature);
            weather = itemView.findViewById(R.id.tv_weather);
            weatherIcon = itemView.findViewById(R.id.iv_weather_icon);
        }
    }
}
