package team7.fpoly.duan1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import team7.fpoly.duan1.database.CamDoDatabase;
import team7.fpoly.duan1.model.NhanVien;

public class NhanVienDAO {

    SQLiteDatabase db;
    SharedPreferences sharedPreferences;

    public NhanVienDAO(Context context) {
        db = new CamDoDatabase(context).getWritableDatabase();
        sharedPreferences = context.getSharedPreferences("TAI_KHOAN",Context.MODE_PRIVATE);
    }

    public List<NhanVien> getAll(){
        String sql = "SELECT * FROM NhanVien";
        return getData(sql);
    }

    public NhanVien getByID(String id){
        String sql = "SELECT * FROM NhanVien WHERE MaNV=?";
        List<NhanVien> list = getData(sql,id);
        return list.get(0);
    }

    public boolean themNhanVien(NhanVien obj){
        ContentValues contentValues = new ContentValues();
        contentValues.put("MaNV",obj.getMaNV());
        contentValues.put("MatKhau",obj.getMatKhau());
        contentValues.put("HoTenNV",obj.getHoTenNV());
        contentValues.put("ChucVu",obj.getChucVu());

        return db.insert("NhanVien",null,contentValues) != -1;
    }

    public boolean suaNhanVien(NhanVien obj){
        ContentValues contentValues = new ContentValues();
        contentValues.put("MatKhau",obj.getMatKhau());
        contentValues.put("HoTenNV",obj.getHoTenNV());
        contentValues.put("ChucVu",obj.getChucVu());

        return db.update("NhanVien",contentValues,"MaNV=?",new String[]{obj.getMaNV()}) != -1;
    }

    public boolean xoaNhanVien(NhanVien obj){
        return db.delete("NhanVien","MaNV=?",new String[]{obj.getMaNV()}) != -1;
    }

    public List<String> xoaAnToanNhanVien(NhanVien obj){
        List<String> list = new ArrayList<>();
        list.add(0,"Không có liên kết");

        Cursor cursor1 = db.rawQuery("SELECT * FROM Phieu WHERE MaNV=?",new String[]{String.valueOf(obj.getMaNV())});
        if (cursor1.getCount()!=0){
            cursor1.close();
            list.add("Có phiếu thế chấp được tạo/sửa bởi nhân viên này");
        }

        Cursor cursor2 = db.rawQuery("SELECT * FROM KhachHang WHERE MaNV=?",new String[]{String.valueOf(obj.getMaNV())});
        if (cursor2.getCount()!=0){
            cursor2.close();
            list.add("Có khách hàng được thêm/sửa bởi nhân viên này");
        }

        Cursor cursor3 = db.rawQuery("SELECT * FROM VatPham WHERE MaNV=?",new String[]{String.valueOf(obj.getMaNV())});
        if (cursor3.getCount()!=0){
            cursor3.close();
            list.add("Có vật phẩm được thêm/sửa bởi nhân viên này");
        }

        if (list.size() == 1){
            int check = db.delete("NhanVien", "MaNV=?", new String[]{String.valueOf(obj.getMaNV())});
            if (check != -1){
                list.set(0,"Xóa thành công");
            } else {
                list.set(0,"Xóa thất bại");
            }
        }

        return list;
    }

    public boolean checkDangNhap(String maNV,String matKhau,boolean remember){
        Cursor cursor = db.rawQuery("SELECT * FROM NhanVien WHERE MaNV=? AND MatKhau=?", new String[]{maNV,matKhau});
        if (cursor.getCount() != 0){
            cursor.moveToFirst();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("MaNV",cursor.getString(cursor.getColumnIndexOrThrow("MaNV")));
            editor.putString("MatKhau",cursor.getString(cursor.getColumnIndexOrThrow("MatKhau")));
            editor.putString("HoTenNV",cursor.getString(cursor.getColumnIndexOrThrow("HoTenNV")));
            editor.putString("ChucVu",cursor.getString(cursor.getColumnIndexOrThrow("ChucVu")));
            editor.putBoolean("Remember",remember);
            editor.apply();
            cursor.close();
            return true;
        } else {
            return false;
        }
    }

    private List<NhanVien> getData(String sql, String...selectionArgs){
        List<NhanVien> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        while (cursor.moveToNext()){
            NhanVien obj = new NhanVien();

            obj.setMaNV(cursor.getString(cursor.getColumnIndexOrThrow("MaNV")));
            obj.setMatKhau(cursor.getString(cursor.getColumnIndexOrThrow("MatKhau")));
            obj.setHoTenNV(cursor.getString(cursor.getColumnIndexOrThrow("HoTenNV")));
            obj.setChucVu(cursor.getString(cursor.getColumnIndexOrThrow("ChucVu")));

            list.add(obj);
        }
        cursor.close();
        return list;
    }

    public List<NhanVien> searchByID(String id){
        String sql = "SELECT * FROM NhanVien WHERE MaNV LIKE '%"+ id +"%'";
        return getData(sql);
    }

    public List<NhanVien> searchByName(String name) {
        String sql = "SELECT * FROM NhanVien WHERE HoTenNV LIKE '%" + name + "%'";
        return getData(sql);
    }
}
