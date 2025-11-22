package com.church.repository

import com.church.model.sermons.Sermon
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SermonRepository : JpaRepository<Sermon, UUID>, PagingAndSortingRepository<Sermon, UUID>
