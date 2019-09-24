package com.aimar.infofortuna

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.ProxyFileDescriptorCallback
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
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Build
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.ActionBarDrawerToggle
import android.text.Layout
import android.transition.Slide
import android.transition.TransitionManager
import android.view.*
import com.aimar.infofortuna.fragment.ClasificacionFragment
import com.aimar.infofortuna.fragment.CochesFragment
import com.aimar.infofortuna.fragment.MesasFragment
import com.aimar.infofortuna.fragment.PartidosFragment


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
    lateinit var seniorInfobtn:Button


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

        seniorInfobtn = findViewById(R.id.infoSenior)
        seniorInfobtn.setOnClickListener { onButtonShowPopupWindowClick(findViewById(R.id.drawer_layout)) }
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






        //run(getResources().getString(R.string.json_partidos))
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.menu_home -> {
                toolbar.title = getString(R.string.home)
                navigateToFragment(PartidosFragment.newInstance("Escolar"))
            }
            R.id.menu_partidos_cadete -> {
                toolbar.title = getString(R.string.partidos)
                navigateToFragment(PartidosFragment.newInstance("Cadete"))
            }
            R.id.menu_partidos_juvenil -> {
                toolbar.title = getString(R.string.partidos)
                navigateToFragment(PartidosFragment.newInstance("Juvenil"))
            }
            R.id.menu_partidos_senior -> {
                toolbar.title = getString(R.string.partidos)
                navigateToFragment(PartidosFragment.newInstance("Senior"))
            }
            R.id.menu_clasi_cadete -> {
                toolbar.title = getString(R.string.clasi)
                navigateToFragment(ClasificacionFragment.newInstance("Cadete"))
            }
            R.id.menu_clasi_juvenil -> {
                toolbar.title = getString(R.string.clasi)
                navigateToFragment(ClasificacionFragment.newInstance("Juvenil"))
            }
            R.id.menu_clasi_senior -> {
                toolbar.title = getString(R.string.clasi)
                navigateToFragment(ClasificacionFragment.newInstance("Senior"))
            }
            R.id.menu_coches -> {
                toolbar.title = getString(R.string.coches)
                navigateToFragment(CochesFragment.newInstance())
            }
            R.id.menu_mesas -> {
                toolbar.title = getString(R.string.mesas)
                navigateToFragment(MesasFragment.newInstance())
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun navigateToFragment(fragmentToNavigate: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragmentToNavigate)
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        //fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }




    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
            toolbar.title = getString(R.string.home)
        } else {
            super.onBackPressed()
        }
    }


    public fun onButtonShowPopupWindowClick(view:View) {


        val inflater:LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // Inflate a custom view using layout inflater
        val view = inflater.inflate(R.layout.popup_entrenamientos,null)
        var drawerLayout:View = findViewById(R.id.drawer_layout)
        // Initialize a new instance of popup window
        val popupWindow = PopupWindow(
                view, // Custom view to show in popup window
                LinearLayout.LayoutParams.WRAP_CONTENT, // Width of popup window
                LinearLayout.LayoutParams.WRAP_CONTENT // Window height
        )

        // Set an elevation for the popup window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.elevation = 10.0F
        }


        // If API level 23 or higher then execute the code
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // Create a new slide animation for popup window enter transition
            val slideIn = Slide()
            slideIn.slideEdge = Gravity.TOP
            popupWindow.enterTransition = slideIn

            // Slide animation for popup window exit transition
            val slideOut = Slide()
            slideOut.slideEdge = Gravity.RIGHT
            popupWindow.exitTransition = slideOut

        }

        // Get the widgets reference from custom view
        //val tv = view.findViewById<TextView>(R.id.)
        //val buttonPopup = view.findViewById<Button>(R.id.button_popup)
                // Set a click listener for popup's button widget
        //buttonPopup.setOnClickListener{
            // Dismiss the popup window
        //    popupWindow.dismiss()
      //  }

        // Set a dismiss listener for popup window
        popupWindow.setOnDismissListener {
            Toast.makeText(applicationContext,"Popup closed",Toast.LENGTH_SHORT).show()
        }


        // Finally, show the popup window on app
        TransitionManager.beginDelayedTransition(drawerLayout)
        popupWindow.showAtLocation(
                drawerLayout, // Location to display popup window
                Gravity.CENTER, // Exact position of layout to display popup
                0, // X offset
                0 // Y offset
        )


    }
}