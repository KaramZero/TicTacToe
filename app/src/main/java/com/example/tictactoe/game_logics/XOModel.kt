/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.tictactoe.game_logics

object XOModel {


    fun getPlayingBoard(): Array<CharArray>{
        return  arrayOf(charArrayOf('_', '_', '_'), charArrayOf('_', '_', '_'), charArrayOf('_', '_', '_'))
    }
    //check any player win 
    fun checkWin(myBoard: Array<CharArray>): Boolean {
        var result = false
        for (i in 0..2) {
            // check rows
            if (myBoard[i][0] != '_' && myBoard[i][0] == myBoard[i][1] && myBoard[i][0] == myBoard[i][2]) {
                result = true
                break
            }
            // check col
            if (myBoard[0][i] != '_' && myBoard[0][i] == myBoard[1][i] && myBoard[0][i] == myBoard[2][i]) {
                result = true
                break
            }
        }
        // check x
        if (myBoard[0][0] != '_' && myBoard[0][0] == myBoard[1][1] && myBoard[0][0] == myBoard[2][2]) result =
            true
        if (myBoard[0][2] != '_' && myBoard[0][2] == myBoard[1][1] && myBoard[0][2] == myBoard[2][0]) result =
            true
        return result
    }

    fun isThereAPlaceToPlay(board: Array<CharArray>): Boolean {
        var result = false
        var i = 0
        while (i < 3 && !result) {
            for (j in 0..2) {
                if (board[i][j] == '_') {
                    result = true
                }
            }
            i++
        }
        return result
    }
}