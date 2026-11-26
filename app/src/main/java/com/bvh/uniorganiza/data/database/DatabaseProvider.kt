package com.bvh.uniorganiza.data.database

import android.content.Context
import androidx.room.Room

// Aquí se crea la instancia de la base de datos
object DatabaseProvider {

    @Volatile
    private var INSTANCE: UniOrganizaDatabase? = null

    // Aquí se obtiene la conexión con Room
    fun obtenerBaseDatos(context: Context): UniOrganizaDatabase {
        return INSTANCE ?: synchronized(this) {
            val instancia = Room.databaseBuilder(
                context.applicationContext,
                UniOrganizaDatabase::class.java,
                "uniorganiza_db"
            ).build()

            INSTANCE = instancia
            instancia
        }
    }
}