package com.github.alenastan.clientvkontakte.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.widget.ImageView;

import com.github.alenastan.clientvkontakte.CoreApplication;
import com.github.alenastan.clientvkontakte.helper.DataManager;
import com.github.alenastan.clientvkontakte.os.LIFOLinkedBlockingDeque;
import com.github.alenastan.clientvkontakte.processing.BitmapProcessor;
import com.github.alenastan.clientvkontakte.processing.Processor;
import com.github.alenastan.clientvkontakte.source.DataSource;
import com.github.alenastan.clientvkontakte.source.HttpDataSource;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by lena on 25.01.2015.
 */
public class ImageLoader {

    public static final String KEY = "ImageLoader";

    public static final int MAX_SIZE = 1 * 1024 * 1024;

    private AtomicBoolean isPause = new AtomicBoolean(false);

    public static ImageLoader get(Context context) {
        return CoreApplication.get(context, KEY);
    }

    private class ComparableRunnable implements Runnable {

        private Handler mHandler;

        private DataManager.Callback<Bitmap> callback;
        private String s;
        private DataSource<InputStream, String> dataSource;
        private Processor<Bitmap, InputStream> processor;

        public ComparableRunnable(Handler handler, DataManager.Callback<Bitmap> callback, String s, DataSource<InputStream, String> dataSource, Processor<Bitmap, InputStream> processor) {
            mHandler = handler;
            this.callback = callback;
            this.s = s;
            this.dataSource = dataSource;
            this.processor = processor;
        }

        @Override
        public void run() {
            try {
                InputStream result = dataSource.getResult(s);
                final Bitmap bitmap = processor.process(result);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onDone(bitmap);
                    }
                });
            } catch (final Exception e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onError(e);
                    }
                });
            }
        }
    }

    private Context mContext;

    private DataSource<InputStream, String> mDataSource;

    private Processor<Bitmap, InputStream> mProcessor;

    private DataManager.ImageLoader<Bitmap, InputStream, String> mImageLoader;

    private LruCache<String, Bitmap> mLruCache = new LruCache<String, Bitmap>(MAX_SIZE) {

        @Override
        protected int sizeOf(String key, Bitmap value) {
            return (value.getWidth() * value.getHeight())*32 + key.length();
        }
    };

    public ImageLoader(Context context, DataSource<InputStream, String> dataSource, Processor<Bitmap, InputStream> processor) {
        this.mContext = context;
        this.mDataSource = dataSource;
        this.mProcessor = processor;
        this.mImageLoader = new DataManager.ImageLoader<Bitmap, InputStream, String>() {

            private ExecutorService executorService = new ThreadPoolExecutor(5, 5, 0, TimeUnit.MILLISECONDS,
                    new LIFOLinkedBlockingDeque<Runnable>());

            @Override
            public void load(final DataManager.Callback<Bitmap> callback, final String s, final DataSource<InputStream, String> dataSource, final Processor<Bitmap, InputStream> processor) {
                callback.onDataLoadStart();
                final Looper looper = Looper.myLooper();
                final Handler handler = new Handler(looper);
                executorService.execute(new ComparableRunnable(handler, callback, s, dataSource, processor));
            }

        };
    }

    public ImageLoader(Context context) {
        this(context, HttpDataSource.get(context), new BitmapProcessor());
    }

    public void pause() {
        isPause.set(true);
    }

    private final Object mDelayedLock = new Object();

    public void resume() {
        isPause.set(false);
        synchronized (mDelayedLock) {
            for (ImageView imageView : delayedImagesViews) {
                Object tag = imageView.getTag();
                if (tag != null) {
                    loadAndDisplay((String) tag, imageView);
                }
            }
            delayedImagesViews.clear();
        }
    }

    private Set<ImageView> delayedImagesViews = new HashSet<ImageView>();

    public void loadAndDisplay(final String url, final ImageView imageView) {
        Bitmap bitmap = mLruCache.get(url);
        imageView.setImageBitmap(bitmap);
        imageView.setTag(url);
        if (bitmap != null) {
            return;
        }
        if (isPause.get()) {
            synchronized (mDelayedLock) {
                delayedImagesViews.add(imageView);
            }
            return;
        }
        if (!TextUtils.isEmpty(url)) {
            DataManager.loadData(new DataManager.Callback<Bitmap>() {
                @Override
                public void onDataLoadStart() {

                }

                @Override
                public void onDone(Bitmap bitmap) {
                    if (bitmap != null) {
                        mLruCache.put(url, bitmap);
                    }
                    if (url.equals(imageView.getTag())) {
                        imageView.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onError(Exception e) {

                }

            }, url, mDataSource, mProcessor, mImageLoader);
        }
    }
}