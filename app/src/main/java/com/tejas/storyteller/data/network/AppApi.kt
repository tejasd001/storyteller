package com.tejas.storyteller.data.network

import com.tejas.storyteller.data.models.Comment
import com.tejas.storyteller.data.models.Photos
import com.tejas.storyteller.data.models.Posts
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface AppApi {

    @GET("posts")
    suspend fun getPosts(): Response<List<Posts>>

    @GET("photos")
    suspend fun getPhotos(): Response<List<Photos>>

    @GET("posts/{id}/comments")
    suspend fun getThreeComments(@Path("id") id: Int): Response<List<Comment>>


    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): AppApi {

            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .build()
                .create(AppApi::class.java)
        }
    }
}