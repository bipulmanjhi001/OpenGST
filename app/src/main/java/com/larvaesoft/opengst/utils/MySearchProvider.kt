package com.larvaesoft.opengst.utils

import android.content.SearchRecentSuggestionsProvider

class MySearchProvider:SearchRecentSuggestionsProvider() {
    val AUTHORITY = "com.larvaesoft.opengst.utils.MySearchProvider"
    val MODE = SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES
    init {
        setupSuggestions(AUTHORITY,MODE)
    }
}
