package fr.epita.android.hellogames

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MainFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MainFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val baseUrl = "https://androidlessonsapi.herokuapp.com/"
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(jsonConverter)
            .build()
        val service : MainActivity.WebServiceInterface = retrofit.create(MainActivity.WebServiceInterface::class.java)

        val wsCallback : Callback<List<gameEl>> = object : Callback<List<gameEl>> {
            override fun onFailure(call: Call<List<gameEl>>, t: Throwable) {
                Log.w("game/list", "WSFailed")
            }

            override fun onResponse(call: Call<List<gameEl>>, response: Response<List<gameEl>>) {
                if (response.code() == 200){
                    val responsedata = response.body()
                    if (responsedata != null && activity != null){
                        val els = responsedata.shuffled()
                        activity?.let {
                            Glide.with(it)
                                .load(els[0].picture)
                                .into(imageView)
                        }
                        activity?.let {
                            Glide.with(it)
                                .load(els[1].picture)
                                .into(imageView2)
                        }
                        activity?.let {
                            Glide.with(it)
                                .load(els[2].picture)
                                .into(imageView3)
                        }
                        activity?.let {
                            Glide.with(it)
                                .load(els[3].picture)
                                .into(imageView4)
                        }

                        imageView.setOnClickListener {
                            val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                            val secFrag = SecondaryFragment(els[0].id)
                            fragmentTransaction.replace(R.id.main_container, secFrag)
                            fragmentTransaction.addToBackStack(null)
                            fragmentTransaction.commit()
                        }

                        imageView2.setOnClickListener {
                            val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                            val secFrag = SecondaryFragment(els[1].id)
                            fragmentTransaction.replace(R.id.main_container, secFrag)
                            fragmentTransaction.addToBackStack(null)
                            fragmentTransaction.commit()
                        }

                        imageView3.setOnClickListener {
                            val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                            val secFrag = SecondaryFragment(els[2].id)
                            fragmentTransaction.replace(R.id.main_container, secFrag)
                            fragmentTransaction.addToBackStack(null)
                            fragmentTransaction.commit()
                        }

                        imageView4.setOnClickListener {
                            val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                            val secFrag = SecondaryFragment(els[3].id)
                            fragmentTransaction.replace(R.id.main_container, secFrag)
                            fragmentTransaction.addToBackStack(null)
                            fragmentTransaction.commit()
                        }
                    }

                }
            }
        }
        service.gameList().enqueue(wsCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
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
}
