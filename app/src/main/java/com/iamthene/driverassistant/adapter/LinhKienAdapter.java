package com.iamthene.driverassistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.model.LinhKien;

import java.util.List;

public class LinhKienAdapter extends RecyclerView.Adapter<LinhKienAdapter.ViewHolder> {
    private List<LinhKien> lstLinhKien;
    private Context mContext;

    public LinhKienAdapter(Context context, List<LinhKien> lstLinhKien) {
        this.mContext = context;
        this.lstLinhKien = lstLinhKien;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LinhKien lk = lstLinhKien.get(position);
        if (lk == null) {
            return;
        }
        holder.tvDateChangePart.setText(lk.getDate());
        holder.tvContentChangePart.setText(lk.getPart());
        holder.tvTime.setText(lk.getTime());
        holder.tvPrice.setText(lk.getPriceFormat());
        holder.cvHistoryPart.setOnClickListener(view -> Toast.makeText(mContext.getApplicationContext(), lk.getContent(), Toast.LENGTH_SHORT).show());
        holder.cvHistoryPart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (lstLinhKien != null) {
            return lstLinhKien.size();
        }

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDateChangePart, tvContentChangePart, tvTime, tvPrice;
        RelativeLayout rlExpandOption3;
        CardView cvHistoryPart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContentChangePart = itemView.findViewById(R.id.tvContentChangePart);
            tvDateChangePart = itemView.findViewById(R.id.tvDateChangePart);
            cvHistoryPart = itemView.findViewById(R.id.cvHistoryPart);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }

    private void onClickDetail() {

    }
}
