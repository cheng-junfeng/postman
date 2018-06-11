package com.postman.db.cache.bean;

import com.postman.ui.module.main.find.bean.KeyListBean;

import java.io.Serializable;
import java.util.ArrayList;

public class KeyInputCache implements Serializable{
    public ArrayList<KeyListBean> keyHeader;
    public ArrayList<KeyListBean> keyBody;
}
