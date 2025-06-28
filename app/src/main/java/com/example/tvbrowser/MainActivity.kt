package com.example.tvbrowser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import com.example.tvbrowser.components.*
import com.example.tvbrowser.ui.theme.TVBrowserTheme
import com.example.tvbrowser.viewmodel.BrowserViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TVBrowserTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape
                ) {
                    TVBrowserScreen(
                        onExitApp = { finish() }
                    )
                }
            }
        }
    }
}

@Composable
fun TVBrowserScreen(
    viewModel: BrowserViewModel = viewModel(),
    onExitApp: () -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    
    val context = LocalContext.current
    
    // 设置屏幕尺寸
    LaunchedEffect(configuration.screenWidthDp, configuration.screenHeightDp) {
        with(density) {
            val screenWidth = configuration.screenWidthDp.dp.toPx()
            val screenHeight = configuration.screenHeightDp.dp.toPx()
            viewModel.setScreenSize(screenWidth, screenHeight)
        }
    }
    
    // 加载频道数据
    LaunchedEffect(Unit) {
        viewModel.loadChannels(context)
    }

    
    
    // 处理返回键
    BackHandler(enabled = viewModel.isOverlayVisible) {
        viewModel.toggleOverlay()
    }
    
    // 处理全屏模式返回键（这将由WebView的全屏处理机制自动处理）
    
    KeyEventHandler(
        onUpPressed = {
            if (viewModel.isOverlayVisible) {
                viewModel.navigateOverlay("up")
            } else {
                viewModel.previousBookmark()
            }
        },
        onDownPressed = {
            if (viewModel.isOverlayVisible) {
                viewModel.navigateOverlay("down")
            } else {
                viewModel.nextBookmark()
            }
        },
        onLeftPressed = {
            if (viewModel.isOverlayVisible) {
                viewModel.navigateOverlay("left")
            }
        },
        onRightPressed = {
            if (viewModel.isOverlayVisible) {
                viewModel.navigateOverlay("right")
            }
        },
        onMouseMoveUp = { viewModel.moveMouseUp() },
        onMouseMoveDown = { viewModel.moveMouseDown() },
        onMouseMoveLeft = { viewModel.moveMouseLeft() },
        onMouseMoveRight = { viewModel.moveMouseRight() },
        onEnterPressed = {
            if (viewModel.isOverlayVisible) {
                viewModel.selectFocusedBookmark()
            } else {
                // 进入页面浏览模式, disabled the function currently
                //viewModel.enterPageBrowsingMode()
            }
        },
        onBackPressed = {
            if (viewModel.isOverlayVisible) {
                viewModel.exitPageBrowsingMode()
            } else {
                // 在频道选择模式中，返回键调出频道列表
                viewModel.toggleOverlay()
            }
        },
        onDoubleBackPressed = {
            // 双击返回键退出应用
            onExitApp()
        },
        onMouseClick = {
            viewModel.performMouseClick()
        },
        onSpacePressed = {
            viewModel.toggleOverlay()
        },
        isPageBrowsingMode = viewModel.isPageBrowsingMode
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // 主浏览区域
            BrowserWebView(
                url = viewModel.currentBookmark.url,
                onPageStarted = { viewModel.onPageStarted() },
                onPageFinished = { 
                    viewModel.onPageFinished()
                    // 页面加载完成后触发插件执行
                    viewModel.triggerPluginExecution()
                },
                pageScrollUpTrigger = viewModel.pageScrollUpTrigger,
                pageScrollDownTrigger = viewModel.pageScrollDownTrigger,
                pageScrollLeftTrigger = viewModel.pageScrollLeftTrigger,
                pageScrollRightTrigger = viewModel.pageScrollRightTrigger,
                mouseClickTrigger = viewModel.mouseClickTrigger,
                mouseX = viewModel.mouseX,
                mouseY = viewModel.mouseY,
                pluginExecuteTrigger = viewModel.pluginExecuteTrigger,
                pluginScript = viewModel.getCurrentChannelPluginScript(context),
                modifier = Modifier.fillMaxSize()
            )
            
            // 鼠标指针（仅在页面浏览模式且鼠标可见时显示）
            if (viewModel.isPageBrowsingMode && viewModel.isMouseVisible) {
                MousePointer(
                    x = viewModel.mouseX,
                    y = viewModel.mouseY,
                    visible = true,
                    clickEffect = viewModel.mouseClickEffect,
                    modifier = Modifier
                )
            }
            
            // 加载指示器
            if (viewModel.isLoading) {
                LoadingIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(120.dp)
                )
            }
            
            // 书签覆盖层  
            if (viewModel.isOverlayVisible) {
                BookmarkOverlay(
                    bookmarks = viewModel.bookmarks,
                    currentIndex = viewModel.currentIndex,
                    focusedIndex = viewModel.focusedCardIndex,
                    onBookmarkSelected = { index ->
                        viewModel.selectBookmark(index)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}