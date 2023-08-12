package com.example.n01371888_zhangzhiwang_owm;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.List;

public class PredictedWeatherDialogFragment extends DialogFragment {

    private RecyclerView recyclerView;
    private ForecastAdapter adapter;
    private List<Forecast> forecastList;

    public PredictedWeatherDialogFragment(List<Forecast> forecastList) {
        this.forecastList = forecastList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_predicted_weather_dialog, container, false);

        ImageButton closeButton = view.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PredictedWeatherDialog", "Close button clicked!");
                dismiss();
            }
        });

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_line));
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new ForecastAdapter(forecastList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}