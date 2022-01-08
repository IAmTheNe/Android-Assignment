package com.iamthene.driverassistant.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.model.Repair;

import java.util.List;

public class RepairAdapter extends RecyclerView.Adapter<RepairAdapter.ViewHolder> {
    private List<Repair> lstRepair;
    private Context mContext;

    public RepairAdapter(Context context, List<Repair> lstRepair) {
        this.mContext = context;
        this.lstRepair = lstRepair;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Repair lk = lstRepair.get(position);
        if (lk == null) {
            return;
        }
        holder.tvDateChangePart.setText(lk.getDate());
        holder.tvContentChangePart.setText(lk.getPart());
        holder.tvTime.setText(lk.getTime());
        holder.tvPrice.setText(lk.getPriceFormat());
        holder.tvCarName.setText(lk.getCarId());
        holder.cvHistoryPart.setOnLongClickListener(view -> {
            new AlertDialog.Builder(view.getContext())
                    .setTitle("Driver Assistant")
                    .setMessage("Bạn có muốn xóa ghi chú này?")
                    .setPositiveButton("Đồng ý", (dialog, which) -> {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Repair");
                            mRef.child(user.getUid()).child(lk.getId()).removeValue();
                        }
                    })
                    .setNegativeButton("Quay lại", null)
                    .show();

            return false;
        });
    }

    @Override
    public int getItemCount() {
        if (lstRepair != null) {
            return lstRepair.size();
        }

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDateChangePart, tvContentChangePart, tvTime, tvPrice, tvCarName;
        RelativeLayout rlExpandOption3;
        CardView cvHistoryPart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContentChangePart = itemView.findViewById(R.id.tvContentChangePart);
            tvDateChangePart = itemView.findViewById(R.id.tvDateChangePart);
            cvHistoryPart = itemView.findViewById(R.id.cvHistoryPart);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvCarName = itemView.findViewById(R.id.tvCarName);
        }
    }

    private void onClickDetail() {

    }
}
