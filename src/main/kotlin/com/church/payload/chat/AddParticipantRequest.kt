package com.church.payload.chat

import java.util.UUID

data class AddParticipantRequest(
    val userIds: List<UUID>
)
