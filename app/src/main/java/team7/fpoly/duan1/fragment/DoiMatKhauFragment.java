package team7.fpoly.duan1.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import team7.fpoly.duan1.MainActivity;
import team7.fpoly.duan1.R;
import team7.fpoly.duan1.dao.NhanVienDAO;
import team7.fpoly.duan1.model.NhanVien;
import team7.fpoly.duan1.screen.DangNhapActivity;

public class DoiMatKhauFragment extends Fragment {

    EditText edPassOld,edPass,edRePass;
    Button btnSave,btnCancel;
    NhanVienDAO dao;
    Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doi_mat_khau, container, false);

        edPassOld = view.findViewById(R.id.edPassOld);
        edPass = view.findViewById(R.id.edPass);
        edRePass = view.findViewById(R.id.edRePass);

        btnSave = view.findViewById(R.id.btnSave);
        btnCancel = view.findViewById(R.id.btnCancel);
        dao = new NhanVienDAO(context);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcClearInput();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("TAI_KHOAN", Context.MODE_PRIVATE);
                String maNV = sharedPreferences.getString("MaNV","");
                if (validate()){
                    NhanVien nv = dao.getByID(maNV);
                    nv.setMatKhau(edPass.getText().toString());
                    if (dao.suaNhanVien(nv)){
                        Toast.makeText(getActivity(), "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        funcClearInput();
                        Intent intent = new Intent(getContext(), DangNhapActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "Thay đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return view;
    }

    public void funcClearInput(){
        edPassOld.setText("");
        edPass.setText("");
        edRePass.setText("");
    }

    public boolean validate(){
        int check = 1;
        boolean checkEmpty = edPassOld.getText().length() == 0 || edPass.getText().length() == 0 || edRePass.getText().length() == 0;

        if (checkEmpty){
            Toast.makeText(getActivity(), "Không được để trống", Toast.LENGTH_SHORT).show();
            check = -1;
        }else{
            SharedPreferences pref = context.getSharedPreferences("TAI_KHOAN", Context.MODE_PRIVATE);
            String sPassOld = pref.getString("MatKhau","");
            String pass = edPass.getText().toString();
            String rePass = edRePass.getText().toString();
            if (!sPassOld.equals(edPassOld.getText().toString())){
                Snackbar.make(getActivity().findViewById(android.R.id.content),
                        "Mật khẩu cũ sai",
                        Snackbar.LENGTH_SHORT)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();
                check = -1;
            }
            if (!pass.equals(rePass)){
                Snackbar.make(getActivity().findViewById(android.R.id.content),
                                "Mật khẩu mới không khớp",
                                Snackbar.LENGTH_SHORT)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();
                check = -1;
            }
        }
        return check != -1;
    }
}