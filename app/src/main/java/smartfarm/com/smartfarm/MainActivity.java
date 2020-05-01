package smartfarm.com.smartfarm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SocketHelper sh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sh = new SocketHelper();
        sh.start();
    }

    public void onButtonClick(View view){
        TextView textView1 = (TextView) findViewById(R.id.TvTemp);
        switch (view.getId()) {
            case R.id.ButtonOpen :
                sh.sendMassage("Open");
                break ;
            case R.id.ButtonClose :
                sh.sendMassage("Close");
                break ;
        }
    }
}
