package com.church.payload.profile

data class MemberStatsResponse(
    val total: Long,
    val active: Long,
    val inactive: Long,
    val suspended: Long
)
