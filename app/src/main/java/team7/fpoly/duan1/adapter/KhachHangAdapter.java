package team7.fpoly.duan1.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
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
import team7.fpoly.duan1.dao.KhachHangDAO;
import team7.fpoly.duan1.model.KhachHang;

public class KhachHangAdapter extends RecyclerView.Adapter<KhachHangAdapter.ViewHolder> {

    Context context;
    KhachHangDAO dao;
    List<KhachHang> list;

    public KhachHangAdapter(Context context) {
        this.context = context;
        dao = new KhachHangDAO(context);
        list = dao.getAll();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_khach_hang,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        KhachHang obj = list.get(holder.getAdapterPosition());
        String txtMaKH = "Mã khách hàng: "+String.valueOf(obj.getMaKH());
        holder.txtMaKH.setText(txtMaKH);
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

        TextView txtMaKH;
        CardView parent;
        ImageView ivDelete,ivEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Ánh xạ view
            txtMaKH = itemView.findViewById(R.id.it_rec_txtMaKH);
            ivEdit = itemView.findViewById(R.id.it_rec_ivEdit);
            ivDelete = itemView.findViewById(R.id.it_rec_ivDelete);
            parent = itemView.findViewById(R.id.parent_KH);
        }
    }

    //FUNCTION

    public void setData(List<KhachHang> newList){
        this.list = newList;
        notifyDataSetChanged();
    }

    public void refreshData(){
        this.list = dao.getAll();
        notifyDataSetChanged();
    }

    private void showDialogChiTiet(int position){
        KhachHang kh = list.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chi Tiết");
        String message = "Mã khách hàng: " + kh.getMaKH()
                + "\nTên khách hàng: " + kh.getTenKH()
                + "\nNăm sinh: " + kh.getNamSinh()
                + "\nGiới tính: " + kh.getGioiTinh()
                + "\nCCCD: " + kh.getCCCD()
                + "\nThêm/sửa lần cuối bởi: " + kh.getMaNV();
        builder.setMessage(message);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDialogSua(int position){
        KhachHang kh = list.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Sửa Khách Hàng");
        View diaView = LayoutInflater.from(context).inflate(R.layout.dialog_khach_hang,null);
        TextInputEditText edtTenKH = diaView.findViewById(R.id.dia_edtTenKH);
        TextInputEditText edtNamSinh = diaView.findViewById(R.id.dia_edtNamSinh);
        TextInputEditText edtQueQuan = diaView.findViewById(R.id.dia_edtQueQuan);
        TextInputEditText edtGioiTinh = diaView.findViewById(R.id.dia_edtGioiTinh);
        TextInputEditText edtCCCD = diaView.findViewById(R.id.dia_edtCCCD);

        edtTenKH.setText(kh.getTenKH());
        edtNamSinh.setText(kh.getNamSinh());
        edtQueQuan.setText(kh.getQueQuan());
        edtGioiTinh.setText(kh.getGioiTinh());
        edtCCCD.setText(kh.getCCCD());

        builder.setView(diaView);

        boolean checkEmpty = edtTenKH.getText().length() == 0 || edtNamSinh.getText().length() == 0 || edtQueQuan.getText().length() == 0 || edtGioiTinh.getText().length() == 0 || edtCCCD.getText().length() == 0;

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (checkEmpty){
                    Toast.makeText(context, "Không được để trống", Toast.LENGTH_SHORT).show();
                } else {
                    kh.setTenKH(edtTenKH.getText().toString());
                    kh.setNamSinh(edtNamSinh.getText().toString());
                    kh.setQueQuan(edtQueQuan.getText().toString());
                    kh.setGioiTinh(edtGioiTinh.getText().toString());
                    kh.setCCCD(edtCCCD.getText().toString());

                    String maNV = context.getSharedPreferences("TAI_KHOAN",Context.MODE_PRIVATE).getString("MaNV","");
                    kh.setMaNV(maNV);
                    boolean check = dao.suaKhachHang(kh);
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
        KhachHang kh = list.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xóa ?");
        String message = "Mã khách hàng: " + kh.getMaKH();
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<String> listCheck = dao.xoaAnToanKhachHang(kh);
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
                    Snackbar.make(context,
                                    LayoutInflater.from(context).inflate(R.layout.fragment_nhan_vien,null),
                                    constraints,
                                    Snackbar.LENGTH_LONG)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .show();
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
