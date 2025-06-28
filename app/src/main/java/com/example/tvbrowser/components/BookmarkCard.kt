package com.example.tvbrowser.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import com.example.tvbrowser.data.Bookmark

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun BookmarkCard(
    bookmark: Bookmark,
    isCurrent: Boolean = false,
    isFocused: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.05f else 1f,
        label = "card_scale"
    )
    
    val borderColor = when {
        isCurrent -> Color(0xFFFF9800) // 橙色表示当前正在浏览
        isFocused -> Color(0xFF4CAF50) // 绿色表示焦点
        else -> Color.Transparent
    }
    
    val cardBackgroundColor = if (isCurrent) {
        Color(0xFFFF9800).copy(alpha = 0.8f)
    } else {
        Color(0xFF1E3C72).copy(alpha = 0.9f)
    }
    
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .scale(scale)
                .background(cardBackgroundColor, RoundedCornerShape(15.dp))
                .border(
                    width = 3.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(15.dp)
                )
                .clickable { onClick() }
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 标题
                Text(
                    text = bookmark.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                // 描述
                Text(
                    text = bookmark.description,
                    fontSize = 16.sp,
                    color = Color(0xFFCCCCCC),
                    textAlign = TextAlign.Center,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    fontStyle = FontStyle.Italic
                )
            }
        }
        
        // 当前浏览标识
        if (isCurrent) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-10).dp, y = (-10).dp)
                    .background(
                        color = Color(0xFFFF5722),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "正在浏览",
                    fontSize = 10.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
} 