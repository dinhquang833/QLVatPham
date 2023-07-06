package team7.fpoly.duan1.model;

public class VatPham {
    private int maVP;
    private String tenVP;
    private int giaVP;
    private String moTa;
    private int maLoai;
    private String tenLoai;
    private String maNV;

    public VatPham() {
    }

    public VatPham(int maVP, String tenVP, int giaVP, String moTa, int maLoai, String tenLoai, String maNV) {
        this.maVP = maVP;
        this.tenVP = tenVP;
        this.giaVP = giaVP;
        this.moTa = moTa;
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
        this.maNV = maNV;
    }

    public int getMaVP() {
        return maVP;
    }

    public void setMaVP(int maVP) {
        this.maVP = maVP;
    }

    public String getTenVP() {
        return tenVP;
    }

    public void setTenVP(String tenVP) {
        this.tenVP = tenVP;
    }

    public int getGiaVP() {
        return giaVP;
    }

    public void setGiaVP(int giaVP) {
        this.giaVP = giaVP;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public int getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(int maLoai) {
        this.maLoai = maLoai;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }
}