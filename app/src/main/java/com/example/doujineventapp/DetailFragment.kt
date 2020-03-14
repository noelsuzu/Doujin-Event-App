package com.example.doujineventapp

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class DetailFragment : Fragment() {

    private var position = -1
    private lateinit var circle: Circle
    private var listener: DetailEventListener? = null

    companion object {
        private const val POSITION = "position"
        private const val CIRCLE = "circle"

        fun newInstance(position: Int, circle: Circle) =
            DetailFragment().apply {
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
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // View取得
        val spaceView = view.findViewById<TextView>(R.id.spaceView)
        val circleNameView = view.findViewById<TextView>(R.id.circleNameView)
        val penNameView = view.findViewById<TextView>(R.id.penNameView)
        val priceView = view.findViewById<TextView>(R.id.priceView)
        val giftView = view.findViewById<TextView>(R.id.giftView)
        val noteView = view.findViewById<TextView>(R.id.noteView)

        val editButton = view.findViewById<Button>(R.id.editButton)
        val deleteButton = view.findViewById<Button>(R.id.deleteButton)

        // データの表示
        spaceView.text = circle.space
        circleNameView.text = circle.circleName
        penNameView.text = circle.penName
        priceView.text = "${circle.price}円"
        giftView.text = if (circle.giftExists) "あり" else "なし"
        noteView.text = circle.note

        // 編集ボタンでEdit画面へ
        editButton.setOnClickListener { view -> listener?.toEdit(position) }

        // 削除ボタンで削除
        deleteButton.setOnClickListener { view ->
            // 確認ダイアログ表示
            AlertDialog.Builder(context)
                .setTitle("確認")
                .setMessage("削除しますか？")
                .setPositiveButton("削除する"){ dialog, which -> listener?.deleteData(position) }
                .setNegativeButton("キャンセル", null)
                .show()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DetailEventListener) {
            listener = context
        } else {
            throw RuntimeException("Listener is not Implementation.")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface DetailEventListener {
        fun toEdit(position: Int)
        fun deleteData(position: Int)
    }
}
