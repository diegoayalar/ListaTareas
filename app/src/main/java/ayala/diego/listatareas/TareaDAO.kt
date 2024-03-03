package ayala.diego.listatareas

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TareaDAO {

    @Query("SELECT * FROM tareas")
    fun obtenerTareas(): List<Tarea>

    @Query("SELECT * FROM tareas WHERE id = :id")
    fun getTarea(id: Int): Tarea

    @Insert
    fun agregarTarea(tarea: Tarea)

    @Update
    fun actualizarTarea(tarea: Tarea)

    @Delete
    fun eliminarTarea(tarea: Tarea)
}