package team7.fpoly.duan1.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import team7.fpoly.duan1.R;
import team7.fpoly.duan1.dao.PhieuDAO;
import team7.fpoly.duan1.model.NhanVien;
import team7.fpoly.duan1.model.Phieu;

public class PhieuAdapter extends RecyclerView.Adapter<PhieuAdapter.ViewHolder> {

    Context context;
    PhieuDAO dao;
    List<Phieu> list;

    public PhieuAdapter(Context context) {
        this.context = context;
        dao = new PhieuDAO(context);
        list = dao.getAll();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_phieu,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Phieu phieu = list.get(holder.getAdapterPosition());
        String maPhieu = "Mã phiếu mượn: "+ phieu.getMaPhieu();
        String trangThai = "Trạng thái: "+ phieu.getTrangThai();
        String ngayTao = "Ngày tạo: "+ phieu.getNgayThang();
        String kiHan = "Kì hạn: "+ phieu.getKiHan();
        holder.txtMaPhieu.setText(maPhieu);
        holder.txtTrangThai.setText(trangThai);
        holder.txtNgayTao.setText(ngayTao);
        holder.txtKiHan.setText(kiHan);

        holder.btnChiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogChiTiet(holder.getAdapterPosition());
            }
        });

        switch (phieu.getTrangThai()){
            case "Đã trả":
                holder.parent.setCardBackgroundColor(Color.parseColor("#016F73"));
                break;
            case "Quá hạn":
                holder.parent.setCardBackgroundColor(Color.parseColor("#BD7F00"));
                break;
        }
        holder.btnDaTra.setVisibility((phieu.getTrangThai().equals("Chưa trả")) ? View.VISIBLE : View.GONE);
        holder.btnQuaHan.setVisibility((phieu.getTrangThai().equals("Chưa trả")) ? View.VISIBLE : View.GONE);

        holder.btnDaTra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDaTra(holder.getAdapterPosition());
            }
        });
        holder.btnQuaHan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogQuaHan(holder.getAdapterPosition());
            }
        });
        holder.btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogXoa(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtMaPhieu,txtTrangThai,txtNgayTao,txtKiHan;
        Button btnChiTiet,btnDaTra,btnQuaHan,btnXoa;
        CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMaPhieu = itemView.findViewById(R.id.it_rec_txtMaPhieu);
            txtTrangThai = itemView.findViewById(R.id.it_rec_txtTrangThai);
            txtNgayTao = itemView.findViewById(R.id.it_rec_txtNgayTao);
            txtKiHan = itemView.findViewById(R.id.it_rec_txtKiHan);
            btnChiTiet = itemView.findViewById(R.id.it_rec_btnChiTiet);
            btnDaTra = itemView.findViewById(R.id.it_rec_btnDaTra);
            btnQuaHan = itemView.findViewById(R.id.it_rec_btnQuaHan);
            btnXoa = itemView.findViewById(R.id.it_rec_btnXoa);

            parent = itemView.findViewById(R.id.parent_Phieu);
        }
    }

    //FUNCTION

    public void setData(List<Phieu> newList){
        this.list = newList;
        notifyDataSetChanged();
    }

    public void refreshData(){
        this.list = dao.getAll();
        notifyDataSetChanged();
    }

    private void showDialogChiTiet(int position){
        Phieu phieu = list.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chi Tiết");
        String message = "Mã phiếu: " + phieu.getMaPhieu()
                + "\nNgày tạo: " + phieu.getNgayThang()
                + "\nKì hạn: " + phieu.getKiHan()
                + "\nTrạng thái: " + phieu.getTrangThai()
                + "\nVốn: " + getFormatedAmount(phieu.getVon())
                + "\nLãi: " + getFormatedAmount(phieu.getLai())
                + "\nMã nhân viên: " + phieu.getMaNV()
                + "\nMã loại: " + phieu.getMaLoai()
                + "\nTên loại: " + phieu.getTenLoai()
                + "\nMã vật phẩm: " + phieu.getMaVP()
                + "\nTên vật phẩm: " + phieu.getTenVP()
                + "\nGiá vật phẩm: " + getFormatedAmount(phieu.getGiaVP())
                + "\nMô tả: " + phieu.getMoTa()
                + "\nMã khách hàng: " + phieu.getMaKH()
                + "\nTên khách hàng: " + phieu.getTenKH()
                + "\nNăm sinh: " + phieu.getNamSinh()
                + "\nGiới tính: " + phieu.getGioiTinh()
                + "\nCCCD: " + phieu.getCCCD();
        builder.setMessage(message);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String getFormatedAmount(int amount){
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }

    private void showDialogXoa(int position){
        Phieu phieu = list.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xóa ?");
        String message = "Mã phiếu: " + phieu.getMaPhieu();
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean check = dao.xoaPhieu(phieu);
                if (check){
                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    refreshData();
                } else {
                    Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("HỦY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDialogDaTra(int position){
        Phieu phieu = list.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận đã trả ?");
        String message = "Mã phiếu: " + phieu.getMaPhieu();
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String trangThai = "Đã trả";
                boolean check = dao.doiTrangThai(phieu.getMaPhieu(),trangThai);
                if (check){
                    Toast.makeText(context, "Thay đổi thành công", Toast.LENGTH_SHORT).show();
                    refreshData();
                } else {
                    Toast.makeText(context, "Thay đổi thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("HỦY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDialogQuaHan(int position){
        Phieu phieu = list.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận quá hạn ?");
        String message = "Mã phiếu: " + phieu.getMaPhieu();
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String trangThai = "Quá hạn";
                boolean check = dao.doiTrangThai(phieu.getMaPhieu(),trangThai);
                if (check){
                    Toast.makeText(context, "Thay đổi thành công", Toast.LENGTH_SHORT).show();
                    refreshData();
                } else {
                    Toast.makeText(context, "Thay đổi thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("HỦY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
