package com.haotang.server.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@Repository
public class Database {
    @Autowired
    @Qualifier("jdbcTemplate1")
    private JdbcTemplate jdbcTemplate1;

    @Autowired
    @Qualifier("jdbcTemplate2")
    private JdbcTemplate jdbcTemplate2;

    private String createRootTableSql;
    private String insertToRootTableSql;
    private String createSkierTableSql;
    private String insertToSkierTableSql;
    private String queryRootTableSql;
    private String queryRootTableSql2;
    private String querySkierVerticalSql;

    private final Lock mutex = new ReentrantLock();

    private final int VERTICAL_FACTOR = 10;

    @PostConstruct
    private void init() {
        this.createRootTableSql = readSqlFromFile("sql/create_root_table.sql");
        this.insertToRootTableSql = readSqlFromFile("sql/insert_to_root_table.sql");
        this.createSkierTableSql = readSqlFromFile("sql/create_skier_table.sql");
        this.insertToSkierTableSql = readSqlFromFile("sql/insert_to_skier_table.sql");
        this.queryRootTableSql = readSqlFromFile("sql/query_root_table.sql");
        this.queryRootTableSql2 = readSqlFromFile("sql/query_root_table2.sql");
        this.querySkierVerticalSql = readSqlFromFile("sql/query_skier_vertical.sql");
        this.createRootTable();
    }

    private void createRootTable() {
        this.jdbcTemplate1.execute(this.createRootTableSql);
    }

    private void insertToRootTable(String resortId, int dayId) {
        StringBuilder sb = new StringBuilder();
        sb.append(resortId.replace(' ', '_')).append("_").append(dayId);
        this.jdbcTemplate1.update(this.insertToRootTableSql, resortId, dayId, sb.toString());
    }

    private void createSkierTable(String table) {
        String sql = String.format(this.createSkierTableSql, table, table);
        this.jdbcTemplate1.execute(sql);
    }

    public void insertToSkierTable(String resortId, int dayId, int skierId, int lyftId, int time) {
        String skierTable = queryRootTable(resortId, dayId);
        if (skierTable.isEmpty()) {
            try {
                mutex.lock();
                skierTable = queryRootTable(resortId, dayId);
                if (skierTable.isEmpty()) {
                    insertToRootTable(resortId, dayId);
                    skierTable = queryRootTable(resortId, dayId);
                    createSkierTable(skierTable);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mutex.unlock();
            }
        }
        insertToSkierTable(skierTable, skierId, lyftId, time);
    }

    private void insertToSkierTable(String table, int skierId, int lyftId, int time) {
        String sql = String.format(this.insertToSkierTableSql, table);
        this.jdbcTemplate1.update(sql, skierId, lyftId, time);
    }

    private String queryRootTable(String resortId, int dayId) {
        String skierTable = "";
        String sql = String.format(this.queryRootTableSql, resortId, dayId);
        try {
            skierTable = jdbcTemplate2.queryForObject(sql, String.class);
        } catch (EmptyResultDataAccessException e) {
            return skierTable;
        }
        return skierTable;
    }

    private List<String> queryRootTable(String resortId) {
        List<String> skierTables = null;
        String sql = String.format(this.queryRootTableSql2, resortId);
        try {
            skierTables = jdbcTemplate2.query(sql, (row, rowNum) -> row.getString("skierTable"));
        } catch (EmptyResultDataAccessException e) {
            System.out.println(resortId + " not found");
            return new ArrayList<String>();
        }
        return skierTables;
    }

    public int queryVertical(String resortId, int skierId) {
        int vertical = 0;
        List<String> skierTables = queryRootTable(resortId);
        for (String skierTable: skierTables) {
            vertical += queryVerticalByTable(skierTable, skierId);
        }
        return vertical;
    }

    public int queryVertical(String resortId, int dayId, int skierId) {
        String skierTable = queryRootTable(resortId, dayId);
        return queryVerticalByTable(skierTable, skierId);
    }

    private int queryVerticalByTable(String skierTable, int skierId) {
        int vertical = 0;
        if (!skierTable.isEmpty()) {
            String sql = String.format(this.querySkierVerticalSql, skierTable, skierId);
            Integer res = this.jdbcTemplate2.queryForObject(sql, Integer.class);
            if (res != null) {
                vertical = res * VERTICAL_FACTOR;
            }
        }
        return vertical;
    }

    private String readSqlFromFile(String path) {
        String sql = null;
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            sql = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sql;
    }
}