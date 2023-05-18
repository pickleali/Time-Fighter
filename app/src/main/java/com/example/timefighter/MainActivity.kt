package com.example.timefighter

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tapBtn: Button
    private lateinit var scoreText: TextView
    private lateinit var timeText: TextView

    private var score: Int = 0
    private var isGameStarted: Boolean = false
    private lateinit var initiateTimer: CountDownTimer

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

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeft = savedInstanceState.getLong(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }

        tapBtn.setOnClickListener { view ->
            val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
            view.startAnimation(bounceAnimation)
            incrementScore()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)

        // menuInflater is an existing property from the parent class 'AppCompatActivity'
        // which is used to instanciate menus from XML files
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.aboutSection) {
            showInfo()
        }
        return true
    }

    private fun showInfo() {
        val dialogTitle = getString(R.string.about_title, BuildConfig.VERSION_NAME)
        val dialogMessage = getString(R.string.about_message)

        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.create().show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstance called")
        outState.putInt(SCORE_KEY, score)
        outState.putLong(TIME_LEFT_KEY, timeLeft)
        initiateTimer.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called")
    }

    private fun startGame(timer: CountDownTimer) {
        isGameStarted = true
        timer.start()
    }

    private fun finishGame() {
        Toast.makeText(this, getString(R.string.done_text, score), Toast.LENGTH_SHORT).show()
        resetGame()

    }

    private fun restoreGame() {
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
                finishGame()
            }
        }
        startGame(continueTimer)
    }

    private fun resetGame() {
        score = 0
        scoreText.text = getString(R.string.score_text, score)
        val timeLeftInSeconds = initialCountTime / 1000
        timeText.text = getString(R.string.time_text, timeLeftInSeconds)
        initiateTimer = object : CountDownTimer(initialCountTime, countTimeInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                isGameStarted = true
                timeText.text = getString(R.string.time_text, millisUntilFinished / 1000)
            }

            override fun onFinish() {
                finishGame()
            }
        }
        isGameStarted = false
    }

    private fun incrementScore() {
        if (!isGameStarted) {
            startGame(initiateTimer)
        }
        score++
        scoreText.text = getString(R.string.score_text, score)
        val blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink)
        scoreText.startAnimation(blinkAnimation)
    }
}