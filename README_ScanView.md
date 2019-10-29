<table  align="center">
 <tr>
    <th>扫描动画</th>
  </tr>
   <tr>
     <td><a href="url"><img src="https://github.com/LillteZheng/AriesUI/raw/master/gif/scanview.gif" align="left" height="200" width="340"></a></td>
  </tr>

</table>

## xml
```
<com.zhengsr.ariesuilib.wieght.ScanView
    android:id="@+id/scan"
    android:layout_width="200dp"
    android:layout_gravity="center"
    app:scan_sweep_color="#14D3BB"
    app:scan_sweep_time="3000"
    app:scan_anim_auto="true"
    app:scan_circle_color="#ffffff"
    app:scan_circle_num="2"
    app:scan_random_color="#14D3BB"
    app:scan_random_num="3"
    app:scan_show_dash="true"
    app:scan_show_random="true"
    android:layout_height="200dp"/>
```

## Java 代码
```
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scan_view);
    mScanView = findViewById(R.id.scan);
}

@Override
protected void onResume() {
    super.onResume();
    mScanView.start();
}

@Override
protected void onDestroy() {
    super.onDestroy();
    mScanView.stop();
}
```

## 自定义属性说明

 <declare-styleable name="ScanView">
        
    
        <attr name="scan_show_random" format="boolean"/>
        <attr name="scan_show_dash" format="boolean"/>
        <attr name="scan_anim_auto" format="boolean"/>
        <attr name="scan_dash_color" format="color|reference"/>
    </declare-styleable>

**ScanView**

| 名称 | 类型 |说明 |
|---|---|---|
|scan_sweep_color|color,reference|扫描的颜色|
|scan_circle_num|integer|圆圈的个数|
|scan_circle_color|color，reference|圆圈的颜色|
|scan_sweep_time|integer|扫描一圈的时间|
|scan_random_num|integer|随机小球的个数|
|scan_random_color|color，reference|随机小球的颜色|
|scan_show_random|boolean|是否显示随机小球|
|scan_show_dash|boolean|是否显示虚线|
|scan_dash_color|color,reference|虚线的颜色|
|scan_anim_auto|boolean|是否播放动画|

 

