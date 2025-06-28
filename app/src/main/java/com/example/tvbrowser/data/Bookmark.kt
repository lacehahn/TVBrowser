package com.example.tvbrowser.data

import android.content.Context
import android.util.Log
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.IOException

@Serializable
data class Bookmark(
    val title: String,
    val url: String,
    val description: String,
    val plugin: String? = null // 可选的插件名称
)

@Serializable
data class ChannelData(
    val channels: List<Bookmark>
)

@Serializable
data class Plugin(
    val name: String,
    val description: String,
    val version: String,
    val script: String,
    val runOn: String = "onPageFinished", // onPageFinished, onPageStarted, onDOMReady
    val enabled: Boolean = true
)

@Serializable
data class PluginData(
    val plugins: Map<String, Plugin>
)

// 从JSON文件加载频道列表
fun loadChannelsFromJson(context: Context): List<Bookmark> {
    return try {
        val jsonString = context.assets.open("channels.json").bufferedReader().use { it.readText() }
        val channelData = Json.decodeFromString<ChannelData>(jsonString)
        Log.d("ChannelLoader", "Successfully loaded ${channelData.channels.size} channels from JSON")
        channelData.channels
    } catch (e: IOException) {
        Log.e("ChannelLoader", "Error reading channels.json", e)
        getDefaultChannels()
    } catch (e: Exception) {
        Log.e("ChannelLoader", "Error parsing channels.json", e)
        getDefaultChannels()
    }
}

// 从JSON文件加载插件配置
fun loadPluginsFromJson(context: Context): Map<String, Plugin> {
    return try {
        val jsonString = context.assets.open("plugins.json").bufferedReader().use { it.readText() }
        val pluginData = Json.decodeFromString<PluginData>(jsonString)
        Log.d("PluginLoader", "Successfully loaded ${pluginData.plugins.size} plugins from JSON")
        pluginData.plugins
    } catch (e: IOException) {
        Log.e("PluginLoader", "Error reading plugins.json", e)
        emptyMap()
    } catch (e: Exception) {
        Log.e("PluginLoader", "Error parsing plugins.json", e)
        emptyMap()
    }
}

// 加载插件脚本内容
fun loadPluginScript(context: Context, scriptName: String): String? {
    return try {
        context.assets.open("plugins/$scriptName").bufferedReader().use { it.readText() }
    } catch (e: IOException) {
        Log.e("PluginLoader", "Error reading plugin script: $scriptName", e)
        null
    }
}

// 备用的预制书签列表 - CCTV央视频道直播（如果JSON加载失败时使用）
private fun getDefaultChannels() = listOf(
    Bookmark("CCTV 1", "https://tv.cctv.com/live/cctv1/?spm=C28340.PotG4tICJnBr.ExidtyEJcS5K.1", "综合频道", "cctv_cleaner"),
    Bookmark("CCTV 2", "https://tv.cctv.com/live/cctv2/?spm=C28340.PoakR0uYBKgp.ExidtyEJcS5K.13", "财经频道", "cctv_cleaner"),
    Bookmark("CCTV 3", "https://tv.cctv.com/live/cctv3/?spm=C28340.PotG4tICJnBr.ExidtyEJcS5K.14", "综艺频道", "cctv_cleaner"),
    Bookmark("CCTV 4", "https://tv.cctv.com/live/cctv4/?spm=C28340.PKIUk5AmVMT8.ExidtyEJcS5K.15", "中文国际频道", "cctv_cleaner"),
    Bookmark("CCTV 5", "https://tv.cctv.com/live/cctv5/?spm=C28340.PR1Y0pj2YGeB.ExidtyEJcS5K.5", "体育频道", "cctv_cleaner"),
    Bookmark("CCTV 5+", "https://tv.cctv.com/live/cctv5plus/?spm=C28340.P8YyNiekrjS1.ExidtyEJcS5K.17", "体育赛事频道", "cctv_cleaner"),
    Bookmark("CCTV 6", "https://tv.cctv.com/live/cctv6/?spm=C28340.P8YyNiekrjS1.ExidtyEJcS5K.18", "电影频道", "cctv_cleaner"),
    Bookmark("CCTV 7", "https://tv.cctv.com/live/cctv7/?spm=C28340.PTEkg7XmjUgN.ExidtyEJcS5K.19", "国防军事频道", "cctv_cleaner"),
    Bookmark("CCTV 8", "https://tv.cctv.com/live/cctv8/?spm=C28340.PkZipr7VceJI.ExidtyEJcS5K.20", "电视剧频道", "cctv_cleaner"),
    Bookmark("CCTV 9", "https://tv.cctv.com/live/cctvjilu/?spm=C28340.PQEMFbwOfB3S.ExidtyEJcS5K.21", "纪录频道", "cctv_cleaner"),
    Bookmark("CCTV 10", "https://tv.cctv.com/live/cctv10/?spm=C28340.PpiaYs0TLjHK.ExidtyEJcS5K.22", "科教频道", "cctv_cleaner"),
    Bookmark("CCTV 11", "https://tv.cctv.com/live/cctv11/?spm=C28340.PMvpCBX0AtTu.ExidtyEJcS5K.23", "戏曲频道", "cctv_cleaner"),
    Bookmark("CCTV 12", "https://tv.cctv.com/live/cctv12/?spm=C28340.PrG0h7df8V0a.ExidtyEJcS5K.24", "社会与法频道", "cctv_cleaner"),
    Bookmark("CCTV 13", "https://tv.cctv.com/live/cctv13/?spm=C28340.Pi47lDigOTVF.ExidtyEJcS5K.25", "新闻频道", "cctv_cleaner"),
    Bookmark("CCTV 14", "https://tv.cctv.com/live/cctvchild/?spm=C28340.POZbQEOQYmoL.ExidtyEJcS5K.26", "少儿频道", "cctv_cleaner"),
    Bookmark("CCTV 15", "https://tv.cctv.com/live/cctv15/?spm=C28340.PlzJnpThRAqb.ExidtyEJcS5K.27", "音乐频道", "cctv_cleaner"),
    Bookmark("CCTV 16", "https://tv.cctv.com/live/cctv16/?spm=C28340.PQ1NrVkX5nqY.ExidtyEJcS5K.28", "奥林匹克频道", "cctv_cleaner"),
    Bookmark("CCTV 17", "https://tv.cctv.com/live/cctv17/?spm=C28340.PKJ5WKItuH8E.ExidtyEJcS5K.29", "农业农村频道", "cctv_cleaner")
) 