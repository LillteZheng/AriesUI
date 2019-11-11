<table  align="center">
 <tr>
    <th>颜色选择</th>
  </tr>
   <tr>
    <td><a href="url"><img src="https://github.com/LillteZheng/AriesUI/raw/master/gif/colors.gif" align="left" height="592" width="401"></a></td>
  </tr>

</table>

## xml
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".activity.ColorActivity"
    android:clipChildren="false"
    android:background="#506E7A"
    android:orientation="vertical">

    <View
        android:id="@+id/view"
        android:layout_width="match_parent"
        android:layout_height="100dp"
        />

    <com.zhengsr.ariesuilib.wieght.colors.ColorGradient
        android:id="@+id/colorgradient"
        android:layout_width="match_parent"
        android:layout_height="200dp"
        android:layout_margin="10dp"
        app:gra_circle_radius="5dp"
        app:gra_default_color="#ff0000"/>
<!-- 自动根据长宽选择方向-->
    <com.zhengsr.ariesuilib.wieght.colors.MultiColors
        android:id="@+id/multicolor"
        android:layout_width="200dp"
        android:layout_height="20dp"
        app:mul_tri_show_line="true"
        app:mul_type="tri"
        app:mul_tri_oritation="top"
        app:mul_tri_size="5dp"
        android:layout_marginStart="20dp"
        app:mul_type_color="#FFFFFF"/>
    <!-- 自动根据长宽选择方向-->
    <com.zhengsr.ariesuilib.wieght.colors.MultiColors
        android:id="@+id/multicolor2"
        android:layout_width="20dp"
        android:layout_height="200dp"
        android:layout_margin="10dp"
        app:mul_type="rect"
        app:mul_type_color="#FFFFFF"/>

</LinearLayout>
```

## Java 代码
```
        final View view = findViewById(R.id.view);
        final ColorGradient gradient = findViewById(R.id.colorgradient);
        gradient.addListener(new ColorSelectLiseter() {
            @Override
            public void readyToShow(int color) {

            }

            @Override
            public void onGetColor(int color) {
                view.setBackgroundColor(color);
            }
        });

        MultiColors colors = findViewById(R.id.multicolor);
        colors.addListener(new ColorSelectLiseter() {
            @Override
            public void readyToShow(int color) {
                gradient.color(color);
                view.setBackgroundColor(color);
            }

            @Override
            public void onGetColor(int color) {
                gradient.color(color);
                view.setBackgroundColor(color);
            }
        });

        MultiColors colors2 = findViewById(R.id.multicolor2);
        colors2.addListener(new ColorSelectLiseter() {
            @Override
            public void readyToShow(int color) {
               // gradient.color(color);
               // view.setBackgroundColor(color);
            }

            @Override
            public void onGetColor(int color) {
                gradient.color(color);
                view.setBackgroundColor(color);
            }
        });
```

## 自定义属性说明

**ColorGradient**

| 名称 | 类型 |说明 |
|---|---|---|
|gra_default_color|color,reference|默认颜色值|
|gra_circle_radius|integer，dimension|小球的半径|
|gra_circle_color|color，reference|小球的颜色|

 

**MultiColors**

| 名称 | 类型 |说明 |
|---|---|---|
|mul_type|tri,rect|指示器是三角形还是矩形|
|mul_type_color|color,reference|指示器的颜色|
|mul_tri_oritation|left,top,right,bottom|指示器的方向|
|mul_tri_size|dimension，integer|三角形的大小，只对三角形有用|
|mul_tri_show_line|boolean|是否显示三角形的下面的线，只对三角形有用|
