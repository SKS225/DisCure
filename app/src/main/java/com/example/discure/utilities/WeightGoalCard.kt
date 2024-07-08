package com.example.discure.utilities

import android.widget.TextView
import com.ekn.gruzer.gaugelibrary.FullGauge
import kotlin.math.abs

fun updateWtCardText(wtGauge: FullGauge, tvWeightGoal: TextView, lastWt: Int, goalWt: Int, initialWt: Int) {
    val gainLoss = if (lastWt > goalWt) "Lost" else "Gained"
    val diffToGoal = abs(lastWt - initialWt)
    val diffToInitial = abs(goalWt - initialWt)

    //handle end cases

    tvWeightGoal.text = "$diffToGoal of $diffToInitial kg $gainLoss"
    setUpFullGauge(wtGauge, diffToGoal.toFloat(), diffToInitial.toFloat())
}