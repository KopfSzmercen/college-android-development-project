package com.example.shoppinglist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ActivityAddFavourite : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_favourite)

        val productNameEditText = findViewById<EditText>(R.id.editTextProductName)
        val saveButton = findViewById<Button>(R.id.buttonAddFavourite)

        saveButton.setOnClickListener {
            val productName = productNameEditText.text.toString()


            if (productName.isBlank()) {
                Toast.makeText(this, "Name should not be empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newFavourite = FavouriteProduct(productName)
            val resultIntent = Intent()
            resultIntent.putExtra(FavouriteProductsActivity.EXTRA_NEW_FAVOURITE, newFavourite)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}