## QQ小红点
<table  align="center">
 <tr>
    <th>QQ小红点</th>
  </tr>
   <tr>
     <td><a href="url"><img src="https://github.com/LillteZheng/AriesUI/raw/master/gif/qqpoint.gif" align="left" height="410" width="360"></a></td>
  </tr>

</table>
调用非常简单：

```

<com.zhengsr.ariesuilib.wieght.point.BezierPointView
    android:id="@+id/pointview2"
    android:layout_width="20dp"
    android:layout_height="20dp"
    android:background="@drawable/scan_circle"
    app:bez_default_circleSize="15dp"
    app:bez_draw_circlecSize="20dp"
    app:bez_draw_color="#ff0000"
    app:bez_max_length="50dp"
    app:bez_isCanMove="true"
    app:bez_recovery_bound="10dp"
    android:gravity="center"
    android:text="99+"
    android:textSize="10dp"
    android:textColor="#ffffff"
    app:bez_animator="@animator/alpha_out"
    android:layout_marginEnd="10dp" />
    
```

BezierPointView 是一个 TextView ，所以只需要配置好背景，其他的和TextView 没啥区别。

默认是爆炸效果，如果你想自定义动画，可以使用bez_animator 或者 bez_animation 标签，当然也可以直接写：

```
pointView.listener(false, new BezierPointView.PointViewListener() {
    @Override
    public void destory(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1, 0);
        animator.setDuration(1000);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                pointView.removeView();
            }
        });
        animator.start();
    }
});
```
目前动画不支持 scale 标签，这个跟 windowmanager 添加View 有关，其他 alpha，translation 都是支持的。 

当自己写动画时，需要在动画结束时使用  **removeView()**; 方法，来移除view，防止会有一个透明的windowmanager view
在上面，导致点击不了底下的view；

如果你怕内存泄漏，建议在界面离开的时候，调用 **removeAnim()** 方法；


## 自定义属性说明

**ColorGradient**

| 名称 | 类型 |说明 |
|---|---|---|
|bez_default_circleSize|integer,dimension|默认位置的圆的大小|
|bez_draw_circlecSize|integer,dimension|在BezierPointView的圆的大小|
|bez_max_length|integer,dimension|断掉之前的最大长度|
|bez_draw_color|color,reference|贝塞尔的颜色|
|bez_recovery_bound|integer,dimension|能恢复的范围|
|bez_isCanMove|boolean|是否能移动|
|bez_animator|reference|属性动画，不支持scale属性|
|bez_animation|reference|补间动画，不支持scale属性|
