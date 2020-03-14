package com.example.doujineventapp

import java.io.Serializable

data class Circle  (
    val id: Int,
    var penName: String,
    var circleName: String,
    var space: String,
    var price: Int,
    var giftExists: Boolean,
    var note: String,
    var isChecked: Boolean
) : Serializable