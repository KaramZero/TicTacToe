package com.example.tictactoe.ui.play_with_friend

import android.view.View
import android.widget.ImageView
import com.example.tictactoe.game_logics.Music
import com.example.tictactoe.game_logics.XOModel
import com.example.tictactoe.model.GameState
import com.example.tictactoe.model.Move
import com.example.tictactoe.ui.play_with_pc.WithPcGameViewModel

class WithFriendGameViewModel(music: Music) : WithPcGameViewModel(music) {

    private var myWinState = GameState.X_WIN
    private var pcWinState = GameState.O_WIN

    fun initiateMyChar(char: String) {
        if (char == "O"){
            myWinState = GameState.O_WIN
            pcWinState = GameState.X_WIN
        }
        initiateMyChar(char = char,1)
    }

    override fun played(view: View) {

        val myMove = getMyMove(view)

        if (myBoard[myMove.row][myMove.col] == '_') {
            if (myTurn) {
                moveMe(view, myMove)
            } else {
                moveFriend(view, myMove)
            }
        }
    }

    private fun moveFriend(view: View, myMove: Move) {
        myTurn = true
        _pcMove.postValue(view as ImageView?)
        myBoard[myMove.row][myMove.col] = pcChar

        if (XOModel.checkWin(myBoard)) {
            _win.postValue(pcWinState)
        } else if (!XOModel.isThereAPlaceToPlay(myBoard)) {
            _win.postValue(GameState.EVEN)
        }
    }

    private fun moveMe(view: View, myMove: Move) {
        myTurn = false
        _myMove.postValue(view as ImageView?)
        myBoard[myMove.row][myMove.col] = myChar

        if (XOModel.checkWin(myBoard)) {
            _win.postValue(myWinState)
        } else if (!XOModel.isThereAPlaceToPlay(myBoard)) {
            _win.postValue(GameState.EVEN)
        }
    }

}