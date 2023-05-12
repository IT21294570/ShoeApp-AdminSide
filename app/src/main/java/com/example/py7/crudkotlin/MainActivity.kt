package com.example.py7.crudkotlin

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private var listBarang = ArrayList<Barang>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            var intent = Intent(this, BarangActivity::class.java)
            startActivity(intent)
        }

        loadData()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    // this function will display item getting them from database
    private fun loadData() {
        var dbAdapter = DBAdapter(this)
        var cursor = dbAdapter.allQuery()

        listBarang.clear()
        if (cursor.moveToFirst()){
            do {
                val id = cursor.getInt(cursor.getColumnIndex("Id"))
                val name = cursor.getString(cursor.getColumnIndex("Name"))
                val brand = cursor.getString(cursor.getColumnIndex("Brand"))
                val price = cursor.getString(cursor.getColumnIndex("Price"))
                val category = cursor.getString(cursor.getColumnIndex("Category"))
                val quantity = cursor.getString(cursor.getColumnIndex("Quantity"))
                val size = cursor.getString(cursor.getColumnIndex("Size"))
                val date = cursor.getString(cursor.getColumnIndex("Date"))
                val description = cursor.getString(cursor.getColumnIndex("Description"))
                val image = cursor.getBlob(cursor.getColumnIndex("Image"))
                listBarang.add(Barang(id, name, brand, price, category, quantity, size, description, date, image))
            }while (cursor.moveToNext())
        }

        var barangAdapter = BarangAdapter(this, listBarang)
        lvBarang.adapter = barangAdapter

        // this will handle search
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if(query == ""){
                    var barangAdapter = BarangAdapter(this@MainActivity, listBarang)
                    lvBarang.adapter = barangAdapter
                }else {
                    var l =
                        listBarang.filter { i -> i.name!!.contains(query, true) } as java.util.ArrayList<Barang>
                    var barangAdapter = BarangAdapter(this@MainActivity, l)
                    lvBarang.adapter = barangAdapter
                }
                    return false
            }

            override fun onQueryTextChange(newText: String): Boolean {

                if(newText == ""){
                    var barangAdapter = BarangAdapter(this@MainActivity, listBarang)
                    lvBarang.adapter = barangAdapter
                }else {
                    var l =
                        listBarang.filter { i -> i.name!!.contains(newText, true) } as java.util.ArrayList<Barang>
                    var barangAdapter = BarangAdapter(this@MainActivity, l)
                    lvBarang.adapter = barangAdapter
                }
                    return false
            }
        })

    }

    // this is the adapter class for item
    inner class BarangAdapter: BaseAdapter{

        private var barangList = ArrayList<Barang>()
        private var context: Context? = null

        constructor(context: Context, barangList: ArrayList<Barang>) : super(){
            this.barangList = barangList
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view: View?
            val vh: ViewHolder

            if (convertView == null){
                view = layoutInflater.inflate(R.layout.barang, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
                Log.i("db", "set tag for ViewHolder, position: " + position)
            }else{
                view = convertView
                vh = view.tag as ViewHolder
            }

            var mBarang = barangList[position]

            vh.tvName.text = mBarang.name
            vh.tvBrand.text = mBarang.brand
            vh.tvPrice.text = "Rp." + mBarang.price

            val x = mBarang.image
            val bmp = BitmapFactory.decodeByteArray(x,0, x!!.size)

            vh.tvImage.setImageBitmap(bmp)


            lvBarang.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
                updateBarang(barangList[position])
            }

            return view
        }

        override fun getItem(position: Int): Any {
            return barangList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return barangList.size
        }

    }

    // this function used to update an item
    private fun updateBarang(barang: Barang) {
        // creating an intent and send data through it to other activity
        var  intent = Intent(this, BarangActivity::class.java)
        intent.putExtra("MainActId", barang.id)
        intent.putExtra("MainActName", barang.name)
        intent.putExtra("MainActBrand", barang.brand)
        intent.putExtra("MainActPrice", barang.price)
        intent.putExtra("MainActCategory", barang.category)
        intent.putExtra("MainActQuantity", barang.quantity)
        intent.putExtra("MainActSize", barang.size)
        intent.putExtra("MainActDate", barang.date)
        intent.putExtra("MainActDescription", barang.description)
        intent.putExtra("MainActImage", barang.image)
        startActivity(intent)
    }

    // this is the skeleton of UI of item
    private class ViewHolder(view: View?){
        val tvName: TextView
        val tvBrand: TextView
        val tvPrice: TextView
        val tvImage: ImageView

        init {
            this.tvName = view?.findViewById(R.id.tvName) as TextView
            this.tvBrand = view?.findViewById(R.id.tvBrand) as TextView
            this.tvPrice = view?.findViewById(R.id.tvPrice) as TextView
            this.tvImage = view?.findViewById(R.id.tvImage) as ImageView
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
