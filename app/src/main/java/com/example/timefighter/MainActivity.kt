package com.example.timefighter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    lateinit var tapBtn: Button
    lateinit var scoreText: TextView
    lateinit var timeText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tapBtn = findViewById(R.id.tapBtn)
        scoreText = findViewById(R.id.scoreText)
        timeText = findViewById(R.id.timeText)

        var counter = 0
        tapBtn.setOnClickListener {
            val timer = object: CountDownTimer(10000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timeText.setText("Time Left: ${millisUntilFinished / 1000}")
                }

                override fun onFinish() {
                    timeText.setText("DONE!")
                    counter = 0
                }
            }
            timer.start()
            counter++
            scoreText.setText("Your Score: $counter")
        }
    }
}