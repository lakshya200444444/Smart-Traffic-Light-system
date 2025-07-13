package com.gub.data.database

import org.mapdb.DB
import org.mapdb.DBMaker
import java.io.File

object DatabaseFactory {

    val dbFile = File("database/traffic.db")

    init {
        if (dbFile.exists().not())
            dbFile.parentFile.mkdirs()
    }

    val db: DB by lazy {
        DBMaker.fileDB(File("database/traffic.db"))
            .fileMmapEnable()
            .transactionEnable()
            .make()
    }
}