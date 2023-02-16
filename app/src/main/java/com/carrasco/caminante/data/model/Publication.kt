package com.carrasco.caminante.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import kotlinx.parcelize.Parcelize

@Parcelize
data class Publication(
    val title: String?= "",
    val description: String?= "",
    val category: String?= "",
    val route: String?= "",
    val imageURL: String?= "https://vivecamino.com/img/noti/av/simbolos-camino-santiago_595.jpg",
    val publicationDate: Timestamp?= Timestamp(0,0),
    val latitude: Double?=0.0,
    val longitude: Double?=0.0
):Parcelable{
    override fun toString(): String {
        return "Publication(title=$title, description=$description, category=$category, route=$route, \n" +
        "imageURL=$imageURL, publicationDate=$publicationDate, locationCoordinates=$latitude, $longitude)"
    }
}