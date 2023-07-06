package team7.fpoly.duan1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import team7.fpoly.duan1.database.CamDoDatabase;
import team7.fpoly.duan1.model.VatPham;

public class VatPhamDAO {

    SQLiteDatabase db;

    public VatPhamDAO(Context context) {
        db = new CamDoDatabase(context).getWritableDatabase();
    }

    public List<VatPham> getAll(){
        String sql = "SELECT VP.*,L.TenLoai FROM VatPham AS VP INNER JOIN Loai AS L ON VP.MaLoai = L.MaLoai";
        return getData(sql);
    }

    public List<VatPham> getUnused(){
        String sql = "SELECT VP.*,L.TenLoai FROM VatPham AS VP INNER JOIN Loai AS L ON VP.MaLoai = L.MaLoai WHERE MaVP NOT IN(SELECT MaVP FROM Phieu)";
        return getData(sql);
    }

    public VatPham getByID(int id){
        String sql = "SELECT * FROM VatPham WHERE MaVP=?";
        List<VatPham> list = getData(sql, String.valueOf(id));
        return list.get(0);
    }

    public boolean themVatPham(VatPham obj){
        ContentValues contentValues = new ContentValues();
        contentValues.put("TenVP",obj.getTenVP());
        contentValues.put("GiaVP",obj.getGiaVP());
        contentValues.put("MoTa",obj.getMoTa());
        contentValues.put("MaLoai",obj.getMaLoai());
        contentValues.put("MaNV",obj.getMaNV());

        return db.insert("VatPham",null,contentValues) != -1;
    }

    public boolean suaVatPham(VatPham obj){
        ContentValues contentValues = new ContentValues();
        contentValues.put("TenVP",obj.getTenVP());
        contentValues.put("GiaVP",obj.getGiaVP());
        contentValues.put("MoTa",obj.getMoTa());
        contentValues.put("MaLoai",obj.getMaLoai());
        contentValues.put("MaNV",obj.getMaNV());

        return db.update("VatPham",contentValues,"MaVP=?",new String[]{String.valueOf(obj.getMaVP())}) != -1;
    }

    public boolean xoaVatPham(VatPham obj){
        return db.delete("VatPham","MaVP=?",new String[]{String.valueOf(obj.getMaVP())}) != -1;
    }

    public List<String> xoaAnToanVatPham(VatPham obj){
        List<String> list = new ArrayList<>();
        list.add(0,"Không có liên kết");

        Cursor cursor1 = db.rawQuery("SELECT * FROM Phieu WHERE MaVP=?",new String[]{String.valueOf(obj.getMaVP())});
        if (cursor1.getCount()!=0){
            cursor1.close();
            list.add("Có phiếu chứa vật phẩm này");
        }

        if (list.size() == 1){
            int check = db.delete("VatPham", "MaVP=?", new String[]{String.valueOf(obj.getMaVP())});
            if (check != -1){
                list.set(0,"Xóa thành công");
            } else {
                list.set(0,"Xóa thất bại");
            }
        }

        return list;
    }

    private List<VatPham> getData(String sql, String...selectionArgs){
        List<VatPham> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        while (cursor.moveToNext()){
            VatPham obj = new VatPham();

            obj.setMaVP(cursor.getInt(cursor.getColumnIndexOrThrow("MaVP")));
            obj.setTenVP(cursor.getString(cursor.getColumnIndexOrThrow("TenVP")));
            obj.setGiaVP(cursor.getInt(cursor.getColumnIndexOrThrow("GiaVP")));
            obj.setMoTa(cursor.getString(cursor.getColumnIndexOrThrow("MoTa")));
            obj.setMaLoai(cursor.getInt(cursor.getColumnIndexOrThrow("MaLoai")));
            obj.setTenLoai(cursor.getString(cursor.getColumnIndexOrThrow("TenLoai")));
            obj.setMaNV(cursor.getString(cursor.getColumnIndexOrThrow("MaNV")));

            list.add(obj);
        }
        cursor.close();
        return list;
    }

    public List<VatPham> searchByID(String id){
        String sql = "SELECT * FROM VatPham AS VP INNER JOIN Loai AS L ON VP.MaLoai = L.MaLoai WHERE MaVP LIKE '%"+ id +"%'";
        return getData(sql);
    }

    public List<VatPham> searchByName(String name){
        String sql = "SELECT * FROM VatPham AS VP INNER JOIN Loai AS L ON VP.MaLoai = L.MaLoai WHERE TenVP LIKE '%"+ name +"%'";
        return getData(sql);
    }

    public List<VatPham> searchByDes(String des){
        String sql = "SELECT * FROM VatPham AS VP INNER JOIN Loai AS L ON VP.MaLoai = L.MaLoai WHERE MoTa LIKE '%"+ des +"%'";
        return getData(sql);
    }

    public List<VatPham> searchByType(String type){
        String sql = "SELECT * FROM VatPham AS VP INNER JOIN Loai AS L ON VP.MaLoai = L.MaLoai WHERE MaLoai LIKE '%"+ type +"%'";
        return getData(sql);
    }

    public List<VatPham> searchByStaff(String staff){
        String sql = "SELECT * FROM VatPham AS VP INNER JOIN Loai AS L ON VP.MaLoai = L.MaLoai WHERE MaNV LIKE '%"+ staff +"%'";
        return getData(sql);
    }
}
