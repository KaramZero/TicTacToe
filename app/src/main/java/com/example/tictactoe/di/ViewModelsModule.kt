package com.example.tictactoe.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import com.example.tictactoe.ui.play_with_pc.WithPcGameViewModel
import com.example.tictactoe.ui.play_with_friend.WithFriendGameViewModel
import com.example.tictactoe.ui.welcome.WelcomeFragmentViewModel
import com.example.tictactoe.ui.play_online.online_home.OnlineFragmentViewModel
import com.example.tictactoe.ui.play_online.online_game.OnlineGameFragmentViewModel


var viewModelModule = module {
    viewModelOf(::WithPcGameViewModel)
    viewModelOf(::WithFriendGameViewModel)
    viewModelOf(::WelcomeFragmentViewModel)
    viewModelOf(::OnlineFragmentViewModel)
    viewModelOf(::OnlineGameFragmentViewModel)

}