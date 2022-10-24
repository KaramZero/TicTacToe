package com.example.tictactoe.ui.play_online.online_game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tictactoe.R
import com.example.tictactoe.databinding.FragmentPlayingGameBinding
import com.example.tictactoe.model.GameState
import com.example.tictactoe.ui.MyAnimator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class OnlineGameFragment : Fragment() {


    private val animator: MyAnimator by inject()

    private val viewModel: OnlineGameFragmentViewModel by viewModel()
    private var endOfGame = false

    private var canPlay = true


    private var outState: Bundle? = null

    private var _binding: FragmentPlayingGameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPlayingGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        viewModel.pcMove.removeObservers(viewLifecycleOwner)
        viewModel.myMove.removeObservers(viewLifecycleOwner)
        viewModel.win.removeObservers(viewLifecycleOwner)
        _binding = null

        super.onDestroyView()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadButtons(binding.playingBoard, onButtonClickedListener)

        val args = OnlineGameFragmentArgs.fromBundle(requireArguments())

        var turn: String

        viewModel.setBackgroundResource(binding.musicImageView)

        binding.musicImageView.setOnClickListener {
            viewModel.switchPlayingState(view = it)
        }

        viewModel.myMove.observe(viewLifecycleOwner) {
            if (it != null) {
                it.setImageResource(viewModel.myResourceID)
                animator.animateScale(it)
            }
            turn = getString(R.string.friend_turn)
            binding.nowTurnTextView.text = turn
        }

        viewModel.pcMove.observe(viewLifecycleOwner) {
            if (it != null) {
                it.setImageResource(viewModel.pcResourceID)
                animator.animateScale(it)
            }
            canPlay = true
            turn = getString(R.string.your_turn)
            binding.nowTurnTextView.text = turn
        }

        viewModel.win.observe(viewLifecycleOwner) {
            handleGameState(gameState = it)
        }

        if (savedInstanceState != null) {
            viewModel.reDraw()
        } else {
            viewModel.initiateMyChar(args.playingChar, args.friendId)
            turn = getString(R.string.your_turn)
            binding.nowTurnTextView.text = turn

            if (args.playingChar == "O") {
                canPlay = false
                turn = getString(R.string.friend_turn)
                binding.nowTurnTextView.text = turn
            }
        }

    }

    private fun handleGameState(gameState: GameState?) {
        when (gameState) {
            GameState.YOU_WIN -> Toast.makeText(
                requireContext(),
                getString(R.string.you_win),
                Toast.LENGTH_SHORT
            )
                .show()

            GameState.YOU_LOSE -> Toast.makeText(
                requireContext(),
                getString(R.string.friend_wins),
                Toast.LENGTH_SHORT
            )
                .show()

            GameState.EVEN -> Toast.makeText(
                requireContext(),
                getString(R.string.even),
                Toast.LENGTH_SHORT
            )
                .show()
            GameState.FRIEND_QUITS -> Toast.makeText(
                requireContext(),
                getString(R.string.friend_quits),
                Toast.LENGTH_SHORT
            )
                .show()

            else -> {}
        }
        endOfGame = true
        requireActivity().onBackPressed()

    }

    private val onButtonClickedListener = OnClickListener {
        if (canPlay){
            canPlay = false
            viewModel.played(it)
        }
    }



    override fun onSaveInstanceState(outState: Bundle) {
        this.outState = outState
        super.onSaveInstanceState(outState)
    }

    override fun onDetach() {
        super.onDetach()
        if (!endOfGame && outState == null)
            viewModel.quit()
    }
}