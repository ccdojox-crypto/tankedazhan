# 🚀 GitHub Actions 自动构建指南

## 📋 设置步骤

### 1. 创建GitHub仓库
```bash
git init
git add .
git commit -m "Initial commit: 延迟测试游戏"
git remote add origin https://github.com/YOUR_USERNAME/latency-test-game.git
git push -u origin main
```

### 2. 推送代码到GitHub
推送后GitHub Actions会自动开始构建!

### 3. 下载APK
1. 进入GitHub仓库的 **Actions** 标签页
2. 点击最新的构建任务
3. 在 **Artifacts** 部分下载APK文件

## 🎮 构建版本说明

### Debug APK
- 文件名: `app-debug.apk`
- 用途: 测试和调试
- 自动触发: 每次`main`分支推送

### Release APK
- 文件名: `app-release.apk`
- 用途: 生产版本
- 自动触发: 每次`main`分支推送

### 签名Release APK (可选)
如果你需要签名版本,需要设置以下GitHub Secrets:

1. 生成keystore:
```bash
keytool -genkey -v -keystore release.keystore -alias latencytest -keyalg RSA -keysize 2048 -validity 10000
```

2. 在GitHub仓库设置中添加Secrets:
- `KEYSTORE_BASE64`: keystore文件的base64编码
```bash
base64 -i release.keystore
```

3. 其他Secrets:
- `KEYSTORE_PASSWORD`: keystore密码
- `KEY_ALIAS`: key别名 (如: latencytest)
- `KEY_PASSWORD`: key密码

## 🔄 手动触发构建
你也可以手动触发构建:
1. 进入Actions页面
2. 选择对应的工作流
3. 点击 **Run workflow**

## 📱 安装APK
1. 下载APK文件
2. 在Android设备上启用"未知来源"安装
3. 点击APK文件安装

## 🛠 项目特性
- ⚡ 自动构建,无需本地环境
- 📦 同时生成Debug和Release版本
- 🔄 支持签名版本(可选)
- 📊 自动上传构建产物
- 🏷 支持标签触发GitHub Release

---

推送代码后GitHub Actions会自动开始构建,等待几分钟就能下载APK了!