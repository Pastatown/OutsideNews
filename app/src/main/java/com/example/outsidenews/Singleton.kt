package com.example.outsidenews

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class Singleton constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: Singleton? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Singleton(context).also {
                    INSTANCE = it
                }
            }
    }
    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }
    //Ajout de la requete
    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
}