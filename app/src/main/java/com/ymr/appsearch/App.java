package com.ymr.appsearch;

import com.ymr.common.BaseApplication;
import com.ymr.common.Env;
import com.ymr.common.IStatModel;
import com.ymr.common.ProjectState;
import com.ymr.common.bean.IApiBase;
import com.ymr.common.net.NetWorkModel;
import com.ymr.common.ui.BaseUIController;
import com.ymr.common.ui.IBaseUIController;

import java.util.List;

/**
 * Created by ymr on 15/11/21.
 */
public class App extends BaseApplication {
    private static App sApp;

    public static App getApp() {
        return sApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        Env.init(this, new Env.InitParams() {
            @Override
            public IBaseUIController.BaseUIParams getBaseUIParams() {
                return new BaseUIController.BaseUIParams() {
                    @Override
                    public int getTitleBgColor() {
                        return getResources().getColor(R.color.colorPrimaryDark);
                    }

                    @Override
                    public int getTitleTextColor() {
                        return getResources().getColor(android.R.color.white);
                    }

                    @Override
                    public int getBackDrawable() {
                        return R.drawable.back_selector;
                    }
                };
            }

            @Override
            public Env.WebUrl getWebUrl() {
                return null;
            }

            @Override
            public List<IStatModel> getStatModels() {
                return null;
            }

            @Override
            public ProjectState getProjectState() {
                ProjectState result = null;
                if (BuildConfig.DEBUG) {
                    result = ProjectState.DBUG;
                } else {
                    result = ProjectState.RELEASE;
                }
                return result;
            }

            @Override
            public Class<? extends IApiBase> getApiBaseClass() {
                return null;
            }

            @Override
            public Class getWebViewOpenActivity() {
                return null;
            }
        }, new Env.FloorErrorDisposer() {
            @Override
            public void onError(NetWorkModel.Error error) {

            }
        });
    }

    @Override
    protected boolean getDebug() {
        return false;
    }
}
