package com.tejas.storyteller.ui.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tejas.storyteller.data.models.Comment
import com.tejas.storyteller.data.models.Photos
import com.tejas.storyteller.data.models.Posts
import com.tejas.storyteller.data.models.PostsAndPhotos
import com.tejas.storyteller.data.repository.MainRepository
import com.tejas.storyteller.utils.setOrPost
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PostsViewModel(
    private val repository: MainRepository
) : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = job + Dispatchers.Main
    private val COMMENTS_NUMBER = 3
    /*private lateinit var job: Job

    private val _post = MutableLiveData<List<Posts>>()
    val post: LiveData<List<Posts>>
        get() = _post

    fun getPost() {
        job = Coroutines.ioThenMain(
            { repository.getPosts() },
            { _post.value = it }
        )
    }

    private val _photos = MutableLiveData<List<Photos>>()
    val photos: LiveData<List<Photos>>
        get() = _photos

    fun getPhotos() {
        job = Coroutines.ioThenMain(
            { repository.getPhotos() },
            { _photos.value = it }
        )
    }*/


    private val postListLiveData = MutableLiveData<List<Posts>>()
    private val photoListLiveData = MutableLiveData<List<Photos>>()

    val postAndPhotoListLiveData = MutableLiveData<PostsAndPhotos>()

    val isLoadingPostsAndPhotos = MutableLiveData<Boolean>().apply { value = true }
    val isTimeoutPostsAndPhotos = MutableLiveData<Boolean>().apply { value = false }

    fun getPostsAndPhotos() {
        launch {
            withContext(Dispatchers.IO) {
                val latestPosts = repository.getPosts()
                val latestPhotos = repository.getPhotos()
                if (!latestPosts.isNullOrEmpty() && !latestPhotos.isNullOrEmpty()) {
                    isLoadingPostsAndPhotos.setOrPost(false)

                    postListLiveData.setOrPost(latestPosts)
                    photoListLiveData.setOrPost(latestPhotos)

                    postAndPhotoListLiveData.setOrPost(PostsAndPhotos(latestPosts, latestPhotos))
                } else {
                    //delay(TIMEOUT_LIMIT)
                    isLoadingPostsAndPhotos.setOrPost(false)
                    isTimeoutPostsAndPhotos.setOrPost(true)
                }
            }
        }
    }

    /*override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }*/

    var selectedPostIdLiveData = MutableLiveData<Int?>()
    private val _currentScrollingPositionLiveData = MutableLiveData<Int>()
    val currentScrollingPositionLiveData = _currentScrollingPositionLiveData
    fun getScrollingPosition(pos: Int) {
        _currentScrollingPositionLiveData.setOrPost(pos)
    }

    private val _comments = MutableLiveData<List<Comment>>()
    val comment: LiveData<List<Comment>>
        get() = _comments

    fun getComments(postId: Int) {
        launch {
            withContext(Dispatchers.IO) {
                val latestComments = repository.getCommentsList(postId)

                if (!latestComments.isNullOrEmpty()) {
                    //isLoadingComments.setOrPost(false)
                    _comments.setOrPost(latestComments.take(COMMENTS_NUMBER).toList())
                } else {

                    //delay(TIMEOUT_LIMIT)
                    //isLoadingComments.setOrPost(false)
                    //isTimeoutComments.setOrPost(true)
                }
            }
        }
    }
}