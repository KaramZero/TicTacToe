/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.tictactoe.game_logics.levels

import com.example.tictactoe.model.Move

/**
 *
 * @author karam
 */
interface Level {
    fun findMove(board: Array<CharArray>, c: Char): Move
}