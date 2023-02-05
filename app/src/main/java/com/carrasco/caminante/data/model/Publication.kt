package com.carrasco.caminante.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class Publication(
    val title: String?= "",
    val description: String?= "",
    val category: String?= "",
    val route: String?= "",
    val imageURL: String?= "",
    val publicationDate: Timestamp?= Timestamp(0,0),
    val locationCoordinates: GeoPoint?= GeoPoint(0.0, 0.0)
){
    override fun toString(): String {
        return "Publication(title=$title, description=$description, category=$category, route=$route, imageURL=$imageURL, publicationDate=$publicationDate, locationCoordinates=$locationCoordinates)"
    }
}