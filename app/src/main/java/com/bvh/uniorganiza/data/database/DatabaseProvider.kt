package com.bvh.uniorganiza.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// Aquí se realiza la migración de la base de datos
private val MIGRATION_1_2 = object : Migration(1, 2) {

    override fun migrate(db: SupportSQLiteDatabase) {

        // Aquí se crea la tabla de categorías
        db.execSQL(
            """
            CREATE TABLE categoria (
                id_categoria INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                nombre TEXT NOT NULL
            )
            """.trimIndent()
        )

        // Aquí se crea una categoría general
        db.execSQL(
            "INSERT INTO categoria (nombre) VALUES ('General')"
        )

        // Aquí se crea la nueva tabla de usuarios
        db.execSQL(
            """
            CREATE TABLE usuario_new (
                id_usuario INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                nombre TEXT NOT NULL,
                correo TEXT NOT NULL,
                contrasena TEXT NOT NULL
            )
            """.trimIndent()
        )

        // Aquí se conservan los usuarios existentes
        db.execSQL(
            """
            INSERT INTO usuario_new (
                id_usuario, nombre, correo, contrasena
            )
            SELECT idUsuario, nombre, correo, contrasena
            FROM usuario
            """.trimIndent()
        )

        // Aquí se guardan temporalmente las tareas existentes
        db.execSQL(
            """
            CREATE TABLE tarea_backup AS
            SELECT idTarea, nombre, descripcion, fecha, hora,
                   completada, idUsuario
            FROM tarea
            """.trimIndent()
        )

        // Aquí se eliminan las tablas anteriores
        db.execSQL("DROP TABLE tarea")
        db.execSQL("DROP TABLE usuario")

        // Aquí se establece el nombre definitivo de usuarios
        db.execSQL("ALTER TABLE usuario_new RENAME TO usuario")

        // Aquí se crea el índice único del correo
        db.execSQL(
            "CREATE UNIQUE INDEX index_usuario_correo ON usuario(correo)"
        )

        // Aquí se crea la nueva tabla de tareas
        db.execSQL(
            """
            CREATE TABLE tarea_new (
                id_tarea INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                nombre TEXT NOT NULL,
                descripcion TEXT NOT NULL,
                fecha TEXT NOT NULL,
                hora TEXT NOT NULL,
                completada INTEGER NOT NULL,
                id_usuario INTEGER NOT NULL,
                id_categoria INTEGER NOT NULL,
                FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
                    ON DELETE CASCADE,
                FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
            )
            """.trimIndent()
        )

        // Aquí se recuperan las tareas anteriores
        db.execSQL(
            """
            INSERT INTO tarea_new (
                id_tarea, nombre, descripcion, fecha, hora,
                completada, id_usuario, id_categoria
            )
            SELECT
                idTarea, nombre, descripcion, fecha, hora,
                completada, idUsuario, 1
            FROM tarea_backup
            """.trimIndent()
        )

        // Aquí se elimina la copia temporal de tareas
        db.execSQL("DROP TABLE tarea_backup")

        // Aquí se establece el nombre definitivo de tareas
        db.execSQL("ALTER TABLE tarea_new RENAME TO tarea")

        // Aquí se crean los índices de tareas
        db.execSQL(
            "CREATE INDEX index_tarea_id_usuario ON tarea(id_usuario)"
        )

        db.execSQL(
            "CREATE INDEX index_tarea_id_categoria ON tarea(id_categoria)"
        )

        // Aquí se crea la tabla de recordatorios
        db.execSQL(
            """
            CREATE TABLE recordatorio (
                id_recordatorio INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                fecha_recordatorio TEXT NOT NULL,
                hora_recordatorio TEXT NOT NULL,
                activo INTEGER NOT NULL,
                id_tarea INTEGER NOT NULL,
                FOREIGN KEY (id_tarea) REFERENCES tarea(id_tarea)
                    ON DELETE CASCADE
            )
            """.trimIndent()
        )

        // Aquí se crea el índice de recordatorios
        db.execSQL(
            "CREATE INDEX index_recordatorio_id_tarea ON recordatorio(id_tarea)"
        )
    }
}

// Aquí se administra la instancia de Room
object DatabaseProvider {

    @Volatile
    private var INSTANCE: UniOrganizaDatabase? = null

    // Aquí se obtiene la conexión con la base de datos
    fun obtenerBaseDatos(context: Context): UniOrganizaDatabase {
        return INSTANCE ?: synchronized(this) {

            val instancia = Room.databaseBuilder(
                context.applicationContext,
                UniOrganizaDatabase::class.java,
                "uniorganiza_db"
            )
                .addMigrations(MIGRATION_1_2)
                .build()

            INSTANCE = instancia
            instancia
        }
    }
}