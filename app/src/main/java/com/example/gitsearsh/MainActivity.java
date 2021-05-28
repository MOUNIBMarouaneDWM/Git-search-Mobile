package com.example.gitsearsh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.gitsearsh.Model.GitUser;
import com.example.gitsearsh.Model.GitUsersResponse;
import com.example.gitsearsh.Model.UsersListViewModel;
import com.example.gitsearsh.Service.GitRepoServiceAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    List<GitUser> data = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);
        EditText editTextQuery = findViewById(R.id.editTextName);
        Button buttonSearch= findViewById(R.id.buttonShearch);
        ListView listViewUsers= findViewById(R.id.listViewUsers);
        UsersListViewModel listViewModel= new UsersListViewModel(this,R.layout.users_list_view_layout,data);
        listViewUsers.setAdapter(listViewModel);
        Retrofit retrofit= new  Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listViewModel.clear();
                String query= editTextQuery.getText().toString();
                GitRepoServiceAPI gitRepoServiceAPI=retrofit.create(GitRepoServiceAPI.class);
                Call<GitUsersResponse> gitUsersCall = gitRepoServiceAPI.shearchUser(query);
                gitUsersCall.enqueue(new Callback<GitUsersResponse>() {
                    @Override
                    public void onResponse(Call<GitUsersResponse> call, Response<GitUsersResponse> response) {
                    if (!response.isSuccessful()){
                        Log.i("info",String.valueOf(response.code()));
                        return;
                    }
                    GitUsersResponse gitUsersResponse = response.body();
                    for (GitUser user:gitUsersResponse.users){
                        data.add(user);
                    }

                        listViewModel.notifyDataSetChanged();

                    }

                    @Override
                    public void onFailure(Call<GitUsersResponse> call, Throwable t) {
                        Log.e("error","Error");

                    }
                });
            }
        });

        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String login = data.get(position).login;
                //Log.i("info",login);
                Intent intent = new Intent(getApplicationContext(),ReposetoryActivity.class);
                intent.putExtra("user.login",login);
                startActivity(intent);

            }
        });

    }
}