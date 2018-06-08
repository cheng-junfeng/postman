package com.postman.app.event;

public class MyEvent {
    private final MyType type;

    private MyEvent(Builder builder) {
        this.type = builder.type;
    }

    public MyType getType() {
        return this.type;
    }

    public static class Builder {
        private final MyType type;

        public Builder(MyType mType) {
            this.type = mType;
        }

        public MyEvent build() {
            return new MyEvent(this);
        }
    }
}
