package com.example.tugaspemin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import api.ApiConfig;
import model.AddMahasiswaResponse;
import model.MahasiswaResponse;
import model.Note;
import model.NoteResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BerandaActivity extends AppCompatActivity {

    private Button btnProfile, btnNotes;
    private TextView juduls;
    private List<Note> noteList;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);

        Intent intent = getIntent();
        String token = intent.getStringExtra("token");

        Intent prfl = new Intent(BerandaActivity.this, ProfileActivity.class);

        btnProfile = findViewById(R.id.profil);
        btnNotes = findViewById(R.id.btn_notes);
        juduls = findViewById(R.id.judulNote);
        progressBar = findViewById(R.id.progressBar);


        btnProfile.setOnClickListener(view -> {
            prfl.putExtra("token", token);
            startActivity(prfl);
        });

        btnNotes.setOnClickListener(view -> {
            Intent baru = new Intent(BerandaActivity.this, PostActivity.class);
            baru.putExtra("token", token);
            startActivity(baru);
        });

        Call<NoteResponse> client = ApiConfig.getApiService().getNote(token);
        client.enqueue(new Callback<NoteResponse>() {
            @Override
            public void onResponse(Call<NoteResponse> call, Response<NoteResponse> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        noteList = response.body().getData();
                        setData(noteList);
                    }
                } else {
                    if (response.body() != null) {
                        Log.e("", "onFailure: " + response.message());
                    }
                }
            }
            @Override
            public void onFailure(Call<NoteResponse> call, Throwable t) {
                Log.e("Error Retrofit","onFailure: " + t.getMessage());
            }
        });

    }
    private void setData(List<Note> noteList) {
        if(noteList.size()>0){
            String tmp2 = "";
            for (int i=0;i<noteList.size();i++){
                String tmp = noteList.get(i).getJudul();
                juduls.setText(tmp2 + "\n" + tmp);
                tmp2 = juduls.getText().toString();
            }
        }
//        juduls.setText(noteList.get(0).getJudul());
    }

}
