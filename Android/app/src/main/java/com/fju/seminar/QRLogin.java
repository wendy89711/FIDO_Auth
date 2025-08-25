package com.fju.seminar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static com.fju.seminar.WebLogin.Classes;
import static com.fju.seminar.WebLogin.password;
import static com.fju.seminar.WebLogin.url;
import static com.fju.seminar.WebLogin.username;

public class QRLogin extends AppCompatActivity {

    private static final String TAG = "QrLogin";
    private Button btnQRLogin;
    private TextView textView;

    private Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_login);

        Uri uri = Uri.parse("https://github.com/g8babe");
        startActivity(new Intent(Intent.ACTION_VIEW,uri));

        //連線
        textView = findViewById(R.id.textView10);

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
        //

        btnQRLogin = findViewById(R.id.qrlogin);

        btnQRLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = "UPDATE T_LOGIN_QR SET Isvalid = 1 where [Time] = (select max([Time]) from T_LOGIN_QR where UserID = 'coolChicken')";
                try {
                    PreparedStatement pst = connection.prepareStatement(query);
                    pst.executeUpdate();

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }
}