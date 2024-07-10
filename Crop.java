package com.mitra.krushi.krushimitra;

public class Crop {
    String c_name;
    int    c_ess_rain;
    String c_ess_soil;
    double c_ess_ph;
    double c_ess_ec;
    double c_ess_oc;

    public Crop(String c_name,int c_ess_rain,String c_ess_soil,double c_ess_ph,double c_ess_ec,double c_ess_oc)
    {
        this.c_name=c_name;
        this.c_ess_rain=c_ess_rain;
        this.c_ess_soil=c_ess_soil;
        this.c_ess_ph=c_ess_ph;
        this.c_ess_ec=c_ess_ec;
        this.c_ess_oc=c_ess_oc;
    }
    public String getC_name()
    {
        return this.c_name;
    }

    public String getC_ess_soil() {
        return this.c_ess_soil;
    }

    public int getC_ess_rain()
    {
        return this.c_ess_rain;
    }

    public double getC_ess_ph()
    {
        return this.c_ess_ph;
    }

    public double getC_ess_ec()
    {
        return this.c_ess_ec;
    }

    public double getC_ess_oc()
    {
        return this.c_ess_oc;
    }
}
