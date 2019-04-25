package fr.epita.android.hellogames

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_second.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        var myIntent = intent
        val imgId = myIntent.getIntExtra("intId", 0)

        Log.w("id", imgId.toString())

        val baseUrl = "https://androidlessonsapi.herokuapp.com/"
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(jsonConverter)
            .build()

        val service : MainActivity.WebServiceInterface = retrofit.create(MainActivity.WebServiceInterface::class.java)

        val wsCallbacker: Callback<gameDetails> = object : Callback<gameDetails> {
            override fun onFailure(call: Call<gameDetails>, t: Throwable) {
                Log.w("game/details", "WSFailed")
            }

            override fun onResponse(call: Call<gameDetails>, response: Response<gameDetails>) {
                if (response.code() == 200){
                    val responsedata = response.body()
                    if (responsedata != null){
                        nameText.text = responsedata.name
                        typeText.text = responsedata.type
                        nbplayersText.text = responsedata.players.toString()
                        yearText.text = responsedata.year.toString()
                        Glide.with(applicationContext)
                            .load(responsedata.picture)
                            .into(imageView5)
                        textView10.text = responsedata.description_en
                        button.setOnClickListener {
                            val i = Intent(Intent.ACTION_VIEW)
                            i.data = Uri.parse(responsedata.url)
                            startActivity(i)
                        }
                    }
                }
            }
        }
        service.gameDetails(imgId).enqueue(wsCallbacker)
    }
}
