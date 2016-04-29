# QxFresco
深入浅出的研究Fresco--ok，图片下载，缓存，图片移除都是Fresco来处理，是不是很强大
1.配置环境

由于我用的是android studio所以这里就只是说一下android studio下如何配置，在强大的gradle，只需要一句话搞定，gradle会帮你下载这个Fresco框架，gradle真好，可以自动维护你项目中的框架

compile 'com.facebook.fresco:fresco:0.5.0+'
2.开始使用Fresco

因为我这里加载的是一张网络图片，所以要获得网络权限

<uses-permission android:name="android.permission.INTERNET"/>
初始化Fresco，如果项目中多处用到Fresco，就直接在application中初始化，如果我只是写着玩玩，直接放在activity中的setContentView()的前面就ok了

Fresco.initialize(context);
用人家的框架就要按照人家的要求来是不，所以控件名字，命名空间都要听人家的

<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:fresco="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.facebook.drawee.view.SimpleDraweeView
        android:id="@+id/image_view"
        android:layout_width="300dp"
        android:layout_height="300dp"
        fresco:placeholderImage="@mipmap/ic_launcher"/>

</LinearLayout>
当然你也可以不写它的命名空间，用里面的属性的时候加上去，不用就别给自己找麻烦了，直接干掉给控件uri就ok了

Uri uri = Uri.parse("http://pic1.nipic.com/2008-09-08/200898163242920_2.jpg");
        imageView.setImageURI(uri);
ok，剩下的图片下载，缓存，图片移除就交给Fresco了，是不是很强大，但这里有一个问题，你的控件的大小必须确定，不能想以前直接使用wrap_content,当然你也可以设置宽高中的一个值，但是要设定宽高比

imageView.setAspectRatio(1.0f);
当然上面说到它强大了，强大就不止只是加载网络图片吧，它本身也支持本地，Content Provider，asset，res的图片

本地：file:// Content provider:content://  asset:  asset://  res: res://,就是让你拼一个uri

当然上面提到的SimpleDraweeView只是Drawee其中的控件，没有什么很特别的需求使用它就够了，下面贴一下它里面的一些属性，方便食用的时候查找

<com.facebook.drawee.view.SimpleDraweeView
    android:id="@+id/image_view"
    android:layout_width="300dp"
    android:layout_height="300dp"
    fresco:fadeDuration="300"
    fresco:actualImageScaleType="focusCrop"
    fresco:placeholderImage="@color/wait_color"
    fresco:placeholderImageScaleType="fitCenter"
    fresco:failureImage="@drawable/error"
    fresco:failureImageScaleType="centerInside"
    fresco:retryImage="@drawable/retrying"
    fresco:retryImageScaleType="centerCrop"
    fresco:progressBarImage="@drawable/progress_bar"
    fresco:progressBarImageScaleType="centerInside"
    fresco:progressBarAutoRotateInterval="1000"
    fresco:backgroundImage="@color/blue"
    fresco:overlayImage="@drawable/watermark"
    fresco:pressedStateOverlayImage="@color/red"
    fresco:roundAsCircle="false"
    fresco:roundedCornerRadius="1dp"
    fresco:roundTopLeft="true"
    fresco:roundTopRight="false"
    fresco:roundBottomLeft="false"
    fresco:roundBottomRight="true"
    fresco:roundWithOverlayColor="@color/corner_color"
    fresco:roundingBorderWidth="2dp"
    fresco:roundingBorderColor="@color/border_color"
  />
简单介绍一下上面的属性：

placeholderImage就是所谓的展位图啦，在图片没有加载出来之前你看到的就是它

failureIamge看到名字就知道是什么了，图片加载失败时显示的图片就是它了

retryImage图片加载失败时显示，提示用户点击重新加载，重复加载4次还是没有加载出来的时候才会显示failureImage的图片

progressBarImage进度条图片

backgroundImage背景图片，这里的背景图片首先被绘制

overlayImage设置叠加图，在xml中只能设置一张叠加图片，如果需要多张图片的话，需要在java代码中设置哦

pressedStateOverlayImage设置点击状态下的叠加图，此叠加图不能缩放

ImageScaleType这个就是各种各样的图片缩放样式了，center，centerCrop，fouseCrop，centerInside，fitCenter，fitStart，fitEnd，fitXY

剩下的就是对圆角的处理了…
