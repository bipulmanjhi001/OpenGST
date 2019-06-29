package com.larvaesoft.opengst.model

import java.util.*

data class Article(
        var title: String="",
        var author: String="",
        var link: String="",
        var pubDate: Date? =null,
        var description: String="",
        var content: String="",
        var image: String="",
        var source:String=""
)