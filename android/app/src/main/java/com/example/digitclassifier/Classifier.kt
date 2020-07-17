package com.example.digitclassifier

import android.app.Activity
import android.graphics.Bitmap
import android.os.SystemClock
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class Classifier(activity: Activity) {

    companion object{
        const val MODEL_NAME = "digit.tflite"
        const val BATCH_SIZE = 1
        const val IMAGE_HEIGHT = 28
        const val IMAGE_WIDTH = 28
        const val NUMCHANNEL = 1
        const val NUMCLASSES = 10
    }

    private var options : Interpreter.Options ?=null
    private var interpreter:Interpreter?=null
    private var imageData : ByteBuffer?=null
    private var imagePixels=IntArray(IMAGE_WIDTH * IMAGE_HEIGHT)
    private var result = Array(1) { FloatArray(NUMCLASSES) }

    init {
        options = Interpreter.Options()
        try {
            interpreter = Interpreter(loadModelFile(activity),options)
            imageData = ByteBuffer.allocateDirect(4 * BATCH_SIZE * IMAGE_HEIGHT * IMAGE_WIDTH * NUMCHANNEL)
            imageData?.order(ByteOrder.nativeOrder())
        }catch (e:IOException){
            e.printStackTrace()
        }

    }

    fun classify(bitmap:Bitmap):Result{
        convertBitmaptoByteBuffer(bitmap)
        val startTime = SystemClock.uptimeMillis()
        interpreter?.run(imageData,result)
        val endTime = SystemClock.uptimeMillis()
        return Result(result[0],endTime-startTime)
    }

    @Throws(IOException::class)
    private fun loadModelFile(activity: Activity):MappedByteBuffer{
        val fileDescriptor = activity.assets.openFd(MODEL_NAME)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun convertBitmaptoByteBuffer(bitmap: Bitmap){
        imageData?.let {
            it.rewind()
            bitmap.getPixels(imagePixels,0,bitmap.width,0,0, bitmap.width,bitmap.height)
            var pixel = 0
            for (i in 0 until IMAGE_WIDTH){
                for (j in 0 until IMAGE_HEIGHT){
                    val value = imagePixels[pixel++]
                    imageData?.putFloat(convertPixel(value))
                }
            }
        }
    }

    private fun convertPixel(value: Int): Float {
        return (((255 - ((((value shr 16) and 0xFF) * 0.299f
                + ((value shr 8) and 0xFF) * 0.587f
                + (value and 0xFF) * 0.114f)))) / 255.0f)
    }

}