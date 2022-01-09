package com.iamthene.driverassistant.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.activity.DetailOilActivity;
import com.iamthene.driverassistant.model.Oil;
import com.iamthene.driverassistant.model.Refuel;

import java.util.List;

public class OilAdapter extends RecyclerView.Adapter<OilAdapter.ViewHolder> {

    private List<Oil> lstOil;
    private Context mContext;

    public OilAdapter(List<Oil> lstOil, Context mContext) {
        this.lstOil = lstOil;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_oil, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Oil o = lstOil.get(position);
        if (o == null) {
            return;
        }

        holder.tvPlaceOil.setText(o.getPlaceOil());
        holder.tvTimeOil.setText(o.getTimeOil());
        holder.tvFeeOil.setText(o.getFeeOil());
        holder.tvVehicleName.setText(o.getCarName());
        holder.item_oil.setOnClickListener(v -> onClickDetail(o));
    }

    private void onClickDetail(Oil o) {
        Intent i = new Intent(mContext, DetailOilActivity.class);
        Bundle d = new Bundle();
        d.putSerializable("oil", o);
        i.putExtras(d);
        mContext.startActivity(i);
    }

    @Override
    public int getItemCount() {
        if (lstOil != null) {
            return lstOil.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout item_oil;
        TextView tvPlaceOil, tvFeeOil, tvTimeOil, tvVehicleName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlaceOil = itemView.findViewById(R.id.tvPlaceOil);
            tvFeeOil = itemView.findViewById(R.id.tvFeeOil);
            tvTimeOil = itemView.findViewById(R.id.tvTimeOil);
            tvVehicleName = itemView.findViewById(R.id.tvVehicleName);
            item_oil = itemView.findViewById(R.id.item_oil);
        }
    }

    public void release() {
        mContext = null;
    }
}