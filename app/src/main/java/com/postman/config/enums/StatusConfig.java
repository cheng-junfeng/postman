package com.postman.config.enums;

public enum StatusConfig {
    INIT,
    RUN,
    PAUSE;

    public static String[] getAll(){
        String[] allStr = new String[]{INIT.name(), RUN.name(), PAUSE.name()};
        return allStr;
    }

    public static int getIndex(StatusConfig value){
        int index = 0;
        switch (value){
            case INIT:index = 0;break;
            case RUN:index = 1;break;
            case PAUSE:index = 2;break;
            default:break;
        }
        return index;
    }

    public static StatusConfig value(int index){
        StatusConfig value = INIT;
        switch (index){
            case 0:value = INIT;break;
            case 1:value = RUN;break;
            case 2:value = PAUSE;break;
            default:break;
        }
        return value;
    }
}
