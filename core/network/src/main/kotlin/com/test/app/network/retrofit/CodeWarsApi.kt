package com.test.app.network.retrofit

import com.test.app.network.BuildConfig
import com.test.app.network.model.NetworkCodeChallengeDetail
import com.test.app.network.model.NetworkCodeChallenges
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CodeWarsApi {

    @GET("users/" + BuildConfig.USER_NAME + "/code-challenges/completed?page={page}")
    suspend fun getCompletedCodeChallenges(
        @Query("page") page: Int
    ): NetworkCodeChallenges

    @GET("code-challenges/{challenge}")
    suspend fun getCodeChallenge(
        @Path("challenge") challengeId: String
    ): NetworkCodeChallengeDetail

}
