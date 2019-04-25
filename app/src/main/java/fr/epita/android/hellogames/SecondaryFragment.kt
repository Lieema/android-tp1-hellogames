package fr.epita.android.hellogames

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_secondary.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SecondaryFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SecondaryFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SecondaryFragment(val imgId : Int) : Fragment() {
    private var param1: Int = 0
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
        }
        val imgId = imgId

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
                        context?.let {
                            Glide.with(it)
                                .load(responsedata.picture)
                                .into(imageView5)
                        }
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_secondary, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
        val mainFrag = MainFragment()
        fragmentTransaction.replace(R.id.main_container, mainFrag)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment SecondaryFragment.
         */
        @JvmStatic
        fun newInstance(param1: Int) =
            SecondaryFragment(param1).apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }
}
