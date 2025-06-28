package com.example.tvbrowser.components

import android.app.Activity
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun BrowserWebView(
    url: String,
    onPageStarted: () -> Unit = {},
    onPageFinished: () -> Unit = {},
    pageScrollUpTrigger: Int = 0,
    pageScrollDownTrigger: Int = 0,
    pageScrollLeftTrigger: Int = 0,
    pageScrollRightTrigger: Int = 0,
    mouseClickTrigger: Int = 0,
    mouseX: Float = 0f,
    mouseY: Float = 0f,
    pluginExecuteTrigger: Int = 0,
    pluginScript: String? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = context as? Activity
    
    val webView = remember {
        WebView(context).apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                builtInZoomControls = false
                displayZoomControls = false
                
                // 设置桌面版User-Agent
                userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
                
                // 启用缩放支持
                setSupportZoom(true)
                
                // 允许文件访问
                allowFileAccess = true
                allowContentAccess = true
                
                // 启用媒体播放
                mediaPlaybackRequiresUserGesture = false
                
                // 支持多窗口
                setSupportMultipleWindows(true)
                
                // 启用混合内容模式（HTTPS页面中的HTTP内容）
                mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
            
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    onPageStarted()
                }
                
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    onPageFinished()
                    
                    // 页面加载完成后执行插件
                    if (!pluginScript.isNullOrEmpty()) {
                        Log.d("BrowserWebView", "Executing plugin script after page finished")
                        view?.postDelayed({
                            view.evaluateJavascript(pluginScript) { result ->
                                Log.d("BrowserWebView", "Plugin execution result: $result")
                            }
                        }, 500) // 延迟500ms确保页面完全渲染
                    }
                }
            }
            
            // 设置WebChromeClient支持全屏视频
            webChromeClient = object : WebChromeClient() {
                private var fullscreenContainer: FrameLayout? = null
                private var customView: View? = null
                private var customViewCallback: CustomViewCallback? = null
                
                override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                    Log.d("BrowserWebView", "Entering fullscreen mode")
                    
                    // 如果已经在全屏模式，先退出
                    if (customView != null) {
                        onHideCustomView()
                        return
                    }
                    
                    customView = view
                    customViewCallback = callback
                    
                    activity?.let { act ->
                        // 隐藏系统UI
                        val window = act.window
                        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
                        windowInsetsController.systemBarsBehavior = 
                            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
                        
                        // 创建全屏容器
                        fullscreenContainer = FrameLayout(context).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            setBackgroundColor(android.graphics.Color.BLACK)
                            addView(view)
                        }
                        
                        // 添加到Activity根视图
                        val decorView = act.window.decorView as ViewGroup
                        decorView.addView(fullscreenContainer)
                    }
                }
                
                override fun onHideCustomView() {
                    Log.d("BrowserWebView", "Exiting fullscreen mode")
                    
                    customView?.let {
                        activity?.let { act ->
                            // 显示系统UI
                            val window = act.window
                            val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
                            windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
                            
                            // 移除全屏容器
                            val decorView = act.window.decorView as ViewGroup
                            fullscreenContainer?.let { container ->
                                decorView.removeView(container)
                            }
                        }
                        
                        fullscreenContainer = null
                        customView = null
                        customViewCallback?.onCustomViewHidden()
                        customViewCallback = null
                    }
                }
                
                override fun getDefaultVideoPoster(): android.graphics.Bitmap? {
                    // 返回默认的视频封面
                    return null
                }
                
                override fun getVideoLoadingProgressView(): View? {
                    // 返回视频加载时的进度视图
                    return null
                }
            }
        }
    }
    
    // 边缘滚动处理
    val scrollAmount = 100 // 滚动距离像素
    
    LaunchedEffect(pageScrollUpTrigger) {
        if (pageScrollUpTrigger > 0) {
            webView.scrollBy(0, -scrollAmount)
        }
    }
    
    LaunchedEffect(pageScrollDownTrigger) {
        if (pageScrollDownTrigger > 0) {
            webView.scrollBy(0, scrollAmount)
        }
    }
    
    LaunchedEffect(pageScrollLeftTrigger) {
        if (pageScrollLeftTrigger > 0) {
            webView.scrollBy(-scrollAmount, 0)
        }
    }
    
    LaunchedEffect(pageScrollRightTrigger) {
        if (pageScrollRightTrigger > 0) {
            webView.scrollBy(scrollAmount, 0)
        }
    }
    
    // 鼠标点击处理
    LaunchedEffect(mouseClickTrigger) {
        if (mouseClickTrigger > 0) {
            Log.d("BrowserWebView", "Mouse click at ($mouseX, $mouseY)")
            
            // 使用Android原生MotionEvent模拟触摸点击
            val downTime = System.currentTimeMillis()
            val eventTime = System.currentTimeMillis()
            
            // 创建ACTION_DOWN事件
            val downEvent = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_DOWN,
                mouseX,
                mouseY,
                0
            )
            
            // 创建ACTION_UP事件
            val upEvent = MotionEvent.obtain(
                downTime,
                eventTime + 100,
                MotionEvent.ACTION_UP,
                mouseX,
                mouseY,
                0
            )
            
            try {
                // 分发触摸事件到WebView
                webView.post {
                    Log.d("BrowserWebView", "Dispatching touch events")
                    webView.dispatchTouchEvent(downEvent)
                    webView.postDelayed({
                        webView.dispatchTouchEvent(upEvent)
                        Log.d("BrowserWebView", "Touch events dispatched")
                    }, 50)
                }
            } catch (e: Exception) {
                Log.e("BrowserWebView", "Error dispatching touch events", e)
            } finally {
                downEvent.recycle()
                upEvent.recycle()
            }
        }
    }
    
    // 插件执行处理
    LaunchedEffect(pluginExecuteTrigger) {
        if (pluginExecuteTrigger > 0 && !pluginScript.isNullOrEmpty()) {
            Log.d("BrowserWebView", "Executing plugin script via trigger")
            webView.evaluateJavascript(pluginScript) { result ->
                Log.d("BrowserWebView", "Plugin execution result: $result")
            }
        }
    }
    
    DisposableEffect(url) {
        webView.loadUrl(url)
        onDispose { 
            // 清理资源，确保退出全屏模式
            try {
                webView.webChromeClient?.onHideCustomView()
            } catch (e: Exception) {
                Log.e("BrowserWebView", "Error cleaning up fullscreen", e)
            }
        }
    }
    
    AndroidView(
        factory = { webView },
        modifier = modifier.fillMaxSize()
    )
} 