package com.tejas.storyteller.data.repository

import com.tejas.storyteller.data.network.AppApi

class MainRepository(
    private val api: AppApi
) : SafeApiRequest() {

    suspend fun getPosts() = apiRequest { api.getPosts() }

    suspend fun getPhotos() = apiRequest { api.getPhotos() }

    suspend fun getCommentsList(postId: Int) = apiRequest { api.getThreeComments(id = postId) }

}