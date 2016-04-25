package com.ymr.mvp.view.viewimp;

import com.ymr.mvp.presenter.LoadDataPresenter;

/**
 * Created by ymr on 16/4/20.
 */
public interface ILoadingAnimView<P extends LoadDataPresenter> {
    void hideLoadingView();

    void showLoadingView();

    void onCreate(P presenter);

    void onDestroy();
}
