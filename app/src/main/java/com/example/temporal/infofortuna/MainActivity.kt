package com.example.temporal.infofortuna

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.ProxyFileDescriptorCallback
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.ProgressBar
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {
    lateinit var progress:ProgressBar
    lateinit var listView_details: ListView
    lateinit var btnPartidos: Button
    lateinit var btnMesas: Button

    var arrayList_details:ArrayList<Partidos> = ArrayList();
    var arrayList_detailsMesas:ArrayList<Mesa> = ArrayList();
    //OkHttpClient creates connection pool between client and server
    val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnPartidos = findViewById(R.id.btnPartidos)
        btnMesas = findViewById(R.id.btnMesas)
        listView_details = findViewById<ListView>(R.id.listView) as ListView
        listView_details.visibility = View.GONE





        btnPartidos.setOnClickListener{
            run(getResources().getString(R.string.json_partidos))
            listView_details.visibility = View.VISIBLE
        }
        btnMesas.setOnClickListener{
            runMesas(resources.getString(R.string.json_mesas))
            listView_details.visibility = View.VISIBLE
        }
        //run(getResources().getString(R.string.json_partidos))
    }

    fun run(url: String) {
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
                    var model:Partidos= Partidos()
                    model.equipo1=json_objectdetail.getString("equipo1")
                    model.equipo2=json_objectdetail.getString("equipo2")
                    model.fechaPartido=json_objectdetail.getString("fechaPartido")
                    model.resultado=json_objectdetail.getString("resultado")
                    if(model.resultado.equals("null")){
                        model.resultado = "0-0"
                    }
                    arrayList_details.add(model)
                }

                runOnUiThread {
                    //stuff that updates ui
                    val obj_adapter : CustomAdapter
                    obj_adapter = CustomAdapter(applicationContext,arrayList_details)
                    listView_details.adapter=obj_adapter
                }
                //progress.visibility = View.GONE
            }
        })
    }

    fun runMesas(url: String) {
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
                arrayList_detailsMesas= ArrayList();
                for (i in 0.. size-1) {
                    var json_objectdetail:JSONObject=jsonarray_info.getJSONObject(i)
                    var model:Mesa= Mesa()
                    model.fecha=json_objectdetail.getString("fecha")
                    model.fecha+="("+json_objectdetail.getString("hora")+")"
                    model.catg=json_objectdetail.getString("categoria")
                    model.mesa1=json_objectdetail.getString("mesa1")
                    model.mesa2=json_objectdetail.getString("mesa2")
                    model.mesa3=json_objectdetail.getString("mesa3")
                    model.hora=json_objectdetail.getString("hora")

                    arrayList_detailsMesas.add(model)
                }

                runOnUiThread {
                    //stuff that updates ui
                    val obj_adapter : CustomAdapterMesas
                    obj_adapter = CustomAdapterMesas(applicationContext,arrayList_detailsMesas)
                    listView_details.adapter=obj_adapter
                }
                //progress.visibility = View.GONE
            }
        })
    }
}