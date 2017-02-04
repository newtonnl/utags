package zuk.toast;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.Toast;

import static android.provider.Settings.*;
import static android.provider.Settings.System.SCREEN_BRIGHTNESS;

public class ToastActivity extends AppCompatActivity {
    String info_toast="INIT";
    private final String TAG = "toast";
    private SensorManager mSensorManager;
    private Sensor mLight;

    public ToastUtils t1Util;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast);
/*
        Toast t1= Toast.makeText(getApplicationContext(),
                info_toast,Toast.LENGTH_LONG);
        t1.setGravity(Gravity.CENTER,0,0);
        t1.show();*/

        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);


    }

    private SensorEventListener mListener = new SensorEventListener() {


        @Override
        public void onSensorChanged(SensorEvent event) {


            Sensor sensor = event.sensor;
            int brightValue = 0;

            if(sensor == mLight){

                //info_toast = String.valueOf(event.values[0]);
                brightValue = getScreenBrightness();

                info_toast = String.valueOf(brightValue) + "|"+ String.valueOf(event.values[0]);
                t1Util.showToast(getApplicationContext(),info_toast);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    public boolean onKeyDown(int keycode, KeyEvent event){
        Log.i(TAG,"key_structa = "+event.getAction());
        if(keycode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            moveTaskToBack(true);
        }

        mSensorManager.registerListener(mListener,mLight,SensorManager.SENSOR_DELAY_FASTEST);

        return true;

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mSensorManager.unregisterListener(mListener);
    }

    public int getScreenBrightness(){
        int value = 0;
        ContentResolver cr = getContentResolver();

        try{
            value = Settings.System.getInt(cr,Settings.System.SCREEN_BRIGHTNESS);
        }catch(SettingNotFoundException e){
            Log.i(TAG,"error ");
        }
        return value;
    }
}
