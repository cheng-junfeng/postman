package com.postman.ui.module.main.main.contract;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

public interface MainContract {

    interface View {
        RxAppCompatActivity getRxActivity();
    }

    interface Presenter {
    }
}