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
import android.support.v4.view.GravityCompat
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.Menu
import android.view.MenuItem
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.ActionBarDrawerToggle
import com.aimar.infofortuna.fragment.ClasificacionFragment


class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{
    lateinit var progress:ProgressBar
    lateinit var listView_details: ListView
    lateinit var btnPartidos: FloatingActionButton
    lateinit var btnMesas: FloatingActionButton
    lateinit var btnCoches: FloatingActionButton
    lateinit var btnClasi: FloatingActionButton
    lateinit var btnEditar: FloatingActionButton
    lateinit var toolbar: Toolbar
    lateinit var drawerLayout:DrawerLayout
    lateinit var drawerTitle:String


    lateinit var btnMenu: FloatingActionsMenu

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
        btnEditar = findViewById(R.id.btnEditar)

        btnClasi = findViewById(R.id.showClasi)
        listView_details = findViewById<ListView>(R.id.listView) as ListView
        listView_details.visibility = View.GONE

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Initialize the action bar drawer toggle instance
        val drawerToggle:ActionBarDrawerToggle = object : ActionBarDrawerToggle(
                this,
                drawer_layout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ){
            override fun onDrawerClosed(view:View){
                super.onDrawerClosed(view)
                //toast("Drawer closed")
            }

            override fun onDrawerOpened(drawerView: View){
                super.onDrawerOpened(drawerView)
                //toast("Drawer opened")
            }
        }
        drawerToggle.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        navview.setNavigationItemSelectedListener(this)


        if(btnMenu.isExpanded){
            listView_details.visibility = View.VISIBLE
        }

        btnPartidos.setOnClickListener{
            btnMenu.collapse()
            listView_details.visibility = View.GONE
        }
        btnMesas.setOnClickListener{
            btnMenu.collapse()
            runMesas(resources.getString(R.string.json_mesas))
            listView_details.visibility = View.VISIBLE
        }
        btnCoches.setOnClickListener {
            btnMenu.collapse()
            listView_details.visibility = View.VISIBLE
            //Toast.makeText(this,"Ouch! Esta opción aun no esta lista jeje",Toast.LENGTH_SHORT).show()
            runCoches(resources.getString(R.string.json_coches))
        }
        btnClasi.setOnClickListener {
            btnMenu.collapse()
            listView_details.visibility = View.GONE
            //Toast.makeText(this,"Ouch! Esta opción aun no esta lista jeje",Toast.LENGTH_SHORT).show()
        }




        //run(getResources().getString(R.string.json_partidos))
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.menu_partidos_escolar -> {
                Toast.makeText(this, "Contact us", Toast.LENGTH_SHORT).show()
            }
            R.id.menu_partidos_cadete -> {
                Toast.makeText(this, "Contact us", Toast.LENGTH_SHORT).show()
            }
            R.id.menu_partidos_juvenil -> {
                Toast.makeText(this, "Contact us", Toast.LENGTH_SHORT).show()
            }
            R.id.menu_partidos_senior -> {
                Toast.makeText(this, "Contact us", Toast.LENGTH_SHORT).show()
            }
            R.id.menu_clasi_escolar -> {
                Toast.makeText(this, "Publication", Toast.LENGTH_SHORT).show()
            }
            R.id.menu_clasi_cadete -> {
                Toast.makeText(this, "Android Store", Toast.LENGTH_SHORT).show()
            }
            R.id.menu_clasi_juvenil -> {
                Toast.makeText(this, "Newsletter", Toast.LENGTH_SHORT).show()
            }
            R.id.menu_clasi_senior -> {
                toolbar.title = getString(R.string.mesas)
                navigateToFragment(ClasificacionFragment.newInstance("Senior"))
            }
            R.id.menu_coches -> {
                Toast.makeText(this, "Contact us", Toast.LENGTH_SHORT).show()
            }
            R.id.menu_mesas -> {
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun navigateToFragment(fragmentToNavigate: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.drawer_layout, fragmentToNavigate)
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
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


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}