package com.example.tvbrowser.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import com.example.tvbrowser.data.Bookmark

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun BookmarkOverlay(
    bookmarks: List<Bookmark>,
    currentIndex: Int,
    focusedIndex: Int,
    onBookmarkSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val gridState = rememberLazyGridState()
    
    // 自动滚动到聚焦的项目
    LaunchedEffect(focusedIndex) {
        val columnsCount = 3
        val rowIndex = focusedIndex / columnsCount
        gridState.animateScrollToItem(focusedIndex)
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.95f),
                        Color.Black.copy(alpha = 0.98f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 标题
            Text(
                text = "📺 频道列表",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 40.dp)
            )
            
            // 书签网格
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                state = gridState,
                contentPadding = PaddingValues(20.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                itemsIndexed(bookmarks) { index, bookmark ->
                    BookmarkCard(
                        bookmark = bookmark,
                        isCurrent = index == currentIndex,
                        isFocused = index == focusedIndex,
                        onClick = { onBookmarkSelected(index) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                    )
                }
            }
            
            // 操作提示
            Text(
                text = "按 Back 返回浏览 | 按 Enter 选择频道",
                fontSize = 20.sp,
                color = Color(0xFFCCCCCC),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 24.dp)
            )
        }
    }
} 