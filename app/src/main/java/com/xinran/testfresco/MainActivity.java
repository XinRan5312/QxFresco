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
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
String urlBig="http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E5%9B%BE%E7%89%87&step_word=&pn=14&spn=0&di=150994728280&pi=&rn=1&tn=baiduimagedetail&is=&istype=0&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=undefined&cs=611483611%2C2895064642&os=2193683596%2C3063830214&simid=0%2C0&adpicid=0&ln=1000&fr=&fmq=1461982725006_R&fm=&ic=undefined&s=undefined&se=&sme=&tab=0&width=&height=&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=&objurl=http%3A%2F%2Fimg05.tooopen.com%2Fimages%2F20150531%2Ftooopen_sy_127457023651.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Bp555rjg_z%26e3Bv54AzdH3FetjoAzdH3Fld8nd0_z%26e3Bip4s&gsm=0&rpstart=0&rpnum=0";
    String urlSmall="http://pic1.nipic.com/2008-09-08/200898163242920_2.jpg";
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
     *
     *
     *
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
     *
     *
     *  例如，我可以设置多张背景图片，我可以设置多张叠加图，这里都可以帮你实现，是不是很强大啊，
     *  想不想拿到特权了一样呢！但是DraweeHiererchy创建时比较耗时，所以要多次利用

        GenericDraweeHierarchy hierarchy1 = imageView.getHierarchy();
        这个框架不仅仅是这些东西，它还有很多更牛逼的东西，例如：它提供了渐进式加载图片，显示gif动画图片等等
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
     *
     * 当我们要从服务器端下载一张高清图片，图片比较大，下载很慢的情况下有些服务器会提供一张缩略图，
     * 同样的Fresco也支持这种方法，在controller中提供了两个不同的方法setLowResImageRequest和setImageRequest，
     * 看到方法名你应该明白了怎么用
     * @param url
     */
private void downLoadImgWithPipeline(String url){
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
}
