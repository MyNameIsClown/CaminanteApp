package com.carrasco.caminante.data.dao


import com.carrasco.caminante.data.model.Publication
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import com.carrasco.caminante.data.model.User
import com.carrasco.caminante.toast
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.tasks.await
import java.net.URL
import java.util.*

abstract class PublicationDao {
    companion object{
        suspend fun get(id: String): Publication? {
            val db = FirebaseFirestore.getInstance()
            val usersRef = db.collection("publication")
            val query = usersRef.whereEqualTo("id", id).limit(1)
            val result = query.get().await()
            return if (!result.isEmpty) {
                val document = result.documents[0]
                document.toObject(Publication::class.java)
            } else {
                null
            }
        }
        fun getAll(): Flow<List<Publication>> {
            return FirebaseFirestore.getInstance()
                .collection("publication")
                .orderBy("publicationDate", Query.Direction.DESCENDING)
                .snapshots().map { snapshot ->
                    snapshot.documents.map {
                        val publication = it.toObject(Publication::class.java)
                        publication?.id = it.id
                        publication
                    }.filterNotNull()
                }
        }
        fun save(publication: Publication){
            val data = hashMapOf(
                "title" to publication.title,
                "description" to publication.description,
                "category" to publication.category,
                "route" to publication.route,
                "publicationDate" to publication.publicationDate,
                "latitude" to publication.latitude,
                "longitude" to publication.longitude,
                "imageURL" to publication.imageURL
            )
            Log.d("Publication Data", data.toString())
            FirebaseFirestore.getInstance().collection("publication").add(data)
        }
        suspend fun uploadImage(imageUri: Uri): Uri{
            Log.d("PRUEBA", imageUri.toString())
            var downloadUrl: Uri? = Uri.EMPTY
            val storage = Firebase.storage("gs://caminante-final-project.appspot.com")
            val storageRef = storage.reference
            val filename = imageUri.lastPathSegment
            val storageFileRef = storageRef.child("images/$filename")
            // Sube el archivo a Firebase Storage
            val uploadTask = storageFileRef.putFile(imageUri)

            // Agrega un listener para saber cuándo se complete la carga
            downloadUrl = uploadTask.continueWithTask{
                if(!it.isSuccessful){
                    it.exception?.let { throw it }
                }
                storageFileRef.downloadUrl
            }.await()

            Log.d("SALIDA", downloadUrl.toString())

            return downloadUrl
        }
    }
}