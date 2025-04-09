package mx.edu.itson.crud


import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import mx.edu.itson.crud.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TareaAdapter
    private lateinit var viewModel: TareaViewModel

    var tareaEdit = Tarea()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()  // Solo una vez

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)  // Solo una vez

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this)[TareaViewModel::class.java]

        viewModel.listaTareas.observe(this) { tareas ->
            setupRecyclerView(tareas)
        }

        binding.btnAgregarTarea.setOnClickListener {
            println("agregado")
            val tarea = Tarea(
                titulo = binding.etTitulo.text.toString(),
                descripcion = binding.etDescripcion.text.toString()
            )

            viewModel.agregarTareas(tarea)

            binding.etTitulo.setText("")
            binding.etDescripcion.setText("")
        }

        binding.btnActualizarTarea.setOnClickListener {
            tareaEdit.titulo = binding.etTitulo.text.toString()
            tareaEdit.descripcion = binding.etDescripcion.text.toString()

            viewModel.actualizarTareas(tareaEdit)
        }

        // Datos de prueba
        val tareasTest = listOf(
            Tarea("1", "Tarea de prueba 1", "Descripción de prueba 1"),
            Tarea("2", "Tarea de prueba 2", "Descripción de prueba 2")
        )
        setupRecyclerView(tareasTest)
    }
    fun setupRecyclerView(listaTareas: List<Tarea>){
        adapter = TareaAdapter(listaTareas, ::borrarTarea, ::actualizarTarea)
        binding.rvTareas.adapter = adapter
    }

    fun borrarTarea(id: String){
        viewModel.borrarTareas(id)
    }

    fun actualizarTarea(tarea: Tarea){
        tareaEdit = tarea

        binding.etTitulo.setText(tareaEdit.titulo)
        binding.etDescripcion.setText(tareaEdit.descripcion)
    }
}