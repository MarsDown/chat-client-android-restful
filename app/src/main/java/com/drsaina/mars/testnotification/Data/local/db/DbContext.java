package com.drsaina.mars.testnotification.Data.local.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.drsaina.mars.testnotification.Data.local.db.model.DaoMaster;
import com.drsaina.mars.testnotification.Data.local.db.model.DaoSession;
import com.drsaina.mars.testnotification.General;

import java.io.File;




/**
 * Created by sayna on 9/23/2016.
 */
public class DbContext {

    public static final String DATABASE_NAME = "sainagram.sqlite";
    public static String DATABASE_Path = "";
    public static String SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();;

    private static DaoMaster.DevOpenHelper sDevOpenHelper;
    private static SQLiteDatabase database;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    private static final boolean ENCRYPTED = true;

    public DbContext ( Context context) {
        try {
            DATABASE_Path = SDCARD + File.separator + DbContext.DATABASE_NAME;
            database = SQLiteDatabase.openOrCreateDatabase(DATABASE_Path, null);
        } catch (Exception ex) {
            DATABASE_Path =context.getExternalFilesDir(null).getAbsolutePath() + File.separator + DATABASE_NAME;
            database = SQLiteDatabase.openOrCreateDatabase(DATABASE_Path, null);
        }

        DaoMaster.createAllTables(database, true);

        sDevOpenHelper = new DaoMaster.DevOpenHelper(context, DATABASE_NAME, null);
        database = sDevOpenHelper.getWritableDatabase();
        daoMaster = new DaoMaster(database);
    }

    public static void destroy() {
        try {
            if (daoMaster != null) {
                daoMaster.getDatabase().close();
                daoMaster = null;
            }

            if (sDevOpenHelper != null) {
                sDevOpenHelper.close();
                sDevOpenHelper = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DaoSession newSession() {
        if (daoMaster == null) {
            throw new RuntimeException("sDaoMaster is null.");
        }
        return daoMaster.newSession();
    }
}
