package com.journal.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.journal.R
import com.journal.database.dao.NoteDao
import com.journal.database.entities.Note
import com.journal.database.entities.Notebook
import java.util.concurrent.Executors

@Database(
    entities = [Note::class, Notebook::class],
    version = 1
)
abstract class JournalDB : RoomDatabase() {

    abstract fun getNoteDao(): NoteDao

    companion object {
        private var INSTANCE: JournalDB? = null


        @JvmStatic
        fun get(context: Context): JournalDB {
            if (INSTANCE == null) {
                INSTANCE =
                    Room.databaseBuilder(context, JournalDB::class.java, "journaldb.db")
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                Executors.newSingleThreadScheduledExecutor().execute {
                                    with(get(context).getNoteDao()) {
                                        insertNoteBook(Notebook(1, "Ruff", "#9E5CFD"))
                                        insertNote(Note(1, context.getString(R.string.guide), 1))
                                    }
                                }
                            }
                        })
                        .build()
            }
            return INSTANCE!!
        }

        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }


}