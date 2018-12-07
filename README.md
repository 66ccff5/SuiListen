随听音乐播放器
===
  	项目主要仿制了网易云的UI界面，此外实现了本地歌曲列表，底部播放栏划切播放上下歌曲，点击底部播放栏进入音乐播放界面，跳转播放进度，切换播放模式，在线搜索歌曲播放等功能。

实现功能：
---
1.启动页面&本地资源文件加载<br>
2.整体框架设计搭建<br>
3.首页（嵌套的滑动界面）<br>
4.用户界面（侧滑列表）<br>
5.音乐播放界面&音乐进度跳转<br>
6.在线搜索音乐<br>
7.切换播放模式<br>

启动页面
---
![image](https://github.com/PengLeixin/SuiListen/blob/master/app/src/main/res/drawable/gif5_1.gif)
>启动页面由一个activity实现，使用一个Thread1设置其在延时四秒后跳转，在这四秒内会对本地音乐文件进行扫描加载

底部播放栏
---
![image](https://github.com/PengLeixin/SuiListen/blob/master/app/src/main/res/drawable/gif5_4.gif)
>底部播放栏由MediaMetadataRetriever获取MP3文件的封面、歌手、歌名等信息并显示，主要实现了左右划切播放上一首下一首歌曲的功能和点击进入播放页面的功能
，该功能通过在onTouchEvent中判断手指落点与起点的x坐标相减得值大于或小于0判断是左滑还是右滑，在设置其绝对值大于一定值则为滑动播放上一首或下一首歌曲，小于该值则为点击进入播放界面

播放界面
---
![image](https://github.com/PengLeixin/SuiListen/blob/master/app/src/main/res/drawable/gif5_5.gif)
>播放界面主要运用了glide框架、高斯模糊处理框架、animation动画,这个界面显示了更多的歌曲信息，可通过播放暂停实现动画运转，通过进度条的拉缩来调换歌曲的播放进度，同时实现了基础的播放暂停上一曲下一曲以及弹出框选取歌曲功能

主界面
---
![image](https://github.com/PengLeixin/SuiListen/blob/master/app/src/main/res/drawable/gif5_8.gif)
>通过一个activity与五个fragment实现，使用了pagerView与TabLayout进行布局，分为分为音乐，发现，我的，电台，伙伴等五部分，有些部分由于没有UI素材故未实现，还有一个用户侧滑栏由DrawerLayout实现


歌曲播放模式切换
---
![image](https://github.com/PengLeixin/SuiListen/blob/master/app/src/main/res/drawable/gif5_6.gif)
>这个功能的实现需要实现MediaPlayer.OnCompletionListener接口并重写其中onCompletion方法，这个方法会在每首歌播放结束时执行，通过在该方法中判断设置的播放模式来决定接着放下一首还是循环播放还是随机播放

在线搜索歌曲
---
![image](https://github.com/PengLeixin/SuiListen/blob/master/app/src/main/res/drawable/gif5_7.gif)
>该功能为联网搜索歌曲并播放，歌曲的搜索及文件接口都是网易云音乐的api接口，由网友提供

使用的开源框架：
---
1.Glide<br>
2.AndroidImageBlur<br>
3.Okhttp<br>
4.Cardview<br>
5.CircleImageview<br>


使用的设计模式：
---
1.适配器模式<br>
2.单例模式<br>


