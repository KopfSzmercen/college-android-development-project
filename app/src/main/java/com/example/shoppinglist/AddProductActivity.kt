package com.example.shoppinglist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.UUID

class AddProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        val productNameEditText: EditText = findViewById(R.id.editTextProductName)
        val productPriceEditText: EditText = findViewById(R.id.editTextProductPrice)
        val productQuantityEditText: EditText = findViewById(R.id.editTextProductQuantity)
        val addButton: Button = findViewById(R.id.buttonAddProduct)

        addButton.setOnClickListener {
            val productName = productNameEditText.text.toString()
            val productPrice = productPriceEditText.text.toString().toDoubleOrNull()
            val productQuantity = productQuantityEditText.text.toString().toIntOrNull()

            if (productName.isBlank()) {
                Toast.makeText(this, "Name should not be empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (productPrice == null || productPrice <= 0) {
                Toast.makeText(this, "Invalid price.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (productQuantity == null || productQuantity <= 0) {
                Toast.makeText(this, "Invalid quantity.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newProduct =
                Product(
                    UUID.randomUUID().toString(),
                    productName,
                    productPrice,
                    productQuantity,
                    false
                )

            val resultIntent = Intent()
            resultIntent.putExtra("newProduct", newProduct)
            resultIntent.putExtra("actionName", "ADD")
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}