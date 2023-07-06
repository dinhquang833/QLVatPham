package team7.fpoly.duan1.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import team7.fpoly.duan1.database.CamDoDatabase;
import team7.fpoly.duan1.model.NhanVien;
import team7.fpoly.duan1.model.Top;

public class ThongKeDAO {

    SQLiteDatabase db;
    Context context;

    public ThongKeDAO(Context context) {
        this.context = context;
        db = new CamDoDatabase(context).getWritableDatabase();
    }


    public int getVon(String tuNgay, String denNgay){
        tuNgay = tuNgay.replace("/","");
        denNgay = denNgay.replace("/","");
        String sqlDoanhThu = "SELECT SUM(Von) AS Von FROM Phieu WHERE substr(NgayThang,7)||substr(NgayThang,4,2)||substr(NgayThang,1,2) BETWEEN ? AND ?";
        List<Integer> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sqlDoanhThu, new String[]{tuNgay,denNgay});

        while (cursor.moveToNext()){
            try {
                list.add(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("Von"))));
            }catch (Exception e){
                list.add(0);
            }
        }
        return list.get(0);
    }

    public int getLai(String tuNgay, String denNgay){
        tuNgay = tuNgay.replace("/","");
        denNgay = denNgay.replace("/","");
        String sqlDoanhThu = "SELECT SUM(Lai) AS Lai FROM Phieu WHERE substr(NgayThang,7)||substr(NgayThang,4,2)||substr(NgayThang,1,2) BETWEEN ? AND ?";
        List<Integer> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sqlDoanhThu, new String[]{tuNgay,denNgay});

        while (cursor.moveToNext()){
            try {
                list.add(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("Lai"))));
            }catch (Exception e){
                list.add(0);
            }
        }
        return list.get(0);
    }

    public List<Top> getTop(String sapXep) {
        String sqlTopSoPhieu = "SELECT MaNV,COUNT(MaPhieu) AS SoPhieu, SUM(Von) AS Von, SUM(Lai) AS Lai FROM Phieu GROUP BY MaNV ORDER BY SoPhieu DESC";
        String sqlTopVon = "SELECT MaNV,COUNT(MaPhieu) AS SoPhieu, SUM(Von) AS Von, SUM(Lai) AS Lai FROM Phieu GROUP BY MaNV ORDER BY Von DESC";
        String sqlTopLai = "SELECT MaNV,COUNT(MaPhieu) AS SoPhieu, SUM(Von) AS Von, SUM(Lai) AS Lai FROM Phieu GROUP BY MaNV ORDER BY Lai DESC";
        List<Top> list = new ArrayList<>();
        NhanVienDAO nhanVienDAO = new NhanVienDAO(context);
        Cursor cursor;
        switch (sapXep){
            case "SoPhieu":
                cursor = db.rawQuery(sqlTopSoPhieu,null);
                break;
            case "Von":
                cursor = db.rawQuery(sqlTopVon,null);
                break;
            case "Lai":
            default:
                cursor = db.rawQuery(sqlTopLai,null);
                break;
        }

        while (cursor.moveToNext()){
            Top top = new Top();
            NhanVien nv = nhanVienDAO.getByID(cursor.getString(cursor.getColumnIndexOrThrow("MaNV")));
            top.setMaNV(nv.getMaNV());
            top.setSoPhieu(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("SoPhieu"))));
            top.setVon(cursor.getInt(cursor.getColumnIndexOrThrow("Von")));
            top.setLai(cursor.getInt(cursor.getColumnIndexOrThrow("Lai")));
            list.add(top);
        }
        cursor.close();
        return list;
    }
}
