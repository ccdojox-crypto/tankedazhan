package com.latencytest.game

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import com.latencytest.game.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val handler = Handler(Looper.getMainLooper())
    private var reactionStartTime = 0L
    private var isWaitingForClick = false
    private var currentLevel = 1
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupGame()
    }

    private fun setupGame() {
        binding.targetButton.setOnClickListener {
            if (isWaitingForClick) {
                val reactionTime = SystemClock.elapsedRealtime() - reactionStartTime
                updateReactionDisplay(reactionTime)
                calculateScore(reactionTime)
                isWaitingForClick = false

                // 下一关
                handler.postDelayed({
                    startNextRound()
                }, 2000)
            }
        }

        binding.startButton.setOnClickListener {
            startGame()
        }

        binding.resetButton.setOnClickListener {
            resetGame()
        }

        updateUI()
    }

    private fun startGame() {
        currentLevel = 1
        score = 0
        updateUI()
        startNextRound()
    }

    private fun startNextRound() {
        val delay = (1000..3000).random() + (currentLevel * 200) // 随着关卡增加延迟增加

        binding.targetButton.isEnabled = false
        binding.targetButton.setBackgroundColor(getColor(R.color.waiting_color))
        binding.statusText.text = "准备... 等待目标出现!"

        handler.postDelayed({
            showTarget()
        }, delay.toLong())
    }

    private fun showTarget() {
        reactionStartTime = SystemClock.elapsedRealtime()
        isWaitingForClick = true
        binding.targetButton.isEnabled = true
        binding.targetButton.setBackgroundColor(getColor(R.color.target_color))
        binding.statusText.text = "点击目标!"

        // 超时处理
        handler.postDelayed({
            if (isWaitingForClick) {
                isWaitingForClick = false
                binding.statusText.text = "太慢了! 重新开始这一关"
                binding.targetButton.isEnabled = false
                binding.targetButton.setBackgroundColor(getColor(R.color.missed_color))

                handler.postDelayed({
                    startNextRound()
                }, 2000)
            }
        }, 5000)
    }

    private fun updateReactionDisplay(reactionTime: Long) {
        val latencyText = when {
            reactionTime < 200 -> "极速! $reactionTime ms"
            reactionTime < 300 -> "很快! $reactionTime ms"
            reactionTime < 400 -> "不错! $reactionTime ms"
            reactionTime < 500 -> "还行 $reactionTime ms"
            else -> "较慢 $reactionTime ms"
        }

        binding.reactionTimeText.text = latencyText
        binding.targetButton.setBackgroundColor(getColor(R.color.success_color))

        // 显示延迟等级
        val latencyLevel = when {
            reactionTime < 200 -> "S级"
            reactionTime < 250 -> "A级"
            reactionTime < 350 -> "B级"
            reactionTime < 500 -> "C级"
            else -> "D级"
        }
        binding.latencyGradeText.text = "延迟等级: $latencyLevel"
    }

    private fun calculateScore(reactionTime: Long) {
        val roundScore = when {
            reactionTime < 200 -> 100 * currentLevel
            reactionTime < 300 -> 80 * currentLevel
            reactionTime < 400 -> 60 * currentLevel
            reactionTime < 500 -> 40 * currentLevel
            else -> 20 * currentLevel
        }
        score += roundScore
        updateUI()
    }

    private fun updateUI() {
        binding.levelText.text = "关卡: $currentLevel"
        binding.scoreText.text = "得分: $score"
    }

    private fun resetGame() {
        isWaitingForClick = false
        currentLevel = 1
        score = 0
        handler.removeCallbacksAndMessages(null)

        binding.targetButton.isEnabled = false
        binding.targetButton.setBackgroundColor(getColor(R.color.default_color))
        binding.statusText.text = "点击开始游戏!"
        binding.reactionTimeText.text = ""
        binding.latencyGradeText.text = ""

        updateUI()
    }

    private fun advanceLevel() {
        currentLevel++
        updateUI()
    }
}