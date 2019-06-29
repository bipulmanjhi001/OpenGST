package com.larvaesoft.opengst.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "items_table", indices = arrayOf(Index(value = "description"), Index(value = "type")))
data class Item(
        @PrimaryKey(autoGenerate = true)
        var id: Int,
        override var type: String = "",
        var description: String = "",
        var rate: String = "",
        var hsn: String = ""
):MyItem