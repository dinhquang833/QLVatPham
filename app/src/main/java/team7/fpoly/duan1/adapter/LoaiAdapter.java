package team7.fpoly.duan1.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import team7.fpoly.duan1.R;
import team7.fpoly.duan1.dao.LoaiDAO;
import team7.fpoly.duan1.model.Loai;
import team7.fpoly.duan1.model.Loai;

public class LoaiAdapter extends RecyclerView.Adapter<LoaiAdapter.ViewHolder> {

    Context context;
    LoaiDAO dao;
    List<Loai> list;

    public LoaiAdapter(Context context) {
        this.context = context;
        dao = new LoaiDAO(context);
        list = dao.getAll();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_loai,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Loai obj = list.get(holder.getAdapterPosition());
        String txtTenLoai = String.valueOf(obj.getTenLoai());
        holder.txtTenLoai.setText(txtTenLoai);
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogSua(holder.getAdapterPosition());
            }
        });
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogXoa(holder.getAdapterPosition());
            }
        });
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogChiTiet(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtTenLoai;
        CardView parent;
        ImageView ivDelete,ivEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenLoai = itemView.findViewById(R.id.it_rec_txtTenLoai);
            ivDelete = itemView.findViewById(R.id.it_rec_ivDelete);
            ivEdit = itemView.findViewById(R.id.it_rec_ivEdit);
            parent = itemView.findViewById(R.id.parent_Loai);
        }
    }

    //FUNCTION

    public void setData(List<Loai> newList){
        this.list = newList;
        notifyDataSetChanged();
    }

    public void refreshData(){
        this.list = dao.getAll();
        notifyDataSetChanged();
    }

    private void showDialogChiTiet(int position){
        Loai l = list.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chi Tiết");
        String message = "Mã loại: " + l.getMaLoai()
                + "\nTên loại: " + l.getTenLoai();
        builder.setMessage(message);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDialogSua(int position){
        Loai l = list.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Sửa Loại");
        View diaView = LayoutInflater.from(context).inflate(R.layout.dialog_loai,null);
        TextInputEditText edtTenLoai = diaView.findViewById(R.id.dia_edtTenLoai);

        edtTenLoai.setText(l.getTenLoai());

        builder.setView(diaView);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                boolean checkEmpty = edtTenLoai.getText().length() == 0;

                if (checkEmpty){
                    Toast.makeText(context, "Không được để trống", Toast.LENGTH_SHORT).show();
                } else {
                    l.setTenLoai(edtTenLoai.getText().toString().trim());

                    boolean check = dao.suaLoai(l);
                    if (check){
                        Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show();
                        refreshData();
                    } else {
                        Toast.makeText(context, "Sửa thất bại", Toast.LENGTH_SHORT).show();
                    }
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

    private void showDialogXoa(int position){
        Loai l = list.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xóa ?");
        String message = "Mã loại: " + l.getMaLoai();
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<String> listCheck = dao.xoaAnToanLoai(l);
                if (listCheck.size() == 1){
                    Toast.makeText(context, listCheck.get(0), Toast.LENGTH_SHORT).show();
                    if (listCheck.get(0).equals("Xóa thành công")){
                        refreshData();
                    }
                } else {
                    String constraints = "";
                    for (int i = 1; i < listCheck.size(); i++) {
                        constraints = constraints.concat(listCheck.get(i)).concat("\n");
                    }
                    Toast.makeText(context, constraints, Toast.LENGTH_SHORT).show();
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
