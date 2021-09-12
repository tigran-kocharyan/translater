//package ru.totowka.translator.data.datastores.db
//
//import androidx.room.ColumnInfo
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//import ru.totowka.translator.data.datastore.db.DatabaseScheme
//
//@Entity(tableName = DatabaseScheme.TranslationTableScheme.TABLE_NAME)
//data class TranslationEntity(
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = DatabaseScheme.TranslationTableScheme.COLUMN_BOOK_ID)
//    val bookId: Long?,
//
//    @ColumnInfo(name = DatabaseScheme.TranslationTableScheme.COLUMN_UUID)
//    val uuid: String?,
//
//    @ColumnInfo(name = DatabaseScheme.TranslationTableScheme.COLUMN_TITLE)
//    val title: String?,
//
//    @ColumnInfo(name = DatabaseScheme.TranslationTableScheme.COLUMN_AUTHOR)
//    val author: String?,
//
//    @ColumnInfo(name = DatabaseScheme.TranslationTableScheme.COLUMN_NUMBER_OF_VOLUMES)
//    val numberOfVolume: Int?,
//
//    @ColumnInfo(name = DatabaseScheme.TranslationTableScheme.COLUMN_SHELF_NUMBER)
//    val shelfNumber: Int?
//)