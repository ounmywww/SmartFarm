package smartfarm.com.smartfarm;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by macbook on 12/05/2020.
 */

public class DbHelper extends AsyncTask<String, Void, String>{
    private static String TAG = "phptest";
    private String mJsonString;

    public ArrayList<Pair<Float, Float>> mTempList;
    public ArrayList<Pair<Float, Float>> mHumidityList;
    public ArrayList<Pair<Float, Integer>> mOpenList;

    public float recentTemp;
    public float recentHumidity;

    public String recentAutoYn;

    String serverURL = "";
    String postParameters = "";

    String errorString = null;

    DbHelper(String hostUrl){
        serverURL = new String();
        postParameters = new String();

        mTempList = new ArrayList<Pair<Float, Float>>();
        mHumidityList = new ArrayList<Pair<Float, Float>>();
        mOpenList = new ArrayList<Pair<Float, Integer>>();

        this.serverURL = hostUrl;
    }


    public String doInBackground(String... params) {

        String gubun = params[0];
        String data = params[1];

        try
        {
            URL url = new URL("http://" + serverURL + "/" + gubun + data + ".php");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();


            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(postParameters.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();


            int responseStatusCode = httpURLConnection.getResponseCode();
            Log.d(TAG, "response code - " + responseStatusCode);

            InputStream inputStream;
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
            } else {
                inputStream = httpURLConnection.getErrorStream();
            }


            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            bufferedReader.close();

            mJsonString = sb.toString().trim();

            // 데이터를 가져올 때만 이구문을 탄다
            if(gubun.equals("get")){
                makeArray(data);
            }

            return "1";
        }
        catch (Exception e)
        {
            Log.d(TAG, "GetData : Error ", e);
            errorString = e.toString();

            return "0";
        }
    }

    private void makeArray(String data){

        mTempList.clear();
        mHumidityList.clear();
        mOpenList.clear();

        // 온도 데이터 가져오기
        if(data.equals("Temp")) {
            String TAG_JSON = "webnautes";
            String TAG_HOURS = "HOURS";
            String TAG_TEMP = "TEMP";
            String TAG_HUMIDITY = "HUMIDITY";
            String TAG_CNT_OPEN = "CNT_OPEN";


            try {
                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject item = jsonArray.getJSONObject(i);

                    Float hours = Float.parseFloat(item.getString(TAG_HOURS).toString());
                    Float temp = Float.parseFloat(item.getString(TAG_TEMP));
                    Float humidity = Float.parseFloat(item.getString(TAG_HUMIDITY));
                    Integer cntOpen = Integer.parseInt(item.getString(TAG_CNT_OPEN));

                    mTempList.add(new Pair<Float, Float>(hours, temp));
                    mHumidityList.add(new Pair<Float, Float>(hours, humidity));
                    mOpenList.add(new Pair<Float, Integer>(hours, cntOpen));
                }

            } catch (JSONException e) {

                Log.d(TAG, "showResult : ", e);
            }
        }
        else if(data.equals("RecentTemp")){
            String TAG_JSON = "webnautes";
            String TAG_TEMP = "TEMP";

            try {
                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject item = jsonArray.getJSONObject(i);

                    Float temp = Float.parseFloat(item.getString(TAG_TEMP));

                    recentTemp = temp;
                }

            } catch (JSONException e) {

                Log.d(TAG, "showResult : ", e);
            }
        }
        else if(data.equals("RecentHum")){
            String TAG_JSON = "webnautes";
            String TAG_HUMIDITY = "Humidity";

            try {
                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject item = jsonArray.getJSONObject(i);

                    Float humidity = Float.parseFloat(item.getString(TAG_HUMIDITY));

                    recentHumidity = humidity;
                }

            } catch (JSONException e) {

                Log.d(TAG, "showResult : ", e);
            }
        }
        else if(data.equals("AutoYn")){
            String TAG_JSON = "webnautes";
            String TAG_AUTOYN = "AutoYn";

            try {
                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject item = jsonArray.getJSONObject(i);

                    String AutoYn = item.getString(TAG_AUTOYN).toString();

                    recentAutoYn = AutoYn;
                }

            } catch (JSONException e) {

                Log.d(TAG, "showResult : ", e);
            }
        }

    }

}
