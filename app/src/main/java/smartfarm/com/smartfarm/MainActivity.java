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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTabText();
        setNumPicker();
        setTempText(); // 초기 온도 설정
        setHumText(); // 초기 습도 설정
        setAutoYn(); // 초기 자동 설정
        setDoorYn(); // 초기 문 이미지 설정
    }

    public void onButtonClick(View view){
        DbHelper dbHelper = new DbHelper(hostName);
        ArrayList<Pair<String,String>> params = new ArrayList<Pair<String,String>>();
        Toast myToast;
        ImageView imgDoor = (ImageView)findViewById(R.id.imgDoor);

        switch (view.getId()) {
            case R.id.ButtonOpen :
                dbHelper.execute("update", "DoorOpen");

                imgDoor.setImageResource(R.drawable.open);

                myToast = Toast.makeText(this.getApplicationContext(), "문이 열립니다.", Toast.LENGTH_SHORT);
                myToast.show();
                break ;
            case R.id.ButtonAuto:
                Button btn = (Button)findViewById(R.id.ButtonAuto);

                dbHelper.execute("update", "AutoYn");

                if(btn.getText().equals("자동")){
                    btn.setText("수동");
                    myToast = Toast.makeText(this.getApplicationContext(), "수동 모드로 전환합니다.", Toast.LENGTH_SHORT);
                    myToast.show();

                    Button btnOpen = (Button)findViewById(R.id.ButtonOpen);
                    Button btnClose = (Button)findViewById(R.id.ButtonClose);

                    btnOpen.setVisibility(View.VISIBLE);
                    btnClose.setVisibility(View.VISIBLE);
                }
                else{
                    btn.setText("자동");
                    myToast = Toast.makeText(this.getApplicationContext(), "자동 모드로 전환합니다.", Toast.LENGTH_SHORT);
                    myToast.show();

                    Button btnOpen = (Button)findViewById(R.id.ButtonOpen);
                    Button btnClose = (Button)findViewById(R.id.ButtonClose);

                    btnOpen.setVisibility(View.GONE);
                    btnClose.setVisibility(View.GONE);
                }
                break;
            case R.id.ButtonClose :
                dbHelper.execute("update", "DoorClose");

                imgDoor.setImageResource(R.drawable.close);

                myToast = Toast.makeText(this.getApplicationContext(), "문이 닫힙니다.", Toast.LENGTH_SHORT);
                myToast.show();

                break ;
            case R.id.drawChartStart:

                GraphHelper gh = new GraphHelper((LineChart)findViewById(R.id.chart));

                NumberPicker npYear = (NumberPicker)findViewById(R.id.numPickYear);
                NumberPicker npMon = (NumberPicker)findViewById(R.id.numPickMon);
                NumberPicker npDay = (NumberPicker)findViewById(R.id.numPickDay);

                String timekey = Integer.toString(npYear.getValue());
                if(npMon.getValue() < 10)
                    timekey += "0" + Integer.toString(npMon.getValue());
                else
                    timekey += Integer.toString(npMon.getValue());

                if(npDay.getValue() < 10)
                    timekey += "0" + Integer.toString(npDay.getValue());
                else
                    timekey += Integer.toString(npDay.getValue());

                params.add(new Pair<String,String>("TIMEKEY", timekey));

                dbHelper = new DbHelper(hostName);

                dbHelper.setParams(params);

                try {
                    dbHelper.execute("get","Temp").get();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if(dbHelper.mTempList.size() == 0) {
                    myToast = Toast.makeText(this.getApplicationContext(), "조회 날짜에 데이터가 없습니다.", Toast.LENGTH_SHORT);
                    myToast.show();
                    return;
                }

                gh.addEntrys(dbHelper.mTempList);

                dbHelper = new DbHelper(hostName);
                try{
                    dbHelper.execute("get", "MaxTemp").get();
                } catch(Exception e){
                    e.printStackTrace();
                }

                gh.setMaxLimitLine(dbHelper.maxTemp);

                dbHelper = new DbHelper(hostName);
                try {
                    dbHelper.execute("get", "MinTemp").get();
                }catch (Exception e){
                    e.printStackTrace();
                }

                gh.setMinLimitLine(dbHelper.minTemp);

                //gh.addEntrys(arr);
                gh.drawChart();

                LineChart lc = (LineChart)findViewById(R.id.chart);
                lc.setVisibility(View.VISIBLE);

                break;
            case R.id.btnMinSave:
                NumberPicker npMinTemp = (NumberPicker)findViewById(R.id.numPickMin);

                String minTemp = Integer.toString(npMinTemp.getValue());

                params.add(new Pair<String, String>("minTemp", minTemp));

                dbHelper.setParams(params);

                dbHelper.execute("update", "MinTemp");

                break;
            case R.id.btnMaxSave:
                NumberPicker npMaxTemp = (NumberPicker)findViewById(R.id.numPickMax);

                String maxTemp = Integer.toString(npMaxTemp.getValue());

                params.add(new Pair<String, String>("maxTemp", maxTemp));

                dbHelper.setParams(params);

                dbHelper.execute("update", "MaxTemp");

                break;
        }
    }

    /*  온도 초기 Text */
    void setTempText(){
        DbHelper dbHelper = new DbHelper(hostName);

        dbHelper.execute("get", "RecentTemp");

        while(dbHelper.recentTemp == -1);

        TextView tv = (TextView)findViewById(R.id.tvCal);

        tv.setText("현재 온도" + " : " + dbHelper.recentTemp + "℃");
    }
    /* 습도 초기 Text */
    void setHumText(){
        DbHelper dbHelper = new DbHelper(hostName);

        dbHelper.execute("get", "RecentHum");

        while(dbHelper.recentHumidity == -1);

        TextView tv = (TextView)findViewById(R.id.tvHum);

        tv.setText("현재 온도" + " : " + dbHelper.recentHumidity + "%");
    }

    void setAutoYn(){
        DbHelper dbHelper = new DbHelper(hostName);

        dbHelper.execute("get", "AutoYn");

        while(dbHelper.recentAutoYn.equals("-1"));

        if(dbHelper.recentAutoYn.equals("Y")){
            Button btn = (Button)findViewById(R.id.ButtonAuto);

            btn.setText("자동");

            Button btnOpen = (Button)findViewById(R.id.ButtonOpen);
            Button btnClose = (Button)findViewById(R.id.ButtonClose);

            btnOpen.setVisibility(View.GONE);
            btnClose.setVisibility(View.GONE);
        }
        else{
            Button btn = (Button)findViewById(R.id.ButtonAuto);

            btn.setText("수동");
        }
    }

    void setDoorYn(){
        DbHelper dbHelper = new DbHelper(hostName);

        try {
            dbHelper.execute("get", "DoorYn").get();
        }catch(Exception ex){
            ex.printStackTrace();
        }

        ImageView imgDoor = (ImageView)findViewById(R.id.imgDoor);

        if(dbHelper.recentDoorYn.equals("Y")){
            imgDoor.setImageResource(R.drawable.open);
        }
        else{
            imgDoor.setImageResource(R.drawable.close);
        }
    }

    void setTabText(){
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

    void setNumPicker(){
        Calendar calendar = Calendar.getInstance();
        int cYear = calendar.get(Calendar.YEAR);
        int cMonth = calendar.get(Calendar.MONTH);
        int cDay = calendar.get(Calendar.DAY_OF_MONTH);

        /* SET MAX */
        DbHelper dbHelper = new DbHelper(hostName);

        dbHelper.execute("get", "MaxTemp");

        while(dbHelper.maxTemp == -1);

        NumberPicker npMax = (NumberPicker) findViewById(R.id.numPickMax);
        npMax.setMinValue(1);
        npMax.setMaxValue(50);
        npMax.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npMax.setWrapSelectorWheel(false);
        npMax.setValue(dbHelper.maxTemp);

        /* SET MIN*/
        dbHelper = new DbHelper(hostName);

        dbHelper.execute("get", "MinTemp");

        while(dbHelper.minTemp == -1);

        NumberPicker npMin = (NumberPicker) findViewById(R.id.numPickMin);
        npMin.setMinValue(1);
        npMin.setMaxValue(50);
        npMin.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npMin.setWrapSelectorWheel(false);
        npMin.setValue(dbHelper.minTemp);

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
