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



前面我们已经使用过SimpleDraweeView这个控件了，显示图片的时候直接写了一个setImageURI（uri），Fresco不仅仅提供了这一个方法来显示图片，它还提供了setController（controller）方法加载图片

DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .build();
        imageView.setController(controller);
当然如果你想监听加载的过程，就加一个ControllerListen

ControllerListener listener = new BaseControllerListener(){
            @Override
            public void onFinalImageSet(String id, Object imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
            }

            @Override
            public void onIntermediateImageFailed(String id, Throwable throwable) {
                super.onIntermediateImageFailed(id, throwable);
            }
        };
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setControllerListener(listener)
                .build();
        imageView.setController(controller);
图片加载成功或者失败，会执行里面的方法，其中图片加载成功时会执行onFinalImageSet方法，图片加载失败时会执行onFailure方法，如果图片设置渐进式，onIntermediateImageFailed会被回调

说完了如何加载uri之后，如何实现在xml中的效果呢？我们继续在java代码中实现xml的效果

GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources())
                .setFadeDuration(300)
                .setBackground(getDrawable(R.drawable.ic_launcher))
                .setPlaceholderImage(getDrawable(R.drawable.ic_launcher))
                .setFailureImage(getDrawable(R.drawable.ic_launcher))
                .build();
        imageView.setHierarchy(hierarchy);
方法很多，你在xml中用到的都可以在这里设置，有些在xml中不能设置的在这里也是可以的，例如，我可以设置多张背景图片，我可以设置多张叠加图，这里都可以帮你实现，是不是很强大啊，想不想拿到特权了一样呢！但是DraweeHiererchy创建时比较耗时，所以要多次利用

 GenericDraweeHierarchy hierarchy1 = imageView.getHierarchy();
这个框架不仅仅是这些东西，它还有很多更牛逼的东西，例如：它提供了渐进式加载图片，显示gif动画图片等等

首先是渐进式图片加载，这方面的功能充分考虑了网络比较慢的情况下，用户不至于一致在等，最起码能看到模糊的照片，这个所谓的渐进式加载就是说用户从图片加载之后，图片会从模糊到清晰的一个渐变过程，当然这个过程仅限于从网络加载图片，本地或者缓存等地方的图片也不需要渐进式加载，没有意义

ProgressiveJpegConfig config = new ProgressiveJpegConfig() {
            @Override
            public int getNextScanNumberToDecode(int i) {
                return 0;
            }

            @Override
            public QualityInfo getQualityInfo(int i) {
                return null;
            }
        };

        ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(this)
                .setProgressiveJpegConfig(config)
                .build();
        Fresco.initialize(getApplicationContext(),imagePipelineConfig);
当然你也可以使用ProgressiveJpegConfig config1= new SimpleProgressiveJpegConfig(list,2);
FLog.setMinimumLoggingLevel(FLog.VERBOSE);
        Set<RequestListener> listeners = new HashSet<>();
        listeners.add(new RequestLoggingListener());
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setRequestListeners(listeners)
                .build();
        Fresco.initialize(this, config);
        setContentView(R.layout.activity_main);

        mProgressiveJpegView = (SimpleDraweeView) findViewById(R.id.my_image_view);

        Uri uri = Uri.parse("http://pooyak.com/p/progjpeg/jpegload.cgi?o=1");
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .build();
        mProgressiveJpegView.setController(controller);
ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(imageView.getController())
                .build();
        imageView.setController(controller);
哎吆，不错哦，可是这个image pipeline这个又是个什么啊？它的来头比较大，负责图片的加载工作

1.检查内存缓存，如有，返回

2.后台线程开始后续工作

3.检查是否在未解码内存缓存中。如有，解码，变换，返回，然后缓存到内存缓存中。

4.检查是否在文件缓存中，如果有，变换，返回。缓存到未解码缓存和内存缓存中。

5.从网络或者本地加载。加载完成后，解码，变换，返回。存到各个缓存中。

继续看gif图片,其实跟显示图片没什么差，主要是动态图片涉及到的动画的停止与播放，如果只是单纯的试用一下，那就直接在controller里面设置setAutoPlayAnimation为true，如果你想手动监听就new一个ControllerListener里面手动控制

当我们要从服务器端下载一张高清图片，图片比较大，下载很慢的情况下有些服务器会提供一张缩略图，同样的Fresco也支持这种方法，在controller中提供了两个不同的方法setLowResImageRequest和setImageRequest，看到方法名你应该明白了怎么用

个人认为这个框架最巧妙的地方，就是把bitmap保存到ashmen，不会启动gc，使的界面不会因为gc而卡死，Fresco使用三级缓存，第一级缓存就是保存bitmap，第二级缓存保存在内存，但是没有解码，使用时需要界面，第三级缓存就是保存在本地文件，同样文件也未解码，使用的时候要先解码啦！

上面谈到的保存的很多内容都未解码，这也是fresco默认使用3个线程的原因，一个线程用来加载uri，一个线程用来解码，最后一个你知道它做什么，其余你想了解的东西自己去官网找找。
