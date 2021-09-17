package ru.totowka.translator.data.datastore.db

import android.content.Context
import androidx.room.Room

/**
 * Объект для доступа к информации БД
 */
object  AppDatabase {

    private var database: RoomDictionaryDatabase? = null

    /**
     * Получить базу данных из контекста [context]
     *
     * @param context контекст
     */
    fun getDatabase(context: Context): RoomDictionaryDatabase {
        if (database == null) {
            database = Room.databaseBuilder(
                context,
                RoomDictionaryDatabase::class.java,
                DatabaseScheme.DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
        return database!!
    }
}