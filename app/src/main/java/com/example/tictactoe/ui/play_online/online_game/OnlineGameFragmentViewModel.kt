package com.example.tictactoe.ui.play_online.online_game

import android.view.View
import android.widget.ImageView
import com.example.tictactoe.game_logics.XOModel
import com.example.tictactoe.model.GameState
import com.example.tictactoe.model.Move
import com.example.tictactoe.model.UserData
import com.example.tictactoe.ui.play_with_pc.WithPcGameViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class OnlineGameFragmentViewModel : WithPcGameViewModel() {

    private var user: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
    private var db: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var myRef: DatabaseReference = db.getReference("userData")
    private lateinit var friendId: String

    private lateinit var userRecordValueEventListener: ValueEventListener



    fun initiateMyChar(char: String, friendId: String) {
        this.friendId = friendId
        initiateMyChar(char = char, 1)
        listenToMyRecordChanges()
    }

    override fun played(view: View) {
        val myMove = getMyMove(view)
        if (myBoard[myMove.row][myMove.col] == '_') {
            moveMe(view, myMove)
            sendMove(myMove)
        }
    }

    private fun sendMove(move: Move) {
        db.getReference("userData/$friendId").child(UserData.INCOMING_MOVE)
            .setValue("${move.row} ${move.col}")
    }

    private fun moveMe(view: View, myMove: Move) {
        _myMove.postValue(view as ImageView?)
        myBoard[myMove.row][myMove.col] = myChar

        if (XOModel.checkWin(myBoard)) {
            _win.postValue(GameState.YOU_WIN)
        } else if (!XOModel.isThereAPlaceToPlay(myBoard)) {
            _win.postValue(GameState.EVEN)
        }
    }

    private fun listenToMyRecordChanges() {
        userRecordValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()) {
                    val ds = dataSnapshot.child(user.uid)
                    val incomingMove = ds.child(UserData.INCOMING_MOVE).getValue(String::class.java)!!

                    if (incomingMove != "null"){
                       val move = Move(incomingMove[0].digitToInt(),incomingMove[2].digitToInt())
                        if (move.col == 5){
                            _win.postValue(GameState.FRIEND_QUITS)
                        }else{
                            myBoard[move.row][move.col] = pcChar
                            _pcMove.postValue(xo[move.row][move.col])
                            if (XOModel.checkWin(myBoard)) {
                                _win.postValue(GameState.YOU_LOSE)
                            } else if (!XOModel.isThereAPlaceToPlay(myBoard)) {
                                _win.postValue(GameState.EVEN)
                            }
                        }

                        db.getReference("userData/${user.uid}").child(UserData.INCOMING_MOVE).setValue("null")
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }

        myRef.addValueEventListener(userRecordValueEventListener)
    }

    fun quit(){
        db.getReference("userData/$friendId").child(UserData.INCOMING_MOVE)
            .setValue("5 5")
    }
    fun clearMyMove(){
        _myMove.postValue(null)
    }
    fun clearFriendMove(){
        _pcMove.postValue(null)
    }
    override fun onCleared() {
        super.onCleared()
        myRef.removeEventListener(userRecordValueEventListener)
    }
}