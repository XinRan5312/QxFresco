package com.xinran.testfresco;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
import com.facebook.imagepipeline.image.QualityInfo;

/**
 * 个人认为这个框架最巧妙的地方，就是把bitmap保存到ashmen，不会启动gc，
 * 使的界面不会因为gc而卡死，Fresco使用三级缓存，第一级缓存就是保存bitmap，
 * 第二级缓存保存在内存，但是没有解码，使用时需要界面，第三级缓存就是保存在本地文件，
 * 同样文件也未解码，使用的时候要先解码啦！
 * Created by qixinh on 16/4/29.
 */
public class QxApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 首先是渐进式图片加载，这方面的功能充分考虑了网络比较慢的情况下，用户不至于一致在等，
         * 最起码能看到模糊的照片，这个所谓的渐进式加载就是说用户从图片加载之后，图片会从模糊到清晰的一个渐变过程，
         * 当然这个过程仅限于从网络加载图片，本地或者缓存等地方的图片也不需要渐进式加载，没有意义
         *
         * 当然你也可以使用ProgressiveJpegConfig config1= new SimpleProgressiveJpegConfig(list,2);
         *
         * 这个image pipeline这个又是个什么啊？它的来头比较大，负责图片的加载工作

         1.检查内存缓存，如有，返回

         2.后台线程开始后续工作

         3.检查是否在未解码内存缓存中。如有，解码，变换，返回，然后缓存到内存缓存中。

         4.检查是否在文件缓存中，如果有，变换，返回。缓存到未解码缓存和内存缓存中。

         5.从网络或者本地加载。加载完成后，解码，变换，返回。存到各个缓存中。
         */
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
    }
}
