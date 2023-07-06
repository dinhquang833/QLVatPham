package team7.fpoly.duan1.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import team7.fpoly.duan1.R;
import team7.fpoly.duan1.dao.LoaiDAO;
import team7.fpoly.duan1.dao.VatPhamDAO;
import team7.fpoly.duan1.model.Loai;
import team7.fpoly.duan1.model.VatPham;
import team7.fpoly.duan1.model.VatPham;

public class VatPhamAdapter extends RecyclerView.Adapter<VatPhamAdapter.ViewHolder> {

    Context context;
    VatPhamDAO dao;
    List<VatPham> list;

    public VatPhamAdapter(Context context) {
        this.context = context;
        dao = new VatPhamDAO(context);
        list = dao.getAll();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_vat_pham,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VatPham vp = list.get(holder.getAdapterPosition());
        String txtMaVP = "Mã vật phẩm: "+String.valueOf(vp.getMaVP());
        holder.txtMaVP.setText(txtMaVP);
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

        TextView txtMaVP;
        CardView parent;
        ImageView ivEdit,ivDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMaVP = itemView.findViewById(R.id.it_rec_txtMaVP);
            ivDelete = itemView.findViewById(R.id.it_rec_ivDelete);
            ivEdit = itemView.findViewById(R.id.it_rec_ivEdit);
            parent = itemView.findViewById(R.id.parent_VP);
        }
    }

    //FUNCTION

    public void setData(List<VatPham> newList){
        this.list = newList;
        notifyDataSetChanged();
    }

    public void refreshData(){
        this.list = dao.getAll();
        notifyDataSetChanged();
    }

    private List<HashMap<String,Object>> getHMLoai(){
        List<Loai> list = new LoaiDAO(context).getAll();
        List<HashMap<String,Object>> listHM = new ArrayList<>();

        for (Loai l : list) {
            HashMap<String,Object> hm = new HashMap<>();
            hm.put("MaLoai", l.getMaLoai());
            hm.put("TenLoai", l.getTenLoai());
            listHM.add(hm);
        }

        return listHM;
    }

    private void showDialogChiTiet(int position){
        VatPham vp = list.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chi Tiết");
        String message = "Mã vật phẩm: " + vp.getMaVP()
                + "\nTên vật phẩm: " + vp.getTenVP()
                + "\nGiá vật phẩm: " + getFormatedAmount(vp.getGiaVP())
                + "\nMô tả: " + vp.getMoTa()
                + "\nMã loại: " + vp.getMaLoai()
                + "\nTên loại: " + vp.getTenLoai()
                + "\nThêm/sửa lần cuối bởi: " + vp.getMaNV();
        builder.setMessage(message);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String getFormatedAmount(int amount){
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }

    private void showDialogSua(int position){
        VatPham vp = list.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Sửa Vật Phẩm");
        View diaView = LayoutInflater.from(context).inflate(R.layout.dialog_vat_pham,null);
        TextInputEditText edtTenVP = diaView.findViewById(R.id.dia_edtTenVP);
        TextInputEditText edtGiaVP = diaView.findViewById(R.id.dia_edtGiaVP);
        TextInputEditText edtMoTa = diaView.findViewById(R.id.dia_edtMoTa);
        Spinner spnLoai = diaView.findViewById(R.id.dia_spnLoai);

        edtTenVP.setText(vp.getTenVP());
        edtGiaVP.setText(String.valueOf(vp.getGiaVP()));

        SimpleAdapter spnAdapter = new SimpleAdapter(
                context,
                getHMLoai(),
                android.R.layout.simple_list_item_1,
                new String[]{"TenLoai"},
                new int[]{android.R.id.text1}
        );
        spnLoai.setAdapter(spnAdapter);

        builder.setView(diaView);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                boolean checkEmpty = edtTenVP.getText().length() == 0 || edtGiaVP.getText().length() == 0;

                if (checkEmpty){
                    Toast.makeText(context, "Không được để trống", Toast.LENGTH_SHORT).show();
                } else {
                    vp.setTenVP(edtTenVP.getText().toString().trim());

                    vp.setGiaVP(Integer.parseInt(edtGiaVP.getText().toString().trim()));

                    vp.setMoTa(edtMoTa.getText().toString().trim());

                    HashMap<String,Object> hm = (HashMap<String, Object>) spnLoai.getSelectedItem();
                    int maLoai = (int) hm.get("MaLoai");
                    vp.setMaLoai(maLoai);

                    String maNV = context.getSharedPreferences("TAI_KHOAN",Context.MODE_PRIVATE).getString("MaNV","");
                    vp.setMaNV(maNV);

                    boolean check = dao.suaVatPham(vp);
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
        VatPham vp = list.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xóa ?");
        String message = "Mã vật phẩm: " + vp.getMaLoai();
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<String> listCheck = dao.xoaAnToanVatPham(vp);
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
