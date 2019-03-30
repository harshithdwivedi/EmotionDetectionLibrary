package app.harshit.emotion

import android.graphics.Bitmap
import android.util.Base64
import com.google.gson.Gson
import okhttp3.*
import okhttp3.Request
import java.io.ByteArrayOutputStream
import java.io.IOException

class EmotionDetector private constructor(private val key: String, private val base64Image: String) {

    private val okHttp =
        OkHttpClient.Builder().build()

    private val gson = Gson()

    private fun getEmotion(callback: CallBack) {

        val requestImage = RequestImage(base64Image)
        val requestObject = FaceDetectionRequest(arrayListOf(Request(requestImage)))

        val body = RequestBody.create(MediaType.parse("application/json"), gson.toJson(requestObject))

        val request = Request.Builder()
            .url("https://vision.googleapis.com/v1/images:annotate?key=$key")
            .post(body)
            .build()


        okHttp.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val result = response.body()?.string()
                val faceDetectionResponse = gson.fromJson(result, FaceDetectionResponse::class.java)

                val detectedFaces = faceDetectionResponse.responses

                //TODO : Extend this to more faces, currently supports only once face
                with(detectedFaces[0].faceAnnotations[0]) {
                    if (angerLikelihood != EmotionLikeness.VERY_UNLIKELY && angerLikelihood != EmotionLikeness.UNKNOWN)
                        callback.onResponse(Emotion.ANGER)
                    if (joyLikelihood != EmotionLikeness.VERY_UNLIKELY && joyLikelihood != EmotionLikeness.UNKNOWN)
                        callback.onResponse(Emotion.HAPPINESS)
                    if (sorrowLikelihood != EmotionLikeness.VERY_UNLIKELY && sorrowLikelihood != EmotionLikeness.UNKNOWN)
                        callback.onResponse(Emotion.SAD)
                    if (surpriseLikelihood != EmotionLikeness.VERY_UNLIKELY && surpriseLikelihood != EmotionLikeness.UNKNOWN)
                        callback.onResponse(Emotion.SURPRISE)
                }
            }

        })

    }

    inner class Builder {
        lateinit var key: String
        lateinit var base64Image: String

        fun setApiKey(apiKey: String): EmotionDetector.Builder {
            key = apiKey
            return this
        }

        fun setBitmap(bitmap: Bitmap): EmotionDetector.Builder {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b = baos.toByteArray()
            base64Image = Base64.encodeToString(b, Base64.DEFAULT)
            return this
        }

        fun build(): EmotionDetector {
            return EmotionDetector(key, base64Image)
        }
    }
}