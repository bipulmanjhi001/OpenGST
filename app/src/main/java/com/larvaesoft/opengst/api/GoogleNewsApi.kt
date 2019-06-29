package com.larvaesoft.opengst.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleNewsApi {

    @GET("https://news.google.com/news/rss/search/section/q/gst/gst?hl=en-IN&gl=IN&ned=in")
    fun getGoogleNews(): Observable<ResponseBody>
}