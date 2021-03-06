package com.aimar.infofortuna.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import com.aimar.infofortuna.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class MesasFragment : Fragment() {


    companion object {
        fun newInstance(): Fragment {
            return MesasFragment()
        }
    }

    fun newInstance(): Fragment {

        return MesasFragment()
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
            openSpreadSheet(getResources().getString(R.string.idHojaMesas))
        }
        txt_title.text="Mesas Fortuna "
        runMesas(resources.getString(R.string.json_mesas))
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
                    var json_objectdetail: JSONObject =jsonarray_info.getJSONObject(i)
                    var model: Mesa = Mesa()
                    model.fecha=json_objectdetail.getString("fecha")
                    model.fecha+="("+json_objectdetail.getString("hora")+")"
                    model.catg=json_objectdetail.getString("categoria")
                    model.mesa1=json_objectdetail.getString("mesa1")
                    model.mesa2=json_objectdetail.getString("mesa2")
                    model.mesa3=json_objectdetail.getString("mesa3")
                    model.hora=json_objectdetail.getString("hora")

                    arrayList_detailsMesas.add(model)
                }
                activity!!.runOnUiThread {
                    val obj_adapter: CustomAdapterMesas
                    obj_adapter = CustomAdapterMesas(activity!!.applicationContext, arrayList_detailsMesas)
                    listView_details.adapter = obj_adapter
                }
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