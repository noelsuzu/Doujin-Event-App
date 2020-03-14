package com.example.doujineventapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), ListFragment.ListEventListener,
        DetailFragment.DetailEventListener, EditFragment.EditEventListener, AddFragment.AddEventListener{

    private lateinit var dbAdapter: DBAdapter
    private val items = arrayListOf<Circle>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // データベースを操作するアダプタ
        dbAdapter = DBAdapter(this)

        // データ読み込み
        loadData()

        // ListFragment表示
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ListFragment.newInstance(items))
            .commit()
    }

    // データベースを読み込みitemsに格納
    private fun loadData() {
        items.clear()
        dbAdapter.readDB()
        val c = dbAdapter.getDB(null)
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

    // ------  ListEventListener  ------

    // データ一覧のチェックボックス状態をデータベースに反映
    override fun onCheckBoxChanged(position: Int, isChecked: Boolean) {
        dbAdapter.openDB()
        dbAdapter.updateDB(position, mapOf(DBAdapter.IS_CHECKED to isChecked))
        dbAdapter.closeDB()
    }

    // DetailFragmentへ遷移
    override fun toDetail(position: Int) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, DetailFragment.newInstance(position, items[position]))
            .addToBackStack(null)
            .commit()
    }

    // AddFragmentへ遷移
    override fun toAdd() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, AddFragment())
            .addToBackStack(null)
            .commit()
    }

    // ------  DetailEventListener  ------

    // EditFragmentへ遷移
    override fun toEdit(position: Int) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, EditFragment.newInstance(position, items[position]))
            .addToBackStack(null)
            .commit()
    }

    // データ削除
    override fun deleteData(position: Int) {
        val circle = items.removeAt(position)

        dbAdapter.openDB()
        dbAdapter.selectDelete(circle.id)
        dbAdapter.closeDB()

        supportFragmentManager.popBackStack()
    }

    // ------  EditEventListener  ------

    // データ保存
    override fun saveData(position: Int, circle: Circle) {
        items[position] = circle

        dbAdapter.openDB()
        dbAdapter.updateDB(circle.id, mapOf(
            DBAdapter.CIRCLE_NAME to circle.circleName,
            DBAdapter.PEN_NAME to circle.penName,
            DBAdapter.SPACE to circle.space,
            DBAdapter.GIFT_EXISTS to circle.giftExists,
            DBAdapter.NOTE to circle.note
        ))
        dbAdapter.closeDB()
        supportFragmentManager.popBackStack()
    }

    // ------  AddEventListener  ------

    // データ追加
    override fun addData(circle: Circle) {
        dbAdapter.openDB()
        val id = dbAdapter.registerDB(circle)
        dbAdapter.closeDB()

        circle.id = id
        items.add(circle)

        supportFragmentManager.popBackStack()
    }
}
