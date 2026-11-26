package com.bvh.uniorganiza.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

// Aquí se conectan las cuatro tablas de la aplicación
@Database(
    entities = [
        UsuarioEntity::class,
        TareaEntity::class,
        CategoriaEntity::class,
        RecordatorioEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class UniOrganizaDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    abstract fun tareaDao(): TareaDao

    abstract fun categoriaDao(): CategoriaDao

    abstract fun recordatorioDao(): RecordatorioDao
}