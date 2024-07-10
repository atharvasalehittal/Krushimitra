package com.mitra.krushi.krushimitra;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
public class suggestdb {
    SQLiteDatabase sqdb;
    private static String DB_PATH = "/data/data/com.mitra.krushi.krushimitra/databases/";

    private static String DB_NAME = "suggestdb";
    private static String tablename = "suggest";
    static String col1="cropname";
    static String col2="ph_high";
    static String col3="ph_low";
    static String col4="ec_high";
    static String col5="ec_low";
    static String col6="oc_high";
    static String col7="oc_low";
    class DataBaseHelper extends SQLiteOpenHelper {



        private SQLiteDatabase myDataBase;

        private final Context myContext;

        public DataBaseHelper(Context context) {

            super(context, DB_NAME, null, 1);
            this.myContext = context;
            this.createDataBase();
        }

        public void createDataBase() {
            try {
                boolean dbExist = checkDataBase();

                if (dbExist) {
                } else {

                    this.getReadableDatabase();


                    copyDataBase();

                }
            } catch (Exception e) {

            }
        }

        private boolean checkDataBase() {

            SQLiteDatabase checkDB = null;

            try {
                String myPath = DB_PATH + DB_NAME;
                checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

            } catch (SQLiteException e) {

                //database does't exist yet.

            }

            if (checkDB != null) {

                checkDB.close();

            }

            return checkDB != null ? true : false;
        }

        private void copyDataBase() {

            try {
                //Open your local db as the input stream
                InputStream myInput = myContext.getAssets().open(DB_NAME);

                // Path to the just created empty db
                String outFileName = DB_PATH + DB_NAME;

                //Open the empty db as the output stream
                OutputStream myOutput = new FileOutputStream(outFileName);

                //transfer bytes from the inputfile to the outputfile
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                //Close the streams
                myOutput.flush();
                myOutput.close();
                myInput.close();
            } catch (Exception e) {
                //catch exception
            }
        }

        public SQLiteDatabase openDataBase() throws SQLException {

            //Open the database
            String myPath = DB_PATH + DB_NAME;
            myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            return myDataBase;

        }


        @Override
        public synchronized void close() {

            if (myDataBase != null) {
                myDataBase.close();
            }

            super.close();

        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

    }
    suggestdb(Context context) {
        suggestdb.DataBaseHelper dbb = new suggestdb.DataBaseHelper(context);
        sqdb =dbb.openDataBase();
    }
    public Cursor query(String[] Projection, String Selection, String[] SelectionArgs, String setOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(tablename);
        Cursor cursor = qb.query(sqdb, Projection, Selection, SelectionArgs, null, null, setOrder);
        return cursor;
    }
}
