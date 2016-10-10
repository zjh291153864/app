package com.zhonghong.xqshijie.util;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zhonghong.xqshijie.R;

/**
 * 图片加载工具类
 */
public class ImageLoaderUtil {

    public static final int IMG_LOAD_DELAY = 200;
    private static ImageLoaderUtil imageLoaderHelper;
    private ImageLoader imageLoader;
    // 显示图片的设置
    private DisplayImageOptions options;


    public static ImageLoaderUtil getInstance() {
        if (imageLoaderHelper == null) {
            synchronized (ImageLoaderUtil.class) {
                if (imageLoaderHelper == null) {
                    imageLoaderHelper = new ImageLoaderUtil();
                }
            }
        }
        return imageLoaderHelper;
    }

    public ImageLoaderUtil() {
        init();
    }

    /**
     * 配置 imagloader 基本信息
     */
    private void init() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.image_onloading_homebig)
                .showImageForEmptyUri(R.drawable.image_onloading_homebig)
                .showImageOnFail(R.drawable.image_onloading_homebig)
                .delayBeforeLoading(IMG_LOAD_DELAY)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)     //设置图片的解码类型
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
//                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();
        imageLoader = ImageLoader.getInstance();

    }


    /**
     * 加载圆角图片
     *
     * @param url
     * @param imageView
     */
    public void loadImageByRoundedImage(String url, ImageView imageView, int defaultID) {
        DisplayImageOptions option = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.image_onloading_homebig)
                .showImageForEmptyUri(R.drawable.image_onloading_homebig)
                .showImageOnFail(R.drawable.image_onloading_homebig)
                .delayBeforeLoading(IMG_LOAD_DELAY)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)     //设置图片的解码类型
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
//                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();
        if (!StringUtils.isNull(url)) {
            url = NetUtils.getDealUrl(url, "https://www.xqshijie.com/");
            imageLoader.displayImage(url, imageView, option);
        } else {
            imageLoader.displayImage("drawable://" + defaultID, imageView, option);
        }
    }

    /**
     * 加载头像图片
     *
     * @param url
     * @param imageView
     */
    public void loadImageByHeadPortrait(String url, ImageView imageView, int defaultID) {
        DisplayImageOptions option = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_member_centre_head_portrait)
                .showImageForEmptyUri(R.drawable.ic_member_centre_head_portrait)
                .showImageOnFail(R.drawable.ic_member_centre_head_portrait)
                .delayBeforeLoading(IMG_LOAD_DELAY)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheOnDisk(false)
                .bitmapConfig(Bitmap.Config.RGB_565)     //设置图片的解码类型
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(120))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();
        if (!StringUtils.isNull(url)) {
            imageLoader.displayImage(url, imageView, option);
        } else {
            imageLoader.displayImage("drawable://" + defaultID, imageView, option);
        }
    }

    /**
     * 加载图片
     *
     * @param url
     * @param imageView
     */
    public void loadImage(String url, ImageView imageView, int defaultID) {
        if (!StringUtils.isNull(url)) {
            url = NetUtils.getDealUrl(url, "https://www.xqshijie.com/");
            imageLoader.displayImage(url, imageView, options);
        } else {
            imageLoader.displayImage("drawable://" + defaultID, imageView, options);
        }
    }

    /**
     * 从内存卡中异步加载本地图片
     *
     * @param uri
     * @param imageView
     */
    private void displayFromSDCard(String uri, ImageView imageView) {
        // String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
        imageLoader.displayImage("file://" + uri, imageView);
    }

    /**
     * 从assets文件夹中异步加载图片
     *
     * @param imageName 图片名称，带后缀的，例如：1.png
     * @param imageView
     */
    private void dispalyFromAssets(String imageName, ImageView imageView) {
        // String imageUri = "assets://image.png"; // from assets
        imageLoader.displayImage("assets://" + imageName, imageView);
    }

    /**
     * 从drawable中异步加载本地图片
     *
     * @param imageId
     * @param imageView
     */
    public void displayFromDrawable(int imageId, ImageView imageView) {
        // String imageUri = "drawable://" + R.drawable.image; // from drawables
        // (only images, non-9patch)
        imageLoader.displayImage("drawable://" + imageId, imageView);
    }

    /**
     * 从内容提提供者中抓取图片
     */
    public void displayFromContent(String uri, ImageView imageView) {
        // String imageUri = "content://media/external/audio/albumart/13"; //
        // from content provider
        imageLoader.displayImage("content://" + uri, imageView);
    }

}
