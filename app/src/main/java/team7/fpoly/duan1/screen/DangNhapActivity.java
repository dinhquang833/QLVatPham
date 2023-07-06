package team7.fpoly.duan1.screen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import team7.fpoly.duan1.MainActivity;
import team7.fpoly.duan1.R;
import team7.fpoly.duan1.dao.NhanVienDAO;
import team7.fpoly.duan1.model.NhanVien;

public class DangNhapActivity extends AppCompatActivity {

    private EditText edUserName,edPassword;
    private Button btnLogin,btnCancel;
    private CheckBox chkRemember;
    private NhanVienDAO dao;

    String strUser,strPass;
    Boolean bRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        setTitle("ĐĂNG NHẬP");

        edUserName = (EditText) findViewById(R.id.edtMaNV);
        edPassword = (EditText) findViewById(R.id.edtMatKhau);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        chkRemember = (CheckBox) findViewById(R.id.chkRemember);

        restoreUser();

        dao = new NhanVienDAO(this);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edUserName.setText("");
                edPassword.setText("");
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });
    }

    private void checkLogin() {
        strUser = edUserName.getText().toString().trim();
        strPass = edPassword.getText().toString().trim();
        bRemember = chkRemember.isChecked();
        if (strUser.isEmpty() || strPass.isEmpty()){
            Toast.makeText(getApplicationContext(), "Tên đăng nhập và mật khẩu không được trống", Toast.LENGTH_SHORT).show();
        } else {
            if (dao.checkDangNhap(strUser,strPass,bRemember)){
                Toast.makeText(this, "Login thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DangNhapActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void restoreUser(){
        SharedPreferences pref = getSharedPreferences("TAI_KHOAN", MODE_PRIVATE);
        boolean isChecked = pref.getBoolean("Remember",false);
        if (isChecked){
            edUserName.setText(pref.getString("MaNV",""));
            edPassword.setText(pref.getString("MatKhau",""));
            chkRemember.setChecked(pref.getBoolean("Remember",false));
        } else {
            edUserName.setText("");
            edPassword.setText("");
            chkRemember.setChecked(false);
        }
    }
}