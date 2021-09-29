package com.donevsky.udacity.diceroller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.donevsky.udacity.diceroller.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.rollButton.setOnClickListener {
            rollDice()
        }
    }

    fun rollDice(){
        val randomInt = Random.nextInt(6)+1
        val drawable = when (randomInt){
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            6 -> R.drawable.dice_6
            else -> R.drawable.empty_dice
        }
        binding.diceImage.setImageResource(drawable)
    }
}