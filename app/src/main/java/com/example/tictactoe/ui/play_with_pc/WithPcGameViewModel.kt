package com.example.tictactoe.ui.play_with_pc

import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tictactoe.R
import com.example.tictactoe.databinding.PlayingBoardBinding
import com.example.tictactoe.game_logics.Music
import com.example.tictactoe.game_logics.XOModel
import com.example.tictactoe.game_logics.levels.EasyLevel
import com.example.tictactoe.game_logics.levels.HardLevel
import com.example.tictactoe.game_logics.levels.Level
import com.example.tictactoe.game_logics.levels.NormalLevel
import com.example.tictactoe.model.GameState
import com.example.tictactoe.model.Move
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


open class WithPcGameViewModel constructor(private val music:Music) : ViewModel() {

    protected val myBoard = XOModel.getPlayingBoard()

    protected var xo = ArrayList<ArrayList<ImageView>>()
    protected var myTurn = true


    private lateinit var pc: Level

    var myChar = 'X'
    var pcChar = 'O'

    protected var _myResourceID = R.drawable.x
    val myResourceID get() = _myResourceID

    protected var _pcResourceID = R.drawable.o
    val pcResourceID get() = _pcResourceID

    protected val _pcMove = MutableLiveData<ImageView>()
    val pcMove: LiveData<ImageView> = _pcMove

    protected val _myMove = MutableLiveData<ImageView>()
    val myMove: LiveData<ImageView> = _myMove

    protected val _win = MutableLiveData<GameState>()
    val win: LiveData<GameState> = _win

    init {
        music.start()
    }
    fun initiateMyChar(char: String, level: Int) {
        if (char == "O") {
            myChar = 'O'
            _myResourceID = R.drawable.o

            pcChar = 'X'
            _pcResourceID = R.drawable.x
        }
        when (level) {
            1 -> pc = EasyLevel()
            2 -> pc = NormalLevel()
            3 -> pc = HardLevel()
        }
    }

    fun loadButtons(binding: PlayingBoardBinding, onButtonClickedListener: OnClickListener) {
        var xoArray = ArrayList<ImageView>()
        xo = ArrayList()

        xoArray.add(binding.xoImageView1)
        xoArray.add(binding.xoImageView2)
        xoArray.add(binding.xoImageView3)
        xo.add(xoArray)

        xoArray = ArrayList()
        xoArray.add(binding.xoImageView4)
        xoArray.add(binding.xoImageView5)
        xoArray.add(binding.xoImageView6)
        xo.add(xoArray)

        xoArray = ArrayList()
        xoArray.add(binding.xoImageView7)
        xoArray.add(binding.xoImageView8)
        xoArray.add(binding.xoImageView9)
        xo.add(xoArray)

        for (array in xo) {
            for (element in array)
                element.setOnClickListener(onButtonClickedListener)
        }

    }

    open fun played(view: View) {

        val myMove = getMyMove(view)

        if (myBoard[myMove.row][myMove.col] == '_') {

            _myMove.postValue(view as ImageView?)
            myBoard[myMove.row][myMove.col] = myChar
            myTurn = false

            if (XOModel.checkWin(myBoard)) {
                _win.postValue(GameState.YOU_WIN)
            } else if (!XOModel.isThereAPlaceToPlay(myBoard)) {
                _win.postValue(GameState.EVEN)
            } else {
                viewModelScope.launch(Dispatchers.IO) {
                    withContext(Dispatchers.IO) {
                        Thread.sleep(500)
                    }
                    makePcMove()
                    myTurn = true
                }
            }
        }
    }

    fun reDraw(){
        for (i in 0..2){
            for (j in 0..2){
                if (myBoard[i][j] == myChar){
                    xo[i][j].setImageResource(myResourceID)
                }else if (myBoard[i][j] == pcChar){
                    xo[i][j].setImageResource(pcResourceID)
                }
            }
        }
        if (myTurn){
            _pcMove.postValue(_pcMove.value)
        }else{
            _myMove.postValue(_myMove.value)
        }
    }

    fun setBackgroundResource(view: View){
        music.setBackgroundResource(view)
    }
    fun switchPlayingState(view :View){
        music.switchPlayingState(view)
    }
    private fun makePcMove() {
        val pcMove = pc.findMove(myBoard, pcChar)
        myBoard[pcMove.row][pcMove.col] = pcChar
        _pcMove.postValue(xo[pcMove.row][pcMove.col])

        if (XOModel.checkWin(myBoard)) {
            _win.postValue(GameState.YOU_LOSE)
        } else if (!XOModel.isThereAPlaceToPlay(myBoard)) {
            _win.postValue(GameState.EVEN)
        }
    }

    protected fun getMyMove(view: View): Move {
        val move = Move()
        for (i in 0..2) {
            for (j in 0..2)
                if (xo[i][j] == view) {
                    move.row = i
                    move.col = j
                }
        }
        return move
    }

    override fun onCleared() {
        super.onCleared()
        music.pause()
    }
}