package com.e.hookbili
import android.util.Log
import com.e.hookbili.garb.GarbEnum
import com.e.hookbili.garb.SetGarb
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import java.net.URL

class Main : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: LoadPackageParam) {

        if (lpparam.packageName == "tv.danmaku.bili")
        {
            try{
                val biliApiDataCallbackClass = lpparam.classLoader.loadClass("com.bilibili.okretro.BiliApiDataCallback")
                XposedBridge.hookAllMethods(biliApiDataCallbackClass, "onResponse", object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam?) {
                        super.beforeHookedMethod(param)
                        var url:String? = null // 当前请求url
                        var data:Any? = null // 网络请求返回值

                        param?.let { it ->
                            val response = it.args[1]
                            val rawMethod = response.javaClass.getMethod("raw").invoke(response)
                            val request = rawMethod.javaClass.getMethod("request").invoke(rawMethod)
                            url = request.javaClass.getMethod("url").invoke(request)?.toString()
                            val responseBody = response.javaClass.getMethod("body").invoke(response)
                            (response.javaClass.getMethod("isSuccessful").invoke(response) as Boolean).let {if (!it)return }
                            val dataField = responseBody.javaClass.getDeclaredField("data")
                            dataField.isAccessible = true
                            data = dataField.get(responseBody)
                        } //初始化

                        when(URL(url).path){
                             "/x/v2/feed/index"->{ //主页相关的修改
                                 /*
                                 * 删除主页广告
                                 * */
                                 val items = XposedHelpers.getObjectField(data,"items") as ArrayList<*>
                                 items.removeIf { XposedHelpers.getObjectField(it, "adInfo") != null } //删除被标记为广告的项
                                 XposedHelpers.setObjectField(data,"items",items)
                            }
                            "/x/resource/show/skin"->{ //皮肤设置,有待完善
//                                log(data.toString())
//                                 val userGarb = XposedHelpers.getObjectField(data,"userGarb")
//                                 val id = XposedHelpers.getObjectField(userGarb,"id")
//                                XposedHelpers.setObjectField(userGarb,"id","1")
//                                log(items)
//                                testField()
                            }
                            "/x/v2/space"->{ //个人空间配置


                                val header = XposedHelpers.getObjectField(data,"header")
//                                val grab = XposedHelpers.getObjectField(header,"garb") // 皮肤
//                                val num = XposedHelpers.getObjectField(grab,"fansNumber")
//
//                                XposedHelpers.setObjectField(header,"imageUrl","https://i0.hdslb.com/bfs/garb/item/bfd6718a07968c58670253c1068bc0ef21a40014.jpg") // 主页头图
//                                XposedHelpers.setObjectField(header,"hasGarb",true) // 标记为已有皮肤
//                                XposedHelpers.setObjectField(header,"showReset",true) // 重置显示？没搞懂
//                                XposedHelpers.setObjectField(header,"goodsAvailable",false) // 标记为绝版皮肤，可能能减少广告？
//                                val constructor = XposedHelpers.findClass("com.bilibili.app.authorspace.api.BiliSpaceHeader\$Garb", lpparam.classLoader).getDeclaredConstructor().newInstance()
//
//                                XposedHelpers.setObjectField(constructor,"fansNumber","NO.000001")   // 修改粉丝牌编号
//                                XposedHelpers.setObjectField(constructor,"garbId","37571")   // 装扮id ，固定值
//                                XposedHelpers.setObjectField(constructor,"imageId","3")   // 未知
//                                XposedHelpers.setObjectField(constructor,"fansLabel","崩坏3·光辉矢愿")   // 未知
//                                XposedHelpers.setObjectField(constructor,"smallImage","https://i0.hdslb.com/bfs/garb/item/0dd4fdf3c88a850325fc40cad19b9491edab97e8.jpg")   // 图片URL
//                                XposedHelpers.setObjectField(constructor,"largeImage","https://i0.hdslb.com/bfs/garb/item/bfd6718a07968c58670253c1068bc0ef21a40014.jpg")   // 图片URL
//                                XposedHelpers.setObjectField(constructor,"jumpUrl",null)   // 删除右下角跳转按钮
//                                setField(constructor,arrayOf(arrayOf("a","a")))


                                SetGarb.setGarb(header, GarbEnum.Test1.setFansNumber(114514),lpparam)

//                                XposedHelpers.setObjectField(header,"garb",constructor) // 应用修改

//                                testField(data, arrayOf("SpaceActivity","SourceContent","BiliSpaceHeader"))

//                                log(grab.toString())
                            }
//                            else -> log("HOST:$url")

                        }

                        //提交修改
                        param?.let{it.thisObject.javaClass.getDeclaredMethod("onDataSuccess", Object::class.java).invoke(it.thisObject, data);it.result = null}
                    }
                })
            }catch (e: Exception){
                e.message?.let { log(it) }
            }
        }

    }
    private fun testField(obj: Any?,list:Array<String>): Unit = list.forEach {i:String ->XposedBridge.log("GET VALUE FOR $i: ${XposedHelpers.getObjectField(obj,i)}");log("GET VALUE FOR $i: ${XposedHelpers.getObjectField(obj,i)}") }
    private fun log(msg:String):Int = Log.d("BILIHOOK", msg)
    private fun log():Int = Log.d("BILIHOOK", "Null")
}
