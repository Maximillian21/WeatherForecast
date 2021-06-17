package com.maxym.weatherforecast.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.internal.StringResourceValueReader;
import com.maxym.weatherforecast.R;
import com.maxym.weatherforecast.model.Forecast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {

    private int selectedItem;
    private List<com.maxym.weatherforecast.model.List> data;
    private ArrayList<Integer> date;
    private Calendar calendar;
    private static OnCardClickListener onCardClickListener;

    public interface OnCardClickListener {
        void onCardClick(int position, List<com.maxym.weatherforecast.model.List> data);
    }

    public static void setOnCardClickListener(OnCardClickListener listener) {
        onCardClickListener = listener;
    }

    public DataAdapter () {
        data = new ArrayList<>();
        date = new ArrayList<>();
        calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        for(int i = 0; i < 7; i++) {
            date.add(day);
            day++;
            if(day == 8) day = 1;
        }
        selectedItem = 0;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.forecast_item, parent, false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        holder.bind(data.get(position).getTemp().getDay(),
                data.get(position).getWeather().get(0).getDescription());
        holder.bindImage(data.get(position).getWeather().get(0).getIcon());
        holder.setWeekDayName(date.get(position));
        holder.cl.setBackgroundResource(R.drawable.gradient_light_blue);
        if(selectedItem == position) {
            holder.cl.setBackgroundResource(R.drawable.gradient_dark_purple);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previousItem = selectedItem;
                selectedItem = position;
                notifyItemChanged(previousItem);
                notifyItemChanged(selectedItem);
                onCardClickListener.onCardClick(position, data);
            }
        });
    }

    public void setData(List<com.maxym.weatherforecast.model.List> forecast) {
        data.clear();
        data.addAll(forecast);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class DataViewHolder extends RecyclerView.ViewHolder {
        TextView tvAverageTemperature;
        TextView tvDate;
        TextView tvDescription;
        ConstraintLayout cl;
        ImageView weatherImage;
        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAverageTemperature = itemView.findViewById(R.id.temperature);
            weatherImage = itemView.findViewById(R.id.weather_icon);
            tvDate = itemView.findViewById(R.id.date);
            tvDescription = itemView.findViewById(R.id.conditions);
            cl = itemView.findViewById(R.id.constraint_layout);
        }

        public void bindImage(String conditions) {
            switch(conditions) {
                case "01d":
                    weatherImage.setImageResource(R.drawable.clear_sky);
                case "02d":
                    weatherImage.setImageResource(R.drawable.few_clouds);
                    break;
                case "03d":
                case "04d":
                    weatherImage.setImageResource(R.drawable.clouds);
                    break;
                case "09d":
                case "10d":
                    weatherImage.setImageResource(R.drawable.rain);
                    break;
                case "11d":
                    weatherImage.setImageResource(R.drawable.thunderstorm);
                    break;
                case "13d":
                    weatherImage.setImageResource(R.drawable.snow);
                    break;
                default:
                    weatherImage.setImageResource(R.drawable.error);
                    break;
            }
        }

        public void bind(float temperature, String conditions) {
            tvAverageTemperature.setText(String.valueOf(temperature) + "°C");
            conditions = conditions.substring(0, 1).toUpperCase() + conditions.substring(1);
            tvDescription.setText(conditions);
        }

        public void setWeekDayName(int day) {
            switch(day) {
                case Calendar.SUNDAY:
                    tvDate.setText("Sunday");
                    break;
                case Calendar.MONDAY:
                    tvDate.setText("Monday");
                    break;
                case Calendar.TUESDAY:
                    tvDate.setText("Tuesday");
                    break;
                case Calendar.WEDNESDAY:
                    tvDate.setText("Wednesday");
                    break;
                case Calendar.THURSDAY:
                    tvDate.setText("Thursday");
                    break;
                case Calendar.FRIDAY:
                    tvDate.setText("Friday");
                    break;
                case Calendar.SATURDAY:
                    tvDate.setText("Saturday");
                    break;
            }
        }
    }
}
