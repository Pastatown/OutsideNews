package com.example.outsidenews

//classe pour récupérer les données de l'api
data class News(

    val title : String,
    val description : String,
    val author : String,
    val url : String,
    val urlToImage : String,
    val publishedAt : String,
    val content : String
)

{

}