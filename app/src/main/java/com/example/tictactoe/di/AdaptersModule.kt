package com.example.tictactoe.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import com.example.tictactoe.ui.play_online.online_home.OnlineUsersListAdapter

var adapterModule = module {
    factoryOf(::OnlineUsersListAdapter)
}