package com.e.hookbili.garb

enum class GarbEnum(internal val garbData: GarbData) {
    Test1(
        GarbData(
            imageUrl = "https://i0.hdslb.com/bfs/garb/item/bfd6718a07968c58670253c1068bc0ef21a40014.jpg",
            garbId = "37571",
            fansLabel = "崩坏3·光辉矢愿",
            smallImage = "https://i0.hdslb.com/bfs/garb/item/0dd4fdf3c88a850325fc40cad19b9491edab97e8.jpg",
            largeImage = "https://i0.hdslb.com/bfs/garb/item/bfd6718a07968c58670253c1068bc0ef21a40014.jpg"
        )
    );




    fun setFansNumber(number: Int? = null): GarbEnum {
        garbData.fansNumber = if (number != null){
            "NO."+("0".repeat(6 - number.toString().length) + number.toString())
        }else{
            "NO.000001"
        }
        return this
    }
    val imageUrl: String
        get() = garbData.imageUrl
    val garbId: String
        get() = garbData.garbId
    val fansLabel: String
        get() = garbData.fansLabel
    val smallImage: String
        get() = garbData.smallImage
    val hasGarb: Boolean
        get() = garbData.hasGarb
    val showReset: Boolean
        get() = garbData.showReset
    val goodsAvailable: Boolean
        get() = garbData.goodsAvailable
    val fansNumber: String
        get() = garbData.fansNumber
    val imageId: String
        get() =garbData.imageId
    val largeImage:String
        get() =garbData.largeImage
    val jumpUrl: String?
        get() =garbData.jumpUrl
}