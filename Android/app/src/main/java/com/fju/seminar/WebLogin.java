package com.fju.seminar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import java.util.Random;

public class WebLogin extends AppCompatActivity {

    private TextView textView;

    static String ip = "10.211.55.4";
    static String port = "4321";
    static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    static String database = "Seminar";
    static String username = "sa";
    static String password = "0000";
    static String url = "jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + database;

    static Connection connection = null;

    private Button bQRCode;
    private Button bVFCode;
    private TextView tvLogin;
    private Activity content = this;

    TelephonyManager tm;
    static String imei;
    TextView imeitxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_login);

        //連線
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        textView = findViewById(R.id.textView2);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Class.forName(Classes);
            connection = DriverManager.getConnection(url, username, password);
            textView.setText("Chicken Key － SUCCESS");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            textView.setText("Chicken Key － ERROR");
        } catch (SQLException e) {
            e.printStackTrace();
            textView.setText("Chicken Key － FAILURE");
        }


        bQRCode = findViewById(R.id.qrcode);
        bVFCode = findViewById(R.id.verification_code);
        tvLogin = findViewById(R.id.TV_login);


        bQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(content);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("掃描QR Code");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.setOrientationLocked(false);
                integrator.initiateScan();
            }
        });

        //IMEI取得
        int permisI = ContextCompat.checkSelfPermission(WebLogin.this, Manifest.permission.READ_PHONE_STATE);

        if(permisI == PackageManager.PERMISSION_GRANTED){
            tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            imei = tm.getImei().toString();
        }
        else {
            ActivityCompat.requestPermissions(WebLogin.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 123);
        }

        bVFCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //6位驗證碼產生
                Random random = new Random();
                String result = "";
                for (int i=0;i<6;i++)
                {
                    result += random.nextInt(9)+1;
                }

                String query = "INSERT into T_LOGIN_CODE (UserID, IMEI, VerifyCode, [Time], Isvalid) values ('coolChicken', "
                        + imei
                        +", "
                        + result
                        + ", GETDATE(), null);";
                try {
                    PreparedStatement pst = connection.prepareStatement(query);
                    pst.executeUpdate();

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }


                new AlertDialog.Builder(WebLogin.this)
                        .setTitle("請於登入網頁輸入驗證碼")
                        .setMessage(result)
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
//                        .setNeutralButton("換一組驗證碼", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                    Random random = new Random();
//                                    String result="";
//                                    for (int i=0;i<6;i++)
//                                    {
//                                        result+=random.nextInt(10);
//                                    }
//                            }
//                        })
                        .create()
                        .show();

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        IntentResult SR = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if(SR != null){
            if (SR.getContents() != null){
                String SC=SR.getContents();
                if (!SC.equals("")){
                    startActivity(new Intent(WebLogin.this, QRLogin.class));
                }
            }
        }else{
            super.onActivityResult(requestCode, resultCode, intent);
            tvLogin.setText("產生錯誤");
        }
    }

}
