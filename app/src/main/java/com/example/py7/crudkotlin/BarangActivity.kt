package com.example.py7.crudkotlin

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_barang.*
import java.io.ByteArrayOutputStream

class BarangActivity : AppCompatActivity() {

    var id=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barang)

        try {
            // getting data from intent
            var bundle: Bundle = intent.extras
            id = bundle.getInt("MainActId", 0)
            if (id !=0){
                txName.setText(bundle.getString("MainActName"))
                txBrand.setText(bundle.getString("MainActBrand"))
                txPrice.setText(bundle.getString("MainActPrice"))
                txCategory.setText(bundle.getString("MainActCategory"))
                txQuantity.setText(bundle.getString("MainActQuantity"))
                txSize.setText(bundle.getString("MainActSize"))
                txDate.setText(bundle.getString("MainActDate"))
                txDescription.setText(bundle.getString("MainActDescription"))
                var x= bundle.getByteArray("MainActImage")
                val bmp = BitmapFactory.decodeByteArray(x,0,x.size)
                txImage.setImageBitmap(bmp)
            }
        }catch (ex: Exception){
        }

    }

//     this function will show delete button
//     if we select an item it will show the delete button
//     else delete button will not appear
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.add_menu, menu)

        val itemDelete: MenuItem = menu.findItem(R.id.action_delete)

        if (id ==0){
            itemDelete.isVisible = false
        }else{
            itemDelete.isVisible = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            // to save or update an item
            R.id.action_save -> {
                var dbAdapter = DBAdapter(this)

                var txImage =  findViewById(R.id.txImage) as ImageView

                var values = ContentValues()
                values.put("Name", txName.text.toString())
                values.put("Brand", txBrand.text.toString())
                values.put("Price", txPrice.text.toString())
                values.put("Category", txCategory.text.toString())
                values.put("Quantity", txQuantity.text.toString())
                values.put("Size", txSize.text.toString())
                values.put("Date", txDate.text.toString())
                values.put("Description", txDescription.text.toString())

                val bitmap = (txImage.getDrawable() as BitmapDrawable).getBitmap()
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
                val imageInByte = stream.toByteArray()
                values.put("Image", imageInByte)

                if (id == 0){
                    val mID = dbAdapter.insert(values)

                    if (mID > 0){
                        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    var selectionArgs = arrayOf(id.toString())
                    val mID = dbAdapter.update(values, "Id=?", selectionArgs)
                    if (mID > 0){
                        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        Toast.makeText(this, "Failed to update data", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
            // to delete an item
            R.id.action_delete ->{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Delete Data")
                builder.setMessage("This Data Will Be Deleted")

                builder.setPositiveButton("Delete") {dialog: DialogInterface?, which: Int ->
                    var dbAdapter = DBAdapter(this)
                    val selectionArgs = arrayOf(id.toString())
                    dbAdapter.delete("Id=?", selectionArgs)
                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
                    finish()
                }
                builder.setNegativeButton("Cancel"){dialog: DialogInterface?, which: Int ->  }

                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // to get an image from device
    public fun insertImg(view: View?){
        var myFileIntent = Intent(Intent.ACTION_GET_CONTENT)
        myFileIntent.setType("image/*")
        startActivityForResult(myFileIntent, 100)
    }

    // to display the image got from the device
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && resultCode == RESULT_OK){
            txImage.setImageURI(data?.data)
        }
    }
}
