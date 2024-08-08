package com.rizwansayyed.zene.data.db.model

data class AppReviewData(
    var type: AppReviewEnum?,
    var atTimestamp: Long?
)

enum class AppReviewEnum(val review: String) {
    DONE("done"), FEEDBACK("feedback")
}