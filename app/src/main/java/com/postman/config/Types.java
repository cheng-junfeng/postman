package com.postman.config;

public enum Types {
    GET,
    POST,
    PUT,
    PATCH,
    DELETE,
    COPY,
    HEAD;

    public static String[] getAll(){
        String[] allStr = new String[]{GET.name(), POST.name(), PUT.name(), PATCH.name(), DELETE.name(), COPY.name(), HEAD.name()};
        return allStr;
    }

    public static int getIndex(Types value){
        int index = 0;
        switch (value){
            case GET:index = 0;break;
            case POST:index = 1;break;
            case PUT:index = 2;break;
            case PATCH:index = 3;break;
            case DELETE:index = 4;break;
            case COPY:index = 5;break;
            case HEAD:index = 6;break;
            default:break;
        }
        return index;
    }

    public static Types value(int index){
        Types value = GET;
        switch (index){
            case 0:value = GET;break;
            case 1:value = POST;break;
            case 2:value = PUT;break;
            case 3:value = PATCH;break;
            case 4:value = DELETE;break;
            case 5:value = COPY;break;
            case 6:value = HEAD;break;
            default:break;
        }
        return value;
    }
}
