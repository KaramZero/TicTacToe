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
import com.example.tictactoe.databinding.PlayingCharDialogBinding
import com.example.tictactoe.model.SelectedChar


class CharSelectionCustomDialog(context: Context, private val selectedLevel:MutableLiveData<SelectedChar?>) : Dialog(context,R.style.PauseDialog), View.OnClickListener {

    private lateinit var binding: PlayingCharDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PlayingCharDialogBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));


        binding.xSelectionImageView.setOnClickListener(this)
        binding.oSelectionImageView.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        dismiss()
        when (v) {
            binding.xSelectionImageView -> {
                selectedLevel.postValue(SelectedChar.X_CHAR)
            }
            binding.oSelectionImageView -> {
                selectedLevel.postValue(SelectedChar.O_CHAR)
            }
            else -> {}
        }
    }
}