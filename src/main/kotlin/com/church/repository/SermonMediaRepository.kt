package com.church.repository

import com.church.model.sermons.SermonMedia
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SermonMediaRepository:JpaRepository<SermonMedia,Long>{
}