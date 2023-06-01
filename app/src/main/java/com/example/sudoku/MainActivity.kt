package com.example.sudoku

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_maindynamic.*
import java.io.*

class MainActivity : AppCompatActivity() {
    var prew = 0
    var game = Sudoku(null)

    fun randomFile():Int{
        return mutableListOf(
            R.raw.s01a,
            R.raw.s01b,
            R.raw.s01c,
            R.raw.s02a,
            R.raw.s02b
            ).shuffled()[0]

    }

    //https://rrtutors.com/tutorials/how-do-i-read-a-text-file-in-android-using-kotlin
    fun read():String{
        var string: String? = ""
        var sudoku = ""
        val stringBuilder = StringBuilder()
        val `is`: InputStream = this.resources.openRawResource(randomFile())
        val reader = BufferedReader(InputStreamReader(`is`))
        while (true) {
            try {
                if (reader.readLine().also { string = it } == null) break
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (string != ""){
                stringBuilder.append(string).append("\n")
            }
            sudoku = stringBuilder.toString()
        }
        `is`.close()
        return sudoku.dropLast(1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maindynamic)
        game = Sudoku(read())
        val SIZE = 3
        val bigGrid = GridLayout(this)
        bigGrid.columnCount = SIZE
        bigGrid.rowCount = SIZE
        bigGrid.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        for (rowb in 0 until bigGrid.rowCount) {
            for (colb in 0 until bigGrid.columnCount) {
                val smallGrid = GridLayout(this)
                smallGrid.columnCount = SIZE
                smallGrid.rowCount = SIZE
                val margin = resources.getDimension(R.dimen.boxmargin).toInt()
                val smallGridParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                smallGridParams.leftMargin = margin
                smallGridParams.rightMargin = margin
                smallGridParams.topMargin = margin
                smallGridParams.bottomMargin = margin
                smallGrid.layoutParams = smallGridParams

                for (row in 0 until smallGrid.rowCount) {
                    for (col in 0 until smallGrid.columnCount) {
                        val button = Button(this)

                        button.setBackgroundColor(Color.WHITE)
                        button.id = 10000 + 3*3*3*rowb + 3*3*row + 3*colb + col
                        button.text = game.getState(button.id)
                        if (game.getState(button.id) != "0"){
                            button.setBackgroundColor(Color.LTGRAY)
                            button.tag = false
                        }else{
                            button.tag = true
                        }
                        //button.tag = "row" + ";" +"col"
                        val buttonSize = resources.getDimension(R.dimen.buttonSize).toInt()
                        val buttonParams = ViewGroup.MarginLayoutParams(buttonSize,buttonSize)
                        val bmargin = resources.getDimension(R.dimen.buttonmargin).toInt()
                        buttonParams.leftMargin = bmargin
                        buttonParams.rightMargin = bmargin
                        buttonParams.topMargin = bmargin
                        buttonParams.bottomMargin = bmargin
                        button.layoutParams = buttonParams
                        button.setOnClickListener { v -> onClick(v) }
                        smallGrid.addView(button)
                    }
                }
                bigGrid.addView(smallGrid)
            }
        }
        ll.addView(bigGrid)
    }

    fun onClick(v: View) {
        Log.d("SUDOKU", "clicked on ${v.id}")
        if (findViewById<Button>(v.id).tag == false) {
            prew = 0
            return
        }

        if (prew == 0){
            prew = v.id
        }else{
            findViewById<Button>(prew).setBackgroundColor(Color.WHITE)

            paintRed()
            prew = v.id
        }

        v.setBackgroundColor(Color.GREEN)
    }

    fun paintRed(){
        game.check()

        for (b in 10000..10080){
            if(findViewById<Button>(b).tag == false){
                findViewById<Button>(b).setBackgroundColor(Color.LTGRAY)
            }else{
                findViewById<Button>(b).setBackgroundColor(Color.WHITE)
            }

        }
        for (b in game.wrong){
            if(findViewById<Button>(b).tag == false){
                findViewById<Button>(b).setBackgroundColor(Color.GRAY)
            }else{
                findViewById<Button>(b).setBackgroundColor(Color.RED)
            }
        }
        if (game.finished()){
            Toast.makeText(this, "DOKONCENE", Toast.LENGTH_LONG).show()
            for (b in 10000..10080){
                findViewById<Button>(b).setBackgroundColor(Color.GREEN)
            }
        }
    }

    fun changeNumber(v: View){
        if (prew!=0){
            game.setState(findViewById<Button>(prew).id, findViewById<Button>(v.id).text as String)
            findViewById<Button>(prew).text = findViewById<Button>(v.id).text
        }
        paintRed()
    }
}