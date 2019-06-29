package com.larvaesoft.opengst.model

data class AdItem(
        override val type: String = "",
        val ad: Any? = null
) : MyItem