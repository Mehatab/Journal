/*
 * Copyright 2020 Mehatab Shaikh.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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