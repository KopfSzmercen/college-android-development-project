import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.EditProductActivity
import com.example.shoppinglist.MainActivity
import com.example.shoppinglist.Product
import com.example.shoppinglist.R


class ProductAdapter(private val productList: MutableList<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view, productList)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class ProductViewHolder(itemView: View, productList: MutableList<Product>) :
        RecyclerView.ViewHolder(itemView) {
        private val textViewProductName: TextView =
            itemView.findViewById(R.id.textViewProductName)
        private val textViewProductPrice: TextView =
            itemView.findViewById(R.id.textViewProductPrice)
        private val textViewProductQuantity: TextView =
            itemView.findViewById(R.id.textViewProductQuantity)
        private val deleteButton: AppCompatImageButton =
            itemView.findViewById(R.id.imageButtonDeleteProduct)
        private val boughtCheckbox: CheckBox = itemView.findViewById(R.id.checkBoxBought)
        private val editProductButton: AppCompatImageButton =
            itemView.findViewById(R.id.imageButtonEditProduct)


        fun bind(product: Product) {
            val spannableString = SpannableString("Name: ${product.name}")
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                4,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            textViewProductName.text = spannableString
            textViewProductPrice.text = product.price.toString()
            textViewProductQuantity.text = product.quantity.toString()
            boughtCheckbox.isChecked = product.bought
        }

        init {

            deleteButton.setOnClickListener {
                val position = layoutPosition
                if (position != RecyclerView.NO_POSITION) {
                    (itemView.context as MainActivity).deleteProduct(position)
                }
            }

            editProductButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val product = productList[position]

                    val intent =
                        Intent(itemView.context as MainActivity, EditProductActivity::class.java)
                    intent.putExtra("product", product)
                    (itemView.context as Activity).startActivityForResult(intent, 2)
                }
            }

            boughtCheckbox.setOnCheckedChangeListener { _, isChecked ->
                val position = layoutPosition
                if (position != RecyclerView.NO_POSITION) {
                    (itemView.context as MainActivity).checkBoughtStatus(position, isChecked)
                }
            }
        }
    }
}