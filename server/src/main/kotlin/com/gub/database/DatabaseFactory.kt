package com.gub.database

import org.jetbrains.exposed.sql.Database


/**
 * Commands to set up the PostgreSQL database:
 *
 * 1. psql postgres
 * 2. CREATE USER ktoruser WITH PASSWORD 'ktorpass';
 * 3. ALTER USER ktoruser CREATEDB;
 * 4. CREATE DATABASE ktordb WITH OWNER ktoruser;
 * 5. \q
 * */

object DatabaseFactory {

    fun init() {
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/ktordb",
            driver = "org.postgresql.Driver",
            user = "ktoruser",
            password = "ktorpass"
        )
    }
}