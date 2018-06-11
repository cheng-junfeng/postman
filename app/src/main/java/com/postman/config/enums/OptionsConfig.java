package com.postman.config.enums;

public enum OptionsConfig {
    HEADER,
    BODY;

    public static int getIndex(OptionsConfig value){
        int index = 0;
        switch (value){
            case HEADER:index = 0;break;
            case BODY:index = 1;break;
            default:break;
        }
        return index;
    }

    public static OptionsConfig value(int index){
        OptionsConfig value = HEADER;
        switch (index){
            case 0:value = HEADER;break;
            case 1:value = BODY;break;
            default:break;
        }
        return value;
    }

    public static OptionsConfig value(String name){
        OptionsConfig value = HEADER;
        if(HEADER.name().equals(name)){
            value = HEADER;
        }else if(BODY.name().equals(name)){
            value = BODY;
        }
        return value;
    }
}
