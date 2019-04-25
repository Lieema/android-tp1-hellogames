package fr.epita.android.hellogames

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query



class MainActivity : AppCompatActivity(), MainFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        val mainFrag = MainFragment()
        fragmentTransaction.add(R.id.main_container, mainFrag)
        fragmentTransaction.commit()
    }
    
    interface WebServiceInterface {
        @GET("/api/game/list")
        fun gameList() : Call<List<gameEl>>

        @GET("/api/game/details")
        fun gameDetails(@Query("game_id") game_id : Int): Call<gameDetails>
    }
}
