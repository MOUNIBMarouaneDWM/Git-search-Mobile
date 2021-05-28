package com.example.gitsearsh;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gitsearsh.Model.GitRepository;
import com.example.gitsearsh.Service.GitRepoServiceAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReposetoryActivity extends AppCompatActivity {

    List<String> data = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable  Bundle savedInstanceState) {
        setTitle("Repositores");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reposetory_layout);
        Intent intent= getIntent();
        String login= intent.getStringExtra("user.login");
        TextView textViewLogin= findViewById(R.id.textViewRepositoryLogin);
        ListView listViewReposotories= findViewById(R.id.listViewReposetories);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,data);

        textViewLogin.setText(login);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GitRepoServiceAPI gitRepoServiceAPI = retrofit.create(GitRepoServiceAPI.class);
        Call<List<GitRepository>> repoCall= gitRepoServiceAPI.userRepositories(login);
        repoCall.enqueue(new Callback<List<GitRepository>>() {
            @Override
            public void onResponse(Call<List<GitRepository>> call, Response<List<GitRepository>> response) {
                if (!response.isSuccessful()){
                    Log.i("info",String.valueOf(response.code()));
                    return;
                }
                List<GitRepository> gitRepositories = response.body();
                for (GitRepository gitRepository:gitRepositories){
                    String content="";
                    content+=gitRepository.id+"\n";
                    content+=gitRepository.name+"\n";
                    content+=gitRepository.language+"\n";
                    content+=gitRepository.size+"\n";
                    data.add(content);

                }
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<GitRepository>> call, Throwable t) {

            }
        });
    }
}
