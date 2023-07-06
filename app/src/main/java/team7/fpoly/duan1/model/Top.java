package team7.fpoly.duan1.model;

public class Top {
    private String maNV;
    private int soPhieu;
    private int von;
    private int lai;

    public Top() {
    }

    public Top(String maNV, int soPhieu, int von, int lai) {
        this.maNV = maNV;
        this.soPhieu = soPhieu;
        this.von = von;
        this.lai = lai;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public int getSoPhieu() {
        return soPhieu;
    }

    public void setSoPhieu(int soPhieu) {
        this.soPhieu = soPhieu;
    }

    public int getVon() {
        return von;
    }

    public void setVon(int von) {
        this.von = von;
    }

    public int getLai() {
        return lai;
    }

    public void setLai(int lai) {
        this.lai = lai;
    }
}
