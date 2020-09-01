/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.game

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {
    private lateinit var viewModel: GameViewModel

    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )
        Log.i("GameFragment", "ViewModelProviders.of Called")
        viewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)
        binding.gameViewModel = viewModel
        binding.lifecycleOwner = this

// No Need for them after setting 'onClick' attributes in the xml layout.
//        binding.correctButton.setOnClickListener {
//            viewModel.onCorrect()
//            //   updateWordText()
////            updateScoreText()
//        }
//        binding.skipButton.setOnClickListener {
//            viewModel.onSkip()
//            //    updateWordText()
//            //    updateScoreText()
//        }

//        viewModel.score.observe(this, Observer { newScore ->
//            binding.scoreText.text = newScore.toString()
//        })
//        viewModel.word.observe(this, Observer { newWord ->
//            binding.wordText.text = newWord
//        })
        viewModel.eventGameFinish.observe(this, Observer { hasFinished ->
            if (hasFinished) {
                gameFinished()
                viewModel.onGameFinishedCompleted()
            }
        })
        viewModel.buzzGameEvent.observe(this, Observer { buzzType ->
            if (buzzType != GameViewModel.BuzzType.NO_BUZZ) {
                buzz(buzzType.pattern)
                viewModel.onBuzzCompleted()
            }
        })
//        viewModel.currentTime.observe(this, Observer { timer ->
//            binding.timerText.text = DateUtils.formatElapsedTime(timer)
//        })
        //   updateWordText()
        //            updateScoreText()
        return binding.root

    }

    /**
     * Resets the list of words and randomizes the order
     */


    /**
     * Called when the game is finished
     */
    private fun gameFinished() {
        val action = GameFragmentDirections.actionGameToScore(viewModel.score.value ?: 0)
        findNavController(this).navigate(action)
    }

    /**
     * Moves to the next word in the list
     */


    /** Methods for buttons presses **/


    /** Methods for updating the UI **/

//    public fun updateWordText() {
//        binding.wordText.text = viewModel.word
//
//    }

//    public fun updateScoreText() {
////        binding.score.text = viewModel.score.toString
////
////    }
    private fun buzz(pattern: LongArray) {
        val buzzer = activity?.getSystemService<Vibrator>()
        buzzer?.let {
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                //deprecated in API 26
                buzzer.vibrate(pattern, -1)
            }
        }
    }
}
