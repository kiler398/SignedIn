package com.srmn.xwork.dao;

import com.srmn.xwork.androidlib.db.BaseDao;
import com.srmn.xwork.entities.PersonInfoEntity;
import com.srmn.xwork.entities.SignedInfoEntity;

import org.xutils.DbManager;

/**
 * Created by kiler on 2016/4/21.
 */
public class SignedInfoDao extends BaseDao<SignedInfoEntity> {

    public SignedInfoDao(DbManager db, DaoContainer daoContainer) {
        super(db, daoContainer);
    }

    @Override
    public String getPkName() {
        return "id";
    }

    @Override
    protected void getClassType() {
        entityClass = SignedInfoEntity.class;
    }
}
