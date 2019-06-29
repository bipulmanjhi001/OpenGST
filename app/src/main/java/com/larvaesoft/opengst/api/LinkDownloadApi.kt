package com.larvaesoft.opengst.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

interface LinkDownloadApi {

    @GET
    fun downloadUrl(@Url url:String): Observable<ResponseBody>
}