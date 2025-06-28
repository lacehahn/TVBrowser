package com.example.tvbrowser.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvbrowser.data.Bookmark
import com.example.tvbrowser.data.Plugin
import com.example.tvbrowser.data.loadChannelsFromJson
import com.example.tvbrowser.data.loadPluginsFromJson
import com.example.tvbrowser.data.loadPluginScript
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BrowserViewModel : ViewModel() {
    
    // 当前选中的书签索引
    var currentIndex by mutableStateOf(0)
        private set
    
    // 是否显示书签覆盖层
    var isOverlayVisible by mutableStateOf(false)
        private set
    
    // 覆盖层中焦点的书签索引
    var focusedCardIndex by mutableStateOf(0)
        private set
    
    // 是否处于页面浏览模式（方向键用于鼠标指针移动）
    var isPageBrowsingMode by mutableStateOf(false)
        private set
    
    // 鼠标指针位置
    var mouseX by mutableStateOf(400f) // 屏幕中心X
        private set
    var mouseY by mutableStateOf(300f) // 屏幕中心Y
        private set
    
    // 鼠标指针可见性（用于自动隐藏）
    var isMouseVisible by mutableStateOf(true)
        private set
    
    // 鼠标点击效果
    var mouseClickEffect by mutableStateOf(false)
        private set
    
    // 屏幕尺寸
    var screenWidth by mutableStateOf(1920f)
        private set
    var screenHeight by mutableStateOf(1080f)
        private set
    
    // 页面滚动触发器（用于边缘滚动）
    var pageScrollUpTrigger by mutableStateOf(0)
        private set
    var pageScrollDownTrigger by mutableStateOf(0)
        private set
    var pageScrollLeftTrigger by mutableStateOf(0)
        private set
    var pageScrollRightTrigger by mutableStateOf(0)
        private set
    
    // 鼠标点击触发器
    var mouseClickTrigger by mutableStateOf(0)
        private set
    
        // 是否正在加载
    var isLoading by mutableStateOf(false)
        private set

    // 书签列表（从JSON文件动态加载）
    var bookmarks by mutableStateOf<List<Bookmark>>(emptyList())
        private set
    
    // 插件列表
    var plugins by mutableStateOf<Map<String, Plugin>>(emptyMap())
        private set
    
    // 插件执行触发器
    var pluginExecuteTrigger by mutableStateOf(0)
        private set
    
    // 当前书签
    val currentBookmark: Bookmark
        get() = if (bookmarks.isNotEmpty()) bookmarks[currentIndex] else 
            Bookmark("加载中...", "about:blank", "正在加载频道列表")
    
    // 切换到上一个书签
    fun previousBookmark() {
        if (!isOverlayVisible) {
            currentIndex = (currentIndex - 1 + bookmarks.size) % bookmarks.size
            isLoading = true
        }
    }
    
    // 切换到下一个书签
    fun nextBookmark() {
        if (!isOverlayVisible) {
            currentIndex = (currentIndex + 1) % bookmarks.size
            isLoading = true
        }
    }
    
    // 切换覆盖层显示/隐藏
    fun toggleOverlay() {
        isOverlayVisible = !isOverlayVisible
        if (isOverlayVisible) {
            focusedCardIndex = currentIndex
        }
    }
    
    // 进入页面浏览模式
    fun enterPageBrowsingMode() {
        isPageBrowsingMode = true
        showMouseAndResetTimer()
    }
    
    // 退出页面浏览模式，回到频道切换模式
    fun exitPageBrowsingMode() {
        isPageBrowsingMode = false
        // 取消自动隐藏计时器
        mouseHideJob?.cancel()
        isMouseVisible = true
    }
    
    // 鼠标指针移动方法
    private val mouseMovementStep = 20f // 每次移动的像素距离
    private val edgeThreshold = 50f // 边缘检测阈值（像素）
    
    // 鼠标自动隐藏相关
    private var mouseHideJob: Job? = null
    private val mouseHideDelay = 5000L // 5秒后自动隐藏
    
    fun setScreenSize(width: Float, height: Float) {
        screenWidth = width
        screenHeight = height
        // 重新居中鼠标
        mouseX = width / 2
        mouseY = height / 2
    }
    
    fun moveMouseUp() {
        mouseY = maxOf(0f, mouseY - mouseMovementStep)
        showMouseAndResetTimer()
        checkEdgeScroll()
    }
    
    fun moveMouseDown() {
        mouseY = minOf(screenHeight, mouseY + mouseMovementStep)
        showMouseAndResetTimer()
        checkEdgeScroll()
    }
    
    fun moveMouseLeft() {
        mouseX = maxOf(0f, mouseX - mouseMovementStep)
        showMouseAndResetTimer()
        checkEdgeScroll()
    }
    
    fun moveMouseRight() {
        mouseX = minOf(screenWidth, mouseX + mouseMovementStep)
        showMouseAndResetTimer()
        checkEdgeScroll()
    }
    
    // 检测边缘并触发页面滚动
    private fun checkEdgeScroll() {
        if (!isPageBrowsingMode) return
        
        // 上边缘
        if (mouseY <= edgeThreshold) {
            pageScrollUpTrigger++
        }
        // 下边缘
        else if (mouseY >= screenHeight - edgeThreshold) {
            pageScrollDownTrigger++
        }
        // 左边缘
        if (mouseX <= edgeThreshold) {
            pageScrollLeftTrigger++
        }
        // 右边缘
        else if (mouseX >= screenWidth - edgeThreshold) {
            pageScrollRightTrigger++
        }
    }
    
    // 选择书签
    fun selectBookmark(index: Int) {
        currentIndex = index
        isOverlayVisible = false
        isLoading = true
    }
    
    // 在覆盖层中导航
    fun navigateOverlay(direction: String) {
        if (!isOverlayVisible) return
        
        val cardsPerRow = 3 // 每行显示3个卡片
        
        when (direction) {
            "up" -> {
                focusedCardIndex = maxOf(0, focusedCardIndex - cardsPerRow)
            }
            "down" -> {
                focusedCardIndex = minOf(bookmarks.size - 1, focusedCardIndex + cardsPerRow)
            }
            "left" -> {
                focusedCardIndex = maxOf(0, focusedCardIndex - 1)
            }
            "right" -> {
                focusedCardIndex = minOf(bookmarks.size - 1, focusedCardIndex + 1)
            }
        }
    }
    
    // 选择当前聚焦的书签
    fun selectFocusedBookmark() {
        if (isOverlayVisible) {
            selectBookmark(focusedCardIndex)
        }
    }
    
    // WebView加载完成
    fun onPageFinished() {
        isLoading = false
    }
    
    // WebView开始加载
    fun onPageStarted() {
        isLoading = true
    }
    
    // 加载频道数据
    fun loadChannels(context: Context) {
        viewModelScope.launch {
            try {
                val loadedChannels = loadChannelsFromJson(context)
                bookmarks = loadedChannels
                Log.d("BrowserViewModel", "Loaded ${loadedChannels.size} channels")
                
                // 确保currentIndex在有效范围内
                if (currentIndex >= loadedChannels.size) {
                    currentIndex = 0
                }
                
                // 同时加载插件
                loadPlugins(context)
            } catch (e: Exception) {
                Log.e("BrowserViewModel", "Failed to load channels", e)
                // 如果加载失败，使用空列表
                bookmarks = emptyList()
            }
        }
    }
    
    // 加载插件数据
    private fun loadPlugins(context: Context) {
        try {
            val loadedPlugins = loadPluginsFromJson(context)
            plugins = loadedPlugins
            Log.d("BrowserViewModel", "Loaded ${loadedPlugins.size} plugins")
        } catch (e: Exception) {
            Log.e("BrowserViewModel", "Failed to load plugins", e)
            plugins = emptyMap()
        }
    }
    
    // 获取当前频道的插件脚本
    fun getCurrentChannelPluginScript(context: Context): String? {
        val currentBookmark = currentBookmark
        val pluginName = currentBookmark.plugin
        
        if (pluginName.isNullOrEmpty()) {
            return null
        }
        
        val plugin = plugins[pluginName]
        if (plugin == null || !plugin.enabled) {
            Log.d("BrowserViewModel", "Plugin $pluginName not found or disabled")
            return null
        }
        
        return loadPluginScript(context, plugin.script)
    }
    
    // 触发插件执行
    fun triggerPluginExecution() {
        pluginExecuteTrigger++
        Log.d("BrowserViewModel", "Plugin execution triggered: $pluginExecuteTrigger")
    }
    
    // 显示鼠标并重置自动隐藏计时器
    private fun showMouseAndResetTimer() {
        if (!isPageBrowsingMode) return
        
        isMouseVisible = true
        mouseHideJob?.cancel()
        mouseHideJob = viewModelScope.launch {
            delay(mouseHideDelay)
            isMouseVisible = false
        }
    }
    
    // 鼠标点击（在页面浏览模式下使用）
    fun performMouseClick() {
        if (isPageBrowsingMode) {
            Log.d("BrowserViewModel", "Mouse click triggered at ($mouseX, $mouseY) in page browsing mode")
            
            // 显示点击效果
            mouseClickEffect = true
            mouseClickTrigger++
            showMouseAndResetTimer() // 点击时也重置计时器
            
            // 500ms后隐藏点击效果
            viewModelScope.launch {
                delay(500)
                mouseClickEffect = false
            }
        } else {
            Log.d("BrowserViewModel", "Mouse click ignored - not in page browsing mode")
        }
    }
} 