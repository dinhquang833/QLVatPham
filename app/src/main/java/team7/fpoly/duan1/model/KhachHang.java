package team7.fpoly.duan1.model;

public class KhachHang {
    private int maKH;
    private String tenKH;
    private String namSinh;
    private String queQuan;
    private String gioiTinh;
    private String CCCD;
    private String maNV;

    public KhachHang() {
    }

    public KhachHang(int maKH, String tenKH, String namSinh, String queQuan, String gioiTinh, String CCCD, String maNV) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.namSinh = namSinh;
        this.queQuan = queQuan;
        this.gioiTinh = gioiTinh;
        this.CCCD = CCCD;
        this.maNV = maNV;
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getNamSinh() {
        return namSinh;
    }

    public void setNamSinh(String namSinh) {
        this.namSinh = namSinh;
    }

    public String getQueQuan() {
        return queQuan;
    }

    public void setQueQuan(String queQuan) {
        this.queQuan = queQuan;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getCCCD() {
        return CCCD;
    }

    public void setCCCD(String CCCD) {
        this.CCCD = CCCD;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }
}
