package com.muqingbfq;

public class xm {
    public String id, name, zz;
    public Object picurl;
    public boolean cz;
    public xm(String id, String name, String zz, String picurl) {
        this.id = id;
        this.name = name;
        this.zz = zz;
        this.picurl = picurl;
    }
    public xm(String id, String name, String picurl, boolean cz) {
        this.id = id;
        this.name = name;
        this.picurl = picurl;
        this.cz = cz;
    }
    public xm(String id, String name, int picurl, boolean cz) {
        this.id = id;
        this.name = name;
        this.picurl = picurl;
        this.cz = cz;
    }
}