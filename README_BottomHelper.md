

## 底部工具类
<table  align="center">
 <tr>
    <th>Fragment</th>
    <th>Viewpager</th>
  </tr>
   <tr>
     <td><a href="url"><img src="https://github.com/LillteZheng/AriesUI/raw/master/gif/cus_fragment.gif" align="left" height="598" width="354"></a></td>
     <td><a href="url"><img src="https://github.com/LillteZheng/AriesUI/raw/master/gif/cus_viewpager.gif" align="left" height="598" width="354"></a></td>
  </tr>

</table>

xml配置：
```
    <com.zhengsr.ariesuilib.wieght.bottom.CusBottomLayout
        android:id="@+id/bottom_ly"
        android:layout_width="match_parent"
        android:layout_height="46dp"
        android:clipChildren="false"
        android:background="@android:color/white"
        >

        <com.zhengsr.ariesuilib.wieght.bottom.CusBottomItemView
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1"
            app:cus_image_margin_top="4dp"
            app:cus_image_size="25dp"
            app:cus_img_normal="@mipmap/home"
            app:cus_img_select="@mipmap/home_focus"
            app:cus_normal_text_color="#8a8a8a"
            app:cus_selected_text_coclor="#1296db"
            app:cus_text="首页"
            app:cus_isChoose="true"
            app:cus_text_margin_top="2dp"
            app:cus_text_size="11sp" />
      ....      
```
直接直接监听接口：
```
mCusBottomLayout = (CusBottomLayout)findViewById(R.id.bottom_ly);
mCusBottomLayout.setBottomClickListener(new CusBottomLayout.IBottomClickListener() {
    @Override
    public void onBottomClick(View view, int curPosition, int prePosition) {
        mViewPager.setCurrentItem(curPosition);
    }
});
```

## 自定义属性说明



| 名称 | 类型 |说明 |
|---|---|---|
|cus_img_normal|reference|正常图片的资源id|
|cus_img_select|reference|点击选中的图片的源id|
|cus_normal_text_color|color,reference|默认字体的颜色|
|cus_selected_text_coclor|color,reference|选中字体的颜色|
|cus_text|reference,string|TextView的字符串|
|cus_text_size|dimension|Textview的字体大小|
|cus_image_size|dimension|ImageView 的大小|
|cus_image_margin_top|dimension|ImageView 相对于父控件向上偏移量|
|cus_text_margin_top|dimension|TextView相对于ImageView 的向上偏移量|
|cus_no_background|boolean|点击默认有水波纹，可以去掉|
|cus_isChoose|boolean|默认是否是选中，首页一般设置为true|
|cus_not_change_img|boolean|是否不改变图片，即像fragment页面中间那个凸起，设置为true|
|cus_change_both|boolean|是否让图片跟着字体改变，像viewpager页面，其实只有一个bitmap|



例子参考:
[activity](https://github.com/LillteZheng/CusBottomHelper/tree/master/app/src/main/java/com/zhengsr/cusbottomhelper/activity)
[xml配置](https://github.com/LillteZheng/CusBottomHelper/tree/master/app/src/main/res/layout)
