package ua.nanit.otpmanager.domain.account

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Named

private typealias AccountCache = MutableMap<String, Account>

class AccountJsonStorage(
    @Named("appDir") private val dir: File
) : AccountStorage {

    private var cache: AccountCache? = null

    override fun getAll(): List<Account> {
        return getOrParseData().entries.map { it.value }
    }

    override fun get(label: String): Account? {
        return getOrParseData()[label.lowercase()]
    }

    override fun add(account: Account) {
        editFile { it[account.label.lowercase()] = account }
    }

    override fun edit(account: Account) {
        add(account) // For json map these operations are same
    }

    override fun remove(account: Account) {
        editFile { it.remove(account.label.lowercase()) }
    }

    private fun editFile(data: (AccountCache) -> Unit) {
        val parsed = getOrParseData()
        data(parsed)
        getOrCreateFile().writeText(Json.encodeToString(parsed))
    }

    private fun getFile(): File? {
        val file = resolveFile()
        return if (file.exists()) file else null
    }

    private fun getOrCreateFile(): File {
        val file = resolveFile()
        if (!file.exists())
            file.createNewFile()
        return file
    }

    private fun getOrParseData(): AccountCache {
        val cache = this.cache
        return if (cache == null) {
            val file = getFile() ?: return mutableMapOf()
            val parsed: AccountCache = Json.decodeFromString(file.readText())
            this.cache = parsed
            parsed
        } else {
            cache
        }
    }

    private fun resolveFile(): File = dir.resolve("accounts.json")
}