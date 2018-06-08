package com.postman.app.listener;

import com.postman.config.Types;

public interface OnRequestListener {
    void onStart(String url, Types type);
}
