package com.bvh.uniorganiza.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

// Aquí se conecta la base de datos con sus tablas
@Database(
    entities = [
        UsuarioEntity::class,
        TareaEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class UniOrganizaDatabase : RoomDatabase() {

    // Aquí se habilitan las operaciones de usuarios
    abstract fun usuarioDao(): UsuarioDao

    // Aquí se habilitan las operaciones de tareas
    abstract fun tareaDao(): TareaDao
}