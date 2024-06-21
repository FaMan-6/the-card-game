import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.faman_akira.thegamechatgpt.CardData
import com.faman_akira.thegamechatgpt.R

class CardAdapter(private val cards: List<CardData>, private val clickListener: (Int) -> Unit) :
    RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cards[position], clickListener)
    }

    override fun getItemCount() = cards.size

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.card_image)

        fun bind(card: CardData, clickListener: (Int) -> Unit) {
            if (card.isFlipped) {
                imageView.setImageResource(card.drawable)
            } else {
                imageView.setImageResource(R.drawable.card)
            }

            itemView.setOnClickListener {
                if (!card.isMatched && !card.isFlipped) {
                    clickListener(adapterPosition)
                }
            }
        }
    }
}