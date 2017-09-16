package cn.edu.seu.display.model;

public class Timedata {
    private int hour;

    private int PVnum=0;
    private int UVnum=0;

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getHour() {
        return hour;
    }

    public void setPVnum(int num) {
        this.PVnum = num;
    }

    public int getUVnum() {
        return UVnum;
    }

    public void addUVnum(int a){
        UVnum+=a;
    }

    public void setUVnum(int UVnum) {
        this.UVnum = UVnum;
    }

    public int getPVnum() {
        return PVnum;
    }

    public void addPVnum(int a){
        PVnum+=a;
    }
}
