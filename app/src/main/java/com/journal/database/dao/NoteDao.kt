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

    @Query("SELECT n.*, nb.color as color from Note n JOIN Notebook nb ON n.notebook_id = nb.notebook_id WHERE (n.note LIKE :query) ORDER BY n.notebook_id ASC")
    fun getNotes(query: String): LiveData<List<NoteInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notebook: Notebook)

    @Insert
    suspend fun insert(notebook: Note): Long

    @Transaction
    suspend fun draft(notebook: Note): Long {
        return insert(notebook)
    }

    @Query("DELETE FROM note where TRIM(note) = '#' OR TRIM(note) = '' ")
    suspend fun deleteDrafts()

    @Insert
    fun insertNote(notebook: Notebook)

    @Query("SELECT * FROM Notebook where notebook_id =:id")
    fun getNotebook(id: Long?): LiveData<Notebook>

    @Query("SELECT n.* from Note n where n.notebook_id =:id AND (n.note LIKE :q) ORDER BY CASE WHEN :isASCOrder THEN -n.note_id ELSE n.note_id END ASC")
    fun getNotes(id: Long?, q: String?, isASCOrder: Boolean): LiveData<List<Note>>


    @Query("SELECT * FROM Note WHERE note_id=:id")
    suspend fun getNote(id: Long?): Note

    @Update
    fun update(value: Note?)
}