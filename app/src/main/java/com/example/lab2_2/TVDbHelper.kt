package com.example.lab2_2

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TVDbHelper (context: Context) :  //наш класс для работы с БД, наследуется от
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){ //стандартного класса
        companion object{ // тут прописываем переменные для БД
            private val DATABASE_NAME = "TV's" //имя БД
            private val DATABASE_VERSION = 1 // версия
            val TABLE_NAME = "tvs_table" // имя таблицы
            val ID_COL = "id" // переменная для поля id
            val NAME_COl = "tv_name" // переменная для поля lang_name
            val TIME_COL = "time" // переменная для поля year
            val CHANNEL_COL = "channel" // переменная для поля channel
            val FIO_COL = "fio"
            val PICTURE_COL = "picture" // переменная для поля picture
        }
        override fun onCreate(db: SQLiteDatabase) { //метод для создания таблицы через SQL-запрос
            val query = ("CREATE TABLE " + TABLE_NAME + " (" //конструируем запрос через
                    + ID_COL + " INTEGER PRIMARY KEY autoincrement, "
                    + NAME_COl + " TEXT,"
                    + TIME_COL + " TEXT,"
                    + CHANNEL_COL + " TEXT,"
                    + FIO_COL + " TEXT,"
                    + PICTURE_COL + " TEXT" + ")"
                    )
            db.execSQL(query) // выполняем SQL-запрос
        }
        override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {//метод для обновления БД
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        }
        fun getCurcor(): Cursor? { // метод для получения всех записей таблицы БД в виде курсора
            val db = this.readableDatabase // получаем ссылку на БД только для чтения
            return db.rawQuery("SELECT * FROM $TABLE_NAME", null) //возвращаем курсор в виде
        } //результата выборки всех записей из нашей таблицы
        fun isEmpty(): Boolean { //метод для проверки БД на отсутствие записей
            val cursor = getCurcor() //получаем курсор таблицы БД с записями
            return !cursor!!.moveToFirst() //и возвращаем результат перехода к первой записи,
        } //инвертируя его, т.е. если нет записей, cursor!!.moveToFirst() вернет false, отрицание его
        //даст true
        fun printDB(){ //метод для печати БД в консоль
            val cursor = getCurcor() //получаем курсор БД
            if (!isEmpty()) { //если БД не пустая
                cursor!!.moveToFirst() //переходим к первой записи
                val nameColIndex = cursor.getColumnIndex(NAME_COl) //получаем индексы для колонок
                val timeColIndex = cursor.getColumnIndex(TIME_COL) //с нужными данными
                val channelColIndex = cursor.getColumnIndex(CHANNEL_COL)
                val fioColIndex = cursor.getColumnIndex(FIO_COL)
                val pictureColIndex = cursor.getColumnIndex(PICTURE_COL)
                if (nameColIndex == -1 || timeColIndex == -1 || channelColIndex == -1 || fioColIndex == -1 || pictureColIndex == -1) {
                    println("Error: One or more columns do not exist in the cursor")
                    return
                }

                do {
                    print("${cursor.getString(nameColIndex)} ")
                    print("${cursor.getString(timeColIndex)} ")
                    println("${cursor.getString(channelColIndex)} ")
                    print("${cursor.getString(fioColIndex)} ")
                    println("${cursor.getString(pictureColIndex)} ")
                } while (cursor.moveToNext())
            } else println("DB is empty")
        }
        fun addArrayToDB(TV: ArrayList<TV>){ //метод для добавления целого массива в БД
            TV.forEach { //цикл по всем элементам массива
                addTV(it) //добавляем элемент массива в БД
            }
        }
        fun addTV(TV: TV){ // метод для добавления языка в БД
            val values = ContentValues() // объект для создания значений, которые вставим в БД
            values.put(NAME_COl, TV.name) // добавляем значения в виде пары ключ-значение
            values.put(TIME_COL, TV.time)
            values.put(CHANNEL_COL, TV.channel)
            values.put(FIO_COL, TV.fio)
            values.put(PICTURE_COL, TV.picture)
            val db = this.writableDatabase //получаем ссылку для записи в БД
            db.insert(TABLE_NAME, null, values) // вставляем все значения в БД в нашу таблицу
            db.close() // закрываем БД (для записи)
        }
        fun changeImgForTV(name: String, img: String){ // метод для изменения картинки для языка
            val db = this.writableDatabase //получаем ссылку для записи в БД
            val values = ContentValues() // объект для изменения записи
            values.put(PICTURE_COL, img) // вставляем новую картинку
//и делаем запрос в БД на изменение поля с нужным названием в нашей таблице
            db.update(TABLE_NAME, values, NAME_COl+" = '$name'", null)
            db.close() // закрываем БД (для записи)
        }
        fun getTVArray(): ArrayList<TV>{ // метод для получения данных из таблицы в виде
    //массива
            val TVArray = ArrayList<TV>() //массив, в который запишем данные
            val cursor = getCurcor() //получаем курсор таблицы БД
            if (!isEmpty()) { //если БД не пустая
                cursor!!.moveToFirst() //переходим к первой записи
                val nameColIndex = cursor.getColumnIndex(NAME_COl) //получаем индексы для колонок
                val timeColIndex = cursor.getColumnIndex(TIME_COL) //с нужными данными
                val ChannelColIndex = cursor.getColumnIndex(CHANNEL_COL)
                val fioColIndex = cursor.getColumnIndex(FIO_COL)
                val pictureColIndex = cursor.getColumnIndex(PICTURE_COL)
                do { //цикл по всем записям
                    val name = cursor.getString(nameColIndex) //получаем данные полей
                    val time = cursor.getString(timeColIndex) //и записываем их в переменные
                    val Channel = cursor.getString(ChannelColIndex)
                    val fio = cursor.getString(fioColIndex)
                    val picture = cursor.getString(pictureColIndex)
                    TVArray.add(TV(name, time, Channel,fio, picture)) //и создаем объект с этими данными
                } while (cursor.moveToNext()) //пока есть записи
            } else println("DB is empty") //иначе пишем, что БД пустая
            return TVArray //возвращаем созданный массив
        }
    fun removeTV(name: String){
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "name=?", Array(1){name})
        db.close()
    }
    }
