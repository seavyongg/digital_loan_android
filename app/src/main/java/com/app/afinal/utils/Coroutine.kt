package com.app.afinal.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback

object Coroutine {
    fun main (
       work : suspend (() -> Unit)) =
           CoroutineScope(Dispatchers.Main).launch {
                work()
           }

    fun < T : Any> ioThanMain(
        work: suspend () -> T?,
        callback: suspend (T?) -> Unit
    )= CoroutineScope(Dispatchers.Main).launch {
        val data = CoroutineScope(Dispatchers.IO).async {
            return@async work()
        }. await()
        callback(data)
    }

}