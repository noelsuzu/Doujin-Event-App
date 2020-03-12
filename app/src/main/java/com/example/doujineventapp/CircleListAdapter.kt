package com.example.doujineventapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView

class CircleListAdapter(context: Context, val items: ArrayList<Circle>, val listener: OnCheckedChangeListener)
    : ArrayAdapter<Circle>(context, 0, items) {

    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: layoutInflater.inflate(R.layout.circle_list_item, parent, false)

        val checkBox = view.findViewById<CheckBox>(R.id.checkbox)
        checkBox.setOnCheckedChangeListener(null)

        val circle = getItem(position) as Circle

        checkBox.isChecked = circle.isChecked

        val spaceLabel = view.findViewById<TextView>(R.id.spaceLabel)
        spaceLabel.text = circle.space
        val circleLabel = view.findViewById<TextView>(R.id.circleLabel)
        circleLabel.text = "${circle.circleName}（${circle.penName}）"
        val priceLabel = view.findViewById<TextView>(R.id.priceLabel)
        priceLabel.text = "${circle.price}円"

        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            items[position].isChecked = isChecked
            listener.onCheckedChange(circle, isChecked)
        }

        return view
    }

    interface OnCheckedChangeListener{
        fun onCheckedChange(circle: Circle, isChecked: Boolean)
    }
}