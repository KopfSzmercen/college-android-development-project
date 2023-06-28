package com.example.shoppinglist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditProductActivity : AppCompatActivity() {
    private lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)

        val productNameEditText: EditText = findViewById(R.id.editTextProductName)
        val productPriceEditText: EditText = findViewById(R.id.editTextProductPrice)
        val productQuantityEditText: EditText = findViewById(R.id.editTextProductQuantity)
        val saveButton: Button = findViewById(R.id.buttonSaveEditProduct)

        product = intent.getSerializableExtra("product") as Product

        productNameEditText.setText(product.name)
        productPriceEditText.setText(product.price.toString())
        productQuantityEditText.setText(product.quantity.toString())

        saveButton.setOnClickListener {
            val productName = productNameEditText.text.toString()
            val productQuantity = productQuantityEditText.text.toString().toInt()
            val productPrice = productPriceEditText.text.toString().toDouble()

            if (productName.isBlank()) {
                Toast.makeText(this, "Name should not be empty.", Toast.LENGTH_SHORT).show()
            } else if (productPrice == null || productPrice <= 0) {
                Toast.makeText(this, "Invalid price.", Toast.LENGTH_SHORT).show()
            } else if (productQuantity == null || productQuantity <= 0) {
                Toast.makeText(this, "Invalid quantity.", Toast.LENGTH_SHORT).show()
            } else {
                product.name = productName
                product.price = productPrice
                product.quantity = productQuantity

                val resultIntent = Intent()
                resultIntent.putExtra("updatedProduct", product)
                resultIntent.putExtra("actionName", "EDIT")
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }

        }
    }

}