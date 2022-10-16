package com.example.tictactoe.ui.play_online.online_home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tictactoe.databinding.FragmentOnlineBinding
import com.example.tictactoe.model.InvitationResponse
import com.example.tictactoe.model.InvitationResponse.CANCEL
import com.example.tictactoe.model.InvitationResponse.OK
import com.example.tictactoe.model.User
import com.example.tictactoe.ui.dialogs.IncomingInvitationCustomDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class OnlineFragment : Fragment(), OnlineFragmentCommunicator {


    private val viewModel: OnlineFragmentViewModel by viewModel()
    private lateinit var adapter: OnlineUsersListAdapter

    private val invitationResponse = MutableLiveData<InvitationResponse?>()
    private var incomingInvitationId = " "

    private var usersList = ArrayList<User>()

    private var _binding: FragmentOnlineBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOnlineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = OnlineUsersListAdapter(requireContext(), this)
        val llm = LinearLayoutManager(requireContext())
        llm.orientation = LinearLayoutManager.VERTICAL

        binding.onlineListRecycleView.layoutManager = llm
        binding.onlineListRecycleView.adapter = adapter


        viewModel.startFireBase()


        viewModel.userList.observe(viewLifecycleOwner) {
            usersList = it
            adapter.setData(it)
        }

        viewModel.myRecord.observe(viewLifecycleOwner) {
            if (it.invite != "null") {
                incomingInvitationId = it.invite
                var name = it.invite
                for (user in usersList)
                    if (user.id == it.invite)
                        name = user.name.toString()

                IncomingInvitationCustomDialog(
                    context = requireContext(),
                    userName = name, invitationResponse
                ).show()
                viewModel.clearLiveData()
            }
            if (it.whoAccepted != "null") {
                incomingInvitationId = it.whoAccepted
                navigateToOnlineGameFragment(view,"X")
                viewModel.clearLiveData()
            }
        }

        invitationResponse.observe(viewLifecycleOwner) {
            when (it) {
                OK -> {
                    viewModel.acceptUserInvitation(incomingInvitationId)
                    navigateToOnlineGameFragment(view, "O")
                    invitationResponse.postValue(null)
                }
                CANCEL -> {
                    invitationResponse.postValue(null)
                }
                null -> {}
            }
        }
    }

    private fun navigateToOnlineGameFragment(view: View, playingChar: String) {
        view.findNavController().navigate(
            OnlineFragmentDirections.actionOnlineFragmentToOnlineGameFragment(
                incomingInvitationId,
                playingChar
            )
        )
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onResume() {
        super.onResume()
        viewModel.resume()
    }

    override fun sendInvitation(userId: String) {
        viewModel.inviteUser(userId)
    }

}

interface OnlineFragmentCommunicator {
    fun sendInvitation(userId: String)
}