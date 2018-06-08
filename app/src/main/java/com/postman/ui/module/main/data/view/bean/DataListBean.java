package com.postman.ui.module.main.data.view.bean;

import android.text.TextUtils;

public class DataListBean {
    private final long id;
    private final String content;
    private String input;
    private String output;
    private String time;

    private DataListBean(Builder builder) {
        this.id = builder.id;
        this.content = builder.content;

        this.input = builder.input;
        this.output = builder.output;
        this.time = builder.time;
    }

    public long getId() {
        return this.id;
    }

    public String getContent() {
        return NoNull(this.content);
    }

    public String getInput() {
        return NoNull(this.input);
    }

    public String getOutput() {
        return NoNull(this.output);
    }

    public String getTime() {
        return NoNull(this.time);
    }

    private String NoNull(String str){
        if(TextUtils.isEmpty(str)){
            return "{}";
        }else{
            return str;
        }
    }

    public static class Builder {
        private final long id;
        private String content;
        private String input;
        private String output;
        private String time;

        public Builder(long mId) {
            this.id = mId;
        }

        public Builder content(String cont) {
            this.content = cont;
            return this;
        }

        public Builder input(String in) {
            this.input = in;
            return this;
        }

        public Builder output(String out) {
            this.output = out;
            return this;
        }

        public Builder time(String tim) {
            this.time = tim;
            return this;
        }

        public DataListBean build() {
            return new DataListBean(this);
        }
    }
}
