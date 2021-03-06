package com.aimar.infofortuna.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import com.aimar.infofortuna.*
import com.getbase.floatingactionbutton.FloatingActionButton
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

val client = OkHttpClient()
var arrayList_details:ArrayList<Partidos> = ArrayList();
var arrayList_clasif:ArrayList<Clasificacion> = ArrayList();
var arrayList_detailsMesas:ArrayList<Mesa> = ArrayList()
var arrayList_detailsCoches:ArrayList<Coche> = ArrayList()
var arrayList_sortedCars:ArrayList<Coche> = ArrayList()
lateinit var listView_details: ListView
lateinit var txt_title: TextView
lateinit var btnEditar : FloatingActionButton

class ClasificacionFragment : Fragment() {

    companion object {
        private var categoria: String=""

        fun newInstance(seleccion:String): Fragment {
            categoria=seleccion
            return ClasificacionFragment()
        }
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
            openSpreadSheet(getResources().getString(R.string.idHojaClasificacion))
        }
        txt_title.text="Clasificación "+ categoria
        runClasificacion(getResources().getString(R.string.json_clasificacion), categoria)
    }
    fun runClasificacion(url: String,categ: String) {

        val request = Request.Builder()
                .url(url)
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {

                var str_response = response.body()!!.string()

                //creating json object
                //val json_contact:JSONObject = JSONObject(str_response)
                //creating json array
                var jsonarray_info = JSONArray(str_response)
                var size:Int = jsonarray_info.length()
                arrayList_clasif= ArrayList()

                for (i in 0.. size-1) {
                    var json_objectdetail:JSONObject=jsonarray_info.getJSONObject(i)
                    var model = Clasificacion()
                    model.category=json_objectdetail.getString("categoria")
                    if(model.category.equals(categ,true)) {
                        model.pos=""+(i+1)
                        model.equipo=json_objectdetail.getString("equipo")
                        model.puntos=json_objectdetail.getString("puntos")
                        model.destacado=json_objectdetail.getString("destacado")
                        arrayList_clasif.add(model)
                    }
                }

                activity!!.runOnUiThread {
                    //stuff that updates ui
                    //tvSelected.text = "Clasificación $categ"
                    val obj_adapter : AdapterClasificacion
                    println(arrayList_clasif.size)
                    obj_adapter = AdapterClasificacion(activity!!.applicationContext,arrayList_clasif)
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