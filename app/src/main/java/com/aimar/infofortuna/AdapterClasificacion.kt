package com.aimar.infofortuna



import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
class AdapterClasificacion(context: Context, arrayListDetails:ArrayList<Clasificacion>) : BaseAdapter(){

    private val layoutInflater: LayoutInflater
    private val arrayListDetails:ArrayList<Clasificacion>
    public val highLightColor : Int = context.resources.getColor(R.color.itemColorWin)
    private val txtColorBlack : Int = context.resources.getColor(R.color.black_semi_transparent)
    init {
        this.layoutInflater = LayoutInflater.from(context)
        this.arrayListDetails=arrayListDetails
    }

    override fun getCount(): Int {
        return arrayListDetails.size
    }

    override fun getItem(position: Int): Any {
        return arrayListDetails.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        println("getView"+ arrayListDetails.get(position).equipo)
        val view: View?
        val listRowHolder: ListRowHolderClasi
        if (convertView == null) {
            view = this.layoutInflater.inflate(R.layout.adapter_clasification_layout, parent, false)
            listRowHolder = ListRowHolderClasi(view)
            view.tag = listRowHolder
        } else {
            view = convertView
            listRowHolder = view.tag as ListRowHolderClasi
        }
        listRowHolder.tvPosicion.text = ""+(position+1)
        listRowHolder.tvEquipo.text = arrayListDetails[position].equipo
        listRowHolder.tvPuntos.text = arrayListDetails[position].puntos.toString()
        if(arrayListDetails[position].equipo.equals("CD Fortuna KE",true)){
            println("ES DESTACADO? "+arrayListDetails[position].destacado)
            listRowHolder.tvEquipo.setTextColor(highLightColor)
            listRowHolder.tvPosicion.setTextColor(highLightColor)
            listRowHolder.tvPuntos.setTextColor(highLightColor)
        }
/*        if(arrayListDetails.get(position).equipo.toString().equals("CD Fortuna KE",true)){

        }*/
        /*
        if(position%2 == 0){
            listRowHolder.layout.setBackgroundColor(itemcolor1)
        }else
            listRowHolder.layout.setBackgroundColor(itemcolor2)*/
        return view
    }
}

private class ListRowHolderClasi(row: View?) {
    public val tvPosicion: TextView
    public val tvEquipo: TextView
    public val tvPuntos: TextView
    public val layout: LinearLayout


    init {
        this.tvPosicion = row?.findViewById<TextView>(R.id.tvPosicion) as TextView
        this.tvEquipo = row?.findViewById<TextView>(R.id.tvEquipo) as TextView
        this.tvPuntos = row?.findViewById<TextView>(R.id.tvPuntos) as TextView
        this.layout = row?.findViewById<LinearLayout>(R.id.linearLayout) as LinearLayout

    }
}