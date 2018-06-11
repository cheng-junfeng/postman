package com.postman.ui.module.main.find.utils;

import com.base.app.event.RxBusHelper;
import com.base.utils.TimeUtil;
import com.postman.app.event.MyEvent;
import com.postman.app.event.MyType;
import com.postman.config.enums.TypesConfig;
import com.postman.db.entity.DataEntity;
import com.postman.db.helper.DataHelper;

public class FindDBUtil {
    public static void insertData(TypesConfig types, String url, String input, String output) {
        DataHelper helper = DataHelper.getInstance();
        DataEntity entity = new DataEntity();
        long current = System.currentTimeMillis();
        entity.setData_id(current);
        entity.setData_lasttime(TimeUtil.milliseconds2String(current));
        entity.setData_name(types.name());
        entity.setData_url(url);
        entity.setData_input(input);
        entity.setData_output(output);

        helper.insert(entity);
        RxBusHelper.post(new MyEvent.Builder(MyType.DATE_UPDATE).build());
    }
}
