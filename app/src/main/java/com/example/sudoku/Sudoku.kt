package com.example.sudoku

import android.util.Log

class Sudoku(plocha: String?) {
   // val plocha: String

    val gameState = mutableListOf<MutableList<Char>>()
    val wrong =  mutableSetOf<Int>()
    init {
        var lines = plocha?.lines()
        lines?.forEach{gameState.add(it.replace(" ", "").toMutableList())}
    }

    fun findCoord(id:Int):Pair<Int,Int>{
        val i = id-10000
        var index = 0
        for (x in 0..8){
            for (y in 0..8){
                if (i == index){
                    return Pair(x, y)
                }
                else{
                    index++
                }
            }
        }
        return Pair(0, 0)
    }

    fun getId(xy:Pair<Int,Int>):Int{
        var index = 0
        for (x in 0..8){
            for (y in 0..8){

                if (x == xy.first && y == xy.second){
                    return index + 10000
                }
                index++
            }
        }
        return 0
    }

    fun getState(id:Int):String{
        val xy = findCoord(id)
        return gameState[xy.first][xy.second].toString()

    }
    fun setState(id:Int, state:String){
        val xy = findCoord(id)
        gameState[xy.first][xy.second] = state.toCharArray()[0]
    }
    fun checkRow(){

        val visited = mutableListOf<Char>()
        val mistake = mutableListOf<Char>()
       // val xy = findCoord(id)
        for (x in 0..8){
            for (y in 0..8){
                val state = gameState[x][y]
                if (!visited.contains(state) && state != '0'){
                    visited.add(state)
                }else{
                    mistake.add(state)
                }
            }
            for (y in 0..8) {
                val state = gameState[x][y]
                if (mistake.contains(state)&& state != '0'){
                    wrong.add(getId(Pair(x,y)))
                }
            }
            mistake.clear()
            visited.clear()
        }


    }

    fun check(){
        wrong.clear()
        checkCol()
        checkRow()
        checkBox()
    }

    fun finished():Boolean{
        if (wrong.isNotEmpty()) return false
        for (x in 0..8){
            for (y in 0..8){
                if (gameState[x][y] == '0') return false
            }
        }
        return true
    }

    fun checkBox(){
        InBox( 0, 0)
        for (x in 0 until 8 step 3) {
            for (y in 0 until 8 step 3) {
                Log.d("CheckB", "$x + $y")
                InBox( x, y)
            }
        }
    }

    fun InBox(startRow: Int, startCol: Int){
        val visited = mutableListOf<Char>()
        val mistake = mutableListOf<Char>()

        for (row in 0..2) {
            for (col in 0..2) {
                val state = gameState[row + startRow][col + startCol]

                if (visited.contains(state)){
                    mistake.add(state)
                }

                if (state != '0'){
                    visited.add(state)
                }
            }
        }
        for (x in 0..2) {
            for (y in 0..2) {
                val state = gameState[x + startRow][y + startCol]
                if (mistake.contains(state)&& state != '0'){
                    wrong.add(getId(Pair(x + startRow,y + startCol)))
                }
            }
            }

        }



    fun checkCol(){
        val visited = mutableListOf<Char>()
        val mistake = mutableListOf<Char>()
        // val xy = findCoord(id)
        for (y in 0..8){
            for (x in 0..8){
                val state = gameState[x][y]
                if (!visited.contains(state) && state != '0'){
                    visited.add(state)
                }else{
                    mistake.add(state)
                }
            }
            for (x in 0..8) {
                val state = gameState[x][y]
                if (mistake.contains(state)&& state != '0'){
                    wrong.add(getId(Pair(x,y)))
                }
                }
                mistake.clear()
                visited.clear()
        }
    }
}