package com.legacyminecraft.poseidon.uuid;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class SQLitePlayerDAO implements UUIDPlayerDAO {
    private Connection connection;


    public SQLitePlayerDAO(File sqliteFile) {
        boolean newDatabase = false;

        //Load the JDBC driver
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            System.out.println("[Poseidon] Error loading SQLite JDBC driver: ");
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        //Create the database file if it doesn't exist
        if (!sqliteFile.exists()) {
            try {
                sqliteFile.createNewFile();
                newDatabase = true;
            } catch (Exception e) {
                System.out.println("[Poseidon] Error creating SQLite database file: " + e.getMessage());
                throw new RuntimeException(e); //Rethrow the exception so the server doesn't start
            }
        }

        //Connect to the database
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteFile.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("[Poseidon] Error connecting to SQLite database: ");
            e.printStackTrace();
            throw new RuntimeException(e); //Rethrow the exception so the server doesn't start
        }

        createTables();

        //Convert from uuidcache.json if this is a new database
        if (newDatabase) {
            System.out.println("[Poseidon] Converting uuidcache.json to SQLite...");
            convertFromUUIDCache(new File("uuidcache.json"));
        }

        //Create a backup of the database file
        try {
            File backupFile = new File(sqliteFile.getAbsolutePath() + ".backup");
            if (backupFile.exists()) {
                backupFile.delete();
            }
            Files.copy(sqliteFile.toPath(), backupFile.toPath());
        } catch (Exception e) {
            System.out.println("[Poseidon] Error creating SQLite database backup: ");
            e.printStackTrace();
        }

        //Register shutdown hook to close the connection
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    System.out.println("[Poseidon] Closing SQLite connection...");
                    connection.close();
                } catch (Exception e) {
                    System.out.println("[Poseidon] Error closing SQLite connection: ");
                    e.printStackTrace();
                }
            }
        });

        //Performance Testing Stuff
        //TODO: Remove this when no-longer needed
        long startTime = System.currentTimeMillis();

        //Load all UUIDs into memory
        UUID[] players = getAllPlayers();

        long endTime = System.currentTimeMillis();
        System.out.println("[Poseidon] Loaded all UUIDs (" + players.length + ") in " + (endTime - startTime) + "ms");

        //Fetch all usernames for UUIDs
        String[] usernames = new String[players.length];
        startTime = System.currentTimeMillis();
        int i = 0;
        for (UUID uuid : players) {
            usernames[i] = getLatestUsername(uuid);
            i++;
        }
        endTime = System.currentTimeMillis();
        System.out.println("[Poseidon] Fetched all usernames in " + (endTime - startTime) + "ms");

        //Fetch all UUID for usernames (case-sensitive)
        startTime = System.currentTimeMillis();
        UUID[] uuids = new UUID[usernames.length];
        i = 0;
        for (String username : usernames) {
            uuids[i] = getLatestUUID(username, false);
            i++;
        }
        endTime = System.currentTimeMillis();
        System.out.println("[Poseidon] Fetched all UUIDs utilizing case-sensitive search in " + (endTime - startTime) + "ms");

        //Fetch all UUID for usernames (case-insensitive)
        startTime = System.currentTimeMillis();
        uuids = new UUID[usernames.length];
        i = 0;
        for (String username : usernames) {
            uuids[i] = getLatestUUID(username, true);
            i++;
        }
        endTime = System.currentTimeMillis();
        System.out.println("[Poseidon] Fetched all UUIDs utilizing case-insensitive search in " + (endTime - startTime) + "ms");

        //Performance Testing End
    }

    private void createTables() {
        //Create the tables if they don't exist
        try {
            //Create the uuidcache table
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS uuidcache (id INTEGER PRIMARY KEY,uuid TEXT,username TEXT,last_updated INTEGER,last_seen INTEGER, mojang_uuid INTEGER DEFAULT 0,username_lower TEXT);");

            //Make UUID and username indexed
            connection.createStatement().execute("CREATE INDEX IF NOT EXISTS uuidcache_uuid_index ON uuidcache (uuid);");
            connection.createStatement().execute("CREATE INDEX IF NOT EXISTS uuidcache_username_index ON uuidcache (username);");
            connection.createStatement().execute("CREATE INDEX IF NOT EXISTS uuidcache_username_lower_index ON uuidcache (username_lower);");

            //Create triggers to update the username_lower column
            connection.createStatement().execute("CREATE TRIGGER IF NOT EXISTS uuidcache_username_lower_insert AFTER INSERT ON uuidcache BEGIN UPDATE uuidcache SET username_lower = lower(username) WHERE id = new.id; END;");
            connection.createStatement().execute("CREATE TRIGGER IF NOT EXISTS uuidcache_username_lower_update AFTER UPDATE OF username ON uuidcache BEGIN UPDATE uuidcache SET username_lower = lower(username) WHERE id = new.id; END;");
        } catch (Exception e) {
            System.out.println("[Poseidon] Error creating SQLite tables: ");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void convertFromUUIDCache(File uuidCacheFile) {
        //Error out if the file doesn't exist
        if (!uuidCacheFile.exists()) {
            System.out.println("[Poseidon] Unable to convert uuidcache.json to SQLite because uuidcache.json doesn't exist");
            return;
        }

        //Read the file
        try {
            JSONParser parser = new JSONParser();
            JSONArray uuidArray = (JSONArray) parser.parse(new FileReader(uuidCacheFile));

            //Loop through the array and add each player to the database
            int count = 0;
            long startTime = System.currentTimeMillis();
            for (Object o : uuidArray) {
                JSONObject player = (JSONObject) o;
                boolean onlineUUID = (boolean) player.get("onlineUUID");
                String username = (String) player.get("name");
                UUID uuid = UUID.fromString((String) player.get("uuid"));
                long lastSeen = (long) player.get("expiresOn");
//                addPlayer(uuid, username, lastSeen, onlineUUID);
                addOrUpdatePlayer(uuid, username, lastSeen-1382400, lastSeen-1382400, onlineUUID);
                count++;
            }
            long endTime = System.currentTimeMillis();
            System.out.println("[Poseidon] Converted " + count + " players from uuidcache.json to SQLite in " + (endTime - startTime) / 1000 + " seconds");
        } catch (Exception e) {
            System.out.println("[Poseidon] Error converting uuidcache.json to SQLite: ");
            e.printStackTrace();
        }
    }

    public void addOrUpdatePlayer(UUID uuid, String username, long lastSeen, long last_updated, boolean mojangUUID) {
        try {
            // Check if the player already exists with the same UUID
            PreparedStatement checkStatement = connection.prepareStatement("SELECT username FROM uuidcache WHERE uuid=?;");
            checkStatement.setString(1, uuid.toString());
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                String existingUsername = resultSet.getString("username");

                if (existingUsername.equals(username)) {
                    // The player exists and the username hasn't changed; update lastSeen and last_updated
                    PreparedStatement updateStatement = connection.prepareStatement("UPDATE uuidcache SET last_seen=?, last_updated=? WHERE uuid=?;");
                    updateStatement.setLong(1, lastSeen);
                    updateStatement.setLong(2, last_updated);
                    updateStatement.setString(3, uuid.toString());
                    updateStatement.execute();
                    updateStatement.close();
                } else {
                    // The player exists but the username has changed; insert a new record
                    insertNewPlayer(uuid, username, lastSeen, last_updated, mojangUUID);
                }
            } else {
                // The player doesn't exist; insert a new record
                insertNewPlayer(uuid, username, lastSeen, last_updated, mojangUUID);
            }

            resultSet.close();
            checkStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertNewPlayer(UUID uuid, String username, long lastSeen, long last_updated, boolean mojangUUID) throws SQLException {
        PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO uuidcache (uuid,username,last_seen,last_updated,mojang_uuid) VALUES (?,?,?,?,?);");
        insertStatement.setString(1, uuid.toString());
        insertStatement.setString(2, username);
        insertStatement.setLong(3, lastSeen);
        insertStatement.setLong(4, last_updated);
        insertStatement.setBoolean(5, mojangUUID);
        insertStatement.execute();
        insertStatement.close();
    }

    public void updateLastSeen(UUID uuid, long lastSeen) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE uuidcache SET last_seen=? WHERE uuid=?;");
            statement.setLong(1, lastSeen);
            statement.setString(2, uuid.toString());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateLastUpdated(UUID uuid, long lastUpdated) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE uuidcache SET last_updated=? WHERE uuid=?;");
            statement.setLong(1, lastUpdated);
            statement.setString(2, uuid.toString());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removePlayer(UUID uuid, String username) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM uuidcache WHERE uuid=? AND username=?;");
            statement.setString(1, uuid.toString());
            statement.setString(2, username);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean isMojangUUID(UUID uuid) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT mojang_uuid FROM uuidcache WHERE uuid=?;");
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Boolean mojangUUID = resultSet.getBoolean("mojang_uuid");
                resultSet.close();
                statement.close();
                return mojangUUID;
            } else {
                resultSet.close();
                statement.close();
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UUID[] getAllPlayers() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT uuid FROM uuidcache;");
            ResultSet resultSet = statement.executeQuery();
            ArrayList<UUID> uuids = new ArrayList<>();
            while (resultSet.next()) {
                uuids.add(UUID.fromString(resultSet.getString("uuid")));
            }
            resultSet.close();
            statement.close();
            return uuids.toArray(new UUID[uuids.size()]);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String[] getAllUsernames(UUID uuid) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT username FROM uuidcache WHERE uuid=? ORDER BY last_updated DESC;");
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            String[] usernames = new String[resultSet.getFetchSize()];
            int i = 0;
            while (resultSet.next()) {
                usernames[i] = resultSet.getString("username");
                i++;
            }
            resultSet.close();
            statement.close();
            return usernames;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removePlayer(UUID uuid) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM uuidcache WHERE uuid=?;");
            statement.setString(1, uuid.toString());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removePlayer(String username) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM uuidcache WHERE username=?;");
            statement.setString(1, username);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UUID getLatestUUID(String username) {
        return getLatestUUID(username, true);
    }

    public UUID getLatestUUID(String username, boolean caseInsensitivity) {
        UUID uuid = getLatestUUID(username, caseInsensitivity, true);
        if (uuid == null) {
            uuid = getLatestUUID(username, caseInsensitivity, false);
        }
        return uuid;
    }

    public UUID getLatestUUID(String username, boolean caseInsensitivity, boolean mojangUUID) {
        try {
            PreparedStatement statement;
            // Determine the column and mojang_uuid condition based on the input flags
            String column = caseInsensitivity ? "username_lower" : "username";
            int mojangFlag = mojangUUID ? 1 : 0;
            // Build the SQL query dynamically
            String sql = String.format("SELECT uuid FROM uuidcache WHERE %s=? AND mojang_uuid=%d ORDER BY last_updated DESC LIMIT 1;", column, mojangFlag);

            statement = connection.prepareStatement(sql);

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String uuidString = resultSet.getString("uuid");
                resultSet.close();
                statement.close();
                return UUID.fromString(uuidString);
            } else {
                resultSet.close();
                statement.close();
                return null; // Username not found
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getLatestUsername(UUID uuid) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT username FROM uuidcache WHERE uuid=? ORDER BY last_seen DESC LIMIT 1;");
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String username = resultSet.getString("username");
                resultSet.close();
                statement.close();
                return username;
            } else {
                resultSet.close();
                statement.close();
                return null; // UUID not found
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public long getLastSeen(UUID uuid) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT last_seen FROM uuidcache WHERE uuid=? ORDER BY last_seen DESC LIMIT 1;");
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                long lastSeen = resultSet.getLong("last_seen");
                resultSet.close();
                statement.close();
                return lastSeen;
            } else {
                resultSet.close();
                statement.close();
                return 0; // UUID not found
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public long getLastUpdated(UUID uuid) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT last_updated FROM uuidcache WHERE uuid=? ORDER BY last_updated DESC LIMIT 1;");
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                long lastUpdated = resultSet.getLong("last_updated");
                resultSet.close();
                statement.close();
                return lastUpdated;
            } else {
                resultSet.close();
                statement.close();
                return 0; // UUID not found
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    public long getLastSeen(String username) {
//        try {
//            PreparedStatement statement = connection.prepareStatement("SELECT last_seen FROM uuidcache WHERE username=? ORDER BY last_seen DESC LIMIT 1;");
//            statement.setString(1, username);
//            ResultSet resultSet = statement.executeQuery();
//
//            if (resultSet.next()) {
//                long lastSeen = resultSet.getLong("last_seen");
//                resultSet.close();
//                statement.close();
//                return lastSeen;
//            } else {
//                resultSet.close();
//                statement.close();
//                return 0; // Username not found
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public boolean isPlayerInCache(UUID uuid) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM uuidcache WHERE uuid=?;");
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();

            boolean isInCache = resultSet.next();
            resultSet.close();
            statement.close();
            return isInCache;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isPlayerInCache(String username) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM uuidcache WHERE username=?;");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            boolean isInCache = resultSet.next();
            resultSet.close();
            statement.close();
            return isInCache;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isPlayerInCache(UUID uuid, String username) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM uuidcache WHERE uuid=? AND username=?;");
            statement.setString(1, uuid.toString());
            statement.setString(2, username);
            ResultSet resultSet = statement.executeQuery();

            boolean isInCache = resultSet.next();
            resultSet.close();
            statement.close();
            return isInCache;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeDatabase() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
