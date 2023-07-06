package team7.fpoly.duan1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import team7.fpoly.duan1.R;
import team7.fpoly.duan1.dao.ThongKeDAO;
import team7.fpoly.duan1.model.Top;

public class TopAdapter extends RecyclerView.Adapter<TopAdapter.ViewHolder> {
    Context context;
    List<Top> list;
    ThongKeDAO dao;

    public TopAdapter(Context context) {
        this.context = context;
        dao = new ThongKeDAO(context);
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_top,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Top top = list.get(holder.getAdapterPosition());
        String maNV = "Mã nhân viên: " + top.getMaNV();
        String soPhieu = "Số phiếu: " + top.getSoPhieu();
        String von = "Vốn: " + getFormatedAmount(top.getVon());
        String lai = "Lãi: " + getFormatedAmount(top.getLai());
        holder.txtMaNV.setText(maNV);
        holder.txtSoPhieu.setText(soPhieu);
        holder.txtVon.setText(von);
        holder.txtLai.setText(lai);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtMaNV,txtSoPhieu,txtVon,txtLai;

        CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMaNV = (TextView) itemView.findViewById(R.id.item_txtMaNV);
            txtSoPhieu = (TextView) itemView.findViewById(R.id.item_txtSoPhieu);
            txtVon = (TextView) itemView.findViewById(R.id.item_txtVon);
            txtLai = (TextView) itemView.findViewById(R.id.item_txtLai);
            parent = (CardView) itemView.findViewById(R.id.parentTop);
        }
    }

    public void setData(ArrayList<Top> newList){
        if (list != null) list.clear();
        list = newList;
        notifyDataSetChanged();
    }

    public void changeDataSet(String sapXep){
        this.list = dao.getTop(sapXep);
        notifyDataSetChanged();
    }

    private String getFormatedAmount(int amount){
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }
}
