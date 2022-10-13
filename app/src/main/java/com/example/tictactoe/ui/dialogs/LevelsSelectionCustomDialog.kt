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
import com.example.tictactoe.databinding.LevelsDialogBinding
import com.example.tictactoe.model.SelectedLevel


class LevelsSelectionCustomDialog(context: Context, private val selectedLevel:MutableLiveData<SelectedLevel?>) : Dialog(context,R.style.PauseDialog), View.OnClickListener {

    private lateinit var binding: LevelsDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LevelsDialogBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.easyImageView.setOnClickListener(this)
        binding.normalImageView.setOnClickListener(this)
        binding.hardImageView.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        dismiss()
        when (v) {
            binding.easyImageView -> {
                selectedLevel.postValue(SelectedLevel.EASY)
            }
            binding.normalImageView -> {
                selectedLevel.postValue(SelectedLevel.NORMAL)
            }
            binding.hardImageView -> {
                selectedLevel.postValue(SelectedLevel.HARD)
            }
            else -> {}
        }
    }
}