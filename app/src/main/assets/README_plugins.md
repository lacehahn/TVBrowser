# 插件系统使用说明

## 📋 概述
本应用支持为每个频道配置JavaScript插件，实现自定义的页面操作功能。

## 📁 文件结构
```
app/src/main/assets/
├── channels.json     # 频道配置（包含插件引用）
├── plugins.json      # 插件配置文件
└── plugins/          # 插件脚本目录
    └── cctv_cleaner.js
```

## 🎯 CCTV清理插件功能
当前已实现的CCTV清理插件可以：
- ✅ 移除导航栏 (nav_wrapper_bg, newtopbz)
- ✅ 移除顶部横幅 (header_nav, newtopbzTV)
- ✅ 隐藏广告和弹窗
- ✅ 自动处理动态加载的干扰元素

## 🔧 插件配置格式

### channels.json 示例
```json
{
  "title": "CCTV 1",
  "url": "https://tv.cctv.com/live/cctv1/...",
  "description": "综合频道",
  "plugin": "cctv_cleaner"
}
```

### plugins.json 示例
```json
{
  "plugins": {
    "cctv_cleaner": {
      "name": "CCTV页面清理器",
      "description": "移除导航栏和顶部横幅",
      "version": "1.0.0",
      "script": "cctv_cleaner.js",
      "runOn": "onPageFinished",
      "enabled": true
    }
  }
}
```

## 🚀 创建新插件

1. **编写JavaScript文件**: 放在 `plugins/` 目录
2. **添加插件配置**: 在 `plugins.json` 中定义
3. **关联频道**: 在 `channels.json` 中指定插件名称
4. **重新编译**: 安装应用测试效果

## 🛠️ JavaScript示例

### 删除指定CSS类的元素
```javascript
(function() {
    // 删除干扰元素
    function cleanPage() {
        const classesToRemove = ['nav_wrapper_bg', 'header_nav'];
        classesToRemove.forEach(className => {
            const elements = document.getElementsByClassName(className);
            for (let i = elements.length - 1; i >= 0; i--) {
                elements[i].remove();
            }
        });
    }
    
    // 延迟执行
    setTimeout(cleanPage, 1000);
})();
```

## 📝 调试信息
在Logcat中搜索标签：
- `PluginLoader`: 插件加载状态
- `BrowserWebView`: 插件执行结果

现在所有CCTV频道都会自动清理页面，提供更好的观看体验！ 