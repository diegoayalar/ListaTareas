package ayala.diego.listatareas

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Tarea:: class], version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun tareaDao(): TareaDAO
}