package com.mitra.krushi.krushimitra;

import java.io.Serializable;

public class User implements Serializable {

    int u_id;
    String u_name;
    String u_district;
    String u_soil_type;
    String ph_no;
    double u_ph;
    double u_ec;
    double u_oc;

    static int s=1;

    public User(String u_name,String u_district,String u_soil_type,double u_ph,double u_ec,double u_oc,String ph_no) {

        this.u_id = s++;
        this.u_name = u_name;
        this.u_district = u_district;
        this.u_soil_type = u_soil_type;
        this.u_ph = u_ph;
        this.u_ec = u_ec;
        this.u_oc = u_oc;
        this.ph_no = ph_no;
    }
    public int getU_id()
    {
        return this.u_id;
    }

    public String getU_name() {
        return u_name;
    }

    public String getU_district() {
        return u_district;
    }

    public String getU_soil_type() {
        return u_soil_type;
    }

    public double getU_ph() {
        return u_ph;
    }

    public double getU_ec() {
        return u_ec;
    }

    public double getU_oc() {
        return u_oc;
    }

    public String getU_phone() {
        return ph_no;
    }

}
