package fr.epita.android.hellogames

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
import kotlin.math.log


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val baseUrl = "https://androidlessonsapi.herokuapp.com/api/"
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
                        Log.d("test", els[0].picture + els[1].picture + els[2].picture)
                    }

                }
            }
        }
        service.gameList().enqueue(wsCallback)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    
    interface WebServiceInterface {
        @GET("game/list")
        fun gameList() : Call<List<gameEl>>
    }
}
