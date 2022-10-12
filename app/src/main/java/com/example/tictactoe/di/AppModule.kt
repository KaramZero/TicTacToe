package com.example.tictactoe.di

import com.example.tictactoe.ui.MyAnimator
import com.example.tictactoe.game_logics.Music
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val  appModule = module{
    factoryOf(::MyAnimator)
    singleOf(::Music)
}