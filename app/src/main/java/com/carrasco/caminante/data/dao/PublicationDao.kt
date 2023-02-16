package com.carrasco.caminante.data.dao


import com.carrasco.caminante.data.model.Publication
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class PublicationDao {
    companion object{
        fun getAll(): Flow<List<Publication>> {
            return FirebaseFirestore.getInstance()
                .collection("publication")
                .orderBy("publicationDate", Query.Direction.DESCENDING)
                .snapshots().map { snapshot ->
                    snapshot.toObjects(Publication::class.java)
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
            FirebaseFirestore.getInstance().collection("publication").add(data)
        }
    }
}