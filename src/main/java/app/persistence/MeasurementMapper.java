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

        String sql = "select * from length where length >= 220 order by length";

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
            throw new DatabaseException(e.getMessage());
        }
        return lengths;
    }

    public static List<Integer> getAllWidths(ConnectionPool connectionPool) throws DatabaseException
    {
        List<Integer> widths = new ArrayList<>();

        String sql = "select * from width where width >= 220 order by width";

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
            throw new DatabaseException(e.getMessage());
        }
        return widths;
    }

    public static List<Integer> getAllHeights(ConnectionPool connectionPool) throws DatabaseException
    {
        List<Integer> heights = new ArrayList<>();

        String sql = "select * from height order by height";

        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ResultSet rs = ps.executeQuery();
                while (rs.next())
                {
                    int height = rs.getInt("height");
                    heights.add(height);
                }
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException(e.getMessage());
        }
        return heights;
    }
}
