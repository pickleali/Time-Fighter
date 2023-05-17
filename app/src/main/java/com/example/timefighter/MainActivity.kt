package com.example.timefighter

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tapBtn: Button
    private lateinit var scoreText: TextView
    private lateinit var timeText: TextView
    private var score: Int = 0
    private var isGameStarted: Boolean = false
    private var initialCountTime: Long = 10000
    private var countTimeInterval: Long = 1000
    private var timeLeft: Long = 10000

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tapBtn = findViewById(R.id.tapBtn)
        scoreText = findViewById(R.id.scoreTextView)
        timeText = findViewById(R.id.timeTextView)

        timeText.text = getString(R.string.time_text, 10)
        scoreText.text = getString(R.string.score_text, 0)

        fun startGame(timer: CountDownTimer) {
            if (!isGameStarted) {
                timer.start()
                isGameStarted = true
            }
        }

        fun finishGame() {
            Toast.makeText(this, getString(R.string.done_text, score), Toast.LENGTH_SHORT).show()
            score = 0
        }

        fun restoreGame() {
            scoreText.text = getString(R.string.score_text, score)
            val timeLeftInSeconds = timeLeft / 1000
            timeText.text = getString(R.string.time_text, timeLeftInSeconds)

            val continueTimer = object : CountDownTimer(timeLeft, countTimeInterval) {
                override fun onTick(millisUntilFinished: Long) {
                    timeLeft = millisUntilFinished
                    isGameStarted = true
                    timeText.text = getString(R.string.time_text, millisUntilFinished / 1000)
                }

                override fun onFinish() {
                    isGameStarted = false
                    finishGame()
                }
            }
            startGame(continueTimer)
        }

        fun resetGame() {
            score = 0
            val initiateTimer = object : CountDownTimer(initialCountTime, countTimeInterval) {
                override fun onTick(millisUntilFinished: Long) {
                    timeLeft = millisUntilFinished
                    isGameStarted = true
                    timeText.text = getString(R.string.time_text, millisUntilFinished / 1000)
                }

                override fun onFinish() {
                    isGameStarted = false
                    finishGame()
                }
            }
            startGame(initiateTimer)

        }

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeft = savedInstanceState.getLong(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }

        fun incrementScore() {
            score++
            scoreText.text = getString(R.string.score_text, score)
        }





        tapBtn.setOnClickListener {
            incrementScore()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstance called")
        outState.putInt(SCORE_KEY, score)
        outState.putLong(TIME_LEFT_KEY, timeLeft)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called")
    }
}