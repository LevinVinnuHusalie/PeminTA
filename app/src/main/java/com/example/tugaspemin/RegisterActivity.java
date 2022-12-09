package com.example.tugaspemin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {
    private EditText regisName, regisEmail, regisPass;
    private Button regisBtn;
    private TextView loginUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regisName = findViewById(R.id.registerName);
        regisEmail = findViewById(R.id.registerEmail);
        regisPass = findViewById(R.id.registerPassword);
        regisBtn = findViewById(R.id.registerBtn);
        loginUser = findViewById(R.id.login);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        loginUser.setOnClickListener(v ->{
            finish();
        });
        regisBtn.setOnClickListener(v -> {
            if(regisName.getText().length()>0 && regisEmail.getText().length()>0 && regisPass.getText().length()>0){
                register(regisName.getText().toString(),regisEmail.getText().toString(),regisPass.getText().toString());
            }else{
                Toast.makeText(getApplicationContext(), "Silahkan isi semua data", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void register(String name, String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful() && task.getResult()!=null){
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    if(firebaseUser!=null) {
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                        firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                reload();
                            }
                        });
                    }else{
                        Toast.makeText(getApplicationContext(), "Register Failed", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void reload(){
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }
}