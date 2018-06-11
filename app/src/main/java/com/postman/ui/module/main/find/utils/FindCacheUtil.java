package com.postman.ui.module.main.find.utils;

import com.postman.config.Cache;
import com.postman.config.Constant;
import com.postman.config.enums.OptionsConfig;
import com.postman.db.cache.PostmanCache;
import com.postman.db.cache.bean.KeyInputCache;
import com.postman.ui.module.main.find.bean.KeyListBean;

import java.util.ArrayList;

public class FindCacheUtil {
    public static ArrayList<KeyListBean> getKeyBean(PostmanCache myCache, OptionsConfig options) {
        KeyInputCache myInput = (KeyInputCache) myCache.getAsObject(Cache.CACHE_INPUT);
        ArrayList<KeyListBean> allBeans = new ArrayList<>();
        if (myInput != null) {
            switch (options) {
                case HEADER:
                    return myInput.keyHeader;
                case BODY:
                    return myInput.keyBody;
                default:
                    break;
            }
        }
        return allBeans;
    }

    public static KeyListBean getKeyBean(PostmanCache myCache, OptionsConfig options, int index) {
        KeyInputCache myInput = (KeyInputCache) myCache.getAsObject(Cache.CACHE_INPUT);
        KeyListBean myBean = null;
        if (myInput != null) {
            switch (options) {
                case HEADER:
                    if (myInput.keyHeader != null && myInput.keyHeader.size() > index) {
                        myBean = myInput.keyHeader.get(index);
                    }
                    break;
                case BODY:
                    if (myInput.keyBody != null && myInput.keyBody.size() > index) {
                        myBean = myInput.keyBody.get(index);
                    }
                    break;
                default:
                    break;
            }
        }
        return myBean;
    }

    public static void initKey(PostmanCache myCache){
        KeyInputCache myInput = new KeyInputCache();
        myInput.keyHeader = new ArrayList<KeyListBean>();
        myInput.keyBody = new ArrayList<KeyListBean>();
        for (int i = 0; i < Constant.INPUT_SIZE; i++) {
            KeyListBean temp = new KeyListBean();
            temp.set = false;
            temp.key = "";
            temp.value = "";
            myInput.keyHeader.add(temp);
            myInput.keyBody.add(temp);
        }
        myCache.put(Cache.CACHE_INPUT, myInput);
    }

    public static void setKeyBean(PostmanCache myCache, OptionsConfig options, int index, KeyListBean beans) {
        KeyInputCache myInput = (KeyInputCache) myCache.getAsObject(Cache.CACHE_INPUT);
        if (myInput == null) {
            initKey(myCache);
        }

        switch (options) {
            case HEADER:
                ArrayList<KeyListBean> allHeaders = myInput.keyHeader;
                if (allHeaders != null && allHeaders.size() > index) {
                    myInput.keyHeader.set(index, beans);
                }
                break;
            case BODY:
                ArrayList<KeyListBean> allBody = myInput.keyBody;
                if (allBody != null && allBody.size() > index) {
                    myInput.keyBody.set(index, beans);
                }
                break;
            default:
                break;
        }
        myCache.put(Cache.CACHE_INPUT, myInput);
    }

    public static void delete(PostmanCache myCache, OptionsConfig options, int index){
        KeyInputCache myInput = (KeyInputCache) myCache.getAsObject(Cache.CACHE_INPUT);
        if (myInput == null) {
            initKey(myCache);
            return;
        }

        switch (options) {
            case HEADER:
                if (myInput.keyHeader != null && myInput.keyHeader.size() > index) {
                    myInput.keyHeader.get(index).set = false;
                    myInput.keyHeader.get(index).key = "";
                    myInput.keyHeader.get(index).value = "";
                }
                break;
            case BODY:
                if (myInput.keyBody != null && myInput.keyBody.size() > index) {
                    myInput.keyBody.get(index).set = false;
                    myInput.keyBody.get(index).key = "";
                    myInput.keyBody.get(index).value = "";
                }
                break;
            default:
                break;
        }
        myCache.put(Cache.CACHE_INPUT, myInput);
    }
}
