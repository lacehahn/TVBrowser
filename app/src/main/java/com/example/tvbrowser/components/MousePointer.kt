package com.example.tvbrowser.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun MousePointer(
    x: Float,
    y: Float,
    visible: Boolean = true,
    clickEffect: Boolean = false,
    modifier: Modifier = Modifier
) {
    if (visible) {
        val density = LocalDensity.current
        
        Box(
            modifier = modifier
                .offset(
                    x = with(density) { x.toDp() },
                    y = with(density) { y.toDp() }
                )
                .size(16.dp)
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                val radius = 8.dp.toPx()
                val shadowOffset = 2.dp.toPx()
                
                // 如果有点击效果，绘制扩展的点击圆圈
                if (clickEffect) {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.3f),
                        radius = radius * 2.5f,
                        center = center
                    )
                    drawCircle(
                        color = Color.White.copy(alpha = 0.6f),
                        radius = radius * 1.8f,
                        center = center
                    )
                }
                
                // 绘制黑色阴影（偏移位置）
                drawCircle(
                    color = Color.Black.copy(alpha = 0.5f),
                    radius = radius,
                    center = Offset(
                        center.x + shadowOffset,
                        center.y + shadowOffset
                    )
                )
                
                // 绘制白色主体
                drawCircle(
                    color = if (clickEffect) Color.Red else Color.White,
                    radius = radius,
                    center = center
                )
            }
        }
    }
} 