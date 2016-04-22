package com.srmn.xwork.dao;

import com.srmn.xwork.androidlib.db.IDaoContainer;

import org.xutils.DbManager;

/**
 * Created by kiler on 2016/2/20.
 */
public class DaoContainer implements IDaoContainer {

    private DbManager _db;

    private PersonInfoDao personInfoDaoDaoInstance;
    private SignedInfoDao signedInfoDaoDaoInstance;

    public DaoContainer(DbManager db) {
        this._db = db;
        personInfoDaoDaoInstance = new PersonInfoDao(this._db, this);
        signedInfoDaoDaoInstance = new SignedInfoDao(this._db, this);
    }

    public PersonInfoDao getPersonInfoDaoInstance() {
        return personInfoDaoDaoInstance;
    }

    public SignedInfoDao getSignedInfoDaoInstance() {
        return signedInfoDaoDaoInstance;
    }
}
