
云音乐是什么?  
一个对接网易云的音乐播放器  
  * 内置集成作者自己写的歌词Lrc组件支持单行歌词和多行歌词支持悬浮窗歌词。  
  * 内置适配Android13的通知栏 （不完善请大佬请教）    
  * 对接了网易云的歌单，歌曲，搜索，二维码登录等其余功能。  
  * main.java-内api变量是网易云SDK后台服务器地址（可变更）  
  * http 是我服务器php接口的地址用于软件更新用或者其他功能（个根据需要做出必须的更改或者删除）  

进度：
* UI
  * 歌单
  * 音乐播放选择列表
  * 音乐播放器控制界面
  * 登陆
  * 设置
  * 侧滑栏内小功能
* 代码
  * 适配主题和UI美化
  * 维护接口（不完善）
  * 蓝牙功能
  * 通知栏控制
  * 悬浮歌词功能
  * 基本播放器功能（控制暂停 上下曲 播放歌单操作）
    
![image](main.png)

有问题反馈
在使用中有任何问题，欢迎反馈给我，可以用以下联系方式跟我交流

* [邮件](<a target="_blank" href="http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=MAEJBgYJBAQDAABwQUEeU19d" style="text-decoration:none;"><img src="http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_12.png"/></a>)
* QQ:1966944300
* 后台源码-Github: [网易云音乐 API](https://github.com/Binaryify/NeteaseCloudMusicApi)

开发者说 :  
在兴趣的驱动下,写一个`免费`的东西，有欣喜，也还有汗水，希望你喜欢我的作品，同时也能支持一下。

导入的包
```javascript
dependencies {
    implementation 'androidx.appcompat:appcompat:1.6.1'

    implementation 'com.google.android.material:material:1.9.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'

    implementation 'com.google.code.gson:gson:2.9.1'

    implementation 'com.squareup.okhttp3:okhttp:4.11.0'
    implementation 'com.github.bumptech.glide:glide:4.16.0'
//    废弃的歌词组件
//    implementation 'com.github.wangchenyan:lrcview:2.2.1'
    implementation 'com.google.android.flexbox:flexbox:3.0.0'

    implementation 'androidx.legacy:legacy-support-v4:1.0.0'

//修改音乐标签库
    implementation 'com.mpatric:mp3agic:0.9.1'
}
```

修改JAR的包
```javascript
com.github.QuadFlask:colorpicker:0.0.15
//主要修改内容MD3化
...