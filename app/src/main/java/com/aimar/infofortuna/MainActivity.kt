package com.aimar.infofortuna

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.ProxyFileDescriptorCallback
import android.view.View
import android.widget.*
import com.getbase.floatingactionbutton.FloatingActionButton
import com.getbase.floatingactionbutton.FloatingActionsMenu
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main2.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.Toolbar


class MainActivity : AppCompatActivity() {
    lateinit var progress:ProgressBar
    lateinit var listView_details: ListView
    lateinit var btnPartidos: FloatingActionButton
    lateinit var btnMesas: FloatingActionButton
    lateinit var btnCoches: FloatingActionButton
    lateinit var btnClasi: FloatingActionButton
    lateinit var btnEscolar: Button
    lateinit var btnSenior: Button
    lateinit var btnCadetes: Button
    lateinit var btnJuveniles: Button
    lateinit var btnEditar: FloatingActionButton
    lateinit var toolbar: Toolbar


    lateinit var btnMenu: FloatingActionsMenu
    lateinit var tvSelected: TextView
    lateinit var catgButtons : LinearLayout

    var arrayList_details:ArrayList<Partidos> = ArrayList();
    var arrayList_clasif:ArrayList<Clasificacion> = ArrayList();
    var arrayList_detailsMesas:ArrayList<Mesa> = ArrayList()
    var arrayList_detailsCoches:ArrayList<Coche> = ArrayList()
    var arrayList_sortedCars:ArrayList<Coche> = ArrayList()
    //OkHttpClient creates connection pool between client and server
    val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        btnPartidos = findViewById(R.id.showPartidos)
        btnMenu = findViewById(R.id.btnActions)
        btnMesas = findViewById(R.id.showMesas)
        btnCoches = findViewById(R.id.showCoches)
        btnCadetes = findViewById(R.id.btnCadetes)
        btnJuveniles = findViewById(R.id.btnJuveniles)
        btnSenior = findViewById(R.id.btnSenior)
        btnEscolar = findViewById(R.id.btnEscolar)
        catgButtons = findViewById(R.id.catgButtons)
        btnEditar = findViewById(R.id.btnEditar)

        btnClasi = findViewById(R.id.showClasi)
        catgButtons.visibility = View.GONE

        tvSelected = findViewById(R.id.tvSelected)
        tvSelected.visibility = View.GONE
        listView_details = findViewById<ListView>(R.id.listView) as ListView
        listView_details.visibility = View.GONE

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)





        if(btnMenu.isExpanded){
            listView_details.visibility = View.VISIBLE
        }

        btnPartidos.setOnClickListener{
            btnMenu.collapse()
            tvSelected.text = "Partidos"
            tvSelected.visibility = View.VISIBLE
            catgButtons.visibility = View.VISIBLE
            listView_details.visibility = View.GONE
        }
        btnMesas.setOnClickListener{
            btnMenu.collapse()
            tvSelected.text = "Mesas"
            catgButtons.visibility = View.INVISIBLE
            tvSelected.visibility = View.VISIBLE
            runMesas(resources.getString(R.string.json_mesas))
            listView_details.visibility = View.VISIBLE
        }
        btnCoches.setOnClickListener {
            btnMenu.collapse()
            tvSelected.text = "Seguimiento Coches"
            catgButtons.visibility = View.INVISIBLE
            tvSelected.visibility = View.VISIBLE
            listView_details.visibility = View.VISIBLE
            //Toast.makeText(this,"Ouch! Esta opción aun no esta lista jeje",Toast.LENGTH_SHORT).show()
            runCoches(resources.getString(R.string.json_coches))
        }
        btnClasi.setOnClickListener {
            btnMenu.collapse()
            tvSelected.text = "Clasificación"
            tvSelected.visibility = View.VISIBLE
            catgButtons.visibility = View.VISIBLE
            listView_details.visibility = View.GONE
            //Toast.makeText(this,"Ouch! Esta opción aun no esta lista jeje",Toast.LENGTH_SHORT).show()
        }

        btnCadetes.setOnClickListener{
            tvSelected.visibility = View.VISIBLE
            if(tvSelected.text.contains("parti",true)) {
                run(getResources().getString(R.string.json_partidos), "Cadete")
            }else if(tvSelected.text.contains("clasif",true)){
                println("Clasificacion cadetes")
                runClasificacion(getResources().getString(R.string.json_clasificacion), "Cadete")
            }
            listView_details.visibility = View.VISIBLE
        }
        btnJuveniles.setOnClickListener{
            tvSelected.visibility = View.VISIBLE
            if(tvSelected.text.contains("parti",true)) {
                run(getResources().getString(R.string.json_partidos), "Juvenil")
            }else if(tvSelected.text.contains("clasif",true)) {
                println("Clasificacion Juvenil")
                runClasificacion(getResources().getString(R.string.json_clasificacion), "Juvenil")

            }
            listView_details.visibility = View.VISIBLE
        }
        btnSenior.setOnClickListener{
            tvSelected.visibility = View.VISIBLE
            if(tvSelected.text.contains("parti",true)) {
                run(getResources().getString(R.string.json_partidos), "Senior")
            }else if(tvSelected.text.contains("clasif",true)) {
                println("Clasificacion Senior")
                runClasificacion(getResources().getString(R.string.json_clasificacion), "Senior")
            }
            listView_details.visibility = View.VISIBLE
        }
        btnEscolar.setOnClickListener{
            tvSelected.visibility = View.VISIBLE
            if(tvSelected.text.contains("parti",true)) {
                run(getResources().getString(R.string.json_partidos), "Escolar")
            }else if(tvSelected.text.contains("clasif",true)) {
                println("Clasificacion Escolar")
                runClasificacion(getResources().getString(R.string.json_clasificacion), "Escolar")
            }
            listView_details.visibility = View.VISIBLE
        }
        btnEditar.setOnClickListener {
            if(tvSelected.text.contains("parti",true)){
                val id: String = getResources().getString(R.string.idHojaPartidos)
                openSpreadSheet(id)
            }else if(tvSelected.text.contains("mesa",true)){
                val id: String = getResources().getString(R.string.idHojaMesas)
                openSpreadSheet(id)
            }else if(tvSelected.text.contains("coche",true)){
                val id: String = getResources().getString(R.string.idHojaCoches)
                openSpreadSheet(id)
            }else if(tvSelected.text.contains("clasi",true)){
                val id: String = getResources().getString(R.string.idHojaClasificacion)
                openSpreadSheet(id)
            }else{
                Toast.makeText(this,getResources().getString(R.string.noCatgSelected),Toast.LENGTH_SHORT).show()
            }

        }


        //run(getResources().getString(R.string.json_partidos))
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
                    var model:Partidos= Partidos()
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

                runOnUiThread {
                    tvSelected.text = "Partidos $catg"
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
                    var model:Coche= Coche()
                    model.nombre=json_objectdetail.getString("nombre")
                    model.numViajes=json_objectdetail.getInt("contador")
                    arrayList_detailsCoches.add(model)
                }

                arrayList_sortedCars= ArrayList(arrayList_detailsCoches.sortedWith(compareBy({ it.numViajes })))

                runOnUiThread {
                    //stuff that updates ui
                    val obj_adapter : AdapterCoche
                    obj_adapter = AdapterCoche(applicationContext,arrayList_sortedCars)
                    listView_details.adapter=obj_adapter
                }
                //progress.visibility = View.GONE
            }
        })
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
                        model.pos=json_objectdetail.getString("pos")
                        model.equipo=json_objectdetail.getString("equipo")
                        model.puntos=json_objectdetail.getString("puntos")
                        model.destacado=json_objectdetail.getString("destacado")
                        arrayList_clasif.add(model)
                    }
                }

                runOnUiThread {
                    //stuff that updates ui
                    tvSelected.text = "Clasificación $categ"
                    val obj_adapter : AdapterClasificacion
                    println(arrayList_clasif.size)
                    obj_adapter = AdapterClasificacion(applicationContext,arrayList_clasif)
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