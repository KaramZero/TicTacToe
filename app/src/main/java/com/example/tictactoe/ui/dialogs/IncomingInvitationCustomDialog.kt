package com.example.tictactoe.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.lifecycle.MutableLiveData
import com.example.tictactoe.R
import com.example.tictactoe.databinding.InvitationDialogBinding
import com.example.tictactoe.model.InvitationResponse


class IncomingInvitationCustomDialog(context: Context, private val userName:String, private val mutableLiveData:MutableLiveData<InvitationResponse?>) : Dialog(context,R.style.PauseDialog), View.OnClickListener {

    private lateinit var binding: InvitationDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = InvitationDialogBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.invitationUserNameTextView.text = userName
        binding.okSelectionImageView.setOnClickListener(this)
        binding.cancelSelectionImageView.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        dismiss()
        when (v) {
            binding.okSelectionImageView -> {
                mutableLiveData.postValue(InvitationResponse.OK)
            }
            binding.cancelSelectionImageView -> {
                mutableLiveData.postValue(InvitationResponse.CANCEL)
            }
            else -> {}
        }
    }
}