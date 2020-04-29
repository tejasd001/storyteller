package com.tejas.storyteller.ui.post

import android.view.View
import com.tejas.storyteller.data.models.Posts

interface RecyclerViewClickListener {
    fun onRecyclerViewItemClick(view: View, posts: Posts, position: Int)
}