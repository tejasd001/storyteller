package com.tejas.storyteller.utils

import android.os.Looper
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.tejas.storyteller.data.models.Photos
import com.tejas.storyteller.data.models.Posts

fun <T> MutableLiveData<T>.setOrPost(t: T) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        value = t
    } else {
        postValue(t)
    }
}

fun View?.setVisibleOrGone(show: Boolean) {
    if (show) {
        this?.visibility = View.VISIBLE
    } else {
        this?.visibility = View.GONE
    }
}