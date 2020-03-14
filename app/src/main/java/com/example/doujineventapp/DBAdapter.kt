package com.example.doujineventapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBAdapter(val context: Context){
    private var db: SQLiteDatabase? = null
    private val helper = DBHelper(context)

    companion object{
        private const val DATABASE_NAME = "CircleDB.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "ciecledb"

        const val ID = "_id"
        const val CIRCLE_NAME = "circleName"
        const val PEN_NAME = "penName"
        const val SPACE = "space"
        const val PRICE = "price"
        const val GIFT_EXISTS = "giftExists"
        const val NOTE = "note"
        const val IS_CHECKED = "isChecked"

        private const val SQL_CREATE_ENTRIES = "CREATE TABLE $TABLE_NAME (" +
                "$ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$CIRCLE_NAME TEXT, " +
                "$PEN_NAME TEXT, " +
                "$SPACE TEXT, " +
                "$PRICE INTEGER, " +
                "$GIFT_EXISTS INTEGER, " +
                "$NOTE TEXT, " +
                "$IS_CHECKED INTEGER);"
    }

    // DBの読み書き
    fun openDB(): DBAdapter {
        db = helper.writableDatabase
        return this
    }

    // DBの読み込み
    fun readDB(): DBAdapter{
        db = helper.readableDatabase
        return this
    }

    // DBを閉じる
    fun closeDB() {
        db?.close()
        db = null
    }

    // DBへの登録
    fun registerDB(circle: Circle): Int {
        db?.beginTransaction()
        var id = -1
        try {
            val values = ContentValues()
            values.put(CIRCLE_NAME, circle.circleName)
            values.put(PEN_NAME, circle.penName)
            values.put(SPACE, circle.space)
            values.put(PRICE, circle.price)
            values.put(GIFT_EXISTS, if (circle.giftExists) 1 else 0)
            values.put(NOTE, circle.note)
            values.put(IS_CHECKED, if (circle.isChecked) 1 else 0)

            db?.insert(TABLE_NAME, null, values)?.let {
                id = it.toInt()
            }

            db?.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.i("register", e.message)
        } finally {
            db?.endTransaction()
        }
        return id
    }

    // DBのデータを取得
    fun getDB(columns: Array<String>?): Cursor {
        db?.query(TABLE_NAME, columns, null, null, null, null, null) ?.let {
            return it
        } ?: throw RuntimeException("Database does not open")
    }

    // DBの検索
    fun searchDB(columns: Array<String>?, column: String, name: Array<String>?): Cursor {
        db?.query(TABLE_NAME, columns, "$column like ?", name, null, null, null) ?.let {
            return it
        } ?: throw RuntimeException("Database does not open")
    }

    // DBの更新
    fun updateDB(position: Int, columns: Map<String, Any>) {
        db?.beginTransaction()
        try {
            val values = ContentValues()
            for ((key, value) in columns) {
                when (value) {
                    is Int -> values.put(key, value)
                    is String -> values.put(key, value)
                    is Boolean -> values.put(key, if (value) 1 else 0)
                }
            }
            db?.update(TABLE_NAME, values, "$ID=?", arrayOf(position.toString()))
            db?.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.i("update", e.message)
        } finally {
            db?.endTransaction()
        }
    }

    // DB全削除
    fun allDelete() {
        db?.beginTransaction()
        try {
            db?.delete(TABLE_NAME, null, null)
            db?.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.i("allDelete", e.message)
        } finally {
            db?.endTransaction()
        }
    }

    // DB選択削除
    fun selectDelete(position: Int) {
        db?.beginTransaction()
        try {
            db?.delete(TABLE_NAME, "$ID=?", arrayOf(position.toString()))
            db?.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.i("selectDelete", e.message)
        } finally {
            db?.endTransaction()
        }
    }


    private inner class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
        override fun onCreate(db: SQLiteDatabase?) {
            // テーブル作成
            db?.execSQL(SQL_CREATE_ENTRIES)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
    }


}