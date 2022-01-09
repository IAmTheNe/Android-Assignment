package com.iamthene.driverassistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.model.Alarm;

import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmVH> {
    private List<Alarm> lstAlarm;
    private Context mContext;

    public AlarmAdapter(Context context, List<Alarm> lstAlarm) {
        this.lstAlarm = lstAlarm;
        this.mContext = context;
    }

    @NonNull
    @Override
    public AlarmVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        return new AlarmVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmVH holder, int position) {
        Alarm alarm = lstAlarm.get(position);
        if (alarm == null) {
            return;
        }
        holder.tvTitle.setText(alarm.getTitle());
        holder.tvAlarmType.setText(alarm.getType());
        holder.tvDateCreated.setText(alarm.getCreatedAt());
        holder.tvAlarmDate.setText(alarm.getAlarmDate());
    }

    @Override
    public int getItemCount() {
        if (lstAlarm != null) {
            return lstAlarm.size();
        }
        return 0;
    }

    public class AlarmVH extends RecyclerView.ViewHolder {
        TextView tvAlarmType, tvDateCreated, tvTitle, tvAlarmDate;

        public AlarmVH(@NonNull View itemView) {
            super(itemView);
            tvAlarmDate = itemView.findViewById(R.id.tvAlarmDate);
            tvAlarmType = itemView.findViewById(R.id.tvAlarmType);
            tvDateCreated = itemView.findViewById(R.id.tvDateCreated);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}
