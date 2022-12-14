package ua.nanit.otpmanager.domain.storage

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ua.nanit.otpmanager.domain.account.Account
import java.io.File
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

private typealias AccountCache = LinkedHashMap<String, Account>

@Singleton
class AccountJsonStorage @Inject constructor(
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
        editFile { cache ->
            if (!cache.contains(account.key()))
                cache[account.key()] = account
        }
    }

    override fun addAll(accounts: List<Account>) {
        editFile { cache ->
            for (account in accounts) {
                if (!cache.contains(account.key()))
                    cache[account.key()] = account
            }
        }
    }

    override fun edit(account: Account) {
        add(account) // For json map these operations are same
    }

    override fun delete(account: Account) {
        editFile { cache ->
            cache.remove(account.key())
        }
    }

    override fun export(): String {
        val format = Json { prettyPrint = true }
        return format.encodeToString(getOrParseData())
    }

    private fun editFile(data: (AccountCache) -> Unit) {
        val parsed = getOrParseData()
        data(parsed)
        getOrCreateFile().writeText(Json.encodeToString(parsed))
    }

    private fun getOrCreateFile(): File {
        val file = resolveFile()
        if (!file.exists()) {
            file.createNewFile()
            file.writeText("{}")
        }
        return file
    }

    private fun getOrParseData(): AccountCache {
        val cache = this.cache
        return if (cache == null) {
            val file = getOrCreateFile()
            val parsed: AccountCache = Json.decodeFromString(file.readText())
            this.cache = parsed
            parsed
        } else {
            cache
        }
    }

    private fun resolveFile(): File = dir.resolve("accounts.json")

    private fun Account.key() = this.label.lowercase()
}