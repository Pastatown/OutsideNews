package com.example.outsidenews

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity(), OnArticleClick {

    private lateinit var mAdaptor : ArticleAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Définit notre recyclerView en LinearLayout
        val recyclerView : RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchNews("https://newsapi.org/v2/top-headlines?country=fr&apiKey=1e77a16c1641498c885ab805ae42c370")
        mAdaptor = ArticleAdaptor(this)
        recyclerView.adapter = mAdaptor

        val sport = findViewById<View>(R.id.buttonSport)
        sport.setOnClickListener(listener)
        val business = findViewById<View>(R.id.buttonBusiness)
        business.setOnClickListener(listener)
        val technology = findViewById<View>(R.id.buttonTech)
        technology.setOnClickListener(listener)
        val divertissement = findViewById<View>(R.id.buttonDivert)
        divertissement.setOnClickListener(listener)
        val generalite = findViewById<View>(R.id.buttonGeneral)
        generalite.setOnClickListener(listener)
        val sante = findViewById<View>(R.id.buttonSante)
        sante.setOnClickListener(listener)
        val science = findViewById<View>(R.id.buttonScience)
        science.setOnClickListener(listener)

        rechercher()
    }

    fun fetchNews(url: String) {
        val queue = Volley.newRequestQueue(this)
        /*val url = "https://newsapi.org/v2/top-headlines?country=fr&apiKey=1e77a16c1641498c885ab805ae42c370"*/
        val getRequest: JsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener {

                val newsJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for(i in 0 until  newsJsonArray.length()){
                    val newsJsonObject = newsJsonArray.getJSONObject(i)

                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("description"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage"),
                        newsJsonObject.getString("publishedAt"),
                        newsJsonObject.getString("content")

                    )

                    newsArray.add(news)
                }
                mAdaptor.updateNews(newsArray)
            },
            Response.ErrorListener {

            }
        )
        {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["User-Agent"] = "Mozilla/5.0"
                return params
            }
        }
        queue.add(getRequest)
    }

    override fun onClicked(news: News) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(news.url))
    }
    val listener= View.OnClickListener { recyclerView ->
        var url = "https://newsapi.org/v2/top-headlines?apiKey=1e77a16c1641498c885ab805ae42c370&category="
        val query : String
        when (recyclerView.id) {
            R.id.buttonSport -> {
                query = "sport"
                url += query
                fetchNews(url)
            }
            R.id.buttonBusiness -> {
                query = "business"
                url += query
                fetchNews(url)
            }
            R.id.buttonTech -> {
                query = "technology"
                url += query
                fetchNews(url)
            }
            R.id.buttonDivert -> {
                query = "entertainment"
                url += query
                fetchNews(url)
            }
            R.id.buttonGeneral -> {
                query = "general"
                url += query
                fetchNews(url)
            }
            R.id.buttonSante -> {
                query = "health"
                url += query
                fetchNews(url)
            }
            R.id.buttonScience -> {
                query = "science"
                url += query
                fetchNews(url)
            }
        }
        url = "https://newsapi.org/v2/top-headlines?apiKey=1e77a16c1641498c885ab805ae42c370&category="
    }


    fun rechercher() { //Fonction de recherche par mot clés
        val search = findViewById<SearchView>(R.id.search)
    //    val listView = findViewById<ListView>(R.id.listVuew)
    //    listView.bringToFront()
    //    val categories = arrayOf("business","entertainment","general","health","science","sports","technology")
    //    val adapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, categories)
        var url = "https://newsapi.org/v2/everything?apiKey=1e77a16c1641498c885ab805ae42c370&q="
     //   listView.adapter = adapter
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
            override fun onQueryTextSubmit(query: String?): Boolean {
                search.clearFocus()
                if (query != null) {
                    url += query // Concaténation de l'url et du mot clé entré dans la searchBar
                    fetchNews(url)
                    url = "https://newsapi.org/v2/everything?apiKey=1e77a16c1641498c885ab805ae42c370&q="//Réinitialisation de l'url
                    return false
                }
                return true
            }
        })
    }



}