package com.example.temporal.infofortuna


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
class AdapterCoche(context: Context, arrayListDetails:ArrayList<Coche>) : BaseAdapter(){

    private val layoutInflater: LayoutInflater
    private val arrayListDetails:ArrayList<Coche>
    private val itemcolor1 : Drawable = context.resources.getDrawable(R.drawable.background_color_fortuna)
    private val itemcolor2 : Int = context.resources.getColor(R.color.itemColor2)
    private val highLightColor : Int = context.resources.getColor(R.color.highLightColor)
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
        val view: View?
        val listRowHolder: ListRowHolderCoches
        if (convertView == null) {
            view = this.layoutInflater.inflate(R.layout.adapter_layout_coches, parent, false)
            listRowHolder = ListRowHolderCoches(view)
            view.tag = listRowHolder
        } else {
            view = convertView
            listRowHolder = view.tag as ListRowHolderCoches
        }

        listRowHolder.tvNombre.text = arrayListDetails.get(position).nombre
        listRowHolder.tvNumViajes.text = arrayListDetails.get(position).numViajes.toString()
        if(position == 0 || position== 1 || position== 2){
            listRowHolder.tvNombre.setTextColor(txtColorBlack)
            listRowHolder.tvNumViajes.setTextColor(txtColorBlack)
            listRowHolder.layout.setBackgroundColor(highLightColor)

        }
        /*if(position%2 == 0){
            listRowHolder.layout.setBackgroundColor(itemcolor1)
        }else
            listRowHolder.layout.setBackgroundColor(itemcolor2)*/
        return view
    }
}

private class ListRowHolderCoches(row: View?) {
    public val tvNombre: TextView
    public val tvNumViajes: TextView
    public val layout: LinearLayout

    init {
        this.tvNombre = row?.findViewById<TextView>(R.id.tvNombre) as TextView
        this.tvNumViajes = row?.findViewById<TextView>(R.id.tvNumViajes) as TextView
        this.layout = row?.findViewById<LinearLayout>(R.id.linearLayout) as LinearLayout

    }
}