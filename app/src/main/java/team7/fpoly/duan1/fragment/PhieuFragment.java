package team7.fpoly.duan1.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import team7.fpoly.duan1.R;
import team7.fpoly.duan1.adapter.PhieuAdapter;
import team7.fpoly.duan1.dao.KhachHangDAO;
import team7.fpoly.duan1.dao.PhieuDAO;
import team7.fpoly.duan1.dao.VatPhamDAO;
import team7.fpoly.duan1.model.KhachHang;
import team7.fpoly.duan1.model.Phieu;
import team7.fpoly.duan1.model.VatPham;

public class PhieuFragment extends Fragment {

    Context context;
    PhieuDAO dao;

    RecyclerView recyclerView;
    FloatingActionButton fabAdd;
    FloatingActionButton fabSearch;
    PhieuAdapter adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        this.dao = new PhieuDAO(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phieu, container, false);
        recyclerView = view.findViewById(R.id.recyclerPhieu);
        fabAdd = view.findViewById(R.id.fabAdd);
        fabSearch = view.findViewById(R.id.fabSearch);

        recyclerView = view.findViewById(R.id.recyclerPhieu);

        adapter = new PhieuAdapter(context);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
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

    //TODO: Chưa xong bạn ei

    private void showDialogThem(){
        Phieu p = new Phieu();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thêm Phiếu");
        View diaView = LayoutInflater.from(context).inflate(R.layout.dialog_phieu,null);

        Spinner spnKH = diaView.findViewById(R.id.dia_spnKH);
        Spinner spnVP = diaView.findViewById(R.id.dia_spnVP);
        Spinner spnKiHan = diaView.findViewById(R.id.dia_spnKiHan);
        TextView txtVon = diaView.findViewById(R.id.dia_txtVon);
        TextView txtLai = diaView.findViewById(R.id.dia_txtLai);

        //Adapter

        ArrayAdapter<String> adapterKH = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                getListMaKH()
        );

        spnKH.setAdapter(adapterKH);

        SimpleAdapter adapterVP = new SimpleAdapter(
                context,
                getHMVP(),
                android.R.layout.simple_list_item_1,
                new String[]{"MaVP"},
                new int[]{android.R.id.text1}
        );
        spnVP.setAdapter(adapterVP);

        ArrayAdapter<String> adapterKiHan = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"1 Tháng","2 Tháng","3 Tháng"});
        spnKiHan.setAdapter(adapterKiHan);

        //Spinner onClick

        spnVP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtVon.setText("0");
                txtLai.setText("0");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       spnKiHan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               HashMap<String,Object> hm = (HashMap<String, Object>) spnVP.getSelectedItem();
               if (hm != null){
                   VatPham vatPham = (VatPham) hm.get("VP");

                   int giaVP = vatPham.getGiaVP();
                   float von = giaVP * 0.7F;
                   float lai = 0;

                   switch (parent.getItemAtPosition(position).toString()){
                       case "1 Tháng":
                           lai = giaVP * 0.06F;
                           break;
                       case "2 Tháng":
                           lai = giaVP * 0.12F;
                           break;
                       default:
                           lai = giaVP * 0.18F;
                           break;
                   }

                   DecimalFormat df = new DecimalFormat("0");
                   String vonText = getFormatedAmount(Integer.parseInt(df.format(von)));
                   txtVon.setText(vonText);

                   String laiText = getFormatedAmount(Integer.parseInt(df.format(lai)));
                   txtLai.setText(laiText);
               }
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

        builder.setView(diaView);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Calendar calendar = Calendar.getInstance();

                String ngay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                String thang = String.valueOf(calendar.get(Calendar.MONTH)+1);
                String nam = String.valueOf(calendar.get(Calendar.YEAR));
                if (calendar.get(Calendar.DAY_OF_MONTH) < 10){
                    ngay = "0" + ngay;
                }
                if (calendar.get(Calendar.MONTH)+1 < 10){
                    thang = "0" + thang;
                }

                String date = nam + "/" + thang + "/" + ngay;
                p.setNgayThang(date);

                String maNV = context.getSharedPreferences("TAI_KHOAN",Context.MODE_PRIVATE).getString("MaNV","");
                p.setMaNV(maNV);

                p.setTrangThai("Chưa trả");

                String kiHan = spnKiHan.getSelectedItem().toString();
                p.setKiHan(kiHan);

                int maKH = Integer.parseInt(spnKH.getSelectedItem().toString());
                p.setMaKH(maKH);

                HashMap<String,Object> hm = (HashMap<String, Object>) spnVP.getSelectedItem();
                int maVP = (int) hm.get("MaVP");
                p.setMaVP(maVP);

                VatPham vatPham = (VatPham) hm.get("VP");
                p.setMaLoai(vatPham.getMaLoai());

                p.setVon(Integer.parseInt(txtVon.getText().toString().replace(",","")));

                p.setLai(Integer.parseInt(txtLai.getText().toString().replace(",","")));

                boolean check = dao.themPhieu(p);
                if (check){
                    Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    adapter.refreshData();
                } else {
                    Toast.makeText(context, "Thêm thất bại", Toast.LENGTH_SHORT).show();
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

    private List<HashMap<String,Object>> getHMVP(){
        List<VatPham> list = new VatPhamDAO(context).getUnused();
        List<HashMap<String,Object>> listHM = new ArrayList<>();

        for (VatPham vp : list) {
            HashMap<String,Object> hm = new HashMap<>();
            hm.put("MaVP", vp.getMaVP());
            hm.put("VP", vp);
            listHM.add(hm);
        }

        return listHM;
    }

    private void showDialogTim(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Tìm Nhân Viên");
        View diaView = LayoutInflater.from(context).inflate(R.layout.dialog_tim,null);
        TextInputEditText edtTim = diaView.findViewById(R.id.dia_edtTim);
        Spinner spnMuc = diaView.findViewById(R.id.dia_spnMuc);

        ArrayAdapter<String> spnAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, new String[]{"Mã","CCCD"});
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

    private List<String> getListMaKH(){
        List<KhachHang> listKH = new KhachHangDAO(context).getAll();
        List<String> listMaKH = new ArrayList<>();

        for (KhachHang kh : listKH) {
            listMaKH.add(String.valueOf(kh.getMaKH()));
        }

        return listMaKH;
    }

    private String getFormatedAmount(int amount){
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }
}