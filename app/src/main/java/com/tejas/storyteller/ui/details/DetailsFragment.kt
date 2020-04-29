package com.tejas.storyteller.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tejas.storyteller.R
import com.tejas.storyteller.data.network.AppApi
import com.tejas.storyteller.data.network.NetworkConnectionInterceptor
import com.tejas.storyteller.data.repository.MainRepository
import com.tejas.storyteller.ui.post.PostViewModelFactory
import com.tejas.storyteller.ui.post.PostsViewModel
import kotlinx.android.synthetic.main.comment_view.view.*
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment() {

    private lateinit var factory: PostViewModelFactory
    private lateinit var viewModel: PostsViewModel

    private var position: Int = -1
    private var title: String? = null
    private var imageUrl: String? = null
    private var body: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        position = arguments?.getInt(ARGS_POSITION, -1) ?: return
        if (position < 0) return

        //imageUrl = arguments?.getString(ARGS_IMAGE_URL, "") ?: return
        //Picasso.with(context).load(imageUrl).into(postImage)

        title = arguments?.getString(ARGS_TITLE_TEXT, "") ?: return
        titleText.text = title ?: return

        body = arguments?.getString(ARGS_BODY_TEXT, "") ?: return
        bodyText.text = body ?: return

        val api = AppApi(NetworkConnectionInterceptor(requireContext()))
        val repository = MainRepository(api)

        factory = PostViewModelFactory(repository)
        viewModel = ViewModelProviders.of(this, factory).get(PostsViewModel::class.java)

        viewModel.getComments(position)

        showComments()
    }

    private fun showComments() {
        viewModel.comment.observe(viewLifecycleOwner, Observer { comment ->
            comment?.let {

                commentOne.commentTitleText.text = comment[0].name
                commentOne.commentBodyText.text = comment[0].body
                commentOne.commentIdText.text =
                    getString(R.string.commentId, comment[0].id.toString())

                commentTwo.commentTitleText.text = comment[1].name
                commentTwo.commentBodyText.text = comment[1].body
                commentTwo.commentIdText.text =
                    getString(R.string.commentId, comment[1].id.toString())

                commentThree.commentTitleText.text = comment[2].name
                commentThree.commentBodyText.text = comment[2].body
                commentThree.commentIdText.text =
                    getString(R.string.commentId, comment[2].id.toString())
            }
        })
    }

    companion object {
        private const val ARGS_POSITION = "args_position"
        private const val ARGS_TITLE_TEXT = "args_title_text"
        private const val ARGS_IMAGE_URL = "args_image_url"
        private const val ARGS_BODY_TEXT = "args_body_text"
    }
}
