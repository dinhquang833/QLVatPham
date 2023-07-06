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
import team7.fpoly.duan1.dao.NhanVienDAO;
import team7.fpoly.duan1.model.NhanVien;

public class NhanVienAdapter extends RecyclerView.Adapter<NhanVienAdapter.ViewHolder> {

    Context context;
    NhanVienDAO dao;
    List<NhanVien> list;

    public NhanVienAdapter(Context context) {
        this.context = context;
        dao = new NhanVienDAO(context);
        list = dao.getAll();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_nhan_vien,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NhanVien obj = list.get(holder.getAdapterPosition());
        String txtMaNV = "Mã nhân viên: "+String.valueOf(obj.getMaNV());
        holder.txtMaNV.setText(txtMaNV);
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

        TextView txtMaNV;
        ImageView ivEdit,ivDelete;
        CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtMaNV = itemView.findViewById(R.id.it_rec_txtMaNV);
            ivDelete = itemView.findViewById(R.id.it_rec_ivDelete);
            ivEdit = itemView.findViewById(R.id.it_rec_ivEdit);
            parent = itemView.findViewById(R.id.parent_NV);
        }
    }

    //FUNCTION

    public void setData(List<NhanVien> newList){
        this.list = newList;
        notifyDataSetChanged();
    }

    public void refreshData(){
        this.list = dao.getAll();
        notifyDataSetChanged();
    }

    private void showDialogChiTiet(int position){
        NhanVien nv = list.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chi Tiết");
        String message = "Mã nhân viên: " + nv.getMaNV()
                + "\nTên nhân viên: " + nv.getHoTenNV()
                + "\nMật khẩu: " + nv.getMatKhau()
                + "\nChức vụ: " + nv.getChucVu();
        builder.setMessage(message);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDialogSua(int position){
        NhanVien nv = list.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Sửa Nhân Viên");
        View diaView = LayoutInflater.from(context).inflate(R.layout.dialog_nhan_vien_sua,null);
        TextInputEditText edtHoTen = diaView.findViewById(R.id.dia_edtHoTen);
        TextInputEditText edtMatKhau = diaView.findViewById(R.id.dia_edtMatKhau);
        Spinner spnChucVu = diaView.findViewById(R.id.dia_spnChucVu);

        edtHoTen.setText(nv.getHoTenNV());
        edtMatKhau.setText(nv.getMatKhau());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, new String[]{"Admin","Nhân Viên"});
        spnChucVu.setAdapter(adapter);

        builder.setView(diaView);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                boolean checkEmpty = edtHoTen.getText().length() == 0 || edtMatKhau.getText().length() == 0;

                if (checkEmpty){
                    Toast.makeText(context, "Không được để trống", Toast.LENGTH_SHORT).show();
                } else {
                    nv.setHoTenNV(edtHoTen.getText().toString().trim());
                    nv.setMatKhau(edtMatKhau.getText().toString().trim());
                    nv.setChucVu(spnChucVu.getSelectedItem().toString().trim());
                    boolean check = dao.suaNhanVien(nv);
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
        NhanVien nv = list.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xóa ?");
        String message = "Mã nhân viên: " + nv.getMaNV() + "\nTên nhân viên: " + nv.getHoTenNV() + "\nMật khẩu: " + nv.getMatKhau();
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<String> listCheck = dao.xoaAnToanNhanVien(nv);
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
