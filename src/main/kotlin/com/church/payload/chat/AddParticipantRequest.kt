package com.church.payload.chat

import java.util.UUID

data class AddParticipantRequest(
    val accountIds: List<UUID>
)
