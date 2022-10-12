package com.example.tictactoe.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import com.example.tictactoe.ui.play_with_pc.WithPcGameViewModel
import com.example.tictactoe.ui.play_with_friend.WithFriendGameViewModel


var viewModelModule = module {
    viewModelOf(::WithPcGameViewModel)
    viewModelOf(::WithFriendGameViewModel)

}