package cn.edu.seu.display.model;

import org.apache.hadoop.classification.InterfaceAudience;

public class Urldata {
    private int num=0;
    private int rank=0;
    private String url;

    public Urldata(int rank, String url,int num){
        setNum(num);
        setUrl(url);
        setRank(rank);
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
