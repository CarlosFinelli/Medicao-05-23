package com.example.famanlogistica.helpers

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.famanlogistica.classes.Eventos
import java.text.SimpleDateFormat
import java.util.Calendar

class DatabaseHelper(private val context: Context) : SQLiteOpenHelper(context, "MyDatabase.db", null, 2) {

    companion object {
        val TABLE_CHEGADA = "chegada"
        val TABLE_SAIDA = "saida"
        val TABLE_EVENTO = "evento"
        val TIPO_CHEGADA = 1
        val TIPO_SAIDA = 2
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableChegada = "CREATE TABLE $TABLE_CHEGADA (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "titulo TEXT, descricao TEXT, data TEXT, tolerancia TEXT, data_solitaria TEXT)"
        val createTableSaida = "CREATE TABLE $TABLE_SAIDA (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "titulo TEXT, descricao TEXT, data TEXT, tolerancia TEXT, data_solitaria TEXT)"
        val createTableEvento = "CREATE TABLE $TABLE_EVENTO (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "titulo TEXT, descricao TEXT, data TEXT, tolerancia TEXT, data_solitaria TEXT, tipo INT)"
        db?.execSQL(createTableChegada)
        db?.execSQL(createTableSaida)
        db?.execSQL(createTableEvento)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CHEGADA")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_SAIDA")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_EVENTO")
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
                evento.data_solitaria = cursor.getString(5)
                arrayList.add(evento)
            } while(cursor.moveToNext())
        }
        cursor.close()
        return arrayList
    }

    fun insertChegada(evento: Eventos) : Long{
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put("titulo", evento.titulo)
            put("descricao", evento.descricao)
            put("data", evento.data)
            put("tolerancia", evento.tolerancia)
            put("data_solitaria", evento.data_solitaria)
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
                evento.data_solitaria = cursor.getString(5)
                arrayList.add(evento)
            } while(cursor.moveToNext())
        }
        cursor.close()
        return arrayList
    }

    fun insertSaida(evento: Eventos) : Long {
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put("titulo", evento.titulo)
            put("descricao", evento.descricao)
            put("data", evento.data)
            put("tolerancia", evento.tolerancia)
            put("data_solitaria", evento.data_solitaria)
        }
        return db.insert(TABLE_SAIDA, null, cv)
    }

//    fun selectAll(data: String): ArrayList<Eventos> {
//        val arrayList = selectAll()
//        arrayList.removeIf {
//            it.data_solitaria != data
//        }
//        return arrayList
//    }

    fun selectAll(data: String): ArrayList<Eventos> {
        val arrayList = selectEvento()
        arrayList.removeIf {
            it.data_solitaria != data
        }
        return arrayList
    }

    fun selectEventsMonth(data: String) {
        val df = SimpleDateFormat("MM/yyyy")
        //val date = df.parse(data.substringAfter('/'))
        val date = data.substringAfter('/')
        val arrayDatas = selectAll()
        val array = ArrayList<String>()
        arrayDatas.forEach {
            array.add(it.data_solitaria.substringAfter('/'))
        }
        array.distinctBy { it }
    }

    @SuppressLint("Recycle")
    fun selectEvento() : ArrayList<Eventos> {
        val db = this.readableDatabase
        val arrayList = ArrayList<Eventos>()
        val cursor = db.rawQuery("SELECT * FROM $TABLE_EVENTO", null)
        if(cursor!!.moveToFirst()) {
            do {
                val evento = Eventos()
                evento.id = cursor.getInt(0)
                evento.titulo = cursor.getString(1)
                evento.descricao = cursor.getString(2)
                evento.data = cursor.getString(3)
                evento.tolerancia = cursor.getString(4)
                evento.data_solitaria = cursor.getString(5)
                evento.tipo = cursor.getInt(6)
                arrayList.add(evento)
            } while(cursor.moveToNext())
        }
        cursor.close()
        return arrayList
    }

    fun selectEventoChegada(date: Calendar) : ArrayList<Eventos> {
        val db = this.readableDatabase
        val data = SimpleDateFormat("dd/MM/yyyy").format(date.time)
        val arrayList = ArrayList<Eventos>()
        val cursor = db.rawQuery("SELECT * FROM $TABLE_EVENTO WHERE tipo = $TIPO_CHEGADA AND data_solitaria = $data", null)
        if(cursor!!.moveToFirst()) {
            do {
                val evento = Eventos()
                evento.id = cursor.getInt(0)
                evento.titulo = cursor.getString(1)
                evento.descricao = cursor.getString(2)
                evento.data = cursor.getString(3)
                evento.tolerancia = cursor.getString(4)
                evento.data_solitaria = cursor.getString(5)
                evento.tipo = cursor.getInt(6)
                arrayList.add(evento)
            } while(cursor.moveToNext())
        }
        cursor.close()
        return arrayList
    }

    fun selectEventoChegada() : ArrayList<Eventos> {
        val db = this.readableDatabase
        val arrayList = ArrayList<Eventos>()
        val cursor = db.rawQuery("SELECT * FROM $TABLE_EVENTO WHERE tipo = $TIPO_CHEGADA", null)
        if(cursor!!.moveToFirst()) {
            do {
                val evento = Eventos()
                evento.id = cursor.getInt(0)
                evento.titulo = cursor.getString(1)
                evento.descricao = cursor.getString(2)
                evento.data = cursor.getString(3)
                evento.tolerancia = cursor.getString(4)
                evento.data_solitaria = cursor.getString(5)
                evento.tipo = cursor.getInt(6)
                arrayList.add(evento)
            } while(cursor.moveToNext())
        }
        cursor.close()
        return arrayList
    }

    fun selectEventoSaida(date: Calendar) : ArrayList<Eventos> {
        val db = this.readableDatabase
        val arrayList = ArrayList<Eventos>()
        val data = SimpleDateFormat("dd/MM/yyyy").format(date.time)
        val cursor = db.rawQuery("SELECT * FROM $TABLE_EVENTO WHERE tipo = $TIPO_SAIDA AND data_solitaria = $data", null)
        if(cursor!!.moveToFirst()) {
            do {
                val evento = Eventos()
                evento.id = cursor.getInt(0)
                evento.titulo = cursor.getString(1)
                evento.descricao = cursor.getString(2)
                evento.data = cursor.getString(3)
                evento.tolerancia = cursor.getString(4)
                evento.data_solitaria = cursor.getString(5)
                evento.tipo = cursor.getInt(6)
                arrayList.add(evento)
            } while(cursor.moveToNext())
        }
        cursor.close()
        return arrayList
    }

    fun selectEventoSaida() : ArrayList<Eventos> {
        val db = this.readableDatabase
        val arrayList = ArrayList<Eventos>()
        val cursor = db.rawQuery("SELECT * FROM $TABLE_EVENTO WHERE tipo = $TIPO_SAIDA", null)
        if(cursor!!.moveToFirst()) {
            do {
                val evento = Eventos()
                evento.id = cursor.getInt(0)
                evento.titulo = cursor.getString(1)
                evento.descricao = cursor.getString(2)
                evento.data = cursor.getString(3)
                evento.tolerancia = cursor.getString(4)
                evento.data_solitaria = cursor.getString(5)
                evento.tipo = cursor.getInt(6)
                arrayList.add(evento)
            } while(cursor.moveToNext())
        }
        cursor.close()
        return arrayList
    }

    fun insertEvento(evento: Eventos) : Long {
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put("titulo", evento.titulo)
            put("descricao", evento.descricao)
            put("data", evento.data)
            put("tolerancia", evento.tolerancia)
            put("data_solitaria", evento.data_solitaria)
            put("tipo", evento.tipo)
        }
        return db.insert(TABLE_EVENTO, null, cv)
    }

    fun deleteEvento(id: Int) : Int {
        val db = this.writableDatabase
        return db.delete(TABLE_EVENTO, "ID = $id", null )
    }
}