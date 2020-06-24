package smartfarm.com.smartfarm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private String hostName = "222.111.78.166";
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(hostName);

        setTabText();
        setNumPicker();
    }

    public void onButtonClick(View view){
        switch (view.getId()) {
            case R.id.ButtonOpen :
                break ;
            case R.id.ButtonAuto:
                break;
            case R.id.ButtonClose :
                break ;
        }
    }

    public void onFindButtonClick(View view){
        GraphHelper gh = new GraphHelper((LineChart)findViewById(R.id.chart));
        ArrayList<Pair<Float, Float>> arr = new ArrayList<Pair<Float, Float>>();

        dbHelper.execute("get","Temp");

        gh.setMaxLimitLine(4);
        gh.setMinLimitLine(3);

        arr.add(new Pair(0f, 1f));
        arr.add(new Pair(1f, 2f));
        arr.add(new Pair(2f, 3f));
        arr.add(new Pair(3f, 10f));
        arr.add(new Pair(4f, 6f));
        arr.add(new Pair(5f, 2f));
        arr.add(new Pair(6f, 7f));

        // gh.addEntrys(dbHelper.mTempList);
        gh.addEntrys(arr);
        gh.drawChart();

        LineChart lc = (LineChart)findViewById(R.id.chart);
        lc.setVisibility(View.VISIBLE);
    }

    public void setTabText(){
        TabHost tabHost1 = (TabHost) findViewById(R.id.tabHost1) ;
        tabHost1.setup() ;

        // 첫 번째 Tab. (탭 표시 텍스트:"TAB 1"), (페이지 뷰:"content1")
        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1") ;
        ts1.setContent(R.id.content1) ;
        ts1.setIndicator("Control") ;
        tabHost1.addTab(ts1)  ;

        // 두 번째 Tab. (탭 표시 텍스트:"TAB 2"), (페이지 뷰:"content2")
        TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2") ;
        ts2.setContent(R.id.content2) ;
        ts2.setIndicator("Graph") ;
        tabHost1.addTab(ts2) ;

        // 세 번째 Tab. (탭 표시 텍스트:"TAB 3"), (페이지 뷰:"content3")
        TabHost.TabSpec ts3 = tabHost1.newTabSpec("Tab Spec 3") ;
        ts3.setContent(R.id.content3) ;
        ts3.setIndicator("Settings") ;
        tabHost1.addTab(ts3) ;
    }

    public void setNumPicker(){
        Calendar calendar = Calendar.getInstance();
        int cYear = calendar.get(Calendar.YEAR);
        int cMonth = calendar.get(Calendar.MONTH);
        int cDay = calendar.get(Calendar.DAY_OF_MONTH);

        /* SET MAX */
        NumberPicker npMax = (NumberPicker) findViewById(R.id.numPickMax);
        npMax.setMinValue(1);
        npMax.setMaxValue(50);
        npMax.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npMax.setWrapSelectorWheel(false);
        npMax.setValue(23);

        /* SET MIN*/
        NumberPicker npMin = (NumberPicker) findViewById(R.id.numPickMin);
        npMin.setMinValue(1);
        npMin.setMaxValue(50);
        npMin.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npMin.setWrapSelectorWheel(false);
        npMin.setValue(23);

        /* SET YEAR */
        NumberPicker npYear = (NumberPicker) findViewById(R.id.numPickYear);
        npYear.setMaxValue(cYear+10);
        npYear.setMinValue(cYear-70);
        npYear.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npYear.setWrapSelectorWheel(false);
        npYear.setValue(cYear);

        /* SET MON */
        NumberPicker npMon = (NumberPicker) findViewById(R.id.numPickMon);
        npMon.setMaxValue(12);
        npMon.setMinValue(1);
        npMon.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npMon.setWrapSelectorWheel(false);
        npMon.setValue(cMonth);

        /* SET DAY */
        NumberPicker npDay = (NumberPicker) findViewById(R.id.numPickDay);
        npDay.setMinValue(1);
        npDay.setMaxValue(31);
        npDay.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npDay.setWrapSelectorWheel(false);
        npDay.setValue(cDay);

    }
}
