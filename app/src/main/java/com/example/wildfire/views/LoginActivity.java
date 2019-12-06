package com.example.wildfire.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.wildfire.R;
import com.example.wildfire.models.LoginView;
import com.example.wildfire.viewmodels.CoordinadorViewModel;

public class LoginActivity extends AppCompatActivity {

    private CoordinadorViewModel coordinadorViewModel;
    private EditText etMail, etClave;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponents();
    }

    private void initComponents() {
        coordinadorViewModel = ViewModelProviders.of(this).get(CoordinadorViewModel.class);
        coordinadorViewModel.getToken().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        etMail = findViewById(R.id.etMail);
        etClave = findViewById(R.id.etClave);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginView loginView = new LoginView(etMail.getText().toString(), etClave.getText().toString());
                coordinadorViewModel.login(loginView);
            }
        });
    }
}
