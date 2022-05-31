package com.example.outsidenews

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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

        val mSwipeRefreshLayout = findViewById<View>(R.id.swipeonrefresh) as SwipeRefreshLayout
        mSwipeRefreshLayout.setOnRefreshListener {
            // Execute code when refresh layout swiped
            finish();
            startActivity(getIntent());
            mSwipeRefreshLayout.isRefreshing = false
        }

        fetchNews("https://newsapi.org/v2/top-headlines?country=fr&apiKey=284cb51b9e774eaa9412b9906d9b2e0c")
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

        val pertinant = findViewById<View>(R.id.radioPertinant)
        pertinant.setOnClickListener(listener)
        val populaire = findViewById<View>(R.id.radioPopulaire)
        populaire.setOnClickListener(listener)
        val recent = findViewById<View>(R.id.radioRecent)
        recent.setOnClickListener(listener)
        rechercher()
    }

    fun fetchNews(url: String) {
        val queue = Volley.newRequestQueue(this)
        /*val url = "https://newsapi.org/v2/top-headlines?country=fr&apiKey=284cb51b9e774eaa9412b9906d9b2e0c"*/
        val getRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.GET,
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
        var urlCategory = "https://newsapi.org/v2/top-headlines?apiKey=284cb51b9e774eaa9412b9906d9b2e0c&category="
        val query : String
        when (recyclerView.id) {
            R.id.buttonSport -> {
                query = "sport"
                urlCategory += query
                fetchNews(urlCategory)
            } R.id.buttonBusiness -> {
                query = "business"
                urlCategory += query
                fetchNews(urlCategory)
            } R.id.buttonTech -> {
                query = "technology"
                urlCategory += query
                fetchNews(urlCategory)
            } R.id.buttonDivert -> {
                query = "entertainment"
                urlCategory += query
                fetchNews(urlCategory)
            } R.id.buttonGeneral -> {
                query = "general"
                urlCategory += query
                fetchNews(urlCategory)
            } R.id.buttonSante -> {
                query = "health"
                urlCategory += query
                fetchNews(urlCategory)
            } R.id.buttonScience -> {
                query = "science"
                urlCategory += query
                fetchNews(urlCategory)
            }
        }
        urlCategory = "https://newsapi.org/v2/top-headlines?apiKey=284cb51b9e774eaa9412b9906d9b2e0c&category="
    }


    fun rechercher() { //Fonction de recherche par mot clés
        val search = findViewById<SearchView>(R.id.search)
        val radio1 = findViewById<View>(R.id.radioPertinant)
        val radio2 = findViewById<View>(R.id.radioPopulaire)
        val radio3 = findViewById<View>(R.id.radioRecent)
    //    val listView = findViewById<ListView>(R.id.listVuew)
    //    listView.bringToFront()
    //    val categories = arrayOf("business","entertainment","general","health","science","sports","technology")
    //    val adapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, categories)
        var url = "https://newsapi.org/v2/everything?apiKey=284cb51b9e774eaa9412b9906d9b2e0c&q="
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
                    url = "https://newsapi.org/v2/everything?apiKey=284cb51b9e774eaa9412b9906d9b2e0c&q="//Réinitialisation de l'url
                    radio1.isVisible = true
                    radio2.isVisible = true
                    radio3.isVisible = true
                    return false
                }
                return true
            }
        })
    }



}