package com.example.freemusicapiintegratinspotifysearchanddownloader

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freemusicapiintegratinspotifysearchanddownloader.databinding.ActivitySearchBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: TracksAdapter
    private val trackList = mutableListOf<ItemXXXXXXXXX>()

    val apiKey = "fefbbccd25mshc792dfdfb4af473p107bb4jsn6df436454e46"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTracks.layoutManager = LinearLayoutManager(this)
        adapter = TracksAdapter(trackList)
        binding.rvTracks.adapter = adapter

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.btnSearch.setOnClickListener {
            val query = binding.txtName.text.toString().replace(" ", "+")
            fetchTracks(query)
        }

    }

    private fun fetchTracks(query: String) {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("x-rapidapi-key", apiKey)
                    .addHeader("x-rapidapi-host", "spotify-downloader9.p.rapidapi.com")
                    .build()
                chain.proceed(request)
            }
            .build()

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://spotify-downloader9.p.rapidapi.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIInterface::class.java) // <-- This must match the interface

        val retrofitData = retrofitBuilder.getTrackByName(query)
        Log.d("QuerySent", query)


        retrofitData.enqueue(object : Callback<Data> {
            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                if (response.isSuccessful) {
                    val tracks = response.body()?.tracks?.items

                    Log.d("Search", "Tracks found: $tracks")
                    Log.d("Search", "Tracks size: ${tracks?.size}")

                    if (!tracks.isNullOrEmpty()) {
                        trackList.clear()
                        trackList.addAll(tracks)
                        adapter.notifyDataSetChanged()
                        Log.d("Search", "Updated track list with ${tracks.size} items")
                    } else {
                        if (response.body()?.tracks != null) {
                            Log.d("Search", "Tracks object exists but items is empty or null")
                            Log.d("Search", "Tracks totalCount: ${response.body()?.tracks?.totalCount}")
                        }
                        Toast.makeText(this@SearchActivity, "No results found", Toast.LENGTH_SHORT).show()
                        Log.d("No results found","${response.code()}")
                    }
                } else {
                    Toast.makeText(this@SearchActivity, "API Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    Log.e("API Error", "Code: ${response.code()}, Message: ${response.message()}")

                    // Log the error body
                    response.errorBody()?.let { errorBody ->
                        Log.e("API Error", "Error body: ${errorBody.string()}")
                    }
                }

                Log.d("RawResponse", response.body().toString())
                Log.d("ResponseSuccess", response.isSuccessful.toString())
                Log.d("ResponseCode", response.code().toString())

            }

            override fun onFailure(call: Call<Data>, t: Throwable) {
                Toast.makeText(this@SearchActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("Search", "Failure: ${t.message}")
            }
        })

    }

}