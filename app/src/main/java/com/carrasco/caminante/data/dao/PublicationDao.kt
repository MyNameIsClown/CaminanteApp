package com.carrasco.caminante.data.dao


import com.carrasco.caminante.data.model.Publication
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class PublicationDao {
    companion object{
        fun getAll(): Flow<List<Publication>> {
            return FirebaseFirestore.getInstance()
                .collection("publication")
                .snapshots().map { snapshot ->
                    snapshot.toObjects(Publication::class.java)
                }
        }
    }
}