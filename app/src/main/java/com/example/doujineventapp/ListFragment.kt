package com.example.doujineventapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListFragment : Fragment() {

    private lateinit var items: ArrayList<Circle>
    private lateinit var totalPriceLabel: TextView
    private var totalPrice = 0
    private var listener: ListEventListener? = null

    companion object{
        private const val ITEMS = "items"

        fun newInstance(items: ArrayList<Circle>) = ListFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ITEMS, items)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            items = it.getSerializable(ITEMS) as ArrayList<Circle>
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listView = view.findViewById<ListView>(R.id.listView)
        totalPriceLabel = view.findViewById(R.id.totalPriceLabel)
        val addButton =  view.findViewById<FloatingActionButton>(R.id.addButton)

        // 合計金額表示
        totalPrice = 0
        for (circle in items) {
            if (!circle.isChecked) {
                totalPrice += circle.price
            }
        }
        totalPriceLabel.text = "合計金額：${totalPrice}円"

        val adapter = context ?.let {
            CircleListAdapter(it, items, object : CircleListAdapter.OnCheckedChangeListener{
                override fun onCheckedChange(circle: Circle, isChecked: Boolean) {
                    updateTotalPrice(circle, isChecked)
                    listener?.onCheckBoxChanged(circle.id, isChecked)
                }
            }) } ?: throw RuntimeException("Invalid context")

        listView.adapter = adapter
        listView.setOnItemClickListener{ parent, view, position, id ->
            listener?.toDetail(position)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ListEventListener) {
            listener = context
        } else {
            throw RuntimeException("Listener is not Implementation.")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun updateTotalPrice(circle: Circle, isChecked: Boolean) {
        if (isChecked) {
            totalPrice -= circle.price
        } else {
            totalPrice += circle.price
        }
        totalPriceLabel.text = "合計金額：" + totalPrice + "円"
    }

    interface ListEventListener {
        fun onCheckBoxChanged(position: Int, isChecked: Boolean)
        fun toDetail(position: Int)
//        fun toAdd()
    }
}
