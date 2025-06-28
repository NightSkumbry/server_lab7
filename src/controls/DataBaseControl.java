package controls;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;

import consoles.Printer;
import models.Coordinates;
import models.Flat;
import models.House;
import models.Transport;
import models.View;

public class DataBaseControl {
    private final String URL = "jdbc:postgresql://localhost:5433/studs";
    private final String my_login = "s466972";
    private final String my_password = "uHlEzwXD9phENmHf";
    private Printer printer;

    public DataBaseControl(Printer printer) {
        this.printer = printer;
    }

    private Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, my_login, my_password);
        } catch (SQLException e) {
            printer.printError("Ошибка подключения к базе данных: " + e.getMessage());
            throw e;
        }
    }


    public boolean authenticateUser(String username, String password) throws SQLException {
        String sql = "SELECT password_hash FROM Users WHERE user_name = ?";
        try (Connection conn = this.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");
                    return PasswordControl.verifyPassword(password, storedHash, username);
                }
                return false;
            }
        }
    }

    public boolean userExists(String username) throws SQLException {
        String sql = "SELECT password_hash FROM Users WHERE user_name = ?";
        try (Connection conn = this.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
                return false;
            }
        }
    }
    /*  
     * finish password control
     * enum types
     * readd ADMIN with actual hash
     * 
     */

    public Long insertFlat(Flat flat, int userId) throws SQLException {
        String coordinatesSql = "INSERT INTO Coordinates (x, y) VALUES (?, ?) RETURNING id";
        String houseSql = "INSERT INTO Houses (name, year, number_of_floors, number_of_flats_on_floor) VALUES (?, ?, ?, ?) RETURNING id";
        String sql = "INSERT INTO Flats (name, coordinates_id, creation_date, area, number_of_rooms, time_to_metro_by_transport, view, transport, house_id, created_by_user) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?::View, ?::Transport, ?, ?) RETURNING id";
        try (Connection conn = this.getConnection()) {
            
            conn.setAutoCommit(false);
            // Insert Coordinates
            int coordId;
            try (PreparedStatement coordStmt = conn.prepareStatement(coordinatesSql)) {
                coordStmt.setFloat(1, flat.getCoordinates().getX());
                coordStmt.setDouble(2, flat.getCoordinates().getY());
                try (ResultSet coordRs = coordStmt.executeQuery()) {
                    if (coordRs.next()) {
                        coordId = coordRs.getInt("id");
                    } else {
                        throw new SQLException("Не удалось создать запись в таблице Coordinates.");
                    }
                }
            }

            // Insert House
            int houseId;
            try (PreparedStatement houseStmt = conn.prepareStatement(houseSql)) {
                House house = flat.getHouse();
                houseStmt.setString(1, house.getName());
                houseStmt.setInt(2, house.getYear());
                houseStmt.setLong(3, house.getNumberOfFloors());
                houseStmt.setLong(4, house.getNumberOfFlatsOnFloor());
                try (ResultSet houseRs = houseStmt.executeQuery()) {
                    if (houseRs.next()) {
                        houseId = houseRs.getInt("id");
                    } else {
                        throw new SQLException("Не удалось создать запись в таблице Houses.");
                    }
                }
            }

            // Insert Flat
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, flat.getName());
                stmt.setInt(2, coordId);
                stmt.setDate(3, Date.valueOf(flat.getCreationDate()));
                stmt.setFloat(4, flat.getArea());
                stmt.setLong(5, flat.getNumberOfRooms());
                stmt.setFloat(6, flat.getTimeToMetroByTransport());
                stmt.setString(7, flat.getView().name());
                stmt.setString(8, flat.getTransport().name());
                stmt.setInt(9, houseId);
                stmt.setInt(10, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getLong("id");
                    }
                }
            }

            // Commit transaction
            conn.commit();
        } catch (SQLException e) {
            printer.printError("Ошибка при добавлении квартиры: " + e.getMessage());
            throw e;
        }
        return null;
    }

    public boolean modifyFlat(long flatId, Flat flat, int userId) throws SQLException {
        // Check access
        if (!hasAccess((int) flatId, userId)) {
            printer.printError("Нет доступа для изменения этой квартиры.");
            return false;
        }

        // Insert new Coordinates
        String coordinatesSql = "INSERT INTO Coordinates (x, y) VALUES (?, ?) RETURNING id";
        int coordId;
        try (Connection conn = this.getConnection();
             PreparedStatement coordStmt = conn.prepareStatement(coordinatesSql)) {
            coordStmt.setFloat(1, flat.getCoordinates().getX());
            coordStmt.setDouble(2, flat.getCoordinates().getY());
            try (ResultSet coordRs = coordStmt.executeQuery()) {
                if (coordRs.next()) {
                    coordId = coordRs.getInt("id");
                } else {
                    throw new SQLException("Не удалось создать запись в таблице Coordinates.");
                }
            }
        }

        // Insert new House
        String houseSql = "INSERT INTO Houses (name, year, number_of_floors, number_of_flats_on_floor) VALUES (?, ?, ?, ?) RETURNING id";
        int houseId;
        try (Connection conn = this.getConnection();
             PreparedStatement houseStmt = conn.prepareStatement(houseSql)) {
            House house = flat.getHouse();
            houseStmt.setString(1, house.getName());
            houseStmt.setInt(2, house.getYear());
            houseStmt.setLong(3, house.getNumberOfFloors());
            houseStmt.setLong(4, house.getNumberOfFlatsOnFloor());
            try (ResultSet houseRs = houseStmt.executeQuery()) {
                if (houseRs.next()) {
                    houseId = houseRs.getInt("id");
                } else {
                    throw new SQLException("Не удалось создать запись в таблице Houses.");
                }
            }
        }

        // Update Flat
        String sql = "UPDATE Flats SET name=?, coordinates_id=?, creation_date=?, area=?, number_of_rooms=?, time_to_metro_by_transport=?, view=?::View, transport=?::Transport, house_id=? WHERE id=? AND created_by_user=?";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, flat.getName());
            stmt.setInt(2, coordId);
            stmt.setDate(3, Date.valueOf(flat.getCreationDate()));
            stmt.setFloat(4, flat.getArea());
            stmt.setLong(5, flat.getNumberOfRooms());
            stmt.setFloat(6, flat.getTimeToMetroByTransport());
            stmt.setString(7, flat.getView().name());
            stmt.setString(8, flat.getTransport().name());
            stmt.setInt(9, houseId);
            stmt.setLong(10, flatId);
            stmt.setInt(11, userId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    public boolean removeFlat(long flatId, int userId) throws SQLException {
        // Check access
        if (!hasAccess((int) flatId, userId)) {
            printer.printError("Нет доступа для удаления этой квартиры.");
            return false;
        }

        String sql = "DELETE FROM Flats WHERE id = ? AND created_by_user = ?";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, flatId);
            stmt.setInt(2, userId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    public boolean registerUser(String username, String password) throws SQLException {
        String hashed = PasswordControl.hashPassword(password, username);
        if (userExists(username)) {
            // printer.printError("Пользователь с таким именем уже существует.");
            return false;
        }

        String sql = "INSERT INTO Users (user_name, password_hash) VALUES (?, ?)";
        try (Connection conn = this.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, hashed);
            stmt.executeUpdate();
            return true;
        }
    }

    public Integer getUserIdByFlatId(long flatId) throws SQLException {
        String sql = "SELECT created_by_user FROM Flats WHERE id = ?";
        try (Connection conn = this.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, flatId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("created_by_user");
                }
            }
        }
        return null;
    }

    public boolean hasAccess(int flatId, int userId) throws SQLException {
        Integer flatUserId = getUserIdByFlatId(flatId);
        return (flatUserId != null && flatUserId == userId) || userId == 1; // Assuming userId 1 is an admin
    }

    public Integer getUserIdFromUsername(String username) throws SQLException {
        String sql = "SELECT id FROM Users WHERE user_name = ?";
        try (Connection conn = this.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return null;
    }

    public HashSet<Flat> load() throws SQLException {
        HashSet<Flat> collection = new HashSet<>();
        try (Connection connection = this.getConnection();
             Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT f.*, c.x AS coord_x, c.y AS coord_y, h.name AS house_name, h.year AS house_year, h.number_of_floors AS house_number_of_floors, h.number_of_flats_on_floor AS house_number_of_flats_on_floor " +
                             "FROM Flats f " +
                             "JOIN Coordinates c ON f.coordinates_id = c.id " +
                             "JOIN Houses h ON f.house_id = h.id")) {
            while (rs.next()) {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                Float coordX = rs.getFloat("coord_x");
                Double coordY = rs.getDouble("coord_y");
                LocalDate creationDate = rs.getDate("creation_date").toLocalDate();
                float area = rs.getFloat("area");
                Long numberOfRooms = rs.getLong("number_of_rooms");
                float timeToMetroByTransport = rs.getFloat("time_to_metro_by_Transport");
                View view = View.valueOf(rs.getString("view"));
                Transport transport = Transport.valueOf(rs.getString("transport"));
                String houseName = rs.getString("house_name");
                int year = rs.getInt("house_year");
                Long numberOfFloors = rs.getLong("house_number_of_floors");
                long numberOfFlatsOnFloor = rs.getLong("house_number_of_flats_on_floor");

                Coordinates coordinates = new Coordinates(coordX, coordY);
                House house = new House(houseName, year, numberOfFloors, numberOfFlatsOnFloor);

                Flat flat = new Flat(id, name, coordinates, creationDate, area, numberOfRooms, timeToMetroByTransport, view, transport, house);
                collection.add(flat);
            }
            printer.println("Коллекция загружена из базы: " + collection.size() + " элементов.");
        } catch (SQLException e) {
            printer.printError("Ошибка загрузки коллекции из базы: " + e.getMessage());
            throw e;
        }
        return collection;
    }
}
