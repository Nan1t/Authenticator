package ua.nanit.otpmanager.database

import androidx.room.*

@Dao
interface AccountDao {

    @Query("select * from AccountEntity")
    suspend fun getAll(): List<AccountEntity>

    @Query("select * from AccountEntity where id = :id")
    suspend fun findById(id: Int): AccountEntity?

    @Insert
    suspend fun create(account: AccountEntity): Int

    @Update
    suspend fun update(account: AccountEntity)

    @Delete
    suspend fun delete(account: AccountEntity)
}