package com.postman.config.enums;

public enum TypesConfig {
    GET,
    POST,
    DOWN,
    UPLOAD;

    public static String[] getAll(){
        String[] allStr = new String[]{GET.name(), POST.name(), DOWN.name(), UPLOAD.name()};
        return allStr;
    }

    public static int getIndex(TypesConfig value){
        int index = 0;
        switch (value){
            case GET:index = 0;break;
            case POST:index = 1;break;
            case DOWN:index = 2;break;
            case UPLOAD:index = 3;break;
            default:break;
        }
        return index;
    }

    public static TypesConfig value(int index){
        TypesConfig value = GET;
        switch (index){
            case 0:value = GET;break;
            case 1:value = POST;break;
            case 2:value = DOWN;break;
            case 3:value = UPLOAD;break;
            default:break;
        }
        return value;
    }
}
