package com.example.doujineventapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.fragment.app.Fragment

class AddFragment : Fragment() {
    private var giftExists: Boolean = false
    private var listener: AddEventListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spaceView = view.findViewById<EditText>(R.id.spaceView)
        val circleNameView = view.findViewById<EditText>(R.id.circleNameView)
        val penNameView = view.findViewById<EditText>(R.id.penNameView)
        val priceView = view.findViewById<EditText>(R.id.priceView)
        val giftSwitch = view.findViewById<Switch>(R.id.giftSwitch)
        val noteView = view.findViewById<EditText>(R.id.noteView)

        giftSwitch.setOnCheckedChangeListener{ buttonView, isChecked ->
            giftExists = isChecked
        }

        val addButton = view.findViewById<Button>(R.id.button)
        addButton.text = getString(R.string.add)

        addButton.setOnClickListener { view ->
            val circle = Circle(-1, penName = penNameView.text.toString(),
                circleName = circleNameView.text.toString(), space = spaceView.text.toString(),
                price = priceView.text.toString().toInt(), giftExists = giftExists,
                note = noteView.text.toString(), isChecked = false)
            listener?.addData(circle)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AddEventListener) {
            listener = context
        } else {
            throw RuntimeException("Listener is not Implementation.")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface AddEventListener {
        fun addData(circle: Circle)
    }
}
