<table  align="center">
 <tr>
    <th>弧形、圆形与封装的EditText</th>
  </tr>
   <tr>
     <td><a href="url"><img src="https://github.com/LillteZheng/AriesUI/raw/master/gif/login.png" align="left" height="598" width="354"></a></td>
  </tr>

</table>

## 弧形图片的配置
```
<com.zhengsr.ariesuilib.wieght.ArcImageView
    android:layout_width="match_parent"
    android:layout_height="260dp"
    app:arc_height="-30dp"
    app:arc_scaleFactor="1.5"
    app:arc_scaleX="120dp"
    app:arc_scaleY="0dp"
    app:arc_blur="12"
    app:layout_constraintTop_toTopOf="parent"
    android:src="@mipmap/liuqi"/>
```
配置很简单，支持动态设置

## 圆形图片
```
<com.zhengsr.ariesuilib.wieght.CircleImageView
  android:id="@+id/user_icon"
  android:layout_width="100dp"
  android:layout_height="100dp"
  android:src="@mipmap/liuqi"
  android:layout_marginTop="200dp"
  android:layout_gravity="center_horizontal"
  app:cv_boardColor="@color/colorAccent"
  app:layout_constraintTop_toTopOf="parent"
  app:layout_constraintLeft_toLeftOf="parent"
  app:layout_constraintRight_toRightOf="parent"
  app:cv_borderWidth="3dp"
  />
```
没啥说的，跟github的圆形配置一样，只是不想多compile而已

## 封装的EditText
一般配置如下：
```
<com.zhengsr.ariesui.PasswordView
    android:layout_width="match_parent"
    android:layout_height="44dp"
    android:layout_marginTop="20dp"
    app:layout_constraintTop_toBottomOf="@+id/user_name"
    android:layout_marginStart="60dp"
    android:layout_marginEnd="60dp"
    app:iv_edHintColor="#999999"
    app:iv_edTextColor="@color/wechat"
    app:iv_edTextSize="16sp"
    app:iv_bottomColor="@color/main_color"
    app:iv_bottomSize="1dp"
    />
```
其中 PasswordView 为自己定义的View，只需要继承InputView，再去配置就可以了，比如：
```
public class PasswordView extends InputView {
    private static final String TAG = "UserName";

    public PasswordView(Context context) {
        this(context,null);
    }

    public PasswordView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public ImageView[] leftImageView() {
        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.lock);
        return new ImageView[]{imageView};
    }

    @Override
    public ImageView[] rightImageView() {
        ImageView clearView = new ImageView(getContext());
        clearView.setClickable(true);
        clearView.setVisibility(INVISIBLE);
        clearView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        clearView.setImageResource(R.mipmap.clear);
        clearView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setText("");
            }
        });

        final ImageView eyeView = new ImageView(getContext());
        eyeView.setClickable(true);
        eyeView.setVisibility(INVISIBLE);
        eyeView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        eyeView.setImageResource(R.mipmap.eye_close);
        eyeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdHideMode()){
                    changeEdPassMode(true);
                    eyeView.setImageResource(R.mipmap.eye_close);
                }else{
                    changeEdPassMode(false);
                    eyeView.setImageResource(R.mipmap.eye);
                }
            }
        });
        return new ImageView[]{clearView,eyeView};
    }

    @Override
    public void config() {
        super.config();
        changeEdPassMode(true);
        mEditText.setHint("请输入密码");
    }

    @Override
    public int[] getRightImgSize() {
        int width =  getIntDpSize(20);
        int height =  getIntDpSize(20);
        return new int[]{width,height};
    }

    @Override
    public int[] getLeftImgSize() {
        int width =  getIntDpSize(22);
        int height =  getIntDpSize(22);
        return new int[]{width,height};
    }
    

}

```


## 自定义属性说明

**弧形图片 ArcImageView**

| 名称 | 类型 |说明 |
|---|---|---|
|arc_height|dimension|弧度的高度|
|arc_blur|integer|图片的模糊度，0到25|
|arc_use_color|color，reference|是否使用背景色|
|arc_scaleX|dimension|缩放中心点X方向|
|arc_scaleY|dimension|缩放中心点Y方向|
|arc_scaleFactor|float|缩放比例|
|arc_auto_fix|boolean|自动适配大小，如果用glide，可以忽略,默认为true|



**CircleImageView**

| 名称 | 类型 |说明 |
|---|---|---|
|cv_boardColor|color,reference|外部圆圈的颜色|
|cv_borderWidth|dimension|外部圆圈的大小|

**InputView**

| 名称 | 类型 |说明 |
|---|---|---|
|iv_edTextColor|color,reference|edittext的text颜色|
|iv_edHintColor|color,reference|edittext的hint的颜色|
|iv_edTextSize|dimension|edittext的hint的字体大小|
|iv_bottomColor|color,reference|下划线的颜色|
|iv_bottomSize|dimension|下划线的大小|
