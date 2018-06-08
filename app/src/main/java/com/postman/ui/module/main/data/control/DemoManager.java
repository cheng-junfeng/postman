package com.postman.ui.module.main.data.control;

import android.content.Context;
import android.content.res.Resources;

import com.base.utils.LogUtil;
import com.base.utils.TimeUtil;
import com.postman.db.entity.DataEntity;
import com.postman.db.entity.NoteEntity;
import com.postman.db.helper.DataHelper;
import com.postman.db.helper.NoteHelper;

import java.util.List;
import java.util.Random;

public class DemoManager {
    private final static String TAG = "DemoManager";

    public static void init(Context mContext) {
        DataHelper helper = DataHelper.getInstance();
        List<DataEntity> allEntity = helper.queryList();
        if (allEntity != null && allEntity.size() > 0) {
            LogUtil.d(TAG, "initDemo:" + allEntity.size());
        } else {
            LogUtil.d(TAG, "initDemo");
            Resources res = mContext.getResources();
            long current = System.currentTimeMillis();

            for (int i = 0; i < 100; i++) {
                long id = current + i;
                DataEntity temp = new DataEntity();
                temp.setData_id(id);
                temp.setData_name((i+1)+" "+new Random().nextInt(100));
                temp.setData_lasttime(Long.toString(current - (10 * i)));
                helper.insert(temp);
            }
        }

        NoteHelper noteHelper = NoteHelper.getInstance();
        List<NoteEntity> allData = noteHelper.queryList();
        if(allData == null || allData.size() == 0){
            long curren = System.currentTimeMillis();
            NoteEntity entity1 = new NoteEntity();
            entity1.setNote_id(curren+1000);
            entity1.setNote_content("Fine");
            entity1.setNote_lasttime(TimeUtil.milliseconds2String(curren+1000));
            noteHelper.insert(entity1);

            NoteEntity entity2 = new NoteEntity();
            entity2.setNote_id(curren+2000);
            entity2.setNote_content("Rain");
            entity2.setNote_lasttime(TimeUtil.milliseconds2String(curren+2000));
            noteHelper.insert(entity2);

            NoteEntity entity3 = new NoteEntity();
            entity3.setNote_id(curren+3000);
            entity3.setNote_content("Rain");
            entity3.setNote_lasttime(TimeUtil.milliseconds2String(curren+3000));
            noteHelper.insert(entity3);

            NoteEntity entity4 = new NoteEntity();
            entity4.setNote_id(curren+4000);
            entity4.setNote_content("Sunny");
            entity4.setNote_lasttime(TimeUtil.milliseconds2String(curren+4000));
            noteHelper.insert(entity4);

            NoteEntity entity5 = new NoteEntity();
            entity5.setNote_id(curren+5000);
            entity5.setNote_content("Rest");
            entity5.setNote_lasttime(TimeUtil.milliseconds2String(curren+5000));
            noteHelper.insert(entity5);
        }
    }
}
