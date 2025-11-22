package com.church.payload.pagination

import org.springframework.data.domain.Page


object PaginationMapper {
    fun <T, R> toPageResponse(page: Page<T>, mapper: (T) -> R): PageResponse<R> {
        val content = page.content.map(mapper)
        return PageResponse(
            content = content,
            page = page.number,
            size = page.size,
            totalElements = page.totalElements,
            totalPages = page.totalPages,
            last = page.isLast,
            hasNext = page.hasNext()
        )
    }

    fun <T> toPageResponse(page:Page<T>): PageResponse<T> {
        return PageResponse(
            content = page.content,
            page = page.number,
            size = page.size,
            totalElements = page.totalElements,
            totalPages = page.totalPages,
            last = page.isLast,
            hasNext = page.hasNext()
        )
    }
}

