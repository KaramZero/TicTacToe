package com.example.tictactoe.ui.play_online.online_game

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
import com.example.tictactoe.databinding.FragmentWithPcGameBinding
import com.example.tictactoe.game_logics.Music
import com.example.tictactoe.model.GameState
import com.example.tictactoe.ui.MyAnimator
import com.example.tictactoe.ui.play_with_pc.WithPcGameFragmentArgs
import com.example.tictactoe.ui.play_with_pc.WithPcGameFragmentDirections
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class OnlineGameFragment : Fragment() {


    private val animator: MyAnimator by inject()
    private val music: Music by inject()
    private val viewModel: OnlineGameFragmentViewModel by viewModel()

    private var _binding: FragmentWithPcGameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWithPcGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadButtons(binding.playingBoard, onButtonClickedListener)

        val args = OnlineGameFragmentArgs.fromBundle(requireArguments())
        viewModel.initiateMyChar(char = args.playingChar, friendId = args.friendId)

        var turn = "Your ${getString(R.string.turn)}"
        binding.nowTurnTextView.text = turn

        if (args.playingChar == "O") {
            binding.blockingLayout.visibility = View.VISIBLE
            turn = "PC ${getString(R.string.turn)}"
            binding.nowTurnTextView.text = turn
        }

        binding.musicImageView.setOnClickListener {
            music.switchPlayingState(view = it)
        }

        viewModel.myMove.observe(viewLifecycleOwner) {

            if (it != null) {
                it.setImageResource(viewModel.myResourceID)
                animator.animateScale(it)
                binding.blockingLayout.visibility = View.VISIBLE
                turn = "PC ${getString(R.string.turn)}"
                binding.nowTurnTextView.text = turn
                viewModel.clearFriendMove()
            }
        }

        viewModel.pcMove.observe(viewLifecycleOwner) {
            if (it != null) {
                it.setImageResource(viewModel.pcResourceID)
                animator.animateScale(it)
                binding.blockingLayout.visibility = View.INVISIBLE
                turn = "Your ${getString(R.string.turn)}"
                binding.nowTurnTextView.text = turn
                viewModel.clearFriendMove()
            }
        }

        viewModel.win.observe(viewLifecycleOwner) {

            handleGameState(gameState = it, view = view)

        }

    }

    private fun handleGameState(gameState: GameState?, view: View) {
        when (gameState) {
            GameState.YOU_WIN -> Toast.makeText(requireContext(), "you win", Toast.LENGTH_SHORT)
                .show()

            GameState.YOU_LOSE -> Toast.makeText(requireContext(), "friend wins", Toast.LENGTH_SHORT)
                .show()

            GameState.EVEN -> Toast.makeText(requireContext(), "It's Even", Toast.LENGTH_SHORT)
                .show()

            else -> {}

        }
        binding.blockingLayout.visibility = View.INVISIBLE
        requireActivity().onBackPressed()

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