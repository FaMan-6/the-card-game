package com.faman_akira.thegamechatgpt

import CardAdapter
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CardAdapter
    private var score = 0
    var firstSelectedIndex: Int? = null
    private val cards = mutableListOf<CardData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val quitButton = findViewById<ImageView>(R.id.quit_button)
        quitButton.setOnClickListener {
            finishAffinity()
        }

        // Initialize cards with shuffled data
        val drawableList = listOf(
            R.drawable.cumi_card, R.drawable.cumi_card,
            R.drawable.daijin_card, R.drawable.daijin_card,
            R.drawable.harumau_card, R.drawable.harumau_card,
            R.drawable.kepiting_card, R.drawable.kepiting_card,
            R.drawable.kodok_card, R.drawable.kodok_card,
            R.drawable.nyitnyit_card, R.drawable.nyitnyit_card,
            R.drawable.tupai_card, R.drawable.tupai_card,
            R.drawable.uler_card, R.drawable.uler_card
        ).shuffled()

        drawableList.forEach { cards.add(CardData(it)) }

        recyclerView = findViewById(R.id.recycle_view)
        recyclerView.layoutManager = GridLayoutManager(this, 4)
        adapter = CardAdapter(cards) { position -> onCardClicked(position) }
        recyclerView.adapter = adapter
    }


    private fun onCardClicked(position: Int) {
        val scoreText = findViewById<TextView>(R.id.score)
        val card = cards[position]
        card.isFlipped = true
        adapter.notifyItemChanged(position)

        if (firstSelectedIndex == null) {
            firstSelectedIndex = position
        } else {
            val firstCard = cards[firstSelectedIndex!!]
            if (firstCard.drawable == card.drawable) {
                score += 1
                firstCard.isMatched = true
                card.isMatched = true
                firstSelectedIndex = null
                scoreText.text = "Score: $score"
            } else {
                recyclerView.postDelayed({
                    firstCard.isFlipped = false
                    card.isFlipped = false
                    score -= 1
                    adapter.notifyItemChanged(firstSelectedIndex!!)
                    adapter.notifyItemChanged(position)
                    firstSelectedIndex = null
                    scoreText.text = "Score: $score"
                }, 250) // Delay to allow user to see second card before flipping back
            }
        }

        if(cards.all { it.isMatched }){
            android.app.AlertDialog.Builder(this@MainActivity)
                .setTitle("YOU WIN🎉🎉")
                .setMessage("Do you want to play again?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, which ->
                    recreate()
                }
                .setNegativeButton("No") { dialog, which ->
                    finishAffinity()
                }
                .show()
        }
    }
}