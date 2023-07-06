package team7.fpoly.duan1.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import team7.fpoly.duan1.R;
import team7.fpoly.duan1.adapter.VatPhamAdapter;
import team7.fpoly.duan1.dao.LoaiDAO;
import team7.fpoly.duan1.dao.VatPhamDAO;
import team7.fpoly.duan1.helper.ThousandSeparatorTextWatcher;
import team7.fpoly.duan1.model.Loai;
import team7.fpoly.duan1.model.VatPham;

public class VatPhamFragment extends Fragment {

    Context context;
    VatPhamDAO dao;

    RecyclerView recyclerView;
    FloatingActionButton fabAdd;
    FloatingActionButton fabSearch;
    VatPhamAdapter adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        this.dao = new VatPhamDAO(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vat_pham, container, false);
        recyclerView = view.findViewById(R.id.recyclerVatPham);
        fabAdd = view.findViewById(R.id.fabAdd);
        fabSearch = view.findViewById(R.id.fabSearch);
        adapter = new VatPhamAdapter(context);

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
        VatPham vp = new VatPham();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thêm Vật Phẩm");
        View diaView = LayoutInflater.from(context).inflate(R.layout.dialog_vat_pham,null);
        TextInputEditText edtTenVP = diaView.findViewById(R.id.dia_edtTenVP);
        TextInputEditText edtGiaVP = diaView.findViewById(R.id.dia_edtGiaVP);
        TextInputEditText edtMoTa = diaView.findViewById(R.id.dia_edtMoTa);
        Spinner spnLoai = diaView.findViewById(R.id.dia_spnLoai);

//        edtGiaVP.addTextChangedListener(new ThousandSeparatorTextWatcher(edtGiaVP));

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

//                    vp.setGiaVP(Integer.parseInt(ThousandSeparatorTextWatcher.getOriginalString(edtGiaVP.getText().toString())));

                    vp.setGiaVP(Integer.parseInt(edtGiaVP.getText().toString()));

                    HashMap<String,Object> hm = (HashMap<String, Object>) spnLoai.getSelectedItem();
                    int maLoai = (int) hm.get("MaLoai");

                    vp.setMoTa(edtMoTa.getText().toString().trim());

                    vp.setMaLoai(maLoai);

                    String maNV = context.getSharedPreferences("TAI_KHOAN",Context.MODE_PRIVATE).getString("MaNV","");
                    vp.setMaNV(maNV);

                    boolean check = dao.themVatPham(vp);
                    if (check){
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
    }

    private void showDialogTim(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Tìm Nhân Viên");
        View diaView = LayoutInflater.from(context).inflate(R.layout.dialog_tim,null);
        TextInputEditText edtTim = diaView.findViewById(R.id.dia_edtTim);
        Spinner spnMuc = diaView.findViewById(R.id.dia_spnMuc);

        ArrayAdapter<String> spnAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, new String[]{"Mã","Tên","Mô tả","Mã loại","Mã nhân viên"});
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
                        case "Mô tả":
                            adapter.setData(dao.searchByDes(input));
                            break;
                        case "Mã loại":
                            adapter.setData(dao.searchByType(input));
                            break;
                        case "Mã nhân viên":
                            adapter.setData(dao.searchByStaff(input));
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
}