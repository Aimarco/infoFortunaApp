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


class PartidosFragment : Fragment() {

    companion object {
        private var categoria: String=""

        fun newInstance(seleccion:String): Fragment {
            categoria=seleccion
            return PartidosFragment()
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
            openSpreadSheet(getResources().getString(R.string.idHojaPartidos))
        }
        txt_title.text="Partidos Equipo "+ categoria
        run(getResources().getString(R.string.json_partidos), categoria)
    }
    fun run(url: String,catg: String) {
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
                var i:Int = 0
                var size:Int = jsonarray_info.length()
                arrayList_details= ArrayList();
                for (i in 0.. size-1) {
                    var json_objectdetail:JSONObject=jsonarray_info.getJSONObject(i)
                    var model: Partidos = Partidos()
                    model.equipo1=json_objectdetail.getString("equipo1")
                    model.equipo2=json_objectdetail.getString("equipo2")
                    model.fechaPartido=json_objectdetail.getString("fechaPartido")
                    model.resultado=json_objectdetail.getString("resultado")
                    model.category=json_objectdetail.getString("categoria")
                    model.resultado2=json_objectdetail.getString("resultado2")
                    model.hora=json_objectdetail.getString("hora")
                    if(model.resultado.equals("null")){
                        model.resultado = "0-0"
                    }
                    if(model.hora.equals("null",true)){
                        model.hora = "??:??"
                    }
                    if(model.category.equals(catg,true))
                        arrayList_details.add(model)

                }

                activity!!.runOnUiThread {

                    //stuff that updates ui
                    val obj_adapter : CustomAdapter
                    obj_adapter = CustomAdapter(activity!!.applicationContext,arrayList_details)
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