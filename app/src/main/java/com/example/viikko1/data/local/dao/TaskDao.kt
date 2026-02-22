package com.example.viikko1.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.viikko1.data.local.entity.Task

// @Dao merkitsee tämän interfacen Data Access Objectiksi
// Room generoi toteutuksen automaattisesti käännösaikana
@Dao
interface TaskDao {

    // ── CREATE (lisääminen) ──────────────────────────────────

    // @Insert lisää uuden rivin tauluun
    // onConflict = REPLACE → jos sama id on jo olemassa, korvaa vanha rivi
    // suspend = toimii korutiinissa (taustasäikeessä, ei jumita UI:ta)
    // Palauttaa lisätyn rivin id:n (Long)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task): Long

    // ── READ (lukeminen) ─────────────────────────────────────

    // @Query suorittaa SQL-kyselyn
    // Flow<List<Task>> = reaktiivinen virta, joka päivittyy automaattisesti
    // kun taulun data muuttuu → UI päivittyy itsestään!
    @Query("SELECT * FROM tasks ORDER BY created_at DESC")
    fun getAllTasks(): Flow<List<Task>>

    // :taskId on parametri, joka tulee funktion argumentista
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Int): Task?

    // Suodata tehtävät tilan mukaan (valmiit / keskeneräiset)
    @Query("SELECT * FROM tasks WHERE is_completed = :completed")
    fun getTasksByStatus(completed: Boolean): Flow<List<Task>>

    // Haku otsikon perusteella (LIKE = osittainen vastaavuus)
    @Query("SELECT * FROM tasks WHERE title LIKE '%' || :searchQuery || '%'")
    fun searchTasks(searchQuery: String): Flow<List<Task>>

    // ── UPDATE (päivittäminen) ───────────────────────────────

    // @Update päivittää rivin pääavaimen (id) perusteella
    @Update
    suspend fun update(task: Task)

    // Päivitä vain yksi sarake SQL:llä
    @Query("UPDATE tasks SET is_completed = :completed WHERE id = :taskId")
    suspend fun updateTaskStatus(taskId: Int, completed: Boolean)

    // ── DELETE (poistaminen) ─────────────────────────────────

    // @Delete poistaa rivin pääavaimen perusteella
    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM tasks WHERE is_completed = 1")
    suspend fun deleteCompletedTasks()

    // ── TILASTOT ─────────────────────────────────────────────

    // COUNT(*) laskee rivien määrän
    @Query("SELECT COUNT(*) FROM tasks WHERE is_completed = 0")
    fun getPendingTaskCount(): Flow<Int>
}