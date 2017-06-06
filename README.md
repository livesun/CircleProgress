## 效果
![grad](https://cloud.githubusercontent.com/assets/27534854/26824126/b0ad978a-4ae2-11e7-9011-8db76bb289db.gif)

## 功能
- 一个炫酷的环形进度条，提供三种颜，支持自定义颜色，支持最大200%的进度展示。
## 如何使用


```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

```
	dependencies {
	
		compile 'com.github.livesun:CircleProgress:v1.0'
	}
```
xml布局文件
```
<test.com.duanzi.CircleProgress
        android:layout_margin="20dp"
        android:id="@+id/pro"
        android:layout_width="180dp"
        android:layout_height="180dp"
        app:progress_width="10dp"
        app:inner_color="@color/colorPrimary"
        app:outer_color="@color/colorAccent"
        />
```
activty的调用

```
progress = (CircleProgress) findViewById(R.id.pro);
//最大值，当前值，时间
progress.show(10000,12000,3000);
```
## 有问题反馈 在使用中有任何问题，欢迎反馈给我，可以用以下联系方式跟我交流

- QQ: 2399747642
