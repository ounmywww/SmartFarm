package smartfarm.com.smartfarm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    private SocketHelper sh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sh = new SocketHelper();
        sh.start();

        setTabText();

        getTemperThread();
    }

    public void onButtonClick(View view){
        switch (view.getId()) {
            case R.id.ButtonOpen :
                sh.sendMassage("Open");
                break ;
            case R.id.ButtonClose :
                sh.sendMassage("Close");
                break ;
        }
    }

    public int byteArrayToInt(byte [] b, int startIndex) {
        int n1 = (b[startIndex    ] & 0xFF);
        int n2 = (b[startIndex + 1] & 0xFF) <<  8;
        int n3 = (b[startIndex + 2] & 0xFF) << 16;
        int n4 = (b[startIndex + 3] & 0xFF) << 24;

        System.out.println("n1 = " + n1 + " n2 = " + n2 + " n3 = "+ n3 + " n4 = " + n4);

        return n1 + n2 + n3 + n4;
    }

    public void getTemperThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                // runOnUiThread를 추가하고 그 안에 UI작업을 한다.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Thread.sleep(1000);
                        }catch (Exception ex){

                        }

                        byte[] bytes = sh.getMassage();

                        int n1 = byteArrayToInt(bytes,  0);
                        int n2 = byteArrayToInt(bytes,  4);
                        int n3 = byteArrayToInt(bytes,  8);
                        int n4 = byteArrayToInt(bytes, 12);

                        /*int n1 = sh.getMassageInt();
                        int n2 = sh.getMassageInt();
                        int n3 = sh.getMassageInt();
                        int n4 = sh.getMassageInt();*/

                        System.out.println("n = " + n1);

                        TextView tvCal   = (TextView)findViewById(R.id.tvCal);
                        TextView tvWater = (TextView)findViewById(R.id.tvWater);

                        tvCal.setText(  "현재 습도 : " + Integer.toString(n1) + "." + Integer.toString(n2));
                        tvWater.setText("현재 온도 : " + Integer.toString(n3) + "." + Integer.toString(n4));
                    }
                });
            }
        }).start();
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
        NumberPicker npCal = (NumberPicker) findViewById(R.id.npCal);

        npCal.setMinValue(0);
        npCal.setMinValue(50);
    }
}
