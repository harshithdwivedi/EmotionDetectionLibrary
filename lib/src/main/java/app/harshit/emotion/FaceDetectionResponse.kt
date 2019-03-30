package app.harshit.emotion

import android.graphics.Rect

class FaceDetectionResponse(
    val responses: ArrayList<FaceResponse> = arrayListOf()
)

class FaceResponse(
    val faceAnnotations: ArrayList<FaceDetectionFace> = arrayListOf()
)

class FaceDetectionFace(
    val boundingPoly: BoundingPoly,
    val joyLikelihood: EmotionLikeness,
    val sorrowLikelihood: EmotionLikeness,
    val angerLikelihood: EmotionLikeness,
    val surpriseLikelihood: EmotionLikeness,
    val blurredLikelihood: EmotionLikeness,
    val headwearLikelihood: EmotionLikeness
)

class BoundingPoly(
    val vertices: ArrayList<BoundingPolyVertices>
)

class BoundingPolyVertices(
    val x: Int,
    val y: Int
)

fun getRectFromVertices(vertices: List<BoundingPolyVertices>): Rect {
    val x1 = vertices[0].x
    val x2 = vertices[1].x
    val y1 = vertices[2].y
    val y2 = vertices[1].y

    return Rect(x1, y1, x2, y2)
}

enum class EmotionLikeness {
    VERY_LIKELY,
    LIKELY,
    POSSIBLE,
    UNLIKELY,
    VERY_UNLIKELY,
    UNKNOWN
}

enum class Emotion {
    ANGER,
    HAPPINESS,
    SURPRISE,
    SAD
}