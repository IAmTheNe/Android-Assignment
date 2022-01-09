package com.iamthene.driverassistant.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.activity.DetailRefuelActivity;
import com.iamthene.driverassistant.model.Refuel;

import java.util.List;

public class RefuelAdapter extends RecyclerView.Adapter<RefuelAdapter.ViewHolder> {
    private List<Refuel> lstRefuel;
    private Context mContext;

    public RefuelAdapter(List<Refuel> lstRefuel, Context mContext) {
        this.lstRefuel = lstRefuel;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_refuel, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Refuel p = lstRefuel.get(position);

        holder.tvPlace.setText(p.getPlace());
        holder.tvFuel.setText(p.getFuel());
        holder.tvFee.setText(p.getFee());
        holder.tvDate.setText(p.getTime());
        holder.tvVehicleName.setText(p.getCarNo());
        holder.item_refuel.setOnClickListener(v -> onClickDetail(p));
    }

    private void onClickDetail(Refuel p) {
        Intent i = new Intent(mContext, DetailRefuelActivity.class);
        Bundle d = new Bundle();
        d.putSerializable("refuel", p);
        i.putExtras(d);
        mContext.startActivity(i);
    }

    @Override
    public int getItemCount() {
        if (lstRefuel != null) {
            return lstRefuel.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item_refuel;
        TextView tvPlace, tvFee, tvFuel, tvDate, tvVehicleName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlace = itemView.findViewById(R.id.tvPlace);
            tvFuel = itemView.findViewById(R.id.tvFuel);
            tvFee = itemView.findViewById(R.id.tvFee);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvVehicleName = itemView.findViewById(R.id.tvVehicleName);
            item_refuel = itemView.findViewById(R.id.item_refuel);
        }
    }

    public void release() {
        mContext = null;
    }

}
