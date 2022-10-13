package com.example.tictactoe.ui.play_online.online_home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tictactoe.model.User
import com.example.tictactoe.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class OnlineFragmentViewModel : ViewModel() {

    private var user: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
    private var db: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var usersListRef: DatabaseReference = db.getReference("users")
    private var userRef: DatabaseReference = db.getReference("userData")


    private lateinit var onlineStatus: DatabaseReference
    private lateinit var connectedRef: DatabaseReference
    private lateinit var userListValueEventListener: ValueEventListener
    private lateinit var userRecordValueEventListener: ValueEventListener

    private val _userList = MutableLiveData<ArrayList<User>>()
    val userList: LiveData<ArrayList<User>> = _userList

    private val _myRecord = MutableLiveData<UserData>()
    val myRecord: LiveData<UserData> = _myRecord

    init {
        _userList.postValue(ArrayList())
    }

    fun startFireBase(){
        addMyRecord(user = user)
        listenToMyRecordChanges()

        addToUserList(user = user)
        populateUserList()
    }

    private fun addToUserList(user: FirebaseUser?) {

        usersListRef.child(user!!.uid).setValue(
            User(
                user.displayName, true,
                user.photoUrl?.toString(), user.uid
            )
        )

        onlineStatus = db.getReference("users/" + user.uid + "/onlineStatus")
        connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected")
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java)!!
                if (connected) {
                    onlineStatus.onDisconnect().setValue(false)
                    onlineStatus.setValue(true)
                } else {
                    onlineStatus.setValue(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun populateUserList() {

        userListValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //first check datasnap shot exist
                //then add all users except current/self user
                if (dataSnapshot.exists()) {
                    val list = ArrayList<User>()
                    var onlineIndex = 0
                    for (ds in dataSnapshot.children) {
                        if (ds.exists() && ds.key != user.uid) {
                            val name = ds.child("name").getValue(String::class.java)!!
                            val onlineStatus =
                                ds.child("onlineStatus").getValue(Boolean::class.java)!!
                            val photoUrl = ds.child("photoUrl").getValue(String::class.java)!!
                            val id = ds.child("id").getValue(String::class.java)!!

                            list.add(onlineIndex, User(name, onlineStatus, photoUrl, id))

                            if (onlineStatus)
                                onlineIndex++

                        }
                    }
                    _userList.postValue(list)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        usersListRef.addValueEventListener(userListValueEventListener)
    }

    private fun addMyRecord(user: FirebaseUser?) {
        userRef.child(user!!.uid).setValue(
            UserData()
        )
    }

    fun inviteUser(userId:String){
        db.getReference("userData/$userId").child(UserData.INVITE).setValue(user.uid)
    }

    fun acceptUserInvitation(userId: String){
        db.getReference("userData/$userId").child(UserData.WHO_ACCEPTED).setValue(user.uid)
    }

    private fun listenToMyRecordChanges() {
        userRecordValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //first check datasnap shot exist
                //then add all users except current/self user
                if (dataSnapshot.exists()) {
                    val ds = dataSnapshot.child(user.uid)
                    val invite = ds.child(UserData.INVITE).getValue(String::class.java)!!
                    val whoAccepted =
                        ds.child(UserData.WHO_ACCEPTED).getValue(String::class.java)!!

                    if (invite != "null" || whoAccepted != "null"){
                        _myRecord.postValue(UserData(invite = invite, whoAccepted = whoAccepted))
                        addMyRecord(user)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }

        userRef.addValueEventListener(userRecordValueEventListener)
    }

    fun clearLiveData(){
        _myRecord.postValue(UserData())
    }

    fun pause(){
        onlineStatus.setValue(false)
        usersListRef.removeEventListener(userListValueEventListener)
        userRef.removeEventListener(userRecordValueEventListener)
    }

    fun resume(){
        onlineStatus.setValue(true)
        userRef.addValueEventListener(userRecordValueEventListener)
        usersListRef.addValueEventListener(userListValueEventListener)

    }
    override fun onCleared() {
        super.onCleared()
        onlineStatus.setValue(false)
        usersListRef.removeEventListener(userListValueEventListener)
        userRef.removeEventListener(userRecordValueEventListener)
    }


}