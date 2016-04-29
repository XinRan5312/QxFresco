package com.xinran.testfresco;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.image_view)
    SimpleDraweeView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        downLoadImgWithHierarchy("http://pic1.nipic.com/2008-09-08/200898163242920_2.jpg");

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
            Toast.makeText(MainActivity.this, "加载图片成功", Toast.LENGTH_LONG);
        }

        //如果图片设置渐进式
        @Override
        public void onIntermediateImageSet(String id, Object imageInfo) {
            super.onIntermediateImageSet(id, imageInfo);
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

}
