package com.example.py7.crudkotlin

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// this the class consist of database details
// table name : Product
// columns : Name, Brand, Price, Category, Quantity, Size, Description, Date, Image
class DBAdapter {

    private val dbName = "dbProduct4"
    private val dbTable = "Product"
    private val colId = "Id"
    private val colName = "Name"
    private val colBrand = "Brand"
    private val colPrice = "Price"
    private val colCategory = "Category"
    private val colQuantity = "Quantity"
    private val colSize = "Size"
    private val colDescription = "Description"
    private val colDate = "Date"
    private val colImage = "Image"
    private val dbVersion = 1

    // create table with specified columns
    private val CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " +
            dbTable + " (" + colId + " " + "INTEGER PRIMARY KEY," + colName + " TEXT, " +colImage+" BLOB, "+
            colCategory+" TEXT, "+colQuantity + " TEXT, "+colSize+" TEXT, "+colDescription+" TEXT, "+colDate+" TEXT, "+
            colBrand + " TEXT, " + colPrice + " TEXT);"
    private var db: SQLiteDatabase? = null

    constructor(context: Context){
        var dbHelper = DatabaseHelper(context)
        db = dbHelper.writableDatabase
    }

    // to get all the item
    fun allQuery(): Cursor {
        return db!!.rawQuery("select * from " + dbTable, null)
    }

    // to insert an item
    fun insert(values: ContentValues): Long{
        val ID = db!!.insert(dbTable, "", values)
        return ID
    }

    // to update an item
    fun update(values: ContentValues, selection: String, selectionArgs: Array<String>): Int{
        val count = db!!.update(dbTable, values, selection, selectionArgs)
        return count
    }

    // to delete an item
    fun delete(selection: String, selectionArgs: Array<String>):Int{
        val count = db!!.delete(dbTable, selection, selectionArgs)
        return count
    }

    inner class DatabaseHelper : SQLiteOpenHelper {

        var context: Context? = null

        constructor(context: Context) : super(context, dbName, null, dbVersion){
            this.context = context
        }

        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(CREATE_TABLE_SQL)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("Drop table IF EXISTS " + dbTable)
        }

    }
}