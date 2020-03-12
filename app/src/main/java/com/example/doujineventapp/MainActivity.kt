package com.example.doujineventapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), ListFragment.ListEventListener {

    private lateinit var dbAdapter: DBAdapter
    private val items = arrayListOf<Circle>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbAdapter = DBAdapter(this)

        dbAdapter.openDB()

//        val c = dbAdapter.searchDB(null, DBAdapter.ID, arrayOf("1"))
//        Log.i("loadData","${c?.moveToFirst()}")

//        dbAdapter.registerDB(Circle(0,"ユーザ1", "サークル1","あ01a",
//            100, false, "特になし", false))
        dbAdapter.registerDB(Circle(0,"ユーザ2", "サークル2","あ01b",
            200, true, "特になし", false))
        dbAdapter.registerDB(Circle(0,"ユーザ3", "サークル3","あ02a",
            300, false, "特になし", false))
        dbAdapter.registerDB(Circle(0,"ユーザ4", "サークル4","あ02b",
            400, false, "特になし", false))
        dbAdapter.registerDB(Circle(0,"ユーザ5", "サークル5","あ03a",
            500, false, "特になし", false))
        dbAdapter.registerDB(Circle(0,"ユーザ6", "サークル6","あ03b",
            600, true, "特になし", false))
        dbAdapter.registerDB(Circle(0,"ユーザ7", "サークル7","あ04a",
            700, true, "特になし", false))
        dbAdapter.registerDB(Circle(0,"ユーザ8", "サークル8","あ04b",
            100, false, "特になし", false))
        dbAdapter.registerDB(Circle(0,"ユーザ9", "サークル9","あ05a",
            400, false, "特になし", false))
        dbAdapter.registerDB(Circle(0,"ユーザ10", "サークル10","あ05b",
            300, true, "特になし", false))
        dbAdapter.registerDB(Circle(0,"ユーザ11", "サークル11","あ06a",
            700, false, "特になし", false))
        dbAdapter.registerDB(Circle(0,"ユーザ12", "サークル12","あ06b",
            1000, false, "特になし", false))
        dbAdapter.closeDB()

        loadData()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ListFragment.newInstance(items))
            .commit()
    }

    private fun loadData() {
        items.clear()
        dbAdapter.openDB()
        val c = dbAdapter.getDB(null)
        Log.i("loadData", "${c.count}")
        if (c.moveToFirst()){
            do{
                val circle = Circle(
                    c.getInt(c.getColumnIndex(DBAdapter.ID)),
                    c.getString(c.getColumnIndex(DBAdapter.PEN_NAME)),
                    c.getString(c.getColumnIndex(DBAdapter.CIRCLE_NAME)),
                    c.getString(c.getColumnIndex(DBAdapter.SPACE)),
                    c.getInt(c.getColumnIndex(DBAdapter.PRICE)),
                    c.getInt(c.getColumnIndex(DBAdapter.GIFT_EXISTS)) == 1,
                    c.getString(c.getColumnIndex(DBAdapter.NOTE)),
                    c.getInt(c.getColumnIndex(DBAdapter.IS_CHECKED)) == 1
                )
                items.add(circle)
            } while (c.moveToNext())
        }
        c.close()

        dbAdapter.closeDB()
    }

    override fun onCheckBoxChanged(position: Int, isChecked: Boolean) {
        dbAdapter.openDB()
        dbAdapter.updateDB(position, mapOf(DBAdapter.IS_CHECKED to isChecked))
        dbAdapter.closeDB()
    }
}
