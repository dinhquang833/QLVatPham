package team7.fpoly.duan1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import team7.fpoly.duan1.database.CamDoDatabase;
import team7.fpoly.duan1.model.Phieu;

public class PhieuDAO {

    SQLiteDatabase db;

    public PhieuDAO(Context context) {
        db = new CamDoDatabase(context).getWritableDatabase();
    }

    public List<Phieu> getAll(){
        String sql = "SELECT P.MaPhieu,P.NgayThang,P.KiHan,P.TrangThai,P.Von,P.Lai,P.MaNV," +
                "L.MaLoai,L.TenLoai," +
                "VP.MaVP,VP.TenVP,VP.GiaVP,VP.MoTa," +
                "KH.MaKH,KH.TenKH,KH.NamSinh,KH.QueQuan,KH.GioiTinh,KH.CCCD " +
                "FROM Phieu AS P " +
                "INNER JOIN KhachHang AS KH ON P.MaKH = KH.MaKH " +
                "INNER JOIN VatPham AS VP ON P.MaVP = VP.MaVP " +
                "INNER JOIN Loai AS L ON P.MaLoai = L.MaLoai";
        return getData(sql);
    }

    public Phieu getByID(int id){
        String sql = "SELECT * FROM Phieu WHERE MaPhieu=?";
        List<Phieu> list = getData(sql, String.valueOf(id));
        return list.get(0);
    }

    public boolean themPhieu(Phieu obj){
        ContentValues contentValues = new ContentValues();
        contentValues.put("NgayThang",obj.getNgayThang());
        contentValues.put("KiHan",obj.getKiHan());
        contentValues.put("TrangThai",obj.getTrangThai());
        contentValues.put("Von",obj.getVon());
        contentValues.put("Lai",obj.getLai());
        contentValues.put("MaNV",obj.getMaNV());
        contentValues.put("MaLoai",obj.getMaLoai());
        contentValues.put("MaVP",obj.getMaVP());
        contentValues.put("MaKH",obj.getMaKH());

        return db.insert("Phieu",null,contentValues) != -1;
    }

    public boolean doiTrangThai(int id, String trangThai){
        ContentValues cv = new ContentValues();
        cv.put("TrangThai", trangThai);
        return db.update("Phieu", cv,"MaPhieu=?", new String[]{String.valueOf(id)}) != -1;
    }

    public boolean suaPhieu(Phieu obj){
        ContentValues contentValues = new ContentValues();
        contentValues.put("NgayThang",obj.getNgayThang());
        contentValues.put("KiHan",obj.getKiHan());
        contentValues.put("TrangThai",obj.getTrangThai());
        contentValues.put("Von",obj.getVon());
        contentValues.put("Lai",obj.getLai());
        contentValues.put("MaNV",obj.getMaNV());
        contentValues.put("MaLoai",obj.getMaLoai());
        contentValues.put("MaVP",obj.getMaVP());
        contentValues.put("MaKH",obj.getMaKH());

        return db.update("Phieu",contentValues,"MaPhieu=?",new String[]{String.valueOf(obj.getMaPhieu())}) != -1;
    }

    public boolean xoaPhieu(Phieu obj){
        return db.delete("Phieu","MaPhieu=?",new String[]{String.valueOf(obj.getMaPhieu())}) != -1;
    }

    private List<Phieu> getData(String sql, String...selectionArgs){
        List<Phieu> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        while (cursor.moveToNext()){
            Phieu obj = new Phieu();

            obj.setMaPhieu(cursor.getInt(cursor.getColumnIndexOrThrow("MaPhieu")));
            obj.setNgayThang(cursor.getString(cursor.getColumnIndexOrThrow("NgayThang")));
            obj.setKiHan(cursor.getString(cursor.getColumnIndexOrThrow("KiHan")));
            obj.setTrangThai(cursor.getString(cursor.getColumnIndexOrThrow("TrangThai")));
            obj.setVon(cursor.getInt(cursor.getColumnIndexOrThrow("Von")));
            obj.setLai(cursor.getInt(cursor.getColumnIndexOrThrow("Lai")));
            obj.setMaNV(cursor.getString(cursor.getColumnIndexOrThrow("MaNV")));
            obj.setMaLoai(cursor.getInt(cursor.getColumnIndexOrThrow("MaLoai")));
            obj.setTenLoai(cursor.getString(cursor.getColumnIndexOrThrow("TenLoai")));
            obj.setMaVP(cursor.getInt(cursor.getColumnIndexOrThrow("MaVP")));
            obj.setTenVP(cursor.getString(cursor.getColumnIndexOrThrow("TenVP")));
            obj.setGiaVP(cursor.getInt(cursor.getColumnIndexOrThrow("GiaVP")));
            obj.setMoTa(cursor.getString(cursor.getColumnIndexOrThrow("MoTa")));
            obj.setMaKH(cursor.getInt(cursor.getColumnIndexOrThrow("MaKH")));
            obj.setTenKH(cursor.getString(cursor.getColumnIndexOrThrow("TenKH")));
            obj.setNamSinh(cursor.getString(cursor.getColumnIndexOrThrow("NamSinh")));
            obj.setQueQuan(cursor.getString(cursor.getColumnIndexOrThrow("QueQuan")));
            obj.setGioiTinh(cursor.getString(cursor.getColumnIndexOrThrow("GioiTinh")));
            obj.setCCCD(cursor.getString(cursor.getColumnIndexOrThrow("CCCD")));

            list.add(obj);
        }
        cursor.close();
        return list;
    }

    public List<Phieu> searchByID(String id){
        String sql =
                "SELECT P.MaPhieu,P.NgayThang,P.KiHan,P.TrangThai,P.Von,P.Lai,P.MaNV," +
                "L.MaLoai,L.TenLoai," +
                "VP.MaVP,VP.TenVP,VP.GiaVP,VP.MoTa," +
                "KH.MaKH,KH.TenKH,KH.NamSinh,KH.QueQuan,KH.GioiTinh,KH.CCCD " +
                "FROM Phieu AS P " +
                "INNER JOIN KhachHang AS KH ON P.MaKH = KH.MaKH " +
                "INNER JOIN VatPham AS VP ON P.MaVP = VP.MaVP " +
                "INNER JOIN Loai AS L ON P.MaLoai = L.MaLoai" +
                "WHERE MaPhieu LIKE '%"+id+"%'";
        return getData(sql);
    }

    public List<Phieu> searchByCCCD(String cccd){
        String sql =
                "SELECT P.MaPhieu,P.NgayThang,P.KiHan,P.TrangThai,P.Von,P.Lai,P.MaNV," +
                        "L.MaLoai,L.TenLoai," +
                        "VP.MaVP,VP.TenVP,VP.GiaVP,VP.MoTa," +
                        "KH.MaKH,KH.TenKH,KH.NamSinh,KH.QueQuan,KH.GioiTinh,KH.CCCD " +
                        "FROM Phieu AS P " +
                        "INNER JOIN KhachHang AS KH ON P.MaKH = KH.MaKH " +
                        "INNER JOIN VatPham AS VP ON P.MaVP = VP.MaVP " +
                        "INNER JOIN Loai AS L ON P.MaLoai = L.MaLoai" +
                        "WHERE KH.CCCD LIKE '%"+cccd+"%'";
        return getData(sql);
    }


}
