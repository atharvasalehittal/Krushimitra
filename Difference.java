package com.mitra.krushi.krushimitra;

import java.io.Serializable;

import static java.lang.StrictMath.abs;

public class Difference {
    String c_name;
    int r_diff;
    double ph_diff;
    double ec_diff;
    double oc_diff;

    public Difference(String c_name,int r_diff,double ph_diff,double ec_diff,double oc_diff)
    {
        this.c_name=c_name;
        this.r_diff=abs(r_diff);
        this.ph_diff=abs(ph_diff);
        this.ec_diff=abs(ec_diff);
        this.oc_diff=abs(oc_diff);
    }

    public String getD_cname() {
        return this.c_name;
    }

    public int getD_rdiff() {
        return this.r_diff;
    }

    public double getD_phdiff() {
        return this.ph_diff;
    }

    public double getD_ecdiff() {
        return this.ec_diff;
    }

    public double getD_ocdiff() {
        return this.oc_diff;
    }
}
