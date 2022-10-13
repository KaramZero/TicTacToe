package com.example.tictactoe.ui.welcome

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.example.tictactoe.R
import com.example.tictactoe.databinding.FragmentWelcomeBinding
import com.example.tictactoe.model.PlayingMode.*
import com.example.tictactoe.model.SelectedChar
import com.example.tictactoe.model.SelectedLevel
import com.example.tictactoe.model.SelectedLevel.*
import com.example.tictactoe.ui.dialogs.CharSelectionCustomDialog
import com.example.tictactoe.ui.dialogs.LevelsSelectionCustomDialog
import com.example.tictactoe.ui.MyAnimator
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class WelcomeFragment : Fragment() {
    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    private val animator: MyAnimator by inject()

    private val selectedLevel: MutableLiveData<SelectedLevel?> = MutableLiveData()
    private val selectedChar: MutableLiveData<SelectedChar?> = MutableLiveData()

    private var playingChar = "X"
    private var playingMode = WITH_PC


    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code: Int = 123
    private lateinit var firebaseAuth: FirebaseAuth

    private val viewModel : WelcomeFragmentViewModel by viewModel()

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


        FirebaseApp.initializeApp(requireContext())

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        firebaseAuth = FirebaseAuth.getInstance()


        binding.onlineImageView.setOnClickListener {
            playingMode = ONLINE
            viewModel.goOnline(requireContext())
            val account = GoogleSignIn.getLastSignedInAccount(requireContext())
            if (account != null) {
                navigateToOnlineFragment()
            }else{
                Toast.makeText(requireContext(), "Logging In", Toast.LENGTH_SHORT).show()
                signInGoogle()
            }

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

    private fun signInGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Req_Code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Req_Code) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                updateUI(account)
            }
        } catch (e: ApiException) {
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navigateToOnlineFragment()
            }
        }
    }

    private fun navigateToOnlineFragment() {
        view?.findNavController()
            ?.navigate(WelcomeFragmentDirections.actionWelcomeFragmentToOnlineFragment())
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