package com.example.gitsearsh.Service;

import com.example.gitsearsh.Model.GitRepository;
import com.example.gitsearsh.Model.GitUsersResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitRepoServiceAPI {
    @GET("search/users")
    public Call<GitUsersResponse> shearchUser(@Query("q") String query);
    @GET("users/{u}/repos")
    public Call<List<GitRepository>> userRepositories(@Path("u") String login);
}
