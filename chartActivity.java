package com.mitra.krushi.krushimitra;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

public class chartActivity extends AppCompatActivity {

    BarChart cropChart;
    graphdb gdb;
    int year1,year2,year3,year4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        String crop = getIntent().getStringExtra("crop");
        gdb = new graphdb(this);
        String[] string = {crop};
        Cursor cursor=gdb.query(null,"cropname=?",string,gdb.col1);

        if(cursor.moveToFirst()){
            do{
                year1=Integer.parseInt(cursor.getString(cursor.getColumnIndex(gdb.col2)));
                year2=Integer.parseInt(cursor.getString(cursor.getColumnIndex(gdb.col3)));
                year3=Integer.parseInt(cursor.getString(cursor.getColumnIndex(gdb.col4)));
                year4=Integer.parseInt(cursor.getString(cursor.getColumnIndex(gdb.col5)));
            }while(cursor.moveToNext());
        }


        cropChart = (BarChart) findViewById(R.id.barGraph);
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0,year1));
        barEntries.add(new BarEntry(1,year2));
        barEntries.add(new BarEntry(2,year3));
        barEntries.add(new BarEntry(3,year4));

        String y=getIntent().getStringExtra("crop_cnt");
        int y5= Integer.parseInt(y);
        barEntries.add(new BarEntry(4,y5));
        BarDataSet barDataSet = new BarDataSet(barEntries,"Stats");

        String[] years = new String[] {"2015","2016","2017","2018","2019"};

        BarData statData = new BarData(barDataSet);
        cropChart.setData(statData);

        XAxis xAxis = cropChart.getXAxis();
        xAxis.setValueFormatter(new MyAxisValueFormatter(years));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

    }

    public class MyAxisValueFormatter implements IAxisValueFormatter
    {
        private String[] mValues;
        public MyAxisValueFormatter(String[] values)
        {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int) value];
        }
    }
}
