package com.postman.app.listener;

import com.postman.config.enums.TypesConfig;

public interface OnRequestListener {
    void onStart(String url, TypesConfig type);
}
