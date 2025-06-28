# TVBrowser - Android TV 浏览器

<div align="center">

![Android TV Browser](https://img.shields.io/badge/Platform-Android%20TV-green.svg)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)
![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-orange.svg)
![License](https://img.shields.io/badge/License-MIT-yellow.svg)

一个专为Android TV设计的现代化浏览器应用，支持遥控器操作和智能导航。

[功能特性](#功能特性) • [安装说明](#安装说明) • [使用方法](#使用方法) • [开发指南](#开发指南) • [贡献](#贡献)

</div>

---

## 📋 目录

- [功能特性](#功能特性)
- [截图展示](#截图展示)
- [安装说明](#安装说明)
- [使用方法](#使用方法)
- [技术架构](#技术架构)
- [配置说明](#配置说明)
- [开发指南](#开发指南)
- [FAQ](#faq)
- [贡献指南](#贡献指南)
- [许可证](#许可证)
- [联系方式](#联系方式)

---

## ✨ 功能特性

### 🎯 核心功能
- **📺 Android TV 专用设计** - 针对电视大屏优化的用户界面
- **🌐 完整网页浏览** - 基于WebView的全功能网页浏览器
- **🔖 智能书签管理** - 预置热门网站，支持自定义书签
- **🎮 遥控器友好** - 完美支持TV遥控器和键盘操作

### 🚀 高级特性
- **🖱️ 虚拟鼠标指针** - 模拟鼠标操作，支持点击和滚动
- **📱 多种操作模式** - 频道切换模式 + 页面浏览模式
- **⚡ 边缘自动滚动** - 鼠标靠近屏幕边缘时自动滚动页面
- **🔧 插件系统** - 支持JavaScript插件扩展功能
- **⏱️ 智能交互** - 自动隐藏界面元素，专注内容浏览
- **🎨 现代化UI** - Material3 设计语言，TV专用组件

### 📋 预置内容
- **央视频道直播** - 内置CCTV 1-17全套频道链接
- **网页清理插件** - 自动移除广告和无关元素
- **响应式布局** - 适配各种Android TV设备屏幕

---

## 🖼️ 截图展示

> 这里可以添加应用的截图展示

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
| **确定键** | 进入页面浏览模式 |
| **返回键** | 显示/隐藏书签列表 |
| **双击返回** | 退出应用 |
| **菜单键** | 显示书签覆盖层 |

#### 操作模式切换

**📺 频道切换模式**（默认）
- 使用↑↓键快速切换预设的网站/频道
- 按确定键进入页面浏览模式
- 按返回键显示书签列表

**🖱️ 页面浏览模式**
- 方向键控制虚拟鼠标指针移动
- 确定键模拟鼠标点击
- 鼠标靠近屏幕边缘时自动滚动页面
- 鼠标5秒无操作后自动隐藏
- 按返回键退出浏览模式

**📋 书签覆盖层**
- 方向键在书签网格中导航
- 确定键选择书签
- 返回键关闭覆盖层

### 高级功能

#### 自定义书签
1. 修改 `app/src/main/assets/channels.json` 文件
2. 添加您的网站信息：
```json
{
  "title": "网站名称",
  "url": "https://example.com",
  "description": "网站描述",
  "plugin": "插件名称（可选）"
}
```

#### 插件系统
1. 在 `app/src/main/assets/plugins/` 目录添加JavaScript文件
2. 在 `plugins.json` 中注册插件
3. 插件可以自动清理页面、修改样式等

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
│   ├── ui/theme/            # 主题样式
│   │   └── BrowserViewModel.kt
│   └── MainActivity.kt      # 主活动
├── assets/                  # 资源文件
│   ├── channels.json        # 频道配置
│   ├── plugins.json         # 插件配置
│   └── plugins/             # JavaScript插件
└── res/                     # Android资源
```

---

## ⚙️ 配置说明

### 频道配置 (`channels.json`)
```json
{
  "channels": [
    {
      "title": "网站标题",
      "url": "网站URL",
      "description": "网站描述",
      "plugin": "关联的插件名称（可选）"
    }
  ]
}
```

### 插件配置 (`plugins.json`)
```json
{
  "plugins": {
    "plugin_name": {
      "name": "插件名称",
      "description": "插件描述",
      "version": "1.0.0",
      "script": "script.js",
      "runOn": "onPageFinished",
      "enabled": true
    }
  }
}
```

---

## 🛠️ 开发指南

### 开发环境搭建
1. 安装 Android Studio Arctic Fox 或更新版本
2. 安装 Kotlin 插件
3. 配置 Android SDK (API 21+)
4. 克隆项目并导入到 Android Studio

### 构建命令
```bash
# 调试构建
./gradlew assembleDebug

# 发布构建
./gradlew assembleRelease

# 运行测试
./gradlew test

# 代码检查
./gradlew lint
```

### 添加新功能
1. 在相应的目录下创建新的Kotlin文件
2. 遵循MVVM架构模式
3. 使用Jetpack Compose构建UI
4. 编写单元测试
5. 更新文档

---

## ❓ FAQ

**Q: 应用在我的TV上无法安装？**
A: 请确保您的设备运行Android 5.0以上，并启用了"未知来源"安装权限。

**Q: 网页加载很慢怎么办？**
A: 检查网络连接，某些网站可能在TV上加载较慢，这是正常现象。

**Q: 如何添加自己喜欢的网站？**
A: 修改`assets/channels.json`文件，添加您的网站信息。

**Q: 鼠标指针不显示？**
A: 确保已进入页面浏览模式（按确定键），鼠标会在操作时显示。

**Q: 应用崩溃怎么办？**
A: 请提交issue并包含设备信息和崩溃日志。

---

## 🤝 贡献指南

欢迎任何形式的贡献！请遵循以下步骤：

1. **Fork** 本仓库
2. 创建您的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交您的修改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启一个 **Pull Request**

### 代码规范
- 使用 Kotlin 官方代码风格
- 添加适当的注释和文档
- 编写单元测试
- 确保代码通过 lint 检查

### 报告问题
如果您发现了bug或有功能建议，请[创建issue](https://github.com/yourusername/TVBrowser/issues)。

---

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

```
MIT License

Copyright (c) 2024 TVBrowser

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## 📧 联系方式

- **项目主页**: [GitHub Repository](https://github.com/yourusername/TVBrowser)
- **问题反馈**: [Issues](https://github.com/yourusername/TVBrowser/issues)
- **功能建议**: [Discussions](https://github.com/yourusername/TVBrowser/discussions)

---

<div align="center">

**如果这个项目对您有帮助，请给个 ⭐ Star 支持一下！**

Made with ❤️ for Android TV Community

</div> 