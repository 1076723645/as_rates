package com.example.administrator.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

/**
 * Created by 小辉 on 2017/7/2.
 */

public class Database extends SQLiteOpenHelper {
    //创建表SQL语句
    private static final String CREATE_TBL1="create table record(id integer primary key autoincrement,start text," +
            "start_mHom text,final text,final_mFor text)";
    //SQLiteDatabase实例
    private SQLiteDatabase db;
    /*
     * 构造方法
     */
    public Database(Context c, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(c,name,factory,version);
    };
    /*
     * 创建表
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TBL1);
    }
    /*
     * 插入方法
     */
    public void insert(String name,ContentValues values)
    {
        //获得SQLiteDatabase实例
        SQLiteDatabase db=getWritableDatabase();
        //插入
        db.insert(name, null, values);
        //关闭
        db.close();
    }

    public int huoqID(){

        String strSql = "select max(id) AS maxId from note_name";
        SQLiteDatabase db=getWritableDatabase();
        Cursor mCursor = db.rawQuery(strSql, null);
        mCursor.moveToNext();
        int id = mCursor .getInt(mCursor.getColumnIndex("maxId"));
        return id;
    }

    public long hangshu() {
        String sql = "Select count(*) from note_name" ;
        SQLiteDatabase db=getWritableDatabase();
        SQLiteStatement statement = db.compileStatement(sql);
        long count = statement.simpleQueryForLong();
        return count;
    }
    /*
     * 查询方法
     */
    public Cursor query(String name, int id)
    {
        //获得SQLiteDatabase实例
        SQLiteDatabase db=getWritableDatabase();
        //查询获得Cursor
        Cursor c=db.query(name, null, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        return c;
    }

    public void update(String table,ContentValues values,String MESS )
    {

        SQLiteDatabase db = getWritableDatabase();

        db.update(table, values, "name=?", new String[] {MESS});
    }//更新数据库

    public Cursor query(String name)
    {
        //获得SQLiteDatabase实例
        SQLiteDatabase db=getWritableDatabase();
        //查询获得Cursor
        Cursor c=db.query(name, null, null,null, null, null, null);
        return c;
    }
    /*
     * 删除方法
     */
    public void del(int id,String name)
    {
        if(db==null)
        {
            //获得SQLiteDatabase实例
            db=getWritableDatabase();
        }
        //执行删除
        db.delete(name, "id=?", new String[]{String.valueOf(id)});
    }
    public void del(String name)
    {
        if(db==null)
        {
            //获得SQLiteDatabase实例
            db=getWritableDatabase();
        }
        //执行删除
        db.delete(name,null,null);
    }
    /*
     * 关闭数据库
     */
    public void colse()
    {
        if(db!=null)
        {
            db.close();
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("drop table if exists note_name");
        db.execSQL("drop table if exists note_message");
        onCreate(db);
    }
}