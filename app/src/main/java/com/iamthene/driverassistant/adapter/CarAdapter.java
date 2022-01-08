package com.iamthene.driverassistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.model.Vehicle;
import com.iamthene.driverassistant.model.VehicleDetail;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarVH> {
    private Context mContext;
    private List<VehicleDetail> lstVehicle;

    public CarAdapter(Context context, List<VehicleDetail> lstVehicle) {
        this.mContext = context;
        this.lstVehicle = lstVehicle;
    }

    @NonNull
    @Override
    public CarVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car, parent, false);
        return new CarVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CarVH holder, int position) {
        VehicleDetail vh = lstVehicle.get(position);
        if (vh==null) {
            return;
        }

        holder.tvVehicleName.setText(vh.getName());
        holder.tvBrand.setText(vh.getBrand());
        holder.tvPlate.setText(vh.getNumber());
        holder.tvType.setText(vh.getType().toString());
    }

    @Override
    public int getItemCount() {
        if (lstVehicle != null) {
            return lstVehicle.size();
        }
        return 0;
    }

    public class CarVH extends RecyclerView.ViewHolder {
        TextView tvVehicleName, tvPlate, tvBrand, tvType;

        public CarVH(@NonNull View itemView) {
            super(itemView);
            tvVehicleName = itemView.findViewById(R.id.tvVehicleName);
            tvPlate = itemView.findViewById(R.id.tvPlate);
            tvBrand = itemView.findViewById(R.id.tvBrand);
            tvType = itemView.findViewById(R.id.tvType);
        }
    }
}
