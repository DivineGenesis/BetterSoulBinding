package dev.divinegenesis.soulbound.storage

import dev.divinegenesis.soulbound.Soulbound
import dev.divinegenesis.soulbound.customdata.DataStack
import dev.divinegenesis.soulbound.logger
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException


class SqliteDatabase : Database() {

    @get:Throws(SQLException::class)
    override var connection: Connection? = null

    override fun loadData(): Map<String, DataStack> {
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
            logger<SqliteDatabase>().error("Unable to read from the database:", e)
        }
        logger<SqliteDatabase>().info("Loaded Soulbound SQLite Data. Count: ${data.size}")
        return data
    }


    init {
        // Load the SQLite JDBC driver
        try {
            Class.forName("org.sqlite.JDBC")
            connection = DriverManager.getConnection(
                "jdbc:sqlite:${Soulbound.configDir}${File.separatorChar}soulbound.db"
            )
            logger<SqliteDatabase>().info("Successfully connected to Soulbound SQLite DB.")
        } catch (e: ClassNotFoundException) {
            logger<SqliteDatabase>().error("Unable to load the JDBC driver:", e)
        } catch (e: SQLException) {
            logger<SqliteDatabase>().error("Unable to connect to Soulbound SQLite DB:", e)
        }
        createTable()
    }
}