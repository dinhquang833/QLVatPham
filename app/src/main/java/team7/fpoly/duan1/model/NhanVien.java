package team7.fpoly.duan1.model;

public class NhanVien {
    private String maNV;
    private String matKhau;
    private String hoTenNV;
    private String chucVu;

    public NhanVien() {
    }

    public NhanVien(String maNV, String matKhau, String hoTenNV, String chucVu) {
        this.maNV = maNV;
        this.matKhau = matKhau;
        this.hoTenNV = hoTenNV;
        this.chucVu = chucVu;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getHoTenNV() {
        return hoTenNV;
    }

    public void setHoTenNV(String hoTenNV) {
        this.hoTenNV = hoTenNV;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }
}
