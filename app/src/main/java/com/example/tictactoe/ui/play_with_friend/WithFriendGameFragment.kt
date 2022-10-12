package com.example.tictactoe.ui.play_with_friend

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.tictactoe.R
import com.example.tictactoe.databinding.FragmentWithFriendGameBinding
import com.example.tictactoe.game_logics.Music
import com.example.tictactoe.model.GameState
import com.example.tictactoe.ui.MyAnimator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class WithFriendGameFragment : Fragment() {
    private var _binding: FragmentWithFriendGameBinding? = null
    private val binding get() = _binding!!

    private val animator: MyAnimator by inject()
    private val music: Music by inject()
    private val viewModel: WithFriendGameViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWithFriendGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadButtons(binding.playingBoard, onButtonClickedListener)

        val args = WithFriendGameFragmentArgs.fromBundle(requireArguments())
        viewModel.initiateMyChar(args.startChar)

        var turn = "Your ${getString(R.string.turn)}"
        binding.nowTurnTextView.text = turn

        binding.musicImageView.setOnClickListener {
            music.switchPlayingState(view = it)
        }

        viewModel.myMove.observe(viewLifecycleOwner) {
            it.setImageResource(viewModel.myResourceID)
            animator.animateScale(it)
            turn = "${viewModel.pcChar}'s ${getString(R.string.turn)}"
            binding.nowTurnTextView.text = turn
        }

        viewModel.pcMove.observe(viewLifecycleOwner) {
            it.setImageResource(viewModel.pcResourceID)
            animator.animateScale(it)
            turn = "${viewModel.myChar}'s ${getString(R.string.turn)}"
            binding.nowTurnTextView.text = turn
        }

        viewModel.win.observe(viewLifecycleOwner) {
            handleGameState(gameState = it, view = view)
        }


    }

    private fun handleGameState(gameState: GameState?, view: View) {
        when (gameState) {
            GameState.X_WIN -> Toast.makeText(requireContext(), "X wins", Toast.LENGTH_SHORT).show()

            GameState.O_WIN -> Toast.makeText(requireContext(), "O wins", Toast.LENGTH_SHORT).show()

            GameState.EVEN -> Toast.makeText(requireContext(), "It's Even", Toast.LENGTH_SHORT)
                .show()

            else -> {}
        }
        view.findNavController()
            .navigate(WithFriendGameFragmentDirections.actionWithFriendGameFragmentToWelcomeFragment())
    }

    private val onButtonClickedListener = OnClickListener {
        viewModel.played(it)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        music.start()
    }

    override fun onDetach() {
        super.onDetach()
        music.pause()
    }

}