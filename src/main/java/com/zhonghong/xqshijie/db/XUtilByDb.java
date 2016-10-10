package com.zhonghong.xqshijie.db;

import android.os.Environment;

import com.zhonghong.xqshijie.app.Constants;

import org.xutils.DbManager;

import java.io.File;

/**
 * Created by xiezl on 16/6/21.
 *
 */
public class XUtilByDb {

    static DbManager.DaoConfig daoConfig;

    /**
     * 创建表步骤:
     * 1.DaoConfig daoConfig=XUtilByDb.getDaoConfig();
     * 2.DbManager db = x.getDb(daoConfig);
     * 3.操作 db.save(person); db.findById();db.selector;等调用API实现
     * @return
     */
    public static DbManager.DaoConfig getDaoConfig() {
        File file = new File(Environment.getExternalStorageDirectory().getPath());
        if (daoConfig == null) {
            daoConfig = new DbManager.DaoConfig()
                    .setDbName(Constants.DBNAME)
                    .setDbDir(file)
                    .setDbVersion(Constants.DBVERSION)
                    .setAllowTransaction(true)
                    .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                        @Override
                        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                        }
                    });
        }
        return daoConfig;
    }



}
