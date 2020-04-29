package com.tejas.storyteller.ui.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tejas.storyteller.R
import com.tejas.storyteller.data.models.PostsAndPhotos
import com.tejas.storyteller.databinding.RecyclerviewPostItemsBinding

class PostsAdapter(
    private val combineList: PostsAndPhotos,
    private val listener: RecyclerViewClickListener
) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    override fun getItemCount() = combineList.posts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PostViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.recyclerview_post_items,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.recyclerviewPostItemsBinding.post = combineList.posts[position]
        holder.recyclerviewPostItemsBinding.photos = combineList.photos[position]
        holder.recyclerviewPostItemsBinding.root.setOnClickListener {
            listener.onRecyclerViewItemClick(
                holder.recyclerviewPostItemsBinding.body,
                combineList.posts[position],
                position
            )
        }
    }

    inner class PostViewHolder(
        val recyclerviewPostItemsBinding: RecyclerviewPostItemsBinding
    ) : RecyclerView.ViewHolder(recyclerviewPostItemsBinding.root)

}