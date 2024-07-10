package com.mitra.krushi.krushimitra;

import java.io.Serializable;

public class SuggestFertil implements Serializable {
    String cropName, PhHigh,PhLow,EcHigh,EcLow,OcHigh,OcLow;
    double ph_diff;
    double ec_diff;
    double oc_diff;

    public SuggestFertil(String cropName,String PhHigh,String PhLow,String EcHigh,String EcLow,String OcHigh, String OcLow,Double ph_diff,Double ec_diff,Double oc_diff)
    {
        this.cropName = cropName;
        this.PhHigh = PhHigh;
        this.PhLow = PhLow;
        this.EcHigh = EcHigh;
        this.OcHigh= OcHigh;
        this.OcLow= OcLow;
        this.ec_diff=ec_diff;
        this.oc_diff=oc_diff;
        this.ph_diff=ph_diff;
    }

    public String getCropName()
    {
        return this.cropName;
    }

    public String getFertilizPH()
    {
        if(ph_diff<0)
            return this.PhLow;
        else
            return this.PhHigh;
    }

    public String getFertilizEC() {
        if(ec_diff<0)
            return this.EcLow;
        else
            return this.EcHigh;
    }

    public String getFertilizOC() {
        if(oc_diff<0)
            return this.OcLow;
        else
            return this.OcHigh;
    }
}
