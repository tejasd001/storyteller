package com.tejas.storyteller.ui.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tejas.storyteller.R
import com.tejas.storyteller.data.models.Posts
import com.tejas.storyteller.data.network.AppApi
import com.tejas.storyteller.data.network.NetworkConnectionInterceptor
import com.tejas.storyteller.data.repository.MainRepository
import com.tejas.storyteller.utils.setOrPost
import kotlinx.android.synthetic.main.posts_fragment.*

class PostsFragment : Fragment(), RecyclerViewClickListener {

    private lateinit var factory: PostViewModelFactory
    private lateinit var viewModel: PostsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.posts_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val api = AppApi(NetworkConnectionInterceptor(requireContext()))
        val repository = MainRepository(api)

        factory = PostViewModelFactory(repository)
        viewModel = ViewModelProviders.of(this, factory).get(PostsViewModel::class.java)

        viewModel.getPostsAndPhotos()

        viewModel.postAndPhotoListLiveData.observe(viewLifecycleOwner, Observer { combineList ->
            recycler_view_posts.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.setHasFixedSize(true)

                viewModel.currentScrollingPositionLiveData.observe(
                    viewLifecycleOwner,
                    Observer { LastShownPosition ->
                        (recycler_view_posts.layoutManager as LinearLayoutManager).scrollToPosition(
                            LastShownPosition
                        )
                    })

                recycler_view_posts.adapter = PostsAdapter(combineList, this)
            }
        })

    }

    override fun onRecyclerViewItemClick(view: View, posts: Posts, position: Int) {
        when (view.id) {
            R.id.body -> {
                viewModel.selectedPostIdLiveData.setOrPost(posts.id)
                viewModel.getScrollingPosition(position)
                val bundle = Bundle()
                bundle.putInt("args_position", position)
                bundle.putString("args_title_text", posts.title)
                bundle.putString("args_image_url", "")
                bundle.putString("args_body_text", posts.body)

                this.findNavController()
                    .navigate(R.id.action_postFragment_to_detailsFragment, bundle)
            }
        }
    }
}

