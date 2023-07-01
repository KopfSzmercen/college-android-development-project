import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.EditFavouriteProduct
import com.example.shoppinglist.FavouriteProduct
import com.example.shoppinglist.FavouriteProductsActivity
import com.example.shoppinglist.R

class FavouriteProductAdapter(private val favourites: MutableList<FavouriteProduct>) :
    RecyclerView.Adapter<FavouriteProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favourite_product, parent, false)
        return ViewHolder(view, favourites)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favouriteProduct = favourites[position]
        holder.bind(favouriteProduct)
    }


    class ViewHolder(itemView: View, favourites: MutableList<FavouriteProduct>) :


        RecyclerView.ViewHolder(itemView) {
        private val productNameTextView: TextView = itemView.findViewById(R.id.productNameTextView)
        private val deleteFromFavouriteButton: ImageButton =
            itemView.findViewById(R.id.deleteFavourite)


        fun bind(product: FavouriteProduct) {
            val spannableString = SpannableString("Name: ${product.name}")
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                4,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            productNameTextView.text = spannableString

            deleteFromFavouriteButton.setOnClickListener {
                val position = layoutPosition
                if (position != RecyclerView.NO_POSITION) {
                    (itemView.context as FavouriteProductsActivity).deleteFavourite(position)
                }
            }
        }

        init {
            val editFavouriteButton = itemView.findViewById<ImageButton>(R.id.editFavourite)

            editFavouriteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val intent = Intent(
                        itemView.context as FavouriteProductsActivity,
                        EditFavouriteProduct::class.java
                    )
                    intent.putExtra("favouriteProduct", favourites[position])
                    intent.putExtra("position", position)
                    (itemView.context as Activity).startActivityForResult(
                        intent,
                        FavouriteProductsActivity.UPDATE_FAVOURITE_REQUEST_CODE
                    )
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return favourites.size
    }
}