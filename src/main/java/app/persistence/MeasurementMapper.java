package app.persistence;

import app.exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MeasurementMapper {
    public static List<Integer> getAllLengths(ConnectionPool connectionPool) throws DatabaseException
    {
        List<Integer> lengths = new ArrayList<>();

        String sql = "select * from length order by id";

        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ResultSet rs = ps.executeQuery();
                while (rs.next())
                {
                    int length = rs.getInt("length");
                    lengths.add(length);
                }
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("Fejl");
        }
        return lengths;
    }

    public static List<Integer> getAllWidths(ConnectionPool connectionPool) throws DatabaseException
    {
        List<Integer> widths = new ArrayList<>();

        String sql = "select * from width order by id";

        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ResultSet rs = ps.executeQuery();
                while (rs.next())
                {
                    int width = rs.getInt("width");
                    widths.add(width);
                }
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("Fejl");
        }
        return widths;
    }

    public static List<Integer> getAllHeights(ConnectionPool connectionPool) throws DatabaseException
    {
        List<Integer> heights = new ArrayList<>();

        String sql = "select * from hight order by id";

        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ResultSet rs = ps.executeQuery();
                while (rs.next())
                {
                    int height = rs.getInt("high");
                    heights.add(height);
                }
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("Fejl");
        }
        return heights;
    }
}
