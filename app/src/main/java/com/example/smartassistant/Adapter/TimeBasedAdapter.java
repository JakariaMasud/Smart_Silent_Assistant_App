package com.example.smartassistant.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartassistant.Model.TimeBasedEvent;
import com.example.smartassistant.R;
import com.example.smartassistant.databinding.SingleTimeEventBinding;
import java.util.ArrayList;
import java.util.List;

public class TimeBasedAdapter extends RecyclerView.Adapter<TimeBasedAdapter.TimeBasedViewHolder> {
    List<TimeBasedEvent> eventList=new ArrayList<>();

    public TimeBasedAdapter(final List<TimeBasedEvent> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public TimeBasedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        SingleTimeEventBinding binding= DataBindingUtil.inflate(inflater, R.layout.single_time_event,parent,false);
        return new TimeBasedViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeBasedViewHolder holder, int position) {
        TimeBasedEvent event=eventList.get(position);
        holder.singleTimeEventBinding.titleTV.setText(event.getTitle());
        holder.singleTimeEventBinding.timeTV.setText("selected time :"+event.getTime());
        holder.singleTimeEventBinding.periodTV.setText("event period :"+event.getPeriod());
        holder.singleTimeEventBinding.typeTV.setText("event type :"+event.getType());
        holder.singleTimeEventBinding.notificationTV.setText("notification before "+String.valueOf(event.getPeriod()+"minutes"));

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class TimeBasedViewHolder extends RecyclerView.ViewHolder {
        SingleTimeEventBinding singleTimeEventBinding;
        public TimeBasedViewHolder(@NonNull SingleTimeEventBinding itemView) {
            super(itemView.getRoot());
            singleTimeEventBinding=itemView;
        }
    }
}