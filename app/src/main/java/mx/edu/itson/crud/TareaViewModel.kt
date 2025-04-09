package mx.edu.itson.crud

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class TareaViewModel : ViewModel() {
    private val db = Firebase.firestore

    private var _listaTareas = MutableLiveData<List<Tarea>>(emptyList())
    val listaTareas: LiveData<List<Tarea>> = _listaTareas

    init {
        obtenerTareas()
    }

    fun obtenerTareas() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val resultado = db.collection("tareas").get().await()

                val tareas = resultado.documents.mapNotNull { it.toObject(Tarea::class.java) }
                _listaTareas.postValue(tareas)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun agregarTareas(tarea: Tarea) {
        tarea.id = UUID.randomUUID().toString()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("tareas").document(tarea.id).set(tarea).await()
                _listaTareas.postValue(_listaTareas.value?.plus(tarea))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun actualizarTareas(tarea: Tarea) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("tareas").document(tarea.id).update(tarea.toMap()).await()
                _listaTareas.postValue(_listaTareas.value?.map { if (it.id == tarea.id) tarea else it })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun borrarTareas(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("tareas").document(id).delete().await()
                _listaTareas.postValue(_listaTareas.value?.filter { it.id != id })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }




}