package app.persistence;

import app.entities.Status;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatusMapper {

    public static List <Status> getAllStatuses(ConnectionPool connectionPool) throws DatabaseException {

        List<Status> statusList = new ArrayList<>();

        String sql = "select * from status order by id";

        try(Connection connection = connectionPool.getConnection()) {
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String status = rs.getString("status");
                    statusList.add(new Status(id, status));
                }

            }

        } catch (SQLException e)
        {
            throw new DatabaseException(e.getMessage());
        }
        return statusList;
    }
}
