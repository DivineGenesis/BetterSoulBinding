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
                        val interact: Int = results.getInt("interact")
                        val pickup: Int = results.getInt("pickup")
                        val craft: Int = results.getInt("craft")
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
                statement?.setInt(2, data.interact)
                statement?.setInt(3, data.pickup)
                statement?.setInt(4, data.craft)
                statement?.execute()
            }
        } catch (e: SQLException) {
            logger<Database>().error("Error inserting data into the database:", e)
        }
    }
}

data class DataStack(val itemID: String, var interact: Int = 0, var pickup: Int = 0, var craft: Int = 0)