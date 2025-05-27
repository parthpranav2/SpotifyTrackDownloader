package com.example.freemusicapiintegratinspotifysearchanddownloader

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freemusicapiintegratinspotifysearchanddownloader.databinding.ActivitySearchBinding
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import kotlin.collections.getValue


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: TracksAdapter
    private val trackList = mutableListOf<ItemXXXXXXXXX>()

    private lateinit var apiKey : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        val dbRef = FirebaseDatabase.getInstance().getReference("freemusicapiintegratinspotifysearchanddownloader").child("apikey")

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                apiKey = snapshot.getValue(String::class.java) ?: ""
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@SearchActivity,
                    "Unable to fetch api key error: ${error.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTracks.layoutManager = LinearLayoutManager(this)

        adapter = TracksAdapter(trackList){ sTrackURL ->
            generateDownloadableTrack(sTrackURL)
        }

        binding.rvTracks.adapter = adapter

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.btnSearch.setOnClickListener {
            val query = binding.txtName.text.toString()
            fetchTracks(query)
        }

    }

    private fun fetchTracks(query: String) {
        Log.d("QuerySent", query)

        // Setup logging interceptor for debugging
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Setup OkHttp client with headers and logging
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("x-rapidapi-key", apiKey)
                    .addHeader("x-rapidapi-host", "spotify-downloader9.p.rapidapi.com")
                    .build()
                chain.proceed(request)
            }
            .build()

        // Setup Retrofit instance
        val apiService = Retrofit.Builder()
            .baseUrl("https://spotify-downloader9.p.rapidapi.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIInterface::class.java)

        // Make API call
        val call = apiService.getTrackByName(query)

        // Enqueue the request
        call.enqueue(object : Callback<SearchApiResponse> {
            override fun onResponse(call: Call<SearchApiResponse>, response: Response<SearchApiResponse>) {
                Log.d("ResponseSuccess", response.isSuccessful.toString())
                Log.d("ResponseCode", response.code().toString())

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("Print JSON", responseBody.toString())

                    if (responseBody?.sucess == true) {
                        Log.d("JSON Received", "success")
                    } else {
                        Log.d("JSON Received", "{${response.body()}")
                    }

                    val tracks = responseBody?.data?.tracks?.items

                    if (!tracks.isNullOrEmpty()) {
                        trackList.clear()
                        trackList.addAll(tracks)
                        adapter.notifyDataSetChanged()
                        Log.d("Search", "Updated track list with ${tracks.size} items")
                    } else {
                        responseBody?.data?.tracks?.let {
                            Log.e("Search", "Tracks object exists but items are empty")
                            Log.e("Search", "Tracks totalCount: ${it.totalCount}")
                        }
                        Toast.makeText(this@SearchActivity, "No results found: ${response.code()}", Toast.LENGTH_SHORT).show()
                        Log.e("Search", "No results found: ${response.code()}")
                    }

                } else {
                    Toast.makeText(this@SearchActivity, "API Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    Log.e("API Error", "Code: ${response.code()}, Message: ${response.message()}")

                    // Print error body if available
                    response.errorBody()?.let { errorBody ->
                        Log.e("API Error", "Error body: ${errorBody.string()}")
                    }
                }
            }

            override fun onFailure(call: Call<SearchApiResponse>, t: Throwable) {
                Toast.makeText(this@SearchActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("Search", "Failure: ${t.message}")
            }
        })
    }

    private fun generateDownloadableTrack(sTrackURL : String){  //here s stands for spotify
        // Setup logging interceptor for debugging
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Setup OkHttp client with headers and logging
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("x-rapidapi-key", apiKey)
                    .addHeader("x-rapidapi-host", "spotify-downloader9.p.rapidapi.com")
                    .build()
                chain.proceed(request)
            }
            .build()

        // Setup Retrofit instance
        val apiService = Retrofit.Builder()
            .baseUrl("https://spotify-downloader9.p.rapidapi.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIInterface::class.java)

        // Make API call
        val call = apiService.getDownloadableFileURL(sTrackURL)

        // Enqueue the request
        call.enqueue(object : Callback<DownloadApiResponse> {
            override fun onResponse(call: Call<DownloadApiResponse>, response: Response<DownloadApiResponse>) {
                if (response.isSuccessful) {
                    val downloadUrl = response.body()?.data?.downloadLink
                    val title = response.body()?.data?.title
                    val id = response.body()?.data?.id
                    val coverPageURL = response.body()?.data?.cover

                    if (!downloadUrl.isNullOrEmpty()) {
                        // 🔽 You can now use this URL to download the MP3
                        GlobalData.downloadURL=downloadUrl
                        GlobalData.trackName=title
                        GlobalData.trackID=id
                        GlobalData.coverPageURL = coverPageURL
                        Log.d("Download", "Download URL: $downloadUrl")

                        val intent = Intent(this@SearchActivity, DownloadActivity::class.java)
                        startActivity(intent)
                        // Optionally store it in a variable or start a download
                    }
                } else {
                    Log.e("Download", "API response unsuccessful")
                }
            }

            override fun onFailure(call: Call<DownloadApiResponse>, t: Throwable) {
                Log.e("Download", "API call failed", t)
            }
        })

    }


}