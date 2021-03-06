package org.javalite.db_migrator;

import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.javalite.db_migrator.JdbcPropertiesOverride.*;
import static org.javalite.test.jspec.JSpec.the;
import static org.junit.Assert.*;
import static org.javalite.db_migrator.DbUtils.*;


public class MySQLMigrationSpec {
    private MigrationManager migrationManager;


    @Before
    public void setup() throws Exception {

        Base.open(driver(), url(), user(), password());
        try {
            exec("drop database mysql_migration_test");
        } catch (Exception e) {/*ignore*/}
        exec("create database mysql_migration_test");
        Base.close();
        Base.open(driver(), "jdbc:mysql://localhost/mysql_migration_test", user(), password());
        migrationManager = new MigrationManager("src/test/resources/test_migrations/mysql/");
    }

    @After
    public void tearDown() throws Exception {
        try {
            exec("drop database mysql_migration_test");
        } catch (Exception e) {/*ignore*/}
        Base.close();
    }

    @Test
    public void shouldApplyPendingMigrations() {
        migrationManager.migrate(new MockLog(), null);
        assertEquals(countMigrations(), 4);
        the(countRows("books")).shouldBeEqual(9);
        the(countRows("authors")).shouldBeEqual(2);
    }
}
