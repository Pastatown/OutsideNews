package com.example.outsidenews

//classe pour récupérer les données de l'api
data class News(
    val name : String,
    val title : String,
    val description : String,
    val author : String,
    val url : String,
    val imageUrl : String,
    val publishedAt : String,
    val content : String
)

{

}