package com.ymr.common;

import android.app.Application;
import android.content.Context;

import com.ymr.common.bean.ApiBase;
import com.ymr.common.bean.IApiBase;
import com.ymr.common.net.NetWorkModel;
import com.ymr.common.ui.BaseUIController;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ymr on 15/6/26.
 */
public class Env {

    private static Application sApp;
    public static WebUrl sWebUrl;
    public static Map<String, String> sParams = new HashMap<>();
    public static Env.FloorErrorDisposer sFloorErrorDisposer;
    public static List<IStatModel> sStatModels = new ArrayList<>();
    public static ProjectState sProjectState;
    private static Class<? extends IApiBase> sApiBaseClass;

    public static void init(Application context,InitParams initParams,FloorErrorDisposer floorErrorDisposer) {
        sApp = context;
        initImageLoader(context);
        BaseUIController.initBaseUIParams(initParams.getBaseUIParams());
        sWebUrl = initParams.getWebUrl();
        sFloorErrorDisposer = floorErrorDisposer;
        List<IStatModel> statModels = initParams.getStatModels();
        if (statModels != null && !statModels.isEmpty()) {
            sStatModels.addAll(statModels);
        }
        sStatModels.add(new UmengStatModel());
        sProjectState = initParams.getProjectState();
        sApiBaseClass = initParams.getApiBaseClass();
    }

    public static Class<? extends IApiBase> getApiBase() {
        return sApiBaseClass != null ? sApiBaseClass : ApiBase.class;
    }

    public interface InitParams{
        BaseUIController.BaseUIParams getBaseUIParams();
        WebUrl getWebUrl();
        List<IStatModel> getStatModels();
        ProjectState getProjectState();
        Class<? extends IApiBase> getApiBaseClass();
    }

    public static interface FloorErrorDisposer {
        void onError(NetWorkModel.Error error);
    }

    private static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);

        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public static class WebUrl{
        public String debug;
        public String release;

        public WebUrl(String debug, String release) {
            this.debug = debug;
            this.release = release;
        }
    }

    public static Application getApp() {
        return sApp;
    }

    public static void setCommonNetParams(Map<String,String> params) {
        sParams.putAll(params);
    }
}
