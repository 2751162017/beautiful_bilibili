package com.e.hookbili.garb

import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class SetGarb {

    companion object {
        private fun setField(obj: Any?, list:Array<Pair<String, Any?>>)=list.forEach { XposedHelpers.setObjectField(obj,it.first,it.second) }
        public fun setGarb(garbObj:Any, garbData: GarbEnum, lpp: XC_LoadPackage.LoadPackageParam){
            val header = XposedHelpers.findClass("com.bilibili.app.authorspace.api.BiliSpaceHeader\$Garb", lpp.classLoader).getDeclaredConstructor().newInstance()
            setField(header, arrayOf(
                Pair("fansNumber",garbData.fansNumber),
                Pair("garbId",garbData.garbId),
                Pair("imageId",garbData.imageId),
                Pair("fansLabel",garbData.fansLabel),
                Pair("smallImage",garbData.smallImage),
                Pair("largeImage",garbData.largeImage),
                Pair("jumpUrl",garbData.jumpUrl),
            ))
            setField(garbObj, arrayOf(
                Pair("imageUrl",garbData.imageUrl),
                Pair("hasGarb",garbData.hasGarb),
                Pair("showReset",garbData.showReset),
                Pair("goodsAvailable",garbData.goodsAvailable),
                Pair("garb",header) // 提交头部修改
            ))
        }
    }
}