package com.krashidbuilt.api.helpers;

import com.krashidbuilt.api.model.ApplicationUser;
import com.krashidbuilt.api.model.Collage;
import com.krashidbuilt.api.model.CollagePic;
import com.krashidbuilt.api.model.PublicApplicationUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ben Kauffman on 1/15/2017.
 */
public class ObjectMapper {

    private static Logger logger = LogManager.getLogger();


    public static PublicApplicationUser publicApplicationUser(ResultSet rs) throws SQLException {
        PublicApplicationUser object = new PublicApplicationUser();

        try {
            object.setId(rs.getInt("id"));
            object.setFirstName(rs.getString("first_name"));
            object.setLastName(rs.getString("last_name"));
            object.setUsername(rs.getString("username"));

        } catch (SQLException ex) {
            logger.error("UNABLE TO GET PUBLIC APPLICATION USER FROM THE RESULT SET ", ex);
        }

        return object;
    }

    public static ApplicationUser applicationUser(ResultSet rs) throws SQLException {
        ApplicationUser object = new ApplicationUser();

        try {
            object.setId(rs.getInt("id"));
            object.setFirstName(rs.getString("first_name"));
            object.setLastName(rs.getString("last_name"));
            object.setEmail(rs.getString("email"));
            object.setUsername(rs.getString("username"));

            //don't display password information for any user ... EVER!

            object.setCreated(rs.getString("created"));
            object.setUpdated(rs.getString("updated"));

        } catch (SQLException ex) {
            logger.error("UNABLE TO GET APPLICATION USER FROM THE RESULT SET ", ex);
        }

        return object;
    }

    public static Collage collage(ResultSet rs) throws SQLException {
        Collage object = new Collage();

        try {
            object.setId(rs.getInt("id"));
            object.setTitle(rs.getString("title"));
            object.setCreated(rs.getString("created"));
            object.setUserId(rs.getInt("application_user_id"));
        } catch (SQLException ex) {
            logger.error("UNABLE TO GET COLLAGE FROM RESULT SET", ex);
        }

        return object;
    }

    public static Collage latestCollage(ResultSet rs) throws SQLException {
        Collage object = new Collage();

        try {
            object.setId(rs.getInt("collage_id"));
            object.setTitle(rs.getString("collage_title"));
            object.setCreated(rs.getString("collage_created"));
            object.setUserId(rs.getInt("collage_application_user_id"));
        } catch (SQLException ex) {
            logger.error("UNABLE TO GET COLLAGE FROM RESULT SET", ex);
        }

        return object;
    }

    public static CollagePic collagePic(ResultSet rs) throws SQLException {
        CollagePic object = new CollagePic();

        try {
            object.setId(rs.getInt("id"));
            object.setCollageId(rs.getInt("collage_id"));
            object.setLocation(rs.getString("location"));
        } catch (SQLException ex) {
            logger.error("UNABLE TO GET PICTURE FROM RESULT SET", ex);
        }

        return object;
    }

    public static CollagePic latestCollagePic(ResultSet rs) throws SQLException {
        CollagePic object = new CollagePic();

        try {
            object.setId(rs.getInt("picture_id"));
            object.setCollageId(rs.getInt("collage_id"));
            object.setLocation(rs.getString("picture_location"));
            object.setCreated(rs.getString("picture_created"));
        } catch (SQLException ex) {
            logger.error("UNABLE TO GET PICTURE FROM RESULT SET", ex);
        }
        return object;
     }

    public static ArrayList<Collage> collages(ResultSet rs) throws SQLException {
        Collage object = new Collage();
        ArrayList<Collage> collages = new ArrayList<>();

        try {
            object.setId(rs.getInt("id"));
            object.setTitle(rs.getString("title"));
            object.setCreated(rs.getString("created"));
            collages.add(object);
        } catch (SQLException ex) {
            logger.error("UNABLE TO GET COLLAGES FROM RESULT SET", ex);
        }
        return collages;
    }


    public static HashMap<String, String> keyStore(ResultSet rs) throws SQLException {
        HashMap<String, String> map = new HashMap<String, String>();
        while (rs.next()) {
            map.put(rs.getString("data_key"), rs.getString("data_value"));
        }
        return map;
    }


    public static List<HashMap<String, Object>> convertResultSetToList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

        while (rs.next()) {
            HashMap<String, Object> row = new HashMap<String, Object>(columns);
            for (int i = 1; i <= columns; ++i) {
                row.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(row);
        }
        return list;
    }


    @SuppressWarnings("PMD.EmptyCatchBlock")
    public static boolean isNull(String columnName, ResultSet rs) {
        String value = null;
        try {
            value = rs.getString(columnName);
        } catch (SQLException e) {
            //ignore error
//            logger.debug(e);
        }

        return value == null;
    }

}
