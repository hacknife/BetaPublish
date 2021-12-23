# BetaPublish
![](https://img.shields.io/badge/platform-android-orange.svg)
![](https://img.shields.io/badge/language-java-yellow.svg)
![](https://jitpack.io/v/iwdael/betapublish.svg)
![](https://img.shields.io/badge/build-passing-brightgreen.svg)
![](https://img.shields.io/badge/license-apache--2.0-green.svg)
![](https://img.shields.io/badge/api-19+-green.svg)

简化蒲公英发布流程，支持蒲公英在线检测更新
## 发布
配置工程根目录build.gradle
```
buildscript {
    repositories {
        maven { url 'https://jitpack.io' }
    }
    dependencies {
        classpath 'com.iwdael.betapublish:publish:0.1.1'
    }
}
```
配置App Module下build.gradle
```
apply plugin: 'com.iwdael.betapublish'

beta {
    apiKey = 'xxxxx'
    file = 'xxxxx' //apk路径
    installType = "2"
    password = "xxxxx"
    updateDescription = 'xxxx'
}
```
查看配置信息:
```
Gradle -> 项目名 -> App module name -> Tasks -> betapublish -> info
```
发布:
```
Gradle -> 项目名 -> App module name -> Tasks -> betapublish -> publish pgyer
```
发布完成后，即可在蒲公英后台查看新发布的版本

## 更新
配置工程根目录build.gradle
```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
配置App Module下build.gradle
```
dependencies {
    debugImplementation com.iwdael.betapublish:install-debug:0.1.1
    releaseImplementation com.iwdael.betapublish:install-release:0.1.1
}
```
初始化
```
     Install.init()
```
检测并弹出更新App提示
```
    Install.install()
```