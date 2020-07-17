package com.example.digitclassifier

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var classifier:Classifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        classifier = Classifier(this)


        btnDetect.setOnClickListener {
            val bitmap = fingerPaint.exportToBitmap(Classifier.IMAGE_WIDTH,Classifier.IMAGE_HEIGHT)
            val result = classifier.classify(bitmap)
            tvProbability.text = "Probability : ${result.getProbability()}"
            tvResult.text = "Predicted Number : ${result.getNumber()}"
            tvTime.text = "Time Taken : ${result.getTimeCost()}"
        }

        btnClear.setOnClickListener {
            fingerPaint.clear()
            tvProbability.text = ""
            tvResult.text = ""
            tvTime.text = ""
        }

    }
}