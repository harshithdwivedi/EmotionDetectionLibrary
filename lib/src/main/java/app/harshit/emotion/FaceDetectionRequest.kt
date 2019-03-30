package app.harshit.emotion

internal class FaceDetectionRequest(
    val requests: ArrayList<Request>
)

internal class Request(
    val image: RequestImage,
    val features: ArrayList<RequestFeature> = arrayListOf(RequestFeature())
)

internal class RequestImage(
    val content: String
)

internal class RequestFeature(
    val type: String = "FACE_DETECTION",
    val maxResults: Int = 5
)
