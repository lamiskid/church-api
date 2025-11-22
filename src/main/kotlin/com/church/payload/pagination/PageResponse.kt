package com.church.payload.pagination



data class PageResponse<T>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val last: Boolean,
    val hasNext: Boolean = (page + 1) < totalPages
)


