package ua.nanit.otpmanager.account

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.ByteBuffer
import java.util.*

class AccountStorage(
    private val dir: File
) {

    fun all(): List<Account> {
        val files = dir.listFiles { _, name -> name.endsWith(".json") }
            ?: return emptyList()
        val accounts = LinkedList<Account>()

        for (file in files) {
            val account: Account = Json.decodeFromString(file.readText())
            accounts.add(account)
        }

        return accounts
    }

    fun get(id: Int): Account? {
        val file = dir.resolve(idToName(id))
        if (!file.exists()) return null
        return Json.decodeFromString(file.readText())
    }

    fun save(account: Account) {
        val file = getOrCreateFile(idToName(account.id))
        val json = Json.encodeToString(account)
        file.writeText(json)
    }

    fun incId(): Int {
        val file = dir.resolve(".last_id")

        if (!file.exists()) {
            file.createNewFile()
            writeIdFile(file, 0)
            return 0
        }

        val lastId = ByteBuffer.wrap(file.readBytes()).int + 1
        writeIdFile(file, lastId)
        return lastId
    }

    private fun writeIdFile(file: File, id: Int) {
        file.writeBytes(ByteBuffer.allocate(4)
            .putInt(id)
            .array())
    }

    private fun idToName(id: Int) = "${id}.json"

    private fun getOrCreateFile(name: String): File {
        val file = dir.resolve(name)
        if (!file.exists()) {
            file.createNewFile()
        }
        return file
    }
}