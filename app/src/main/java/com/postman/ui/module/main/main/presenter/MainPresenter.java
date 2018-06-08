package com.postman.ui.module.main.main.presenter;


import com.postman.ui.module.main.main.contract.MainContract;

public class MainPresenter implements MainContract.Presenter {
    private final static String TAG = "MainPresenter";
    MainContract.View mView;

    public MainPresenter(MainContract.View view) {
        this.mView = view;
    }

}
