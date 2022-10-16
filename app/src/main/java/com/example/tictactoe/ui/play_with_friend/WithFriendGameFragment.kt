package com.example.tictactoe.ui.play_with_friend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.tictactoe.R
import com.example.tictactoe.databinding.FragmentPlayingGameBinding
import com.example.tictactoe.model.GameState
import com.example.tictactoe.ui.MyAnimator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class WithFriendGameFragment : Fragment() {

    private var _binding: FragmentPlayingGameBinding? = null
    private val binding get() = _binding!!

    private val animator: MyAnimator by inject()
    private val viewModel: WithFriendGameViewModel by viewModel()

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


        var turn: String

        viewModel.setBackgroundResource(binding.musicImageView)

        binding.musicImageView.setOnClickListener {
            viewModel.switchPlayingState(view = it)
        }

        viewModel.myMove.observe(viewLifecycleOwner) {
            if (it != null) {
                it.setImageResource(viewModel.myResourceID)
                animator.animateScale(it)
                turn = "${viewModel.pcChar} ${getString(R.string.turn)}"
                binding.nowTurnTextView.text = turn
            }
        }

        viewModel.pcMove.observe(viewLifecycleOwner) {
            if (it != null) {
                it.setImageResource(viewModel.pcResourceID)
                animator.animateScale(it)
                turn = "${viewModel.myChar} ${getString(R.string.turn)}"
                binding.nowTurnTextView.text = turn
            }
        }

        viewModel.win.observe(viewLifecycleOwner) {
            handleGameState(gameState = it, view = view)
        }

        viewModel.loadButtons(binding.playingBoard, onButtonClickedListener)

        if (savedInstanceState != null) {
            viewModel.reDraw()
        } else {
            val args = WithFriendGameFragmentArgs.fromBundle(requireArguments())
            viewModel.initiateMyChar(args.startChar)
        }

        turn = "${viewModel.myChar} ${getString(R.string.turn)}"
        binding.nowTurnTextView.text = turn
    }

    private fun handleGameState(gameState: GameState?, view: View) {
        when (gameState) {
            GameState.X_WIN -> Toast.makeText(
                requireContext(),
                getString(R.string.x_wins),
                Toast.LENGTH_SHORT
            ).show()

            GameState.O_WIN -> Toast.makeText(
                requireContext(),
                getString(R.string.o_wins),
                Toast.LENGTH_SHORT
            ).show()

            GameState.EVEN -> Toast.makeText(
                requireContext(),
                getString(R.string.even),
                Toast.LENGTH_SHORT
            )
                .show()

            else -> {}
        }
        view.findNavController()
            .navigate(WithFriendGameFragmentDirections.actionWithFriendGameFragmentToWelcomeFragment())
    }

    private val onButtonClickedListener = OnClickListener {
        viewModel.played(it)
    }


}