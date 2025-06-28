# 频道列表管理说明

## JSON 格式频道配置

本应用使用 `channels.json` 文件来管理频道列表，便于动态更新和维护。

### 文件位置
```
app/src/main/assets/channels.json
```

### JSON 格式说明

```json
{
  "channels": [
    {
      "title": "频道名称",
      "url": "频道直播地址",
      "description": "频道描述"
    }
  ]
}
```

### 字段说明

- **title**: 频道显示名称（如 "CCTV 1"）
- **url**: 频道直播页面完整URL
- **description**: 频道简短描述（如 "综合频道"）

### 更新频道列表

要添加、删除或修改频道：

1. 编辑 `app/src/main/assets/channels.json` 文件
2. 按照上述JSON格式添加或修改频道信息
3. 重新编译安装应用

### 示例：添加新频道

```json
{
  "channels": [
    {
      "title": "新频道",
      "url": "https://example.com/live",
      "description": "新频道描述"
    }
  ]
}
```

### 注意事项

1. **JSON格式**: 确保JSON格式正确，否则应用会回退到默认频道列表
2. **URL有效性**: 确保所有URL都是有效的直播地址
3. **字符编码**: 使用UTF-8编码保存文件
4. **备份**: 修改前建议备份原文件

### 错误处理

如果JSON文件读取失败或格式错误，应用会：
- 在日志中记录错误信息
- 自动使用内置的默认频道列表
- 确保应用正常运行

### 调试

可以在Android Studio的Logcat中搜索 `ChannelLoader` 标签查看加载状态：
- 成功：`Successfully loaded X channels from JSON`
- 失败：`Error reading/parsing channels.json` 