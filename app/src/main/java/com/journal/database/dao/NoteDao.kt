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

package com.journal.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.journal.database.entities.Note
import com.journal.database.entities.NoteInfo
import com.journal.database.entities.Notebook

@Dao
interface NoteDao {
    @Query("SELECT * from Notebook WHERE title LIKE :query")
    fun getNotebooks(query: String): LiveData<List<Notebook>>

    @Query("SELECT COUNT(*) from Notebook")
    fun hasNotebooks(): LiveData<Boolean>

    @Query("SELECT n.*, nb.color as color from Note n JOIN Notebook nb ON n.notebook_id = nb.notebook_id WHERE (n.note LIKE :query) ORDER BY n.notebook_id ASC")
    fun getNotes(query: String): LiveData<List<NoteInfo>>

    @Query("SELECT n.* from Note n WHERE n.notebook_id = :notebookId")
    fun getNotes(notebookId: Long): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notebook: Notebook)

    @Insert
    suspend fun insert(notebook: Note): Long

    @Insert
    fun insertNote(notebook: Note): Long

    @Transaction
    suspend fun draft(notebook: Note): Long {
        return insert(notebook)
    }

    @Query("DELETE FROM note where TRIM(note) = '#' OR TRIM(note) = '' ")
    suspend fun deleteDrafts()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNoteBook(notebook: Notebook)

    @Query("SELECT * FROM Notebook where notebook_id =:id")
    fun getNotebook(id: Long?): LiveData<Notebook>

    @Query("SELECT n.* from Note n where n.notebook_id =:id AND (n.note LIKE :q) ORDER BY CASE WHEN :isASCOrder THEN -n.note_id ELSE n.note_id END DESC")
    fun getNotes(id: Long?, q: String?, isASCOrder: Boolean): LiveData<List<Note>>


    @Query("SELECT * FROM Note WHERE note_id=:id")
    suspend fun getNote(id: Long?): Note

    @Update
    fun update(value: Note?)

    @Query("DELETE FROM Notebook WHERE notebook_id =:notebookId")
    suspend fun deleteNotebook(notebookId: Long)
}