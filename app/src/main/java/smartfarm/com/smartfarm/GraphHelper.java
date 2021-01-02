package smartfarm.com.smartfarm;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Created by macbook on 13/05/2020.
 */

public class GraphHelper {
    LineChart lineChart = null;
    LineDataSet lineDataSet;
    LineData lineData;
    ArrayList<Entry> entries;
    ArrayList<Pair<Float, Float>> arrayList;
    LimitLine maxLimitLine;
    LimitLine minLimitLine;

    GraphHelper(LineChart lineChart){
        this.lineChart = lineChart;
        entries = new ArrayList<Entry>();
    }

    public void setMaxLimitLine(float maxlimit){
        maxLimitLine = new LimitLine(maxlimit, "최대 온도");
        maxLimitLine.setLineWidth(4f);
        maxLimitLine.enableDashedLine(10f, 10f, 0f);
        maxLimitLine.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        maxLimitLine.setTextSize(10f);
    }

    public void setMinLimitLine(float minlimit){
        minLimitLine = new LimitLine(minlimit, "최소 온도");
        minLimitLine.setLineWidth(4f);
        minLimitLine.enableDashedLine(10f, 10f, 0f);
        minLimitLine.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        minLimitLine.setTextSize(10f);
    }


    private void setChart(){
        lineDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        lineDataSet.setDrawFilled(false); //그래프 밑부분 색칠
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(3);
        lineDataSet.setMode(LineDataSet.Mode.LINEAR);
    }

    /* 온도 데이터들 모임 */
    public void addEntrys(ArrayList<Pair<Float, Float>> arrayList){
        entries.clear();
        this.arrayList = arrayList;

        try {
            for (int i = 0; i < this.arrayList.size(); i++) {
                entries.add(new Entry(this.arrayList.get(i).first, this.arrayList.get(i).second));
            }
        }catch (Exception ex){

        }

        lineDataSet = new LineDataSet(entries, "# of Calls");
    }

    public void drawChart(){
        lineChart.clear();
        lineChart.getAxisLeft().removeAllLimitLines();
        setChart();

        lineData = new LineData(lineDataSet);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(23);
        xAxis.setLabelCount(24,true);
        xAxis.setDrawGridLines(false);

        YAxis yAxis = lineChart.getAxisRight();
        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxis.setEnabled(false);

        float min = minLimitLine.getLimit();
        float max = maxLimitLine.getLimit();

        // first : X축의 값
        // second : Y축의 값
        for(int i =0; i<arrayList.size(); i++){
            min = arrayList.get(i).second <  min ? arrayList.get(i).second : min;
            max = arrayList.get(i).second <  max ?  max : arrayList.get(i).second;
        }

        yAxisLeft.setSpaceBottom(50);
        yAxisLeft.setSpaceTop(50);
        yAxisLeft.setAxisMinValue(min*1.1f);
        yAxisLeft.setAxisMaxValue(max*1.1f);
        yAxisLeft.setStartAtZero(false);
        yAxisLeft.setLabelCount(10);
        yAxisLeft.setTextColor(Color.BLACK);
//        yAxisLeft.setDrawGridLines(true);

        yAxisLeft.removeAllLimitLines();
        yAxisLeft.setDrawLimitLinesBehindData(true);

        if(maxLimitLine != null){
            yAxisLeft.addLimitLine(maxLimitLine);
        }

        if(minLimitLine != null){
            yAxisLeft.addLimitLine(minLimitLine);
        }


        lineChart.setData(lineData);

        lineChart.setDoubleTapToZoomEnabled(false);
    }


}




















