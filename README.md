# Lyra UI

<div align="center">

**🎨 一个现代化、高性能的 Android UI 框架**

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg)](https://android-arsenal.com/api?level=24)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-purple.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-1.7.5-blue.svg)](https://developer.android.com/jetpack/compose)

</div>

---

## ✨ 特性

### 🎯 核心功能

- **🎨 三大面板系统** - 优雅的左侧、右侧、底部面板，支持手势交互
- **⚡ 高性能动画** - 流畅的缩放和模糊效果，支持快速中断
- **🎭 手势交互** - 直观的滑动、拖拽、点击手势
- **🎛️ 动态设置** - 可扩展的设置系统，防止代码臃肿
- **🎪 自定义组件** - Q弹 Switch、无 Ripple 效果等

### 🌍 国际化支持

支持 **9 种语言**，覆盖 **45 亿+** 用户：

- 🇺🇸 English
- 🇪🇸 Español
- 🇨🇳 简体中文
- 🇹🇼 繁體中文
- 🇫🇷 Français
- 🇩🇪 Deutsch
- 🇵🇹 Português
- 🇯🇵 日本語
- 🇸🇦 العربية (RTL)

### 🚀 性能优化

- **条件渲染** - 未打开的面板零内存开销
- **动画中断** - 毫秒级响应，极限流畅
- **智能缓存** - 减少不必要的重组
- **硬件加速** - 充分利用 GPU 渲染

### 🎨 主题系统

- **Material 3** - 遵循最新设计规范
- **动态配色** - 支持 Material You
- **日夜模式** - 自动跟随系统
- **扩展颜色** - 自定义品牌色

---

## 📱 系统要求

| 项目 | 要求 |
|------|------|
| **最低 Android 版本** | Android 7.0 (API 24) |
| **目标 Android 版本** | Android 14 (API 34) |
| **开发语言** | Kotlin 2.0.21 |
| **UI 框架** | Jetpack Compose |
| **构建工具** | Gradle 8.7 |

---

## 🛠️ 技术栈

- **架构模式**: MVVM
- **UI 框架**: Jetpack Compose
- **依赖注入**: Hilt
- **状态管理**: StateFlow + ViewModel
- **异步处理**: Kotlin Coroutines
- **数据持久化**: SharedPreferences

---

## 📦 集成指南

### 1. 克隆项目

```bash
git clone https://github.com/你的用户名/LyraUI.git
cd LyraUI
```

### 2. 在 Android Studio 中打开

```
File → Open → 选择 LyraUI 目录
```

### 3. 同步 Gradle

等待 Gradle 自动同步依赖

### 4. 运行项目

点击 Run 按钮或按 `Shift + F10`

---

## 🎯 使用示例

### 基础使用

```kotlin
@Composable
fun MyApp() {
    LyraScaffold(
        leftPanelContent = { /* 左侧面板内容 */ },
        rightPanelContent = { /* 右侧面板内容 */ },
        bottomPanelContent = { /* 底部面板内容 */ }
    ) {
        // 你的主界面内容
        Text("Hello Lyra UI!")
    }
}
```

### 自定义设置

```kotlin
// 在 ViewModel 中
viewModel.setEnableHomeScale(true)  // 启用首页缩放
viewModel.setEnableHomeBlur(true)   // 启用首页模糊
```

---

## 🏗️ 项目结构

```
LyraUI/
├── app/src/main/java/com/petehsu/lyraui/
│   ├── ui/
│   │   ├── home/              # 主界面
│   │   │   ├── MainScreen.kt  # 主屏幕
│   │   │   ├── MainViewModel.kt
│   │   │   ├── panel/         # 面板系统
│   │   │   └── gesture/       # 手势处理
│   │   ├── onboarding/        # 引导页
│   │   ├── scaffold/          # 脚手架
│   │   └── theme/             # 主题系统
│   ├── data/                  # 数据层
│   └── di/                    # 依赖注入
├── app/src/main/res/
│   ├── values/                # 默认资源
│   ├── values-v27/            # API 27+ 资源
│   ├── values-{语言}/         # 多语言资源
│   └── ...
└── gradle/                    # Gradle 配置
```

---

## 🎨 核心组件

### LyraScaffold

统一的脚手架组件，提供完整的面板系统支持。

### LyraPanelContainer

通用面板容器，支持左侧、右侧、底部三种类型。

### LyraSwitch

自定义 Switch 组件，带有 Q 弹动画效果。

### LyraPanelGestureHandler

手势处理器，支持滑动、拖拽、点击等交互。

---

## 🤝 贡献指南

欢迎贡献代码！请遵循以下步骤：

1. **Fork** 本仓库
2. 创建你的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交你的改动 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启一个 **Pull Request**

### 编码规范

- 使用 Kotlin 编写代码
- 遵循 [Kotlin 官方编码规范](https://kotlinlang.org/docs/coding-conventions.html)
- 确保代码通过 Detekt 和 Ktlint 检查
- 所有面向用户的字符串必须在 `strings.xml` 中定义

---

## 📄 开源协议

本项目采用 [Apache License 2.0](LICENSE) 开源协议。

```
Copyright 2025 Pete Hsu

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

---

## 🙏 致谢

- [Jetpack Compose](https://developer.android.com/jetpack/compose) - 现代 Android UI 工具包
- [Material Design 3](https://m3.material.io/) - 设计系统
- [Kotlin](https://kotlinlang.org/) - 现代编程语言

---

## 📧 联系方式

- **作者**: Pete Hsu
- **邮箱**: [你的邮箱]
- **GitHub**: [@你的用户名](https://github.com/你的用户名)

---

## 📊 项目状态

- ✅ **代码质量**: 生产级（0 Errors）
- ✅ **Lint 检查**: 通过
- ✅ **API 兼容**: Android 7.0+
- ✅ **国际化**: 9 种语言
- ✅ **性能优化**: 已完成

---

<div align="center">

**如果这个项目对你有帮助，请给一个 ⭐️ Star 支持一下！**

Made with ❤️ by Pete Hsu

</div>

