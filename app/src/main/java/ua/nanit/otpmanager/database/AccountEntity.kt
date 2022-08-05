package ua.nanit.otpmanager.database

import androidx.room.*
import androidx.room.TypeConverter

enum class AccountType {
    TOTP,
    HOTP
}

@Entity
class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    @TypeConverters(TypeConverter::class)
    val type: AccountType,
    val name: String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val secret: ByteArray,
    val counter: Long
)

class TypeConverter {

    @TypeConverter
    fun toAccountType(value: String) =
        enumValueOf<AccountType>(value.uppercase())

    @TypeConverter
    fun fromAccountType(value: AccountType) = value.toString()

}