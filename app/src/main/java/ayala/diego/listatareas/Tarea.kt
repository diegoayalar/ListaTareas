package ayala.diego.listatareas

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tareas")
data class Tarea (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var descripcion: String
) {
    override fun toString(): String {
        return descripcion
    }
}