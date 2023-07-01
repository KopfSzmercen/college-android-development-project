package com.example.shoppinglist

import FavouriteProductAdapter
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavouriteProductsActivity : AppCompatActivity() {
    private lateinit var favourites: MutableList<FavouriteProduct>
    private lateinit var adapter: FavouriteProductAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var recyclerView: RecyclerView
    private val favouritesListKey = "favourites_list"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

        favourites = mutableListOf()
        setContentView(R.layout.activity_favourite_products)

        recyclerView = findViewById(R.id.favouriteProductsRecyclerView)
        adapter = FavouriteProductAdapter(favourites)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        val addNewFavouriteButton = findViewById<Button>(R.id.goToAddNewFavourite)
        addNewFavouriteButton.setOnClickListener {
            val intent = Intent(this, ActivityAddFavourite::class.java)
            startActivityForResult(intent, ADD_FAVOURITE_REQUEST_CODE)
        }

        val goBackButton = findViewById<Button>(R.id.goBackButton)
        goBackButton.setOnClickListener {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_FAVOURITE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val newFavourite = data?.getSerializableExtra(EXTRA_NEW_FAVOURITE) as FavouriteProduct
            newFavourite.let {
                favourites.add(it)
                updateProductListStorage()
                adapter.notifyDataSetChanged()
            }
        } else if (requestCode == UPDATE_FAVOURITE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val updatedFavourite =
                data?.getSerializableExtra(EXTRA_UPDATED_FAVOURITE) as FavouriteProduct
            val updatedFavouritePosition = data.getSerializableExtra(
                EXTRA_UPDATED_FAVOURITE_POSITION
            ) as Int
            favourites[updatedFavouritePosition] = updatedFavourite
            updateProductListStorage()
            adapter.notifyDataSetChanged()

        }
    }

    private fun updateProductListStorage() {
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(favourites)
        editor.putString(favouritesListKey, json)
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        retrieveFavouritesList()
    }

    fun deleteFavourite(position: Int) {
        favourites.removeAt(position)
        updateProductListStorage()
        adapter.notifyDataSetChanged()
    }

    private fun retrieveFavouritesList() {
        val json = sharedPreferences.getString(favouritesListKey, "[]")
        val type = object : TypeToken<MutableList<FavouriteProduct>>() {}.type
        val savedProductList: MutableList<FavouriteProduct> = Gson().fromJson(json, type)
        favourites = savedProductList

        adapter = FavouriteProductAdapter(favourites)
        recyclerView.adapter = adapter
    }


    companion object {
        const val ADD_FAVOURITE_REQUEST_CODE = 1
        const val EXTRA_NEW_FAVOURITE = "extra_new_favourite"

        const val UPDATE_FAVOURITE_REQUEST_CODE = 3
        const val EXTRA_UPDATED_FAVOURITE = "extra_new_favourite"
        const val EXTRA_UPDATED_FAVOURITE_POSITION = "extra_new_favourite_position"
    }
}