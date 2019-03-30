package app.harshit.emotion

import java.io.IOException

interface CallBack {

    fun onError(exception: IOException)

    fun onResponse(emotionLikeness: Emotion)

}