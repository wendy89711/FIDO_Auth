package com.fju.seminar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class Biometrics extends AppCompatActivity {

    private TextView authStatusTv;
    private Button bFinger;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometrics);

        authStatusTv = findViewById(R.id.authStatusTv);
        bFinger = findViewById(R.id.finger);

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(Biometrics.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                authStatusTv.setText("認證錯誤： " + errString);
                Toast.makeText(Biometrics.this, "認證錯誤： " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                authStatusTv.setText("認證成功！");
                startActivity(new Intent(Biometrics.this, WebLogin.class));
                Toast.makeText(Biometrics.this, "認證成功！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                authStatusTv.setText("認證失敗！");
                Toast.makeText(Biometrics.this, "認證失敗！", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("生物驗證")
                .setSubtitle("使用指紋辨識登入")
                .setNegativeButtonText("使用App密碼")
                .build();

        bFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });
    }
}
