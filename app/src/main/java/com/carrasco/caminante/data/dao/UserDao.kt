package com.carrasco.caminante.data.dao

import android.content.Context
import android.util.Log
import com.carrasco.caminante.data.model.Publication
import com.carrasco.caminante.data.model.User
import com.carrasco.caminante.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await

abstract class UserDao {
    companion object{
        fun save(user: User){
            val data = hashMapOf(
                "email" to user.email,
                "savedPublication" to user.savedPublication
            )
            Log.d("Publication Data", data.toString())
            FirebaseFirestore.getInstance().collection("user").add(data)
        }

        suspend fun get(email: String): User? {
            val db = FirebaseFirestore.getInstance()
            val usersRef = db.collection("user")
            val query = usersRef.whereEqualTo("email", email).limit(1)
            val result = query.get().await()
            return if (!result.isEmpty) {
                val document = result.documents[0]
                document.toObject(User::class.java)
            } else {
                null
            }
        }

        suspend fun getSavedPublications(): Flow<List<Publication>> = flow {
            val db = FirebaseFirestore.getInstance()
            val userQuery = db.collection("user").whereEqualTo("email", getCurrentUserEmail())
            val userSnap = userQuery.get().await()
            val savedPublicationRefs = userSnap.documents[0].get("savedPublication") as? List<String>
            if (savedPublicationRefs == null || savedPublicationRefs.isEmpty()) {
                emit(emptyList<Publication>())
            } else {
                val publicationChunks = savedPublicationRefs.chunked(10)
                val publicationRequests = publicationChunks.map { chunk ->
                    db.collection("publication")
                        .whereIn(FieldPath.documentId(), chunk)
                        .get()
                        .asDeferred()
                }
                val publicationSnapshots = awaitAll(*publicationRequests.toTypedArray())
                val publications = publicationSnapshots.flatMap { snapshot ->
                    snapshot.toObjects(Publication::class.java)
                }
                emit(publications)
            }
        }


        fun getCurrentUserEmail(): String{
            return FirebaseAuth.getInstance().currentUser?.email!!
        }

        fun savePublication(publication: Publication, context: Context){
            val db = FirebaseFirestore.getInstance()
            val userQuery = db.collection("user").whereEqualTo("email", getCurrentUserEmail())
            userQuery.get()
                .addOnSuccessListener { querySnapshot ->
                    // Manejar los resultados de la consulta
                    if (!querySnapshot.isEmpty) {
                        val documento = querySnapshot.documents[0] // Obtener el primer documento que coincide con la consulta
                        val publicacionesGuardadas = documento.get("savedPublication") as ArrayList<String>// Obtener el array de publicaciones guardadas
                        if(!publication.id!!.isEmpty()){
                            publicacionesGuardadas.add(publication.id!!) // Agregar un nuevo valor al array
                        }
                        documento.reference.update("savedPublication", publicacionesGuardadas) // Actualizar el documento en Firestore con el nuevo valor
                        context.toast("Publicacion guardada")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Publication save error", exception.toString())
                }

        }
        suspend fun isSaved(publication: Publication): Boolean{
            var result = false
            val db = FirebaseFirestore.getInstance()
            val userQuery = db.collection("user").whereEqualTo("email", getCurrentUserEmail())
            userQuery.get()
                .addOnSuccessListener { querySnapshot ->
                    // Manejar los resultados de la consulta
                    if (!querySnapshot.isEmpty) {
                        val documento = querySnapshot.documents[0] // Obtener el primer documento que coincide con la consulta
                        val publicacionesGuardadas = documento.get("savedPublication") as ArrayList<String> // Obtener el array de publicaciones guardadas
                        Log.d("isSaved", publicacionesGuardadas.toString())
                        if(publicacionesGuardadas.contains(publication.id)){
                            Log.d("isSaved", "in condition")
                            Log.d("isSaved", publication.id.toString())
                            result = true
                        }
                    }
                }.await()
            Log.d("isSaved", result.toString())
            return result
        }
    }
}