package com.zcdyy.personalparameter.views.showimage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zcdyy.personalparameter.R;

import java.io.File;


import pl.droidsonroids.gif.GifDrawable;

public class MyImageView extends View implements
        View.OnClickListener {
    protected static BitmapUtils bmUtils;
    protected static HttpUtils httpUtils;
    private static int windowWidth;
    private static int windowHeight;
    public boolean theEnd;//所有图片视图均已列出
    protected HttpHandler<File> httpHandler;
    protected int maxHeight = Config.maxHeight;
    protected long progress;
    protected long max = 100;
    boolean 可以启动动画 = true;
    private String address;
    private String address2;
    private boolean isGif;
    private boolean 是否缩放 = Config.sf;
    private onLodingOverListener onOver;
    private int height;
    private int width;
    private int jiaodu;
    protected Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            jiaodu += 3;
            jiaodu = jiaodu % 360;
            postInvalidate();
            handler.sendEmptyMessageDelayed(0, 5);
        }
    };

    {
        if (windowWidth == 0) {
            WindowManager wm = (WindowManager) getContext()
                    .getSystemService(Context.WINDOW_SERVICE);
            windowWidth = wm.getDefaultDisplay().getWidth();
            windowHeight = wm.getDefaultDisplay().getHeight();
        }
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
        if (bmUtils == null) {
            bmUtils = new BitmapUtils(context);
        }
        if (httpUtils == null) {
            httpUtils = new HttpUtils(3000);
        }
    }

    public MyImageView(Context context) {
        super(context);
        setOnClickListener(this);
        if (bmUtils == null) {
            bmUtils = new BitmapUtils(context);
        }
        if (httpUtils == null) {
            httpUtils = new HttpUtils(3000);
        }
    }

    /**
     * 请为图片设置是否进行缩放
     * {@link #是否缩放(boolean)}
     * {@link Config#设置默认缩放(boolean)}
     *
     * @param context
     * @param 缩放
     */
    public MyImageView(Context context, boolean 缩放) {
        this(context);
        是否缩放(缩放);
    }

    /**
     * 默认开启缩放,可通过{@link Config#设置默认缩放(boolean)}设置全局缩放功能是否开启,
     * 也可通过本方法为每个图片单独设置
     * 当缩放为true时,
     * 图片将限制最高高度为{@link #maxHeight},
     * 并将图片清晰度设置为{@link Bitmap.Config#RGB_565},
     * 否则为{@link Bitmap.Config#ARGB_8888}
     *
     * @param 是否缩放
     */
    public void 是否缩放(boolean 是否缩放) {
        this.是否缩放 = 是否缩放;
    }

    /**
     * 设置当前图片在缩放启用时的最大高度,默认为{@link Config#setMaxHeight(int)}设置的值,若没有设置,则为屏幕高度
     *
     * @param maxHeight
     */
    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    /**
     * 可以在任何时机中断下载并停止动画
     */
    public void 初始化() {
        // ViewGroup parent = (ViewGroup) getParent();
        // if(parent!=null)
        // parent.removeView(this);
        Log.e("TGAI", "初始化()");
        handler.removeCallbacksAndMessages(null);
        if (httpHandler != null)
            httpHandler.cancel();

        setBackgroundColor(Color.WHITE);
        address = null;
        address2 = null;
        isGif = false;
        max = 100;
        progress = 0;
        可以启动动画 = true;
        postInvalidate();
    }

    public void setOnOverListener(onLodingOverListener onOver) {
        this.onOver = onOver;
    }

    /**
     * 当该方法被调用,标志着所有等待加载的MyImageView已全部列出,可以开始加载了,
     * 本方法仅在使用{@link #setAddress(String, String, boolean, String, String, MyImageView)}时有效.
     */
    public void theEnd() {
        theEnd = true;
    }

    /**
     * 若希望多个图片依次加载请使用本方法
     * 并在所有希望依次加载的图片都列出后调用最后一个MyImageView的{@link #theEnd()}方法
     * 另见{@link #setAddress(String, String, boolean, String, String)}
     *
     * @param myImageView 后一个MyImageView的引用
     * @return 自身
     */
    public MyImageView setAddress(final String address, final String address2, final boolean isGif, final String width2, final String height2, MyImageView myImageView) {
        初始化();
        计算图片高度(isGif, width2, height2);

        if (myImageView != null)
            myImageView.setOnOverListener(new onLodingOverListener() {
                @Override
                public void onOver(MyImageView myImageView) {
                    new Thread(new Runnable() {

                        public void run() {
                            while (onOver == null && !theEnd) {
                                SystemClock.sleep(10);
                            }
                            ((Activity) getContext())
                                    .runOnUiThread(new Runnable() {
                                        public void run() {
                                            setAddress(address, address2, isGif, width2, height2);
                                        }
                                    });
                        }
                    }).start();
                }
            });
        else
            new Thread(new Runnable() {
                public void run() {
                    while (onOver == null && !theEnd) {
                        SystemClock.sleep(10);
                    }
                    ((Activity) getContext()).runOnUiThread(new Runnable() {
                        public void run() {
                            setAddress(address, address2, isGif, width2,
                                    height2);
                        }
                    });
                }
            }).start();


        return this;
    }

    /**
     * @param address  缩略图地址
     * @param address2 详图地址
     * @param isGif    是否为gif
     * @param height2  图片的高度
     * @param width2   图片的宽度
     * @return
     */
    public MyImageView setAddress(String address, String address2, boolean isGif, String width2, String height2) {
        Log.e("TGAI", "setAddress()+isGif" + isGif + ",address" + address + ",address2"
                + address2 + " width2" + width2 + " height2" + height2);


        初始化();
        计算图片高度(isGif, width2, height2);


        this.address = address == null ? address2 : address;
        this.address2 = address2;

        if (isGif) {
            return 读取Gif(address2);
        }

        return 使用BitmapUtils加载图片(是否缩放 ? this.address : address2);


    }

    protected void 计算图片高度(boolean isGif,
                          String width2, String height2) {

        Log.i("TGAIMG", "得到屏幕的宽度为" + windowWidth);


        this.isGif = isGif;
        int width3 = Integer.parseInt(width2);
        int height3 = Integer.parseInt(height2);

        width = windowWidth;
        height = height3 * windowWidth / width3;
        Log.i("TGAIMG", "计算后图片的高度" + height);

        if (!isGif)
            height = height > windowHeight ? windowHeight : height;


        setMeasuredDimension(width, height);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        setLayoutParams(params);
    }

    protected MyImageView 使用BitmapUtils加载图片(String address) {
        BitmapDisplayConfig bigPicDisplayConfig = null;
        try {
            bigPicDisplayConfig = new BitmapDisplayConfig();
            bigPicDisplayConfig.setBitmapConfig(是否缩放 ? Bitmap.Config.RGB_565 : Bitmap.Config.ARGB_8888);
        } catch (Exception e) {
            e.printStackTrace();
        }
        DefaultBitmapLoadCallBack<View> callBack = new DefaultBitmapLoadCallBack<View>() {

            @Override
            public void onLoadStarted(View container, String uri,
                                      BitmapDisplayConfig config) {
                start();
                try {
                    super.onLoadStarted(container, uri, config);
                } catch (Exception e) {
                    // TODO 自动生成的 catch 块
                    e.printStackTrace();
                }
            }

            @Override
            public void onLoading(View container, String uri,
                                  BitmapDisplayConfig config, long total, long current) {

                max = total;
                progress = current;
                try {
                    super.onLoading(container, uri, config, total, current);
                } catch (Exception e) {
                    // TODO 自动生成的 catch 块
                    e.printStackTrace();
                }

            }

            @Override
            public void onLoadCompleted(View container, String uri,
                                        Bitmap bitmap, BitmapDisplayConfig config,
                                        BitmapLoadFrom from) {

                try {
                    if (maxHeight == 0)
                        maxHeight = windowHeight;


                    //如果不缩放
                    if (!是否缩放 || bitmap.getHeight() <= maxHeight) {
                        width = bitmap.getWidth();
                        height = bitmap.getHeight();
                        setMeasuredDimension(width, height);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        setLayoutParams(params);
                        super.onLoadCompleted(container, uri, bitmap, config, from);
                        return;
                    }
                    Matrix matrix = new Matrix();
                    float bl = 0.1f + windowWidth / bitmap.getWidth();
                    matrix.postScale(bl, bl); //长和宽放大缩小的比例
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                            maxHeight * bitmap.getWidth() / windowWidth, matrix, true);
                    Canvas canvas = new Canvas(bitmap);

                    Paint paint = new Paint();
                    paint.setAntiAlias(true);
                    paint.setColor(Color.parseColor("#99000000"));
                    paint.setStyle(Style.FILL);// 设置填满

                    canvas.drawRect(0, bitmap.getHeight() - 60, bitmap.getWidth(),
                            bitmap.getHeight(), paint);// 长方形

                    paint.setAlpha(255);
                    paint.setColor(Color.parseColor("#BDBEBD"));
                    paint.setTextAlign(Align.CENTER);
                    canvas.drawText("点击打开", bitmap.getWidth() / 2, bitmap.getHeight() - 30,
                            paint);
//					width = bitmap.getWidth();
//					height = bitmap.getHeight();
//					setMeasuredDimension(width,height);
//					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,maxHeight);
//					setLayoutParams(params );
                    super.onLoadCompleted(container, uri, bitmap, config, from);
                } finally {
                    if (onOver != null) onOver.onOver(MyImageView.this);
                }
            }


            @Override
            public void onLoadFailed(View container, String uri,
                                     Drawable drawable) {

                Log.e("THAIMG", "bitmapUtils下载失败!");
                super.onLoadFailed(container, uri, drawable);

                if (onOver != null) onOver.onOver(MyImageView.this);

            }


        };
        bmUtils.display(this, address, bigPicDisplayConfig, callBack);

        return this;
    }

    protected MyImageView 读取Gif(String address2) {
        File cacheDir = getContext().getExternalCacheDir();
        final File file = new File(cacheDir, MD5.md5(address2) + ".gif");
        try {
            if (file.exists()) {
                GifDrawable drawable = new GifDrawable(file);
                MyImageView.this.setBackgroundDrawable(drawable);
                可以启动动画 = true;
                if (onOver != null)
                    onOver.onOver(MyImageView.this);
                return this;
            }
        } catch (Throwable t) {
            t.printStackTrace();
            Log.e("TGAERR",
                    "<MyImageView#读取Gif()>Throwable!");
            MyImageView.this
                    .setBackgroundResource(R.drawable.wunai);
            if (onOver != null)
                onOver.onOver(MyImageView.this);
        }

        httpHandler = httpUtils.download(address2, file.getPath(),
                new RequestCallBack<File>() {

                    private String asyAddress;

                    @Override
                    public void onStart() {
                        asyAddress = MyImageView.this.address2;
                        Log.i("TGAIMG", "启动下载");
                        max = 101;
                        progress = 5;
                        start();
                        super.onStart();
                    }

                    @Override
                    public void onLoading(long total, long current,
                                          boolean isUploading) {
                        max = total;
                        progress = current;
                        super.onLoading(total, current, isUploading);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<File> arg0) {
                        File file = arg0.result;
                        Log.e("TGAIMG", "onSuccess()");
                        if (asyAddress != MyImageView.this.address2) {
                            return;
                        }
                        try {
                            GifDrawable drawable = new GifDrawable(file);
                            MyImageView.this.setBackgroundDrawable(drawable);

                            可以启动动画 = true;
                            if (onOver != null)
                                onOver.onOver(MyImageView.this);
                        } catch (Throwable t) {
                            t.printStackTrace();
                            Log.e("TGAERR",
                                    "<MyImageView#onSuccess()>Throwable!");
                            MyImageView.this
                                    .setBackgroundResource(R.drawable.wunai);
                            if (onOver != null)
                                onOver.onOver(MyImageView.this);
                        }

                    }

                    @Override
                    public void onCancelled() {
                        super.onCancelled();
                        Log.e("TGA", "Gif的下载被取消了");
                        if (file.exists())
                            file.delete();
                        // 初始化();
                        if (onOver != null)
                            onOver.onOver(MyImageView.this);
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        Toast.makeText(getContext(), "加载网络GIF图片出错" + arg1,
                                Toast.LENGTH_SHORT).show();
                        if (file.exists())
                            file.delete();
                        MyImageView.this
                                .setBackgroundResource(R.drawable.wunai);
                        if (onOver != null)
                            onOver.onOver(MyImageView.this);

                    }
                });

        return this;
    }

    @Override
    public void onClick(View v) {
        Log.e("TGAIMG", "点击事件发送信息:" + address2);
        Intent intent = new Intent(getContext(), ShowImageActivity.class);
        intent.putExtra("isGif", isGif);
        intent.putExtra("address", address2);
        intent.putExtra("width", width + "");
        intent.putExtra("height", height + "");
        getContext().startActivity(intent);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (progress < max && max != 100) {
            canvas.drawColor(Color.WHITE);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.parseColor("#999999"));
            paint.setStrokeWidth(10);
            paint.setTextSize(15);
            paint.setStyle(Style.STROKE);
//		 canvas.drawCircle(width/2, height/2, width/2-5, paint );

            paint.setColor(Color.RED);
            int width2 = canvas.getWidth();
            int height2 = canvas.getHeight();
            int r = width2 / 6;
            RectF oval = new RectF(width2 / 2 - r, height2 / 2 - r, width2 / 2 + r, height2 / 2 + r);
            canvas.drawArc(oval, jiaodu, progress * 360 / max, false, paint);

            Rect bounds = new Rect();
            // paint.setTextSize(20);
            String text = progress * 100 / max + "%";
            paint.getTextBounds(text, 0, text.length(), bounds);

            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(1);
            canvas.drawText(text, (width2 - bounds.width()) / 2,
                    (height2 + bounds.height()) / 2, paint);

        }
        if (progress == max) {
            max = 100;
            handler.removeMessages(0);
            postInvalidate();
            // max=100;
            // progress=0;
        }

    }

    protected void start() {
        handler.sendEmptyMessageDelayed(0, 100);
    }

    @Override
    protected void onDetachedFromWindow() {
        handler.removeCallbacksAndMessages(null);
        if (httpHandler != null)
            httpHandler.cancel();
        super.onDetachedFromWindow();
    }

    public interface onLodingOverListener {
        public void onOver(MyImageView myImageView);
    }

    /**
     * 用于设置默认的{@link MyImageView#bmUtils}、{@link MyImageView#httpUtils}、{@link MyImageView#maxHeight}、{@link MyImageView#是否缩放}
     *
     * @author FuHan
     */
    public static class Config {
        protected static boolean sf = true;
        protected static int maxHeight;
        private static Config config = new Config();

        private Config() {
        }

        /**
         * 为MyImageView设置默认的BitmapUtils,若不设置MyImageView将new一个新的
         *
         * @param bitmapUtils
         * @return
         */
        public static Config setBitmapUtils(BitmapUtils bitmapUtils) {
            MyImageView.bmUtils = bitmapUtils;
            return config;
        }

        /**
         * 为MyImageView设置默认的HttpUtils,若不设置HttpUtils将new一个新的
         *
         * @param httpUtils
         * @return
         */
        public static Config setHttpUtils(HttpUtils httpUtils) {
            MyImageView.httpUtils = httpUtils;
            return config;
        }

        /**
         * 是否缩放,默认开启缩放,也可为每个图片单独设置{@link MyImageView#是否缩放(boolean)}
         *
         * @param sf
         * @return
         */
        public static Config 设置默认缩放(boolean sf) {
            Config.sf = sf;
            return config;
        }

        /**
         * 设置当缩放启用时图片的最高高度,当不设置时,当缩放启用时的默认最高高度为手机屏幕的高度,也可单独为每张图片单独设置max高度{@link MyImageView#setMaxHeight(int)}
         *
         * @param maxHeight
         * @return
         */
        public static Config setMaxHeight(int maxHeight) {
            Config.maxHeight = maxHeight;
            return config;
        }
    }
}
