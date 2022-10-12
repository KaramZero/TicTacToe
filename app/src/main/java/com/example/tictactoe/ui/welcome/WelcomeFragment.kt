package com.example.tictactoe.ui.welcome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.example.tictactoe.databinding.FragmentWelcomeBinding
import com.example.tictactoe.model.PlayingMode
import com.example.tictactoe.model.PlayingMode.*
import com.example.tictactoe.model.SelectedChar
import com.example.tictactoe.model.SelectedLevel
import com.example.tictactoe.model.SelectedLevel.*
import com.example.tictactoe.ui.dialogs.CharSelectionCustomDialog
import com.example.tictactoe.ui.dialogs.LevelsSelectionCustomDialog
import com.example.tictactoe.ui.MyAnimator
import org.koin.android.ext.android.inject


class WelcomeFragment : Fragment() {
    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    private val animator: MyAnimator by inject()

    private val selectedLevel: MutableLiveData<SelectedLevel?> = MutableLiveData()
    private val selectedChar: MutableLiveData<SelectedChar?> = MutableLiveData()

    private var playingChar = "X"
    private var playingMode = WITH_PC

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        manageAnimation()

        binding.onlineImageView.setOnClickListener {
            playingMode = ONLINE
            Toast.makeText(requireContext(), "coming soon", Toast.LENGTH_SHORT).show()
        }

        binding.withFriendImageView.setOnClickListener {
            playingMode = WITH_FRIEND
            CharSelectionCustomDialog(requireContext(), selectedChar).show()
        }

        binding.withPcImageView.setOnClickListener {
            playingMode = WITH_PC
            CharSelectionCustomDialog(requireContext(), selectedChar).show()
        }

        selectedChar.observe(viewLifecycleOwner) {
            if (it != null) {
                playingChar = when (it) {
                    SelectedChar.X_CHAR -> {
                        "X"
                    }

                    SelectedChar.O_CHAR -> {
                        "O"
                    }
                }
                when(playingMode){
                    WITH_FRIEND -> navigateToWithFriendFragment(view = view, char = playingChar)
                    ONLINE -> {}
                    WITH_PC ->   LevelsSelectionCustomDialog(requireContext(), selectedLevel).show()
                }


                selectedChar.postValue(null)
            }
        }

        selectedLevel.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    EASY -> navigateToGameFragment(view = view, char = playingChar, level = 1)

                    NORMAL -> navigateToGameFragment(view = view, char = playingChar, level = 2)

                    HARD -> navigateToGameFragment(view = view, char = playingChar, level = 3)
                }
                selectedLevel.postValue(null)
            }

        }

    }

    private fun navigateToWithFriendFragment(view: View, char: String) {
        view.findNavController()
            .navigate(
                WelcomeFragmentDirections.actionWelcomeFragmentToWithFriendGameFragment(
                    char
                )
            )
    }

    private fun navigateToGameFragment(view: View, char: String, level: Int) {
        view.findNavController()
            .navigate(WelcomeFragmentDirections.actionWelcomeFragmentToGameFragment(char, level))
    }

    private fun manageAnimation() {
        animator.animateTranslationX(binding.xBackgroundImageView, -380f)
        animator.animateTranslationX(binding.oBackgroundImageView, 380f)

        animator.animateFading(binding.ticTacToeTextView)
        //    animator.animateTranslationY(binding.ticTacToeTextView, 1000f)

        animator.animateFading(binding.modesLayout)
        //   animator.animateTranslationY(binding.modesLayout, 1000f)

    }


}