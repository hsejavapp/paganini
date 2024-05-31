package ru.hse.myapplication

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.hse.myapplication.ui.theme.MyApplicationTheme
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.toColor
import org.checkerframework.checker.nullness.compatqual.NullableType

import ru.hse.myapplication.Letters
//import ru.hse.myapplication.Game
import ru.hse.myapplication.words.getCollisions
import ru.hse.myapplication.words.getWord


class GameActivity : ComponentActivity() {
    private var buffer: StringBuffer = StringBuffer(5)
    private lateinit var rectangles: List<TextView>
    private var attemptCnt: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val btnExit = findViewById<Button>(R.id.btnExit)
        val btnDelete = findViewById<ImageButton>(R.id.btnDelete)
        rectangles = listOf(
            findViewById<TextView>(R.id.rectangle11),
            findViewById<TextView>(R.id.rectangle12),
            findViewById<TextView>(R.id.rectangle13),
            findViewById<TextView>(R.id.rectangle14),
            findViewById<TextView>(R.id.rectangle15),

            findViewById<TextView>(R.id.rectangle21),
            findViewById<TextView>(R.id.rectangle22),
            findViewById<TextView>(R.id.rectangle23),
            findViewById<TextView>(R.id.rectangle24),
            findViewById<TextView>(R.id.rectangle25),

            findViewById<TextView>(R.id.rectangle31),
            findViewById<TextView>(R.id.rectangle32),
            findViewById<TextView>(R.id.rectangle33),
            findViewById<TextView>(R.id.rectangle34),
            findViewById<TextView>(R.id.rectangle35),

            findViewById<TextView>(R.id.rectangle41),
            findViewById<TextView>(R.id.rectangle42),
            findViewById<TextView>(R.id.rectangle43),
            findViewById<TextView>(R.id.rectangle44),
            findViewById<TextView>(R.id.rectangle45),

            findViewById<TextView>(R.id.rectangle51),
            findViewById<TextView>(R.id.rectangle52),
            findViewById<TextView>(R.id.rectangle53),
            findViewById<TextView>(R.id.rectangle54),
            findViewById<TextView>(R.id.rectangle55),

            findViewById<TextView>(R.id.rectangle61),
            findViewById<TextView>(R.id.rectangle62),
            findViewById<TextView>(R.id.rectangle63),
            findViewById<TextView>(R.id.rectangle64),
            findViewById<TextView>(R.id.rectangle65),

            )
        getWord()
        processButtons()

        btnExit.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        btnDelete.setOnClickListener {
            clickDeleteButton()
        }
    }

    private fun processButtons() {
        val rootView: View = findViewById(android.R.id.content)
        // empty initialization of allButtons due to recursive findButtons
        val allButtons = mutableListOf<Button>()
        findButtons(rootView, allButtons)
        for (btn in allButtons) {
            createOnclickAction(btn)
        }
    }

    private fun findButtons(view: View, allButtons: MutableList<Button>) {
        if (view is Button) {
            allButtons.add(view)
        } else if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val childView = view.getChildAt(i)
                findButtons(childView, allButtons)
            }
        }
    }

    private fun createOnclickAction(btn: Button) {
        if (btn.text in Letters.LETTER_LIST) {
            btn.setOnClickListener {
                println("Button ${btn.text} clicked")
                clickLetterProcess(btn)
            }
        }
    }

    private fun clickLetterProcess(btn: Button) {
        val letter = btn.text.firstOrNull()
        if (letter != null)
            buffer.append(letter)
        retextRectangle(btn.text.toString())
        if (buffer.length == 5) {
            val collisions = getCollisions(buffer)
            if (collisions == null || collisions == listOf<Int>()) {
                // wrong word
                // тут нужно будет поднимать всплывающее окно
                val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(600, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    vibrator.vibrate(600)
                }
                for (i in 0 until 5)
                    clickDeleteButton()
                return
            }
            attemptCnt++
            paintRectangles(collisions.toList())
            buffer = StringBuffer()
        }
        println(buffer)
    }

    private fun clickDeleteButton() {
        if (buffer.isEmpty()) {
            return
        }
        retextRectangle("")
        buffer = buffer.deleteCharAt(buffer.length - 1)
    }

    private fun retextRectangle(letter: String) {
        val bufLength = buffer.length
        rectangles[attemptCnt * 5 + bufLength - 1].text = letter
    }

    private fun paintRectangles(collisions: List<Int>) {
        println(collisions)
        for (i in 0 until 5) {
            if (collisions[i] == 2)
                rectangles[(attemptCnt - 1) * 5 + i].setBackgroundColor(Color.Green.toArgb())
            if (collisions[i] == 1)
                rectangles[(attemptCnt - 1) * 5 + i].setBackgroundColor(Color.Yellow.toArgb())
        }
        if (collisions == listOf(2, 2, 2, 2, 2)) {
            // тут нужно поднимать окошко о победе
            {}
        }
    }
}
