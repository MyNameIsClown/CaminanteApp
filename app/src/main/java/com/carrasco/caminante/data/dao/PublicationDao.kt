package com.carrasco.caminante.data.dao

import com.carrasco.caminante.data.model.Publication
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

abstract class PublicationDao {
    companion object{
        fun getAll(): List<Publication>{
            val publicationList: MutableList<Publication> = mutableListOf()
            FirebaseFirestore.getInstance().collection("publication")
                .get().addOnSuccessListener { documents ->
                    for(document in documents){
                        publicationList.add(document.toObject(Publication::class.java))
                    }
            }
            return publicationList
        }
    }
}