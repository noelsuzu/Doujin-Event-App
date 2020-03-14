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
        super.onViewCreated(view, savedInstanceState)

        // View取得
        val spaceView = view.findViewById<EditText>(R.id.spaceView)
        val circleNameView = view.findViewById<EditText>(R.id.circleNameView)
        val penNameView = view.findViewById<EditText>(R.id.penNameView)
        val priceView = view.findViewById<EditText>(R.id.priceView)
        val giftSwitch = view.findViewById<Switch>(R.id.giftSwitch)
        val noteView = view.findViewById<EditText>(R.id.noteView)

        val saveButton = view.findViewById<Button>(R.id.button)
        saveButton.text = getString(R.string.save) // ボタンに保存と表示

        // 初期値設定
        spaceView.setText(circle.space)
        circleNameView.setText(circle.circleName)
        penNameView.setText(circle.penName)
        priceView.setText(circle.price.toString())
        giftSwitch.isChecked = circle.giftExists
        noteView.setText(circle.note)

        // Switchの状態を取得
        giftSwitch.setOnCheckedChangeListener{ buttonView, isChecked ->
            circle.giftExists = isChecked
        }

        // 保存ボタンで保存
        saveButton.setOnClickListener { view ->
            // 新しいデータでcircleを更新
            circle.space = spaceView.text.toString()
            circle.circleName = circleNameView.text.toString()
            circle.penName = penNameView.text.toString()
            circle.price = priceView.text.toString().toInt()
            circle.note = noteView.text.toString()

            //保存
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
