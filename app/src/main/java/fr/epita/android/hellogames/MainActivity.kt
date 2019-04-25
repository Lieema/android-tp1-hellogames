package fr.epita.android.hellogames

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.math.log


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val baseUrl = "https://androidlessonsapi.herokuapp.com/"
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(jsonConverter)
            .build()
        val service : WebServiceInterface = retrofit.create(WebServiceInterface::class.java)

        val wsCallback : Callback<List<gameEl>> = object : Callback<List<gameEl>> {
            override fun onFailure(call: Call<List<gameEl>>, t: Throwable) {
                Log.w("game/list", "WSFailed")
            }

            override fun onResponse(call: Call<List<gameEl>>, response: Response<List<gameEl>>) {
                if (response.code() == 200){
                    val responsedata = response.body()
                    if (responsedata != null){
                        val els = responsedata.shuffled()
                        Glide.with(applicationContext)
                            .load(els[0].picture)
                            .into(imageView)
                        Glide.with(applicationContext)
                            .load(els[1].picture)
                            .into(imageView2)
                        Glide.with(applicationContext)
                            .load(els[2].picture)
                            .into(imageView3)
                        Glide.with(applicationContext)
                            .load(els[3].picture)
                            .into(imageView4)

                        imageView.setOnClickListener {
                            val explicitIntent = Intent(applicationContext, SecondActivity::class.java)
                            explicitIntent.putExtra("intId", els[0].id)
                            startActivity(explicitIntent)
                        }

                        imageView2.setOnClickListener {
                            val explicitIntent = Intent(applicationContext, SecondActivity::class.java)
                            explicitIntent.putExtra("intId", els[1].id)
                            startActivity(explicitIntent)
                        }

                        imageView3.setOnClickListener {
                            val explicitIntent = Intent(applicationContext, SecondActivity::class.java)
                            explicitIntent.putExtra("intId", els[2].id)
                            startActivity(explicitIntent)
                        }

                        imageView4.setOnClickListener {
                            val explicitIntent = Intent(applicationContext, SecondActivity::class.java)
                            explicitIntent.putExtra("intId", els[3].id)
                            startActivity(explicitIntent)
                        }
                    }

                }
            }
        }
        service.gameList().enqueue(wsCallback)
    }
    
    interface WebServiceInterface {
        @GET("/api/game/list")
        fun gameList() : Call<List<gameEl>>

        @GET("/api/game/details")
        fun gameDetails(@Query("game_id") game_id : Int): Call<gameDetails>
    }
}
