package com.carrasco.caminante.data.model

data class User(
    val email : String? ="",
    val savedPublication: MutableList<Publication>? = mutableListOf()
){
    override fun toString(): String {
        return "User(email=$email, savedPublication=$savedPublication)"
    }
    public fun addPublication(publication: Publication){
        savedPublication?.add(publication)
    }
}
