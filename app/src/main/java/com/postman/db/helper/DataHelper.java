package com.postman.db.helper;




import com.postman.db.control.BaseDao;
import com.postman.db.entity.DataEntity;
import com.postman.db.entity.DataEntityDao;

import java.util.List;

public class DataHelper extends BaseDao<DataEntity> {
    private volatile static DataHelper instance;

    private DataHelper() {
        super();
    }

    public static DataHelper getInstance() {
        if (instance == null) {
            synchronized (DataHelper.class) {
                if (instance == null) {
                    instance = new DataHelper();
                }
            }
        }
        return instance;
    }

    public boolean insert(DataEntity message) {
        try {
            daoSession.insert(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void delete(DataEntity message) {
        super.deleteObject(message);
    }

    public void clear() {
        List<DataEntity> allEntitys = queryList();
        for(DataEntity entity : allEntitys){
            delete(entity);
        }
    }

    public boolean update(DataEntity user) {
        try {
            daoSession.update(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<DataEntity> queryList() {
        DataEntityDao messDao = daoSession.getDataEntityDao();
        return messDao.queryBuilder()
                .orderDesc(DataEntityDao.Properties.Data_lasttime).list();
    }

    public List<DataEntity> queryListByUserId(String userId) {
        DataEntityDao messDao = daoSession.getDataEntityDao();
        List<DataEntity> userList = messDao.queryBuilder().where(DataEntityDao.Properties.UserId.eq(userId)).list();
        if (userList == null || userList.size() == 0) {
            return null;
        } else {
            return userList;
        }
    }

    public DataEntity queryListByVideoId(long videoId) {
        DataEntityDao messDao = daoSession.getDataEntityDao();
        DataEntity mess = messDao.queryBuilder().where(DataEntityDao.Properties.Data_id.eq(videoId)).unique();
        if (mess == null) {
            return null;
        } else {
            return mess;
        }
    }

    public DataEntity queryListByVideoName(String videoName) {
        DataEntityDao messDao = daoSession.getDataEntityDao();
        DataEntity mess = messDao.queryBuilder().where(DataEntityDao.Properties.Data_name.eq(videoName)).unique();
        if (mess == null) {
            return null;
        } else {
            return mess;
        }
    }
}
