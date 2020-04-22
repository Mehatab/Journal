package com.journal.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.journal.database.entities.Note
import com.journal.database.entities.NoteInfo
import com.journal.database.entities.Notebook

@Dao
interface NoteDao {
    @Query("SELECT * from Notebook WHERE title LIKE :query")
    fun getNotebooks(query: String): LiveData<List<Notebook>>

    @Query("SELECT n.*, nb.color as color from Note n JOIN Notebook nb ON n.notebook_id = nb.notebook_id WHERE (n.note_title LIKE :query OR n.note LIKE :query)")
    fun getNotes(query: String): LiveData<List<NoteInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notebook: Notebook)

    @Insert
    suspend fun insert(notebook: Note)

    @Insert
    fun insertNote(notebook: Notebook)

    @Query("SELECT * FROM Notebook where notebook_id =:id")
    fun getNotebook(id: Long?): LiveData<Notebook>

    @Query("SELECT n.* from Note n where n.notebook_id =:id AND (n.note_title LIKE :q OR n.note LIKE :q)")
    fun getNotes(id: Long?, q: String?): LiveData<List<Note>>

}