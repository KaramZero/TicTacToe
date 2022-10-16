package com.example.tictactoe.ui.play_with_pc

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
import com.example.tictactoe.databinding.FragmentPlaynigGameBinding
import com.example.tictactoe.game_logics.Music
import com.example.tictactoe.model.GameState
import com.example.tictactoe.model.GameState.*
import com.example.tictactoe.ui.MyAnimator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class WithPcGameFragment : Fragment() {

    private var _binding: FragmentPlaynigGameBinding? = null
    private val binding get() = _binding!!

    private val animator: MyAnimator by inject()
    private val music: Music by inject()
    private val viewModel: WithPcGameViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPlaynigGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadButtons(binding.playingBoard, onButtonClickedListener)

        val args = WithPcGameFragmentArgs.fromBundle(requireArguments())
        viewModel.initiateMyChar(args.startChar, args.level)

        var turn = getString(R.string.your_turn)
        binding.nowTurnTextView.text = turn

        binding.musicImageView.setOnClickListener {
            music.switchPlayingState(view = it)
        }

        viewModel.myMove.observe(viewLifecycleOwner) {
            it.setImageResource(viewModel.myResourceID)
            animator.animateScale(it)
            binding.blockingLayout.visibility = View.VISIBLE
            turn = getString(R.string.pc_turn)
            binding.nowTurnTextView.text = turn
        }

        viewModel.pcMove.observe(viewLifecycleOwner) {
            it.setImageResource(viewModel.pcResourceID)
            animator.animateScale(it)
            binding.blockingLayout.visibility = View.INVISIBLE
            turn = getString(R.string.your_turn)
            binding.nowTurnTextView.text = turn
        }

        viewModel.win.observe(viewLifecycleOwner) {

            handleGameState(gameState = it, view = view)

        }

    }

    private fun handleGameState(gameState: GameState?, view: View) {
        when (gameState) {
            YOU_WIN -> Toast.makeText(requireContext(), getString(R.string.you_win), Toast.LENGTH_SHORT).show()

            YOU_LOSE -> Toast.makeText(requireContext(), getString(R.string.pc_wins), Toast.LENGTH_SHORT).show()

            EVEN -> Toast.makeText(requireContext(), getString(R.string.even), Toast.LENGTH_SHORT).show()

            else -> {}
        }
        binding.blockingLayout.visibility = View.INVISIBLE
        view.findNavController()
            .navigate(WithPcGameFragmentDirections.actionGameFragmentToWelcomeFragment())
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