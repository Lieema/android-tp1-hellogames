package fr.epita.android.hellogames

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        var myIntent = intent
        val imgId = myIntent.getIntExtra("intId", 0)

        Log.w("id", imgId.toString())
    }
}
