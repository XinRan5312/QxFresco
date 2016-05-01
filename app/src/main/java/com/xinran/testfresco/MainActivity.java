package com.xinran.testfresco;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    String urlBig = "http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E5%9B%BE%E7%89%87&step_word=&pn=14&spn=0&di=150994728280&pi=&rn=1&tn=baiduimagedetail&is=&istype=0&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=undefined&cs=611483611%2C2895064642&os=2193683596%2C3063830214&simid=0%2C0&adpicid=0&ln=1000&fr=&fmq=1461982725006_R&fm=&ic=undefined&s=undefined&se=&sme=&tab=0&width=&height=&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=&objurl=http%3A%2F%2Fimg05.tooopen.com%2Fimages%2F20150531%2Ftooopen_sy_127457023651.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Bp555rjg_z%26e3Bv54AzdH3FetjoAzdH3Fld8nd0_z%26e3Bip4s&gsm=0&rpstart=0&rpnum=0";
    String urlSmall = "http://pic1.nipic.com/2008-09-08/200898163242920_2.jpg";
    @Bind(R.id.image_view)
    SimpleDraweeView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        downLoadImgWithPipeline(urlSmall);

    }

    /**
     * 当然上面说到它强大了，强大就不止只是加载网络图片吧，它本身也支持本地，Content Provider，asset，res的图片
     * <p/>
     * 本地：file://
     * Contentprovider:  content://
     * asset:  asset://
     * resouse: res://,
     * 其实就是让你拼一个uri
     */
    private void downLoadImgSimple(String url) {
        Uri uri = Uri.parse(url);
        imageView.setImageURI(uri);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * 显示图片的时候直接写了一个setImageURI（uri），
     * Fresco不仅仅提供了这一个方法来显示图片，它还提供了setController（controller）方法加载图片
     * 当然如果你想监听加载的过程，就加一个ControllerListener
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ControllerListener mControllerListener = new BaseControllerListener() {
        @Override
        public void onFailure(String id, Throwable throwable) {
            super.onFailure(id, throwable);
            Toast.makeText(MainActivity.this, "加载图片失败" + throwable.getMessage(), Toast.LENGTH_LONG);
        }

        @Override
        public void onFinalImageSet(String id, Object imageInfo, Animatable animatable) {
            super.onFinalImageSet(id, imageInfo, animatable);
            Toast.makeText(MainActivity.this, "加载图片成功", Toast.LENGTH_SHORT);
        }

        //如果图片设置渐进式
        @Override
        public void onIntermediateImageSet(String id, Object imageInfo) {
            super.onIntermediateImageSet(id, imageInfo);
            Toast.makeText(MainActivity.this, "渐进式", Toast.LENGTH_SHORT);
        }

        @Override
        public void onIntermediateImageFailed(String id, Throwable throwable) {
            super.onIntermediateImageFailed(id, throwable);
        }

        @Override
        public void onRelease(String id) {
            super.onRelease(id);
        }

        @Override
        public void onSubmit(String id, Object callerContext) {
            super.onSubmit(id, callerContext);
        }
    };

    private void downLoadImgWithDraweeController(String url) {
        AbstractDraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(url)
                .setControllerListener(mControllerListener)
                .build();
        imageView.setController(draweeController);
    }

    /**
     * SimpleDraweeView imageView许多xml可以设置的属性通过GenericDraweeHierarchy都可以设置
     * 而且比xml更多
     * <p/>
     * <p/>
     * 例如，我可以设置多张背景图片，我可以设置多张叠加图，这里都可以帮你实现，是不是很强大啊，
     * 想不想拿到特权了一样呢！但是DraweeHiererchy创建时比较耗时，所以要多次利用
     * <p/>
     * GenericDraweeHierarchy hierarchy1 = imageView.getHierarchy();
     * 这个框架不仅仅是这些东西，它还有很多更牛逼的东西，例如：它提供了渐进式加载图片，显示gif动画图片等等
     */
    private void downLoadImgWithHierarchy(String url) {
        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources())
                .setFadeDuration(300)
                .setBackground(getResources().getDrawable(R.mipmap.guide1))
                .setOverlay(getDrawable(R.mipmap.ic_launcher))
                .setPlaceholderImage(getDrawable(R.mipmap.guide3))
                .setFailureImage(getDrawable(R.mipmap.ic_launcher))
                .build();
        imageView.setHierarchy(hierarchy);
        downLoadImgWithDraweeController(url);
    }

    /**
     * PipelineDraweeController是AbstractDraweeController的子类
     * <p/>
     * 当我们要从服务器端下载一张高清图片，图片比较大，下载很慢的情况下有些服务器会提供一张缩略图，
     * 同样的Fresco也支持这种方法，在controller中提供了两个不同的方法setLowResImageRequest和setImageRequest，
     * 看到方法名你应该明白了怎么用
     *
     * @param url
     */
    private void downLoadImgWithPipeline(String url) {
        ImageRequest requestLow = ImageRequestBuilder
                .newBuilderWithResourceId(R.mipmap.ic_launcher)
                .setProgressiveRenderingEnabled(true)
                .build();
        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(url))
                .setProgressiveRenderingEnabled(true)
                .build();
        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources())
                .setFadeDuration(300)
                .setBackground(getResources().getDrawable(R.mipmap.guide1))
                .setOverlay(getDrawable(R.mipmap.ic_launcher))
                .setPlaceholderImage(getDrawable(R.mipmap.guide3))
                .setFailureImage(getDrawable(R.mipmap.ic_launcher))
                .build();
        imageView.setHierarchy(hierarchy);
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)
                .setLowResImageRequest(requestLow)//缩略图
                .setImageRequest(request)//高清图
                .setOldController(imageView.getController())
                .build();

        imageView.setController(controller);
    }

    /**
     * 图片的渐进式加载（网速有点快，如果仔细看的话，是可以看出来的）：
     * <p/>
     * Fresco 支持渐进式的网络JPEG图。在开始加载之后，图会从模糊到清晰渐渐呈现。
     * <p/>
     * 你可以设置一个清晰度标准，在未达到这个清晰度之前，会一直显示占位图。
     * <p/>
     * 渐进式JPEG图仅仅支持网络图。
     * <p/>
     * 第一步，在MyApplication里面配置：
     * <p/>
     * ` ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
     * .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
     * .build();
     * Fresco.initialize(this,config);`
     * 第二步在代码中设置：
     **/
    private void requestImageJPEG(String url) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .setAutoRotateEnabled(true)
                .build();

        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .build();

        imageView.setController(controller);
    }

    /**
     * 多图请求及图片复用
     * <p/>
     * 第一种：先显示低分辨率的图，然后是高分辨率的图
     */
    private void requestImageMuilty(SimpleDraweeView imageView, String lowResUri, String highResUri) {
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setLowResImageRequest(ImageRequest.fromUri(lowResUri))
                .setImageRequest(ImageRequest.fromUri(highResUri))
                .setOldController(imageView.getController())
                .build();
        imageView.setController(controller);

    }

    // 缩略图预览

    private void requestimageThumbnailPreviews(SimpleDraweeView imageView, String uri) {

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri))
                .setLocalThumbnailPreviewsEnabled(true)
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(imageView.getController())
                .build();
        imageView.setController(controller);
    }

    /**
     * 加载最先可用的图片#
     * 但是假设同一张图片有多个 URI 的情况。比如，你可能上传过一张拍摄的照片。原始图片太大而不能上传，
     * 所以图片首先经过了压缩。在这种情况下，首先尝试获取本地压缩后的图片 URI，如果失败的话，尝试获取本地原始图片 URI，
     * 如果还是失败的话，尝试获取上传到网络的图片 URI。直接下载我们本地可能已经有了的图片不是一件光彩的事。
     * Image pipeline 会首先从内存中搜寻图片，然后是磁盘缓存，再然后是网络或其他来源。对于多张图片，
     * 不是一张一张按上面的过程去做，而是 pipeline 先检查所有图片是否在内存。只有没在内存被搜寻到的才会寻找磁盘缓存。
     * 还没有被搜寻到的，才会进行一个外部请求。
     * <p/>
     * <p/>
     * ##### 这些请求中只有一个会被展示。第一个被发现的，无论是在内存，磁盘或者网络，都会是被返回的那个。
     * pipeline 认为数组中请求的顺序即为优先顺序。
     */
    private void requestimageFirstUser(SimpleDraweeView simpleDraweeView, String uri1, String uri2) {
        ImageRequest request = ImageRequest.fromUri(uri1);
        ImageRequest request2 = ImageRequest.fromUri(uri2);
        ImageRequest[] requests = {request, request2};

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setFirstAvailableImageRequests(requests)
                .setOldController(simpleDraweeView.getController())
                .build();
        simpleDraweeView.setController(controller);
    }
}


