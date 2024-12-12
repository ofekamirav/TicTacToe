package com.example.tictactoe

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var turnTextView: TextView
    lateinit var imageViews: Array<Array<Int>>
    lateinit var frameLayouts: Array<Array<Int>>
    val board = Array(3) { IntArray(3) }

    // 1 for X, -1 for O, 0 for empty
    var isXturn = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        turnTextView = findViewById(R.id.turn_text)

        frameLayouts = arrayOf(
            arrayOf(R.id.button00, R.id.button01, R.id.button02),
            arrayOf(R.id.button10, R.id.button11, R.id.button12),
            arrayOf(R.id.button20, R.id.button21, R.id.button22)
        )
        imageViews = arrayOf(
            arrayOf(R.id.image00, R.id.image01, R.id.image02),
            arrayOf(R.id.image10, R.id.image11, R.id.image12),
            arrayOf(R.id.image20, R.id.image21, R.id.image22)
        )

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize board
        for (i in 0..2) {
            for (j in 0..2) {
                board[i][j] = 0
                val imageView = findViewById<ImageView>(imageViews[i][j])
                imageView.setImageResource(R.drawable.ic_launcher_foreground)
            }
        }

        // Set onClickListeners
        for (i in 0..2) {
            for (j in 0..2) {
                val frameLayout = findViewById<FrameLayout>(frameLayouts[i][j])
                val imageView = findViewById<ImageView>(imageViews[i][j])
                frameLayout.setOnClickListener { handleOnClick(imageView, i, j) }
            }
        }
    }

    private fun handleOnClick(imageView: ImageView, row: Int, col: Int) {
        if (board[row][col] == 0) {
            if (isXturn) {
                imageView.setImageResource(R.drawable.x)
                board[row][col] = 1
            } else {
                imageView.setImageResource(R.drawable.circle)
                board[row][col] = -1
            }
            isXturn = !isXturn
            updateTurnText() // קריאה לפונקציה מעודכנת
        }
    }

    private fun checkWin(): Boolean {
        // ... (same as before)
        //row check
        for (i in 0..2) {
            if (board[i][0] != 0 && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return true
            }
        }

        // column check
        for (j in 0..2) {
            if (board[0][j] != 0 && board[0][j] == board[1][j] && board[1][j] == board[2][j]) {
                return true
            }
        }
        //Slant check
        if(board[0][0]==board[1][1] && board[1][1]==board[2][2])
            return true
        if(board[0][2]==board[1][1] && board[1][1]==board[2][0])
            return true


        return false
    }

    private fun checkDraw(): Boolean {
        for (i in 0..2)
            for (j in 0..2)
                if (board[i][j] == 0)
                    return false // יש מקום פנוי, אין תיקו
        return true // הלוח מלא, תיקו
    }

    private fun updateTurnText() {
        if (checkWin()) {
            val winner = if (isXturn) "O" else "X"
            turnTextView.text = "ניצחון ל-$winner!"
            GameOver("ניצחון ל-$winner!")
        } else if (checkDraw()) {
            turnTextView.text = "תיקו!"
            GameOver("תיקו!")
        } else {
            turnTextView.text = if (isXturn) "תור של X" else "תור של O"
        }
    }

    private fun GameOver(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("המשחק נגמר!")
        resetGame()
        builder.setMessage(message)

        builder.setPositiveButton("שחק שוב") { dialog, which ->
            updateTurnText()
        }

        builder.setNegativeButton("יציאה") { dialog, which ->
            finish() // סגירת האפליקציה
        }
        builder.setCancelable(false)//למנוע יציאה מהדיאלוג על ידי לחיצה על המסך
        builder.show()
    }

    private fun resetGame() {
        for (i in 0..2) {
            for (j in 0..2) {
                board[i][j] = 0
                val imageView = findViewById<ImageView>(imageViews[i][j])
                imageView.setImageResource(R.drawable.ic_launcher_foreground)
            }
        }
        isXturn = true
        updateTurnText();
    }
}