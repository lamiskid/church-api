package com.church.payload.profile

import com.church.model.profile.Profile


fun Profile.toDto() = ProfileResponse(
    firstName = this.firstName,
    lastName = this.lastName,
    phone = this.phone,
    address = this.address,
    profilePictureUrl = "https://d1bmlnac6yzit1.cloudfront.net/"+ this.profilePictureUrl,
)
