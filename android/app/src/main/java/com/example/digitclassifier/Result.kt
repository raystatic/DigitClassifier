package com.example.digitclassifier

class Result(prob:FloatArray, time:Long) {
    private var number:Int?=0
    private var probability:Float?=0f
    private var timeCost:Long?=0

    init {
        number = argMax(prob)
        probability = prob[number!!]
        timeCost = time
    }

    private fun argMax(prob: FloatArray): Int? {
        var maxId = -1
        var maxProb = 0.0f
        prob.forEachIndexed { index, fl ->
            if (fl>maxProb){
                maxProb = fl
                maxId = index
            }
        }

        return maxId
    }

    fun getNumber(): Int? {
        return number
    }

    fun getProbability():Float?{
        return probability
    }

    fun getTimeCost():Long?{
        return timeCost
    }

}