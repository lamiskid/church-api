package com.church.repository

import com.church.model.account.UserRole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID


@Repository
interface UserRoleRepository:JpaRepository<UserRole,Long> {

    @Query(value = "SELECT * FROM user_role ur WHERE ur.user_id = :userId", nativeQuery = true)
    fun findAccountRoles(@Param("userId") userId: UUID): List<UserRole>

   /* @Query("SELECT ur FROM UserRole ur WHERE ur.account.id = :userId")
    fun findAccountRoles1(@Param("userId") userId: UUID): List<UserRole>*/
}