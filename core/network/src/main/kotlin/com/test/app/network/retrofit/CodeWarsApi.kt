package com.test.app.network.retrofit

import com.test.app.network.model.NetworkCodeChallengeDetail
import com.test.app.network.model.NetworkCodeChallenges
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CodeWarsApi {

    @GET("users/{user}/code-challenges/completed?page={page}")
    suspend fun getCompletedCodeChallenges(
        @Path("user") user: String,
        @Query("page") page: String
    ): NetworkCodeChallenges

    @GET("code-challenges/{challenge}")
    suspend fun getCodeChallenge(
        @Path("challenge") challenge: String
    ): NetworkCodeChallengeDetail

}
