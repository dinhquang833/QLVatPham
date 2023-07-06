package team7.fpoly.duan1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import team7.fpoly.duan1.database.CamDoDatabase;
import team7.fpoly.duan1.model.KhachHang;
import team7.fpoly.duan1.model.NhanVien;

public class KhachHangDAO {

    SQLiteDatabase db;

    public KhachHangDAO(Context context) {
        db = new CamDoDatabase(context).getWritableDatabase();
    }

    public List<KhachHang> getAll(){
        String sql = "SELECT * FROM KhachHang";
        return getData(sql);
    }

    public KhachHang getByID(int id){
        String sql = "SELECT * FROM KhachHang WHERE MaKH=?";
        List<KhachHang> list = getData(sql, String.valueOf(id));
        return list.get(0);
    }

    public boolean themKhachHang(KhachHang obj){
        ContentValues contentValues = new ContentValues();
        contentValues.put("TenKH",obj.getTenKH());
        contentValues.put("NamSinh",obj.getNamSinh());
        contentValues.put("QueQuan",obj.getQueQuan());
        contentValues.put("GioiTinh",obj.getGioiTinh());
        contentValues.put("CCCD",obj.getCCCD());
        contentValues.put("MaNV",obj.getMaNV());

        return db.insert("KhachHang",null,contentValues) != -1;
    }

    public boolean suaKhachHang(KhachHang obj){
        ContentValues contentValues = new ContentValues();
        contentValues.put("TenKH",obj.getTenKH());
        contentValues.put("NamSinh",obj.getNamSinh());
        contentValues.put("QueQuan",obj.getQueQuan());
        contentValues.put("GioiTinh",obj.getGioiTinh());
        contentValues.put("CCCD",obj.getCCCD());
        contentValues.put("MaNV",obj.getMaNV());

        return db.update("KhachHang",contentValues,"MaKH=?",new String[]{String.valueOf(obj.getMaKH())}) != -1;
    }

    public boolean xoaKhachHang(KhachHang obj){
        return db.delete("KhachHang","MaKH=?",new String[]{String.valueOf(obj.getMaKH())}) != -1;
    }

    public List<String> xoaAnToanKhachHang(KhachHang obj){
        List<String> list = new ArrayList<>();
        list.add(0,"Không có liên kết");

        Cursor cursor1 = db.rawQuery("SELECT * FROM Phieu WHERE MaKH=?",new String[]{String.valueOf(obj.getMaKH())});
        if (cursor1.getCount()!=0){
            cursor1.close();
            list.add("Có phiếu thế chấp chứa khách hàng này");
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

    private List<KhachHang> getData(String sql, String...selectionArgs){
        List<KhachHang> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        while (cursor.moveToNext()){
            KhachHang obj = new KhachHang();

            obj.setMaKH(cursor.getInt(cursor.getColumnIndexOrThrow("MaKH")));
            obj.setTenKH(cursor.getString(cursor.getColumnIndexOrThrow("TenKH")));
            obj.setNamSinh(cursor.getString(cursor.getColumnIndexOrThrow("NamSinh")));
            obj.setQueQuan(cursor.getString(cursor.getColumnIndexOrThrow("QueQuan")));
            obj.setGioiTinh(cursor.getString(cursor.getColumnIndexOrThrow("GioiTinh")));
            obj.setCCCD(cursor.getString(cursor.getColumnIndexOrThrow("CCCD")));
            obj.setMaNV(cursor.getString(cursor.getColumnIndexOrThrow("MaNV")));

            list.add(obj);
        }
        cursor.close();

        return list;
    }

    public boolean checkCCCD(String cccd){
        String sql = "SELECT * FROM KhachHang WHERE CCCD LIKE '%"+ cccd +"%'";
        return !getData(sql).isEmpty();
    }

    public List<KhachHang> searchByID(String id){
        String sql = "SELECT * FROM KhachHang WHERE MaKH LIKE '%"+ id +"%'";
        return getData(sql);
    }

    public List<KhachHang> searchByName(String name){
        String sql = "SELECT * FROM KhachHang WHERE TenKH LIKE '%"+ name +"%'";
        return getData(sql);
    }

    public List<KhachHang> searchByCCCD(String cccd){
        String sql = "SELECT * FROM KhachHang WHERE CCCD LIKE '%"+ cccd +"%'";
        return getData(sql);
    }
}
