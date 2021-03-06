package com.postman.db.helper;


import com.postman.db.control.BaseDao;
import com.postman.db.entity.NoteEntity;
import com.postman.db.entity.NoteEntityDao;

import java.util.List;

public class NoteHelper extends BaseDao<NoteEntity> {
    private volatile static NoteHelper instance;

    private NoteHelper() {
        super();
    }

    public static NoteHelper getInstance() {
        if (instance == null) {
            synchronized (NoteHelper.class) {
                if (instance == null) {
                    instance = new NoteHelper();
                }
            }
        }
        return instance;
    }

    public boolean insert(NoteEntity message) {
        try {
            daoSession.insert(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void delete(NoteEntity message) {
        super.deleteObject(message);
    }

    public void delete(long noteId) {
        NoteEntity entity = queryByNoteId(noteId);
        if(entity != null){
            delete(entity);
        }
    }

    public void clear() {
        super.deleteAll(NoteEntity.class);
    }

    public boolean update(NoteEntity user) {
        try {
            daoSession.update(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<NoteEntity> queryList() {
        NoteEntityDao messDao = daoSession.getNoteEntityDao();
        return messDao.queryBuilder()
                .orderDesc(NoteEntityDao.Properties.Note_lasttime)
                .list();
    }

    public NoteEntity queryByNoteId(long noteId) {
        NoteEntityDao messDao = daoSession.getNoteEntityDao();
        return messDao.queryBuilder()
                .where(NoteEntityDao.Properties.Note_id.eq(noteId))
                .unique();
    }
}
