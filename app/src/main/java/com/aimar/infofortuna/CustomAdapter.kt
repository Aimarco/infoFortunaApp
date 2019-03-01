package com.aimar.infofortuna

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

class CustomAdapter(context: Context,arrayListDetails:ArrayList<Partidos>) : BaseAdapter(){

    private val layoutInflater: LayoutInflater
    private val arrayListDetails:ArrayList<Partidos>
    private val itemcolorWin : Int = context.resources.getColor(R.color.itemColorWin)
    private val itemcolorLose : Int = context.resources.getColor(R.color.itemColorLose)
    private val itemcolorDraw : Int = context.resources.getColor(R.color.white)

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
        val view: View?
        val listRowHolder: ListRowHolder
        if (convertView == null) {
            view = this.layoutInflater.inflate(R.layout.adapter_layout, parent, false)
            listRowHolder = ListRowHolder(view)
            view.tag = listRowHolder
        } else {
            view = convertView
            listRowHolder = view.tag as ListRowHolder
        }

        listRowHolder.tvEquipo1.text = arrayListDetails.get(position).equipo1
        listRowHolder.tvEquipo2.text = arrayListDetails.get(position).equipo2
        listRowHolder.tvFechaPartido.text = arrayListDetails.get(position).fechaPartido
        listRowHolder.tvResultado.text = arrayListDetails.get(position).resultado
        listRowHolder.tvHora.text = arrayListDetails.get(position).hora


        if(arrayListDetails.get(position).resultado2.contains("V",true)){
            listRowHolder.layoutR2.setBackgroundColor(itemcolorWin)

        }else if(arrayListDetails.get(position).resultado2.contains("P",true)){
            listRowHolder.layoutR2.setBackgroundColor(itemcolorLose)
        }else{
            listRowHolder.layoutR2.setBackgroundColor(itemcolorDraw)
        }
       /* if(position%2 == 0){
            listRowHolder.layout.setBackgroundColor(itemcolor1)
        }else
            listRowHolder.layout.setBackgroundColor(itemcolor2)*/
        return view
    }
}

private class ListRowHolder(row: View?) {
    public val tvEquipo1: TextView
    public val tvEquipo2: TextView
    public val tvFechaPartido: TextView
    public val tvResultado: TextView
    public val tvHora: TextView
    public val layout: RelativeLayout
    public val layoutR2: LinearLayout

    init {
        this.tvEquipo1 = row?.findViewById<TextView>(R.id.tvEquipo1) as TextView
        this.tvEquipo2 = row?.findViewById<TextView>(R.id.tvEquipo2) as TextView
        this.tvFechaPartido = row?.findViewById<TextView>(R.id.tvFechaPartido) as TextView
        this.tvResultado= row?.findViewById<TextView>(R.id.tvResultado) as TextView
        this.tvHora= row?.findViewById<TextView>(R.id.tvHora) as TextView
        this.layout = row?.findViewById<RelativeLayout>(R.id.linearLayout) as RelativeLayout
        this.layoutR2 = row?.findViewById<LinearLayout>(R.id.resultMarker) as LinearLayout
    }
}