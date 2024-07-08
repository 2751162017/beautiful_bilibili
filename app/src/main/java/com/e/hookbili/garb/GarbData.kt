package com.e.hookbili.garb

data class GarbData(
    val imageUrl: String,
    val hasGarb: Boolean = true,
    val showReset: Boolean = true,
    val goodsAvailable: Boolean = false,
    var fansNumber:String = "NO.000001",
    val garbId: String,
    val imageId: String = "3",
    val fansLabel: String,
    val smallImage:String,
    val largeImage:String = smallImage,
    val jumpUrl: String? = null
)
