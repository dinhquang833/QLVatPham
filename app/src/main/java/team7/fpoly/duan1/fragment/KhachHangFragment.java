package team7.fpoly.duan1.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import team7.fpoly.duan1.R;
import team7.fpoly.duan1.adapter.KhachHangAdapter;
import team7.fpoly.duan1.dao.KhachHangDAO;
import team7.fpoly.duan1.model.KhachHang;

public class KhachHangFragment extends Fragment {

    Context context;
    KhachHangDAO dao;

    RecyclerView recyclerView;
    FloatingActionButton fabAdd;
    FloatingActionButton fabSearch;
    KhachHangAdapter adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        this.dao = new KhachHangDAO(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_khach_hang, container, false);
        recyclerView = view.findViewById(R.id.recyclerKhachHang);
        fabAdd = view.findViewById(R.id.fabAdd);
        fabSearch = view.findViewById(R.id.fabSearch);
        adapter = new KhachHangAdapter(context);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogThem();
            }
        });

        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogTim();
            }
        });
        return view;
    }

    private void showDialogThem(){
        KhachHang kh = new KhachHang();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thêm Khách Hàng");
        View diaView = LayoutInflater.from(context).inflate(R.layout.dialog_khach_hang,null);
        TextInputEditText edtTenKH = diaView.findViewById(R.id.dia_edtTenKH);
        TextInputEditText edtNamSinh = diaView.findViewById(R.id.dia_edtNamSinh);
        TextInputEditText edtQueQuan = diaView.findViewById(R.id.dia_edtQueQuan);
        TextInputEditText edtGioiTinh = diaView.findViewById(R.id.dia_edtGioiTinh);
        TextInputEditText edtCCCD = diaView.findViewById(R.id.dia_edtCCCD);

        builder.setView(diaView);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                boolean checkEmpty = edtTenKH.getText().length() == 0 || edtNamSinh.getText().length() == 0 || edtQueQuan.getText().length() == 0 || edtGioiTinh.getText().length() == 0 || edtCCCD.getText().length() == 0;

                boolean checkCCCD = dao.checkCCCD(edtCCCD.getText().toString());

                if (checkEmpty) {
                    Toast.makeText(context, "Không được để trống", Toast.LENGTH_SHORT).show();
                } else if (checkCCCD){
                    Toast.makeText(context, "Đã tồn tại CCCD này", Toast.LENGTH_SHORT).show();
                } else {
                    kh.setTenKH(edtTenKH.getText().toString().trim());
                    kh.setNamSinh(edtNamSinh.getText().toString().trim());
                    kh.setQueQuan(edtQueQuan.getText().toString().trim());
                    kh.setGioiTinh(edtGioiTinh.getText().toString().trim());
                    kh.setCCCD(edtCCCD.getText().toString().trim());

                    String maNV = context.getSharedPreferences("TAI_KHOAN", Context.MODE_PRIVATE).getString("MaNV", "");
                    kh.setMaNV(maNV);
                    boolean check = dao.themKhachHang(kh);
                    if (check) {
                        Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show();
                        adapter.refreshData();
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

        builder.setView(diaView);
    }

    private void showDialogTim(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Tìm Khách Hàng");
        View diaView = LayoutInflater.from(context).inflate(R.layout.dialog_tim,null);
        TextInputEditText edtTim = diaView.findViewById(R.id.dia_edtTim);
        Spinner spnMuc = diaView.findViewById(R.id.dia_spnMuc);

        ArrayAdapter<String> spnAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, new String[]{"Mã","Tên","CCCD"});
        spnMuc.setAdapter(spnAdapter);

        builder.setView(diaView);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                boolean checkEmpty = edtTim.getText().length() == 0;

                if (checkEmpty){
                    Toast.makeText(context, "Không được để trống", Toast.LENGTH_SHORT).show();
                } else {
                    String input = edtTim.getText().toString();
                    switch (spnMuc.getSelectedItem().toString()){
                        case "Mã":
                            adapter.setData(dao.searchByID(input));
                            break;
                        case "Tên":
                            adapter.setData(dao.searchByName(input));
                            break;
                        case "CCCD":
                            adapter.setData(dao.searchByCCCD(input));
                            break;
                    }

                }
            }
        });

        builder.setNeutralButton("BỎ TÌM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.refreshData();
            }
        });

        builder.setNegativeButton("HỦY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        builder.setView(diaView);
    }
}