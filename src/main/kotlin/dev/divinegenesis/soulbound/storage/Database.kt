package dev.divinegenesis.soulbound.storage

import dev.divinegenesis.soulbound.logger
import java.sql.Connection
import java.sql.SQLException

abstract class Database {
    @get:Throws(SQLException::class)
    abstract val connection: Connection?

    fun createTable() {
        try {
            connection?.createStatement().use { statement ->
                statement?.queryTimeout = 30

                // Create the database schema
                val table = "CREATE TABLE IF NOT EXISTS soulbound (" +
                        "itemID			STRING PRIMARY KEY," +
                        "interact		INT," +
                        "pickup		    INT," +
                        "craft			INT" +
                        ")"

                // Create the islands table (execute statement)
                statement?.executeUpdate(table)
            }
        } catch (e: SQLException) {
            logger<Database>().error("Unable to create Soulbound database", e)
        }
    }

    open fun loadData(): Map<String, DataStack> {
        val data = hashMapOf<String, DataStack>()
        try {
            connection?.createStatement().use { statement ->
                statement?.executeQuery("SELECT * FROM soulbound").use { results ->
                    while (results?.next() == true) {
                        val itemID: String = results.getString("itemID")
                        val interact: Short = results.getShort("interact")
                        val pickup: Short = results.getShort("pickup")
                        val craft: Short = results.getShort("craft")
                        val dataStack = DataStack(itemID, interact, pickup, craft)
                        data[itemID] = dataStack
                    }
                    return data
                }
            }
        } catch (e: SQLException) {
            logger<Database>().error("Unable to read from the database.", e)
        }
        logger<Database>().info("Loaded Soulbound MySQL Data. Count: ${data.size}")
        return data
    }

    fun saveStack(data: DataStack) {
        val sql =
            "REPLACE INTO soulbound(itemID, interact, pickup, craft) VALUES(?, ?, ?, ?)"
        try {
            connection?.prepareStatement(sql).use { statement ->
                statement?.setString(1, data.itemID)
                statement?.setShort(2, data.interact)
                statement?.setShort(3, data.pickup)
                statement?.setShort(4, data.craft)
                statement?.execute()
            }
        } catch (e: SQLException) {
            logger<Database>().error("Error inserting data into the database:", e)
        }
    }

    fun removeData(data: DataStack) {
        val sql = "DELETE FROM soulbound WHERE itemID = ?"
        try {
            connection?.prepareStatement(sql).use { statement ->
                statement?.setString(1, data.itemID)
                statement?.execute()
            }
        } catch (e: SQLException) {
            logger<Database>().error("Error removing Island from the database:", e)
        }
    }
}

data class DataStack(val itemID: String, val interact: Short = 0, val pickup: Short = 0, val craft: Short = 0)