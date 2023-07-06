package team7.fpoly.duan1.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

import team7.fpoly.duan1.R;
import team7.fpoly.duan1.dao.ThongKeDAO;

public class ThongKeFragment extends Fragment {

    Context context;
    TextView txtDoanhThu;
    EditText edtTuNgay,edtDenNgay;
    Button btnTraCuu;
    RadioGroup rdgVonLai;
    RadioButton rdoVon,rdoLai;
    Calendar calendar;
    ThongKeDAO dao;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thong_ke, container, false);
        txtDoanhThu = view.findViewById(R.id.tvDoanhThuTK);
        edtTuNgay = view.findViewById(R.id.edtTuNgay);
        edtDenNgay = view.findViewById(R.id.edtDenNgay);
        rdgVonLai = view.findViewById(R.id.rdgVonLai);
        rdoVon = view.findViewById(R.id.rdoVon);
        rdoLai = view.findViewById(R.id.rdoLai);
        btnTraCuu = view.findViewById(R.id.btnTraCuuTK);

        dao = new ThongKeDAO(getContext());

        calendar = Calendar.getInstance();

        rdoVon.setChecked(true);

        edtTuNgay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            getContext(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    String ngay = String.valueOf(dayOfMonth);
                                    String thang = String.valueOf(month+1);
                                    String nam = String.valueOf(year);
                                    if (dayOfMonth < 10){
                                        ngay = "0"+dayOfMonth;
                                    }
                                    if ((month+1) < 10){
                                        thang = "0"+(month+1);
                                    }
                                    edtTuNgay.setText(String.format("%s/%s/%s", nam,thang,ngay));
                                }
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                    );

                    datePickerDialog.show();
                    edtTuNgay.clearFocus();
                }
            }
        });

        edtDenNgay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            getContext(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    String ngay = String.valueOf(dayOfMonth);
                                    String thang = String.valueOf(month + 1);
                                    String nam = String.valueOf(year);
                                    if (dayOfMonth < 10) {
                                        ngay = "0" + dayOfMonth;
                                    }
                                    if ((month + 1) < 10) {
                                        thang = "0" + (month + 1);
                                    }
                                    edtDenNgay.setText(String.format("%s/%s/%s", nam, thang, ngay));
                                }
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                    );

                    datePickerDialog.show();
                    edtDenNgay.clearFocus();
                }
            }
        });

        btnTraCuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ngayBD = edtTuNgay.getText().toString();
                String ngayKT = edtDenNgay.getText().toString();
                int choice = (rdoVon.isChecked()) ? 1 : 2;

                if (choice == 1){
                    int doanhThu = dao.getVon(ngayBD,ngayKT);
                    String textDoanhThu = getFormatedAmount(doanhThu)+" VND";
                    txtDoanhThu.setText(textDoanhThu);
                } else {
                    int doanhThu = dao.getLai(ngayBD,ngayKT);
                    String textDoanhThu = getFormatedAmount(doanhThu)+" VND";
                    txtDoanhThu.setText(textDoanhThu);
                }
            }
        });
        return view;
    }

    private String getFormatedAmount(int amount){
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }
}