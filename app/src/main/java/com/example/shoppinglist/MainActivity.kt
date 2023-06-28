package com.example.shoppinglist

import ProductAdapter
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MainActivity : AppCompatActivity() {
    private lateinit var productList: MutableList<Product>
    private lateinit var adapter: ProductAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences
    private val productListKey = "product_list"
    private var isEditingProduct = false

    private val addProductLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val actionName = result.data?.getSerializableExtra("actionName") as String
                if (actionName === "EDIT") {
                    val updatedProduct = data?.getSerializableExtra("updatedProduct") as? Product
                    if (updatedProduct != null) {
                        val index = productList.indexOfFirst { it.id == updatedProduct.id }
                        if (index != -1) {
                            productList[index] = updatedProduct
                            adapter.notifyItemChanged(index)
                            updateProductListStorage()
                        }
                    }
                    isEditingProduct = false
                } else {
                    val newProduct = data?.getSerializableExtra("newProduct") as? Product
                    if (newProduct != null) {
                        productList.add(newProduct)
                        updateProductListStorage()
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }

    private fun updateProductListStorage() {
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(productList)
        editor.putString(productListKey, json)
        editor.apply()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        

        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {

            val updatedProduct = data?.getSerializableExtra("updatedProduct") as? Product
            if (updatedProduct != null) {
                val index = productList.indexOfFirst { it.id == updatedProduct.id }
                if (index != -1) {
                    productList[index] = updatedProduct
                    adapter.notifyItemChanged(index)
                    updateProductListStorage()
                }
            }
            isEditingProduct = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

        productList = mutableListOf()
        adapter = ProductAdapter(productList)

        recyclerView = findViewById(R.id.recyclerViewProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val buttonGoToAddProduct: Button = findViewById(R.id.buttonGoToAddProductScreen)
        buttonGoToAddProduct.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            addProductLauncher.launch(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        retrieveProductList()
    }

    private fun retrieveProductList() {
        val json = sharedPreferences.getString(productListKey, "[]")
        val type = object : TypeToken<MutableList<Product>>() {}.type
        val savedProductList: MutableList<Product> = Gson().fromJson(json, type)
        productList = savedProductList

        var priceSumText = findViewById<TextView>(R.id.textViewListPriceSum)
        val newPriceSum = productList.sumOf { p -> p.price * p.quantity }
        priceSumText.text = newPriceSum.toString()

        adapter = ProductAdapter(productList)
        recyclerView.adapter = adapter
    }


    fun deleteProduct(position: Int) {
        productList.removeAt(position)
        adapter.notifyItemRemoved(position)
        var priceSumText = findViewById<TextView>(R.id.textViewListPriceSum)
        val newPriceSum = productList.sumOf { p -> p.price * p.quantity }
        priceSumText.text = newPriceSum.toString()

        updateProductListStorage()
    }

    fun checkBoughtStatus(position: Int, isChecked: Boolean) {
        productList[position].bought = isChecked
        updateProductListStorage()
    }
}