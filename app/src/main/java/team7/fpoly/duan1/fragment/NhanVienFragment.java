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
import team7.fpoly.duan1.adapter.NhanVienAdapter;
import team7.fpoly.duan1.dao.NhanVienDAO;
import team7.fpoly.duan1.model.NhanVien;

public class NhanVienFragment extends Fragment {

    Context context;
    NhanVienDAO dao;
    NhanVienAdapter adapter;

    RecyclerView recyclerView;
    FloatingActionButton fabAdd;
    FloatingActionButton fabSearch;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        this.dao = new NhanVienDAO(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nhan_vien, container, false);
        recyclerView = view.findViewById(R.id.recyclerNhanVien);
        fabAdd = view.findViewById(R.id.fabAdd);
        fabSearch = view.findViewById(R.id.fabSearch);

        adapter = new NhanVienAdapter(context); //truyen vao adapter
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
        NhanVien nv = new NhanVien();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thêm Nhân Viên");
        View diaView = LayoutInflater.from(context).inflate(R.layout.dialog_nhan_vien_them,null);
        TextInputEditText edtMaNV = diaView.findViewById(R.id.dia_edtMaNV);
        TextInputEditText edtHoTen = diaView.findViewById(R.id.dia_edtHoTen);
        TextInputEditText edtMatKhau = diaView.findViewById(R.id.dia_edtMatKhau);

        Spinner spnChucVu = diaView.findViewById(R.id.dia_spnChucVu);

        ArrayAdapter<String> spnAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, new String[]{"Admin","Nhân Viên"});
        spnChucVu.setAdapter(spnAdapter);

        builder.setView(diaView);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                boolean checkEmpty = edtHoTen.getText().length() == 0 || edtMatKhau.getText().length() == 0 || edtMaNV.getText().length() == 0;

                if (checkEmpty){
                    Toast.makeText(context, "Không được để trống", Toast.LENGTH_SHORT).show();
                } else {
                    nv.setMaNV(edtMaNV.getText().toString().trim());
                    nv.setHoTenNV(edtHoTen.getText().toString().trim());
                    nv.setMatKhau(edtMatKhau.getText().toString().trim());
                    nv.setChucVu(spnChucVu.getSelectedItem().toString());
                    boolean check = dao.themNhanVien(nv);
                    if (check){
                        Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        adapter.refreshData();
                    } else {
                        Toast.makeText(context, "Thêm thất bại", Toast.LENGTH_SHORT).show();
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

    private void showDialogTim(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Tìm Nhân Viên");
        View diaView = LayoutInflater.from(context).inflate(R.layout.dialog_tim,null);
        TextInputEditText edtTim = diaView.findViewById(R.id.dia_edtTim);
        Spinner spnMuc = diaView.findViewById(R.id.dia_spnMuc);

        ArrayAdapter<String> spnAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, new String[]{"Mã","Tên"});
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