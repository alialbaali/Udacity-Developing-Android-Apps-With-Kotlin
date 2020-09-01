package com.example.android.guesstheword.screens.score

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(finalScore: Int) : ViewModel() {
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private val _onPlayAgainEvent = MutableLiveData<Boolean>()
    val onPlayAgainEvent: LiveData<Boolean>
        get() = _onPlayAgainEvent

    init {
        _score.value = finalScore
        Log.i("ScoreViewModel", "Final score is $finalScore")
    }

    fun onPlayAgainCompleted() {
        _onPlayAgainEvent.value = false
    }

    fun onPlayAgain() {
        _onPlayAgainEvent.value = true
    }
}
