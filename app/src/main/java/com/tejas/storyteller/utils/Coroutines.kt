package com.tejas.storyteller.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object Coroutines {

    fun <T : Any> ioThenMain(work: suspend (() -> T?), callback: ((T?) -> Unit)) =
        CoroutineScope(Dispatchers.Main).launch {
            val data = CoroutineScope(Dispatchers.IO).async rt@{
                return@rt work()
            }.await()
            callback(data)
        }


    fun <T : Any> ioThenMain1(
        work1: suspend (() -> T?),
        work2: suspend (() -> T?),
        callback: ((T?, T?) -> Unit)
    ) =
        CoroutineScope(Dispatchers.Main).launch {
            val data1 = CoroutineScope(Dispatchers.IO).async rt@{
                return@rt work1()
            }.await()
            val data2 = CoroutineScope(Dispatchers.IO).async rt@{
                return@rt work2()
            }.await()
            callback(data1, data2)
        }

}