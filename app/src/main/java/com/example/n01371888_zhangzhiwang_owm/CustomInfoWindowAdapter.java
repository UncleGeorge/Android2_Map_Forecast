package com.example.n01371888_zhangzhiwang_owm;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View mWindow;
    private Context mContext;

    public CustomInfoWindowAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    private void renderWindowText(Marker marker, View view){
        String title = marker.getTitle();
        String snippet = marker.getSnippet();

        TextView tvTitle = view.findViewById(R.id.title);
        TextView tvTemp = view.findViewById(R.id.temp);
        ImageView ivWeatherIcon = view.findViewById(R.id.weather_icon);

        if (title != null) {
            tvTitle.setText(title);
        }

        if (snippet != null) {
            String[] parts = snippet.split(";");
            if (parts.length == 2) {
                String iconUrl = parts[1];
                tvTemp.setText(parts[0] + "°C");
                Picasso.get()
                        .load(iconUrl)
                        .into(ivWeatherIcon, new Callback() {
                            @Override
                            public void onSuccess() {
                                if (marker != null && marker.isInfoWindowShown()) {
                                    marker.hideInfoWindow();
                                    marker.showInfoWindow();
                                }
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e("PicassoError", "Error loading image with Picasso", e);
                            }
                        });
            }
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }
}
