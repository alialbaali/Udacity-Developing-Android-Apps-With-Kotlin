package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)


class GameViewModel : ViewModel() {

    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }

    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L

        private const val COUNTDOWN_PANIC_SECONDS = 10L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 60000L
    }

    private val timer: CountDownTimer

    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime

    val currentTimeString = Transformations.map(currentTime, { time ->
        DateUtils.formatElapsedTime(time)
    })
    // The current word
//    var word = 0
//    val word = MutableLiveData<String>()
    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word

    // The current score
//    var score = 0false
//    val score = MutableLiveData<Int>()
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score
    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    private val _buzzGameEvent = MutableLiveData<BuzzType>()
    val buzzGameEvent: LiveData<BuzzType>
        get() = _buzzGameEvent

    init {
        resetList()
        nextWord()
        _score.value = 0
        _word.value = ""
        _eventGameFinish.value = false
        _currentTime.value = 0
        _buzzGameEvent.value = BuzzType.NO_BUZZ
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onTick(p0: Long) {
                _currentTime.value = p0 / ONE_SECOND
                if (p0 / ONE_SECOND <= COUNTDOWN_PANIC_SECONDS) {
                    _buzzGameEvent.value = BuzzType.COUNTDOWN_PANIC
                }
            }

            override fun onFinish() {
                _currentTime.value = DONE
                _buzzGameEvent.value = BuzzType.GAME_OVER
                _eventGameFinish.value = true
            }

        }
        timer.start()

        Log.i("GameViewModel", "GameViewModel Called")
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
        Log.i("GameViewModel", "GameViewModel Destroyed")
    }

    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
        }
        _word.value = wordList.removeAt(0)

//        if (wordList.isEmpty()) {
//            //      gameFinished()
//            _eventGameFinish.value = true
//        } else {
////            word = wordList.removeAt(0)
//            _word.value = wordList.removeAt(0)
    }

    fun onSkip() {
//        score--
        _score.value = (_score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
//        score++
        _buzzGameEvent.value = BuzzType.CORRECT
        _score.value = (_score.value)?.plus(1)
        nextWord()
    }

    fun onGameFinishedCompleted() {
        _eventGameFinish.value = false
    }

    fun onBuzzCompleted() {
        _buzzGameEvent.value = BuzzType.NO_BUZZ
    }
}

