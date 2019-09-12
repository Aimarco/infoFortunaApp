package com.aimar.infofortuna.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aimar.infofortuna.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class CochesFragment : Fragment() {


    companion object {
        fun newInstance(): Fragment {
            return CochesFragment()
        }
    }

    fun newInstance(): Fragment {

        return CochesFragment()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView_details = view.findViewById(R.id.listView)
        txt_title = view.findViewById(R.id.tvTitle)
        btnEditar= view.findViewById(R.id.btnEditar)
        btnEditar.setOnClickListener{
            openSpreadSheet(getResources().getString(R.string.idHojaCoches))
        }
        txt_title.text="Listado coches Senior"
        runCoches(resources.getString(R.string.json_coches))
    }
    fun runCoches(url: String) {
        val request = Request.Builder()
                .url(url)
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {

                var str_response = response.body()!!.string()
                var jsonarray_info = JSONArray(str_response)
                var i:Int = 0
                var size:Int = jsonarray_info.length()
                arrayList_detailsCoches= ArrayList();
                for (i in 0.. size-1) {
                    var json_objectdetail:JSONObject=jsonarray_info.getJSONObject(i)
                    var model: Coche = Coche()
                    model.nombre=json_objectdetail.getString("nombre")
                    model.numViajes=json_objectdetail.getInt("contador")
                    arrayList_detailsCoches.add(model)
                }

                arrayList_sortedCars= ArrayList(arrayList_detailsCoches.sortedWith(compareBy({ it.numViajes })))

                activity!!.runOnUiThread {
                    //stuff that updates ui
                    val obj_adapter : AdapterCoche
                    obj_adapter = AdapterCoche(activity!!.applicationContext,arrayList_sortedCars)
                    listView_details.adapter=obj_adapter
                }
                //progress.visibility = View.GONE
            }
        })
    }
    fun openSpreadSheet(id:String){
        val url = "https://docs.google.com/spreadsheets/d/$id"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

}