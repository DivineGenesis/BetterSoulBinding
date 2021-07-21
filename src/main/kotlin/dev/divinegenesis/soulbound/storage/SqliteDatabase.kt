package dev.divinegenesis.soulbound.storage

import com.google.common.collect.Maps
import com.google.common.io.Files
import dev.divinegenesis.soulbound.Soulbound
import dev.divinegenesis.soulbound.logger
import java.io.File
import java.io.IOException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*
import kotlin.collections.HashMap


class SqliteDatabase : Database() {

    @get:Throws(SQLException::class)
    override var connection: Connection? = null

    override fun loadData(): Map<String, DataStack> {
        val data = hashMapOf<String, DataStack>()
        try {
            connection!!.createStatement().use { statement ->
                statement.executeQuery("SELECT * FROM soulbound").use { results ->
                    while (results.next()) {
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
            logger<SqliteDatabase>().error("Unable to read from the database:", e)
        }
        logger<SqliteDatabase>().info("Loaded Soulbound SQLite Data. Count: {}", data.size)
        return data
    }


    init {
        // Load the SQLite JDBC driver
        try {
            Class.forName("org.sqlite.JDBC")
            connection = DriverManager.getConnection(
                String.format("jdbc:sqlite:${Soulbound.configDir}${File.pathSeparator}soulbound.db")
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