//package ru.totowka.translator.data.datastore.db
//
//import android.content.Context
//import androidx.room.Room
//
//object AppDatabase {
//
//    private var database: RoomLibraryDatabase? = null
//
//    fun getDatabase(context: Context): RoomLibraryDatabase {
//        if (database == null) {
//            database = Room.databaseBuilder(
//                context,
//                RoomLibraryDatabase::class.java,
//                DatabaseScheme.DB_NAME)
//                .build()
//        }
//        return database!!
//    }
//}