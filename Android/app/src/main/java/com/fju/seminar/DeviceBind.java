package com.fju.seminar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeviceBind extends AppCompatActivity {

    private Button bBinding;
//    private Activity context=this;
    private TextView textView;
    private Connection connection;

    TelephonyManager tm;
    static String imei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_bind);
        //連線
        textView = findViewById(R.id.textView6);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Class.forName(WebLogin.Classes);
            connection = DriverManager.getConnection(WebLogin.url, WebLogin.username, WebLogin.password);
            textView.setText("Chicken Key － SUCCESS");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            textView.setText("Chicken Key － ERROR");
        } catch (SQLException e) {
            e.printStackTrace();
            textView.setText("Chicken Key － FAILURE");
        }
        //

        bBinding = findViewById(R.id.binding);

        //IMEI取得
        int permisI = ContextCompat.checkSelfPermission(DeviceBind.this, Manifest.permission.READ_PHONE_STATE);

        if(permisI == PackageManager.PERMISSION_GRANTED){
            tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            imei = tm.getImei().toString();
        }
        else {
            ActivityCompat.requestPermissions(DeviceBind.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 123);
        }

        bBinding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = "INSERT into T_USER_DV (UserID, DvNo, IMEI, [Time], Isvalid) values ('coolChicken', 1, "
                        + imei
                        + ", GETDATE(), null)";
                try {
                    PreparedStatement pst = connection.prepareStatement(query);
                    pst.executeUpdate();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                startActivity(new Intent(DeviceBind.this, Biometrics.class));
            }
        });
    }


//    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//
//        IntentResult SR = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
//
//        if(SR != null){
//            if (SR.getContents() != null){
//                String SC=SR.getContents();
//                if (!SC.equals("")){
//                    tvBinding.setText(SC.toString());
//                }
//            }
//        }else{
//            super.onActivityResult(requestCode, resultCode, intent);
//            tvBinding.setText("產生錯誤");
//        }
//    }
}
