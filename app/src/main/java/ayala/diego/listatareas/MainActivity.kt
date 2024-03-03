package ayala.diego.listatareas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.room.Room

class MainActivity : AppCompatActivity() {
    private lateinit var etTarea: EditText
    private lateinit var btnAgregar: Button
    private lateinit var listViewTareas: ListView
    private lateinit var tareas: ArrayList<Tarea>
    private lateinit var adapter: ArrayAdapter<Tarea>
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etTarea = findViewById(R.id.edit_text_tarea)
        btnAgregar = findViewById(R.id.btn_agregar)
        listViewTareas = findViewById(R.id.list_view_tareas)

        tareas = ArrayList()

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "tareas-db"
        ).allowMainThreadQueries().build()

        obtenerTareas()

        adapter = ArrayAdapter(this, R.layout.item_lista, tareas)
        listViewTareas.adapter = adapter

        btnAgregar.setOnClickListener {
            val descripcion = etTarea.text.toString()

            if(descripcion.isNotBlank()) {
                val nuevaTarea = Tarea(descripcion = descripcion)
                db.tareaDao().agregarTarea(nuevaTarea)

                tareas.clear()
                tareas.addAll(db.tareaDao().obtenerTareas())

                etTarea.setText("")
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this, "Llenar campo", Toast.LENGTH_SHORT).show()
            }
        }

        listViewTareas.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val tarea = tareas[position]

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Actualizar tarea")
            val input = EditText(this)
            input.setText(tarea.descripcion)
            builder.setView(input)

            builder.setPositiveButton("Actualizar") { dialog, which ->
                val nuevaDescripcion = input.text.toString()
                if (nuevaDescripcion.isNotBlank()) {
                    tarea.descripcion = nuevaDescripcion
                    db.tareaDao().actualizarTarea(tarea)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "El campo no puede estar vacío", Toast.LENGTH_SHORT).show()
                }
            }

            builder.setNegativeButton("Cancelar") { dialog, which -> dialog.cancel() }

            builder.show()
        }

        listViewTareas.onItemLongClickListener = AdapterView.OnItemLongClickListener { parent, view, position, id ->
            val tarea = db.tareaDao().getTarea(tareas[position].id)

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Eliminar tarea")
            builder.setMessage("¿Seguro de eliminar la tarea \"${tarea.descripcion}\"?")

            builder.setPositiveButton("Eliminar") { dialog, which ->
                db.tareaDao().eliminarTarea(tarea)
                tareas.removeAt(position)
                adapter.notifyDataSetChanged()
            }

            builder.setNegativeButton("Cancelar") { dialog, which -> dialog.cancel() }

            builder.show()

            true
        }
    }

    private fun obtenerTareas() {
        val tareasDb = db.tareaDao().obtenerTareas()
        tareas.addAll(tareasDb)
    }
}