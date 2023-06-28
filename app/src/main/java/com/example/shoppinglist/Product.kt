package com.example.shoppinglist

import java.io.Serializable

class Product(
    var id: String,
    var name: String,
    var price: Double,
    var quantity: Int,
    var bought: Boolean
) :
    Serializable
