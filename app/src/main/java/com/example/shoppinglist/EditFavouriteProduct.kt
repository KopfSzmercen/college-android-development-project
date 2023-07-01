package com.example.shoppinglist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditFavouriteProduct : AppCompatActivity() {
    private lateinit var favouriteProduct: FavouriteProduct

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_favourite)

        val favouriteProductNameEditText: EditText = findViewById(R.id.productNameEditText)
        favouriteProduct = intent.getSerializableExtra("favouriteProduct") as FavouriteProduct

        val position = intent.getSerializableExtra("position") as Int

        favouriteProductNameEditText.setText(favouriteProduct.name)

        val saveButton = findViewById<Button>(R.id.saveButton)

        saveButton.setOnClickListener {

            val productName = favouriteProductNameEditText.text.toString()

            if (productName.isBlank()) {
                Toast.makeText(this, "Name should not be empty.", Toast.LENGTH_SHORT).show()
            } else {
                favouriteProduct.name = productName

                val resultIntent = Intent()

                resultIntent.putExtra(
                    FavouriteProductsActivity.EXTRA_UPDATED_FAVOURITE,
                    favouriteProduct
                )
                resultIntent.putExtra(
                    FavouriteProductsActivity.EXTRA_UPDATED_FAVOURITE_POSITION,
                    position
                )
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }
}