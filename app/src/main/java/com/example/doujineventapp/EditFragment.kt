package com.example.doujineventapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment

class EditFragment : Fragment() {
    private var position = -1
    private lateinit var circle: Circle
    private var listener: EditEventListener? = null

    companion object {
        private const val POSITION = "position"
        private const val CIRCLE = "circle"

        fun newInstance(position: Int, circle: Circle) =
            EditFragment().apply {
                arguments = Bundle().apply {
                    putInt(POSITION, position)
                    putSerializable(CIRCLE, circle)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(POSITION)
            circle = it.getSerializable(CIRCLE) as Circle
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val spaceView = view.findViewById<TextView>(R.id.spaceView)
        val circleNameView = view.findViewById<TextView>(R.id.circleNameView)
        val penNameView = view.findViewById<TextView>(R.id.penNameView)
        val priceView = view.findViewById<TextView>(R.id.priceView)
        val giftSwitch = view.findViewById<Switch>(R.id.giftSwitch)
        val noteView = view.findViewById<TextView>(R.id.noteView)

        spaceView.text = circle.space
        circleNameView.text = circle.circleName
        penNameView.text = circle.penName
        priceView.text = circle.price.toString()
        giftSwitch.isChecked = circle.giftExists
        noteView.text = circle.note

        giftSwitch.setOnCheckedChangeListener{ buttonView, isChecked ->
            circle.giftExists = isChecked
        }

        val saveButton = view.findViewById<Button>(R.id.button)
        saveButton.text = getString(R.string.save)

        saveButton.setOnClickListener { view ->
            circle.space = spaceView.text.toString()
            circle.circleName = circleNameView.text.toString()
            circle.penName = penNameView.text.toString()
            circle.price = priceView.text.toString().toInt()
            circle.note = noteView.text.toString()

            listener?.saveData(position, circle)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is EditEventListener) {
            listener = context
        } else {
            throw RuntimeException("Listener is not Implementation.")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface EditEventListener {
        fun saveData(position: Int, circle: Circle)
    }
}
