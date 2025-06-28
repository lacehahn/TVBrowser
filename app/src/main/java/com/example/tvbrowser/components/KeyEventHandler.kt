package com.example.tvbrowser.components

import android.view.KeyEvent
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager

@Composable
fun KeyEventHandler(
    onUpPressed: () -> Unit,
    onDownPressed: () -> Unit,
    onLeftPressed: () -> Unit,
    onRightPressed: () -> Unit,
    onMouseMoveUp: () -> Unit,
    onMouseMoveDown: () -> Unit,
    onMouseMoveLeft: () -> Unit,
    onMouseMoveRight: () -> Unit,
    onEnterPressed: () -> Unit,
    onBackPressed: () -> Unit,
    onDoubleBackPressed: () -> Unit,
    onMouseClick: () -> Unit,
    onSpacePressed: () -> Unit = onEnterPressed,
    isPageBrowsingMode: Boolean,
    content: @Composable () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    
    // 双击返回键管理
    var lastBackPressTime by remember { mutableStateOf(0L) }
    val doubleBackPressInterval = 500L // 500ms内快速双击有效
    
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    
    Box(
        modifier = Modifier
            .focusRequester(focusRequester)
            .focusable()
            .onKeyEvent { keyEvent ->
                when (keyEvent.type) {
                    KeyEventType.KeyDown -> {
                        when (keyEvent.nativeKeyEvent.keyCode) {
                            // Android TV 遥控器和键盘上方向键
                            KeyEvent.KEYCODE_DPAD_UP -> {
                                if (isPageBrowsingMode) {
                                    onMouseMoveUp()
                                } else {
                                    onUpPressed()
                                }
                                true
                            }
                            // Android TV 遥控器和键盘下方向键
                            KeyEvent.KEYCODE_DPAD_DOWN -> {
                                if (isPageBrowsingMode) {
                                    onMouseMoveDown()
                                } else {
                                    onDownPressed()
                                }
                                true
                            }
                            // Android TV 遥控器和键盘左方向键
                            KeyEvent.KEYCODE_DPAD_LEFT -> {
                                if (isPageBrowsingMode) {
                                    onMouseMoveLeft()
                                } else {
                                    onLeftPressed()
                                }
                                true
                            }
                            // Android TV 遥控器和键盘右方向键
                            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                                if (isPageBrowsingMode) {
                                    onMouseMoveRight()
                                } else {
                                    onRightPressed()
                                }
                                true
                            }
                            KeyEvent.KEYCODE_DPAD_CENTER,
                            KeyEvent.KEYCODE_ENTER,
                            KeyEvent.KEYCODE_NUMPAD_ENTER -> {
                                if (isPageBrowsingMode) {
                                    onMouseClick()
                                } else {
                                    onEnterPressed()
                                }
                                true
                            }
                            KeyEvent.KEYCODE_BACK,
                            KeyEvent.KEYCODE_ESCAPE -> {
                                val currentTime = System.currentTimeMillis()
                                if (currentTime - lastBackPressTime < doubleBackPressInterval) {
                                    // 双击退出
                                    onDoubleBackPressed()
                                } else {
                                    // 单击返回
                                    onBackPressed()
                                    lastBackPressTime = currentTime
                                }
                                true
                            }
                            KeyEvent.KEYCODE_SPACE -> {
                                onSpacePressed()
                                true
                            }
                            else -> false
                        }
                    }
                    else -> false
                }
            }
    ) {
        content()
    }
} 