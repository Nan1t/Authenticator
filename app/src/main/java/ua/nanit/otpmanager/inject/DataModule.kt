package ua.nanit.otpmanager.inject

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ua.nanit.otpmanager.database.AccountDao
import ua.nanit.otpmanager.database.AccountDatabase
import javax.inject.Singleton

@Module
class DataModule(private val ctx: Context) {

    @Singleton
    @Provides
    fun provideDatabase(): AccountDatabase {
        return Room.databaseBuilder(ctx, AccountDatabase::class.java, "accounts-db")
            .build()
    }

    @Singleton
    @Provides
    fun provideDao(db: AccountDatabase): AccountDao {
        return db.accountDao()
    }

}