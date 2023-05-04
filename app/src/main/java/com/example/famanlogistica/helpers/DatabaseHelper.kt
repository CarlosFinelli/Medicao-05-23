package com.example.famanlogistica.helpers

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.famanlogistica.classes.Eventos
import java.text.SimpleDateFormat
import java.util.Calendar

class DatabaseHelper(private val context: Context) : SQLiteOpenHelper(context, "MyDatabase.db", null, 1) {

    companion object {
        val TABLE_CHEGADA = "chegada"
        val TABLE_SAIDA = "saida"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableChegada = "CREATE TABLE $TABLE_CHEGADA (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "titulo TEXT, descricao TEXT, data TEXT, tolerancia TEXT)"
        val createTableSaida = "CREATE TABLE $TABLE_SAIDA (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "titulo TEXT, descricao TEXT, data TEXT, tolerancia TEXT)"
        db?.execSQL(createTableChegada)
        db?.execSQL(createTableSaida)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CHEGADA")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_SAIDA")
        onCreate(db)
    }

    fun selectAll() : ArrayList<Eventos>{
        val db = this.readableDatabase
        val arrayList = ArrayList<Eventos>()
        val arrayChegada = selectChegadas()
        val arraySaidas = selectSaidas()
        arrayChegada.forEach { arrayList.add(it) }
        arraySaidas.forEach { arrayList.add(it) }
        //arrayList.groupBy { it.data }
        arrayList.sortBy { it.data }
        return arrayList
    }

    fun selectRange(currentDate: Calendar, days: Int) : ArrayList<Eventos> {
        val db = this.readableDatabase
        val arrayList = selectAll()
        val array = arrayList
//        arrayList.removeIf{
//            val dateFomat = SimpleDateFormat("dd/MM/yyyy HH:mm")
//            dateFomat.parse(it.data).before(currentDate.time)
//        }
//        arrayList.removeIf{
//            //currentDate.set(Calendar.DAY_OF_MONTH, currentDate.get(Calendar.DAY_OF_MONTH) + days)
//            val date = Calendar.getInstance()
//            date.set(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH))
//            date.set(Calendar.DAY_OF_MONTH, currentDate.get(Calendar.DAY_OF_MONTH + days))
//            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
//            val data = dateFormat.parse(it.data)
//            data.after(date.time)
//        }
        return arrayList
    }

    @SuppressLint("Recycle")
    fun selectChegadas() : ArrayList<Eventos> {
        val db = this.readableDatabase
        val arrayList = ArrayList<Eventos>()
        val cursor = db.rawQuery("SELECT * FROM $TABLE_CHEGADA", null)
        if(cursor!!.moveToFirst()) {
            do {
                val evento = Eventos()
                evento.id = cursor.getInt(0)
                evento.titulo = cursor.getString(1)
                evento.descricao = cursor.getString(2)
                evento.data = cursor.getString(3)
                evento.tolerancia = cursor.getString(4)
                arrayList.add(evento)
            } while(cursor.moveToNext())
        }
        return arrayList
    }

    fun insertChegada(evento: Eventos) : Long{
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put("titulo", evento.titulo)
            put("descricao", evento.descricao)
            put("data", evento.data)
            put("tolerancia", evento.tolerancia)
        }
        return db.insert(TABLE_CHEGADA, null, cv)
    }

    @SuppressLint("Recycle")
    fun selectSaidas() : ArrayList<Eventos> {
        val db = this.readableDatabase
        val arrayList = ArrayList<Eventos>()
        val cursor = db.rawQuery("SELECT * FROM $TABLE_SAIDA", null)
        if(cursor!!.moveToFirst()) {
            do {
                val evento = Eventos()
                evento.id = cursor.getInt(0)
                evento.titulo = cursor.getString(1)
                evento.descricao = cursor.getString(2)
                evento.data = cursor.getString(3)
                evento.tolerancia = cursor.getString(4)
                arrayList.add(evento)
            } while(cursor.moveToNext())
        }
        return arrayList
    }

    fun insertSaida(evento: Eventos) : Long {
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put("titulo", evento.titulo)
            put("descricao", evento.descricao)
            put("data", evento.data)
            put("tolerancia", evento.tolerancia)
        }
        return db.insert(TABLE_SAIDA, null, cv)
    }
}