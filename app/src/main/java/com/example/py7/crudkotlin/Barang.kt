package com.example.py7.crudkotlin

import java.util.Date

class Barang {

    var id: Int? = null
    var name: String? = null
    var brand: String? = null
    var price: String? = null
    var category: String? = null
    var quantity: String? = null
    var size: String? = null
    var description: String? = null
    var date: String? = null
    var image: ByteArray? = null

    constructor(id: Int, name: String, brand: String, price:String, category: String, quantity: String, size: String, description: String, date: String, image: ByteArray){
        this.id = id
        this.name = name
        this.brand = brand
        this.price = price
        this.category = category
        this.quantity = quantity
        this.size = size
        this.description = description
        this.date = date
        this.image = image
    }
}