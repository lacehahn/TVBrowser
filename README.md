# TVBrowser - Android TV 浏览器

> 本项目在 **Cursor + Claude-4** 的辅助下开发完成，感谢AI技术为开发效率带来的巨大提升。

<div align="center">

![Android TV Browser](https://img.shields.io/badge/Platform-Android%20TV-green.svg)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)
![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-orange.svg)

一个专为Android TV设计的现代化浏览器应用，支持遥控器操作和智能导航。

[功能特性](#-功能特性) • [安装说明](#-安装说明) • [使用方法](#-使用方法) • [配置说明](#️-配置说明) • [开发指南](#️-开发)

</div>

---

## ✨ 功能特性

### 🎯 核心功能
- **📺 Android TV 专用设计** - 针对电视大屏优化的用户界面
- **🌐 完整网页浏览** - 基于WebView的网页浏览器，支持现代网页标准
- **🔖 智能书签管理** - 预置热门视频网站，支持JSON配置自定义
- **🎮 遥控器友好** - 支持TV遥控器和键盘操作

### 🚀 高级特性
- **🖱️ 虚拟鼠标指针** - 模拟鼠标操作，支持点击和页面滚动
- **📱 多种操作模式** - 频道切换模式 + 页面浏览模式无缝切换
- **🔧 插件系统** - 支持JavaScript插件扩展，可自动清理页面、修改样式
- **🛡️ 灵活控制** - 支持per-channel的浏览器模式开关

---

## 📦 安装说明

### 系统要求
- **Android版本**: Android 5.0 (API 21) 及以上
- **设备类型**: Android TV、TV Box、Chromecast with Google TV
- **内存要求**: 推荐2GB以上RAM
- **网络**: 需要互联网连接

### 安装方式

#### 方式一：直接安装APK
1. 下载最新版本的APK文件
2. 在Android TV上启用"未知来源"安装权限
3. 使用文件管理器或ADB安装APK
4. 在应用列表中找到"TVBrowser"并启动

#### 方式二：从源码构建
```bash
# 克隆项目
git clone https://github.com/yourusername/TVBrowser.git
cd TVBrowser

# 构建APK
./gradlew assembleDebug

# 安装到设备
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## 🎮 使用方法

### 基本操作

#### 遥控器按键功能
| 按键 | 功能 |
|------|------|
| **↑/↓** | 切换频道/书签 |
| **←/→** | 在书签覆盖层中导航 |
| **确定键** | 进入页面浏览模式（如果频道支持）|
| **返回键** | 显示/隐藏书签列表 |
| **双击返回** | 退出应用 |
| **菜单键/空格** | 显示书签覆盖层 |

### 操作模式详解

#### 📺 频道切换模式（默认）
- 使用↑↓键快速切换预设的网站/频道
- 按确定键进入页面浏览模式（如果频道允许）
- 按返回键显示书签列表
- 适合快速浏览不同网站

#### 🖱️ 页面浏览模式
- **方向键控制**: 虚拟鼠标指针移动
- **确定键**: 模拟鼠标点击
- **边缘滚动**: 鼠标靠近屏幕边缘时自动滚动页面
- **自动隐藏**: 鼠标5秒无操作后自动隐藏
- **退出方式**: 按返回键退出浏览模式

#### 📋 书签覆盖层
- 方向键在书签网格中导航（3列布局）
- 确定键选择书签并跳转
- 返回键关闭覆盖层

---

## ⚙️ 配置说明

### 自定义频道
编辑 `app/src/main/assets/channels.json` 文件：
```json
{
  "channels": [
    {
      "title": "网站名称",
      "url": "https://example.com",
      "description": "网站描述",
      "plugin": "插件名称（可选）",
      "disableBrowserMode": false
    }
  ]
}
```

**字段说明**：
- `title`: 显示的频道名称
- `url`: 网站URL地址
- `description`: 频道描述信息
- `plugin`: 关联的插件名称（可选）
- `disableBrowserMode`: 是否禁用页面浏览模式

### 插件系统

#### 创建插件
1. 在 `app/src/main/assets/plugins/` 目录添加JavaScript文件
2. 在 `plugins.json` 中注册插件：

```json
{
  "plugins": {
    "my_plugin": {
      "name": "我的插件",
      "description": "插件描述",
      "version": "1.0.0",
      "script": "my_plugin.js",
      "runOn": "onPageFinished",
      "enabled": true
    }
  }
}
```

#### 插件示例
```javascript
// my_plugin.js
// 自动隐藏广告
document.addEventListener('DOMContentLoaded', function() {
    // 隐藏广告元素
    const ads = document.querySelectorAll('.ad, .advertisement');
    ads.forEach(ad => ad.style.display = 'none');
    
    // 移除干扰元素
    const overlays = document.querySelectorAll('.overlay, .popup');
    overlays.forEach(overlay => overlay.remove());
});
```

---

## 🏗️ 技术架构

### 技术栈
- **开发语言**: Kotlin
- **UI框架**: Jetpack Compose + Compose for TV
- **架构模式**: MVVM (Model-View-ViewModel)
- **网页引擎**: Android WebView
- **数据格式**: JSON (Kotlinx Serialization)
- **异步处理**: Kotlin Coroutines

### 项目结构
```
app/src/main/
├── java/com/example/tvbrowser/
│   ├── components/           # UI组件
│   │   ├── BookmarkCard.kt
│   │   ├── BookmarkOverlay.kt
│   │   ├── BrowserWebView.kt
│   │   ├── KeyEventHandler.kt
│   │   ├── LoadingIndicator.kt
│   │   └── MousePointer.kt
│   ├── data/                # 数据模型
│   │   └── Bookmark.kt
│   ├── viewmodel/           # 视图模型
│   │   └── BrowserViewModel.kt
│   └── MainActivity.kt      # 主活动
├── assets/                  # 配置文件
│   ├── channels.json        # 频道配置
│   ├── plugins.json         # 插件配置
│   └── plugins/             # JavaScript插件
└── res/                     # Android资源
```

---

## 🛠️ 开发

### 开发环境
- Android Studio Arctic Fox 或更新版本
- Kotlin 插件
- Android SDK (API 21+)

### 构建应用
```bash
# 克隆项目
git clone https://github.com/yourusername/TVBrowser.git
cd TVBrowser

# 调试构建
./gradlew assembleDebug

# 发布构建
./gradlew assembleRelease

# 安装到设备
adb install app/build/outputs/apk/debug/app-debug.apk
```

### 开发指南
1. 遵循MVVM架构模式
2. 使用Jetpack Compose构建UI
3. 所有配置通过JSON文件管理
4. 插件系统支持动态加载JavaScript

---

## ❓ 常见问题

**Q: 应用无法安装？**
A: 确保设备运行Android 5.0以上，并启用"未知来源"安装权限。

**Q: 网页加载很慢？**
A: 检查网络连接，某些网站在TV上可能加载较慢，属于正常现象。

**Q: 如何添加自己的网站？**
A: 修改`assets/channels.json`文件，添加网站信息和配置。

**Q: 某些频道无法进入浏览模式？**
A: 部分频道设置了`disableBrowserMode: true`，禁用了页面浏览功能。

**Q: 鼠标指针不显示？**
A: 确保已进入页面浏览模式（按确定键），鼠标会在移动时显示。

**Q: 如何创建自定义插件？**
A: 在`plugins/`目录添加JavaScript文件，并在`plugins.json`中注册。

---

## 贡献

欢迎贡献代码！请遵循以下步骤：

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交修改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

---

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

---

## 📧 联系方式

- **项目主页**: [GitHub Repository](https://github.com/yourusername/TVBrowser)
- **问题反馈**: [GitHub Issues](https://github.com/yourusername/TVBrowser/issues)
- **功能建议**: [GitHub Discussions](https://github.com/yourusername/TVBrowser/discussions)

---

<div align="center">

**如果这个项目对您有帮助，请给个 ⭐ Star 支持一下！**

Made with ❤️ for Android TV Community

</div> 