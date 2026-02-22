package com.example.viikko1.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.viikko1.data.local.dao.TaskDao
import com.example.viikko1.data.local.entity.Task
import com.example.viikko1.model.mockTasks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

// @Database kertoo Room:ille mitä tauluja (entities) tietokanta sisältää
// version = tietokannan versio (kasvatetaan kun rakenne muuttuu)
// exportSchema = false → ei tallenna skeemaa tiedostoon (tuotannossa true)
@Database(
    entities = [Task::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // Abstrakti funktio, jonka kautta pääsee DAO:n metodeihin
    // Room generoi toteutuksen automaattisesti
    abstract fun taskDao(): TaskDao

    // Singleton-pattern: vain yksi tietokantayhteys koko sovelluksessa
    // Tämä estää usean yhtäaikaisen yhteyden ongelmat
    companion object {
        // @Volatile = muuttujan arvo näkyy heti kaikille säikeille
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // Jos instanssi on jo olemassa, palauta se
            // synchronized = vain yksi säie kerrallaan voi luoda tietokannan
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,  // Application context (ei Activity)
                    AppDatabase::class.java,     // Tietokantaluokka
                    "task_database"              // Tietokantatiedoston nimi
                )
                    // VAROITUS: fallbackToDestructiveMigration() tuhoaa kaiken datan
                    // kun tietokannan versio muuttuu! Tuotannossa käytä migraatioita.
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                // Prepopulate database with mock data on first creation if it's empty
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val dao = instance.taskDao()
                        val current = dao.getAllTasks().first()
                        if (current.isEmpty()) {
                            mockTasks.forEach { task ->
                                // insert uses @Insert(onConflict = REPLACE)
                                dao.insert(task)
                            }
                        }
                    } catch (e: Exception) {
                        // Log or ignore: DB might be in migration state; keep silent to avoid crash
                    }
                }

                instance
            }
        }
    }
}

// Tietokantatiedosto luodaan polkuun:
// /data/data/com.example.taskapp/databases/task_database