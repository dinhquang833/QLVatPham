package team7.fpoly.duan1.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CamDoDatabase extends SQLiteOpenHelper {

    //CONSTANT

    private final static String DB_NAME = "CamDoDatabase";
    private final static int DB_VERSION = 1;

    //CREATE

    private final static String CREATE_NHAN_VIEN = "CREATE TABLE NhanVien(" +
            "MaNV TEXT PRIMARY KEY," +
            "MatKhau TEXT NOT NULL," +
            "HoTenNV TEXT NOT NULL," +
            "ChucVu TEXT NOT NULL)";
    private final static String CREATE_LOAI = "CREATE TABLE Loai(" +
            "MaLoai INTEGER PRIMARY KEY AUTOINCREMENT," +
            "TenLoai TEXT NOT NULL)";
    private final static String CREATE_VAT_PHAM = "CREATE TABLE VatPham(" +
            "MaVP INTEGER PRIMARY KEY AUTOINCREMENT," +
            "TenVP TEXT NOT NULL," +
            "GiaVP INTEGER NOT NULL," +
            "MoTa TEXT NOT NULL," +
            "MaLoai INTEGER REFERENCES Loai(MaLoai)," +
            "MaNV TEXT REFERENCES NhanVien(MaNV))";
    private final static String CREATE_KHACH_HANG = "CREATE TABLE KhachHang(" +
            "MaKH INTEGER PRIMARY KEY AUTOINCREMENT," +
            "TenKH TEXT NOT NULL," +
            "NamSinh TEXT NOT NULL," +
            "QueQuan TEXT NOT NULL," +
            "GioiTinh TEXT NOT NULL," +
            "CCCD TEXT NOT NULL," +
            "MaNV TEXT REFERENCES NhanVien(MaNV))";
    private final static String CREATE_PHIEU = "CREATE TABLE Phieu(" +
            "MaPhieu INTEGER PRIMARY KEY AUTOINCREMENT," +
            "NgayThang TEXT NOT NULL," +
            "KiHan TEXT NOT NULL," +
            "TrangThai TEXT NOT NULL," +
            "Von INTEGER NOT NULL," +
            "Lai INTEGER NOT NULL," +
            "MaNV TEXT REFERENCES NhanVien(MaNV)," +
            "MaLoai INTEGER REFERENCES Loai(MaLoai)," +
            "MaVP INTEGER REFERENCES VatPham(MaVP)," +
            "MaKH INTEGER REFERENCES KhachHang(MaKH))";

    //ADMIN

    private final static String CREATE_ADMIN = "INSERT INTO NhanVien VALUES('Admin','Admin','Admin','Admin')";

    //DROP

    private final static String DROP_NHAN_VIEN = "DROP TABLE IF EXISTS NhanVien";
    private final static String DROP_LOAI = "DROP TABLE IF EXISTS Loai";
    private final static String DROP_VAT_PHAM = "DROP TABLE IF EXISTS KhachHang";
    private final static String DROP_KHACH_HANG = "DROP TABLE IF EXISTS VatPham";
    private final static String DROP_PHIEU = "DROP TABLE IF EXISTS Phieu";
    
    //CONSTRUCTOR

    public CamDoDatabase(Context context) {
        super(context, DB_NAME,null, DB_VERSION);
    }
    
    //ON_CREATE

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NHAN_VIEN);
        db.execSQL(CREATE_LOAI);
        db.execSQL(CREATE_VAT_PHAM);
        db.execSQL(CREATE_KHACH_HANG);
        db.execSQL(CREATE_PHIEU);
        db.execSQL(CREATE_ADMIN);
    }

    //ON_UPGRADE
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_NHAN_VIEN);
        db.execSQL(DROP_LOAI);
        db.execSQL(DROP_VAT_PHAM);
        db.execSQL(DROP_KHACH_HANG);
        db.execSQL(DROP_PHIEU);
        onCreate(db);
    }
}
