package team7.fpoly.duan1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

import team7.fpoly.duan1.fragment.DieuKhoanFragment;
import team7.fpoly.duan1.fragment.DoiMatKhauFragment;
import team7.fpoly.duan1.fragment.KhachHangFragment;
import team7.fpoly.duan1.fragment.LoaiFragment;
import team7.fpoly.duan1.fragment.NhanVienFragment;
import team7.fpoly.duan1.fragment.PhieuFragment;
import team7.fpoly.duan1.fragment.ThongKeFragment;
import team7.fpoly.duan1.fragment.TopFragment;
import team7.fpoly.duan1.fragment.VatPhamFragment;
import team7.fpoly.duan1.screen.DangNhapActivity;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    FrameLayout frameLayoutContent;
    TextView txtHoTenNV;
    View headerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        frameLayoutContent = findViewById(R.id.frameLayoutContent);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);

        headerLayout = navigationView.getHeaderView(0);
        txtHoTenNV = headerLayout.findViewById(R.id.txtHoTenNV);


        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //hien thi icon len toolbar
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle("Quản lí phiếu");

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new PhieuFragment();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frameLayoutContent, fragment)
                .commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment fragment;
                switch (item.getItemId()){
                    case R.id.mQLNhanVien:
                        fragment = new NhanVienFragment();
                        break;
                    case R.id.mQLKhachHang:
                        fragment = new KhachHangFragment();
                        break;
                    case R.id.mQLLoaiVatPham:
                        fragment = new LoaiFragment();
                        break;
                    case R.id.mQLVatPham:
                        fragment = new VatPhamFragment();
                        break;
                    case R.id.mQLThongKe:
                        fragment = new ThongKeFragment();
                        break;
                    case R.id.mBXH:
                        fragment = new TopFragment();
                        break;
                    case R.id.mDieuKhoan:
                        fragment = new DieuKhoanFragment();
                        break;
                    case R.id.mDoiMK:
                        fragment = new DoiMatKhauFragment();
                        break;
                    case R.id.mThoat:
                        Intent intent = new Intent(MainActivity.this, DangNhapActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    case R.id.mQLPhieu:
                    default:
                        fragment = new PhieuFragment();
                        break;
                }
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.frameLayoutContent, fragment)
                        .commit();

                drawerLayout.closeDrawer(GravityCompat.START);
                setTitle(item.getTitle());
                return false;
            }
        });

        funcSetUserNameHeader();
        funcSetVisibility();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    private void funcSetUserNameHeader() {
        SharedPreferences pref = getSharedPreferences("TAI_KHOAN", MODE_PRIVATE);
        String hoTenNV = pref.getString("HoTenNV","");
        txtHoTenNV.setText(hoTenNV);
    }

    private void funcSetVisibility(){
        SharedPreferences pref = getSharedPreferences("TAI_KHOAN", MODE_PRIVATE);
        String chucVu = pref.getString("ChucVu","");
        if (!chucVu.equals("Admin")){
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.mQLNhanVien).setVisible(false);
            menu.findItem(R.id.mQLThongKe).setVisible(false);
            menu.findItem(R.id.mBXH).setVisible(false);
            menu.findItem(R.id.mQLLoaiVatPham).setVisible(false);
        }
    }
}