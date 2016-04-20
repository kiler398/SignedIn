package com.srmn.xwork.dao;

import com.srmn.xwork.androidlib.db.BaseDao;
import com.srmn.xwork.entities.PersonInfoEntity;
import com.srmn.xwork.utils.xfSDkUtil;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.sql.Time;
import java.util.List;

/**
 * Created by kiler on 2016/4/13.
 */
public class PersonInfoDao extends BaseDao<PersonInfoEntity> {

    public PersonInfoDao(DbManager db, DaoContainer daoContainer) {
        super(db, daoContainer);
    }

    public DaoContainer getDaoContainerInstance() {
        return (DaoContainer) this.dao;
    }

    @Override
    public String getPkName() {
        return "id";
    }

    @Override
    protected void getClassType() {
        entityClass = PersonInfoEntity.class;
    }

    public PersonInfoEntity getCurrentPersonInfo() {


        PersonInfoEntity person = null;

        try {
            person = this.getSelector().findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }

        if(person==null)
        {
            person = new PersonInfoEntity();
            person.setCode(xfSDkUtil.getUNDeviceID());
            person.setName("");
            person.setEnableLocationCheck(false);
            person.setEnableFaceCheck(false);
            person.setEnableVoiceCheck(false);

            try {
                this.saveBindingId(person);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        return person;


    }
}
