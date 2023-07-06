package team7.fpoly.duan1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import team7.fpoly.duan1.database.CamDoDatabase;
import team7.fpoly.duan1.model.Loai;
import team7.fpoly.duan1.model.Loai;

public class LoaiDAO {

    SQLiteDatabase db;

    public LoaiDAO(Context context) {
        db = new CamDoDatabase(context).getWritableDatabase();
    }

    public List<Loai> getAll(){
        String sql = "SELECT * FROM Loai";
        return getData(sql);
    }

    public Loai getByID(int id){
        String sql = "SELECT * FROM Loai WHERE MaLoai=?";
        List<Loai> list = getData(sql, String.valueOf(id));
        return list.get(0);
    }

    public boolean themLoai(Loai obj){
        ContentValues contentValues = new ContentValues();
        contentValues.put("TenLoai",obj.getTenLoai());

        return db.insert("Loai",null,contentValues) != -1;
    }

    public boolean suaLoai(Loai obj){
        ContentValues contentValues = new ContentValues();
        contentValues.put("TenLoai",obj.getTenLoai());

        return db.update("Loai",contentValues,"MaLoai=?",new String[]{String.valueOf(obj.getMaLoai())}) != -1;
    }

    public boolean xoaLoai(Loai obj){
        return db.delete("Loai","MaLoai=?",new String[]{String.valueOf(obj.getMaLoai())}) != -1;
    }

    public List<String> xoaAnToanLoai(Loai obj){
        List<String> list = new ArrayList<>();
        list.add(0,"Không có liên kết");

        Cursor cursor1 = db.rawQuery("SELECT * FROM VatPham WHERE MaLoai=?",new String[]{String.valueOf(obj.getMaLoai())});
        if (cursor1.getCount()!=0){
            cursor1.close();
            list.add("Có vật phẩm thuộc loại này");
        }

        if (list.size() == 1){
            int check = db.delete("Loai", "MaLoai=?", new String[]{String.valueOf(obj.getMaLoai())});
            if (check != -1){
                list.set(0,"Xóa thành công");
            } else {
                list.set(0,"Xóa thất bại");
            }
        }

        return list;
    }

    private List<Loai> getData(String sql, String...selectionArgs){
        List<Loai> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        while (cursor.moveToNext()){
            Loai obj = new Loai();

            obj.setMaLoai(cursor.getInt(cursor.getColumnIndexOrThrow("MaLoai")));
            obj.setTenLoai(cursor.getString(cursor.getColumnIndexOrThrow("TenLoai")));
            list.add(obj);
            
        }
        cursor.close();
        return list;
    }
}
