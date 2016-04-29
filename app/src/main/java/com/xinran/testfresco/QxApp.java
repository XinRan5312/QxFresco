package com.xinran.testfresco;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
import com.facebook.imagepipeline.image.QualityInfo;

/**
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
