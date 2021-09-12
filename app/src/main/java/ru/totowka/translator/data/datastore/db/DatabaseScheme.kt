//package ru.totowka.translator.data.datastore.db
//
//object DatabaseScheme {
//    const val DB_NAME = "dictionary"
//    const val DB_VERSION = 1
//
//    object TranslationTableScheme {
//        const val TABLE_NAME = "words"
//        const val COLUMN_BOOK_ID = "book_id"
//        const val COLUMN_UUID = "uuid"
//        const val COLUMN_TITLE = "title"
//        const val COLUMN_AUTHOR = "author"
//        const val COLUMN_NUMBER_OF_VOLUMES = "number_of_volume"
//        const val COLUMN_SHELF_NUMBER = "shelf_number"
//
//        const val SQL_CREATE_SCRIPT =
//            "CREATE TABLE $TABLE_NAME (" +
//                    "$COLUMN_BOOK_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
//                    "$COLUMN_UUID TEXT," +
//                    "$COLUMN_TITLE TEXT," +
//                    "$COLUMN_AUTHOR TEXT," +
//                    "$COLUMN_NUMBER_OF_VOLUMES INTEGER," +
//                    "$COLUMN_SHELF_NUMBER INTEGER)"
//    }
//}
//
