package com.aimar.infofortuna

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RelativeLayout
import android.widget.TextView
class CustomAdapterMesas(context: Context, arrayListDetails:ArrayList<Mesa>) : BaseAdapter(){

    private val layoutInflater: LayoutInflater
    private val arrayListDetails:ArrayList<Mesa>
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
        val listRowHolder: ListRowHolderMesas
        if (convertView == null) {
            view = this.layoutInflater.inflate(R.layout.adapter_layout_mesas, parent, false)
            listRowHolder = ListRowHolderMesas(view)
            view.tag = listRowHolder
        } else {
            view = convertView
            listRowHolder = view.tag as ListRowHolderMesas
        }

        listRowHolder.tvFechaMesa.text = arrayListDetails.get(position).fecha
        listRowHolder.tvCatg.text = arrayListDetails.get(position).catg
        listRowHolder.tvMesa1.text = arrayListDetails.get(position).mesa1
        listRowHolder.tvMesa2.text = arrayListDetails.get(position).mesa2
        listRowHolder.tvMesa3.text = arrayListDetails.get(position).mesa3
        /*if(position%2 == 0){
            listRowHolder.layout.setBackgroundColor(itemcolor1)
        }else
            listRowHolder.layout.setBackgroundColor(itemcolor2)*/
        return view
    }
}

private class ListRowHolderMesas(row: View?) {
    public val tvFechaMesa: TextView
    public val tvMesa1: TextView
    public val tvMesa2: TextView
    public val tvMesa3: TextView
    public val tvCatg: TextView
    public val layout: RelativeLayout

    init {
        this.tvMesa1 = row?.findViewById<TextView>(R.id.tvMesa1) as TextView
        this.tvMesa2 = row?.findViewById<TextView>(R.id.tvMesa2) as TextView
        this.tvMesa3 = row?.findViewById<TextView>(R.id.tvMesa3) as TextView
        this.tvFechaMesa = row?.findViewById<TextView>(R.id.tvFechaMesa) as TextView
        this.tvCatg= row?.findViewById<TextView>(R.id.tvCatg) as TextView
        this.layout = row?.findViewById<RelativeLayout>(R.id.linearLayout) as RelativeLayout

    }
}