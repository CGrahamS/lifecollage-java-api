package com.krashidbuilt.api.data;

import com.krashidbuilt.api.helpers.ObjectMapper;
import com.krashidbuilt.api.model.Collage;
import com.krashidbuilt.api.model.ThrowableError;
import com.krashidbuilt.api.service.MySQL;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CGrahamS on 2/3/17.
 */

@SuppressFBWarnings("OBL_UNSATISFIED_OBLIGATION")
public class CollageData {

    private static Logger logger = LogManager.getLogger();

    public static Collage create(int userId, Collage in) throws ThrowableError {
        logger.debug("CREATE COLLAGE START");

        MySQL db = new MySQL();

        String sql = "INSERT INTO collage (application_user_id, id, title)\n" +
                "VALUES (?, ?, ?)";
        try {
            db.setpStmt(db.getConn().prepareStatement(sql));

            db.getpStmt().setInt(1, userId);
            db.getpStmt().setInt(2, 0);
            db.getpStmt().setString(3, in.getTitle());

            db.getpStmt().executeUpdate();

        } catch (SQLException e) {
            logger.error("UNABLE TO CREATE COLLAGE", e);
        }

        db.cleanUp();

        logger.debug("CREATE COLLAGE END");

        return in;
    }

    public static Collage getCollage(int collageId) {
        logger.debug("GET COLLAGE WITH ID {} START", collageId);
        Collage collage = new Collage();

        MySQL db = new MySQL();

        String sql = "SELECT * FROM collage WHERE id = ? LIMIT 1";
        try {
            db.setpStmt(db.getConn().prepareStatement(sql));
            db.getpStmt().setInt(1, collageId);

            db.setRs(db.getpStmt().executeQuery());

            while (db.getRs().next()) {
                collage = ObjectMapper.collage(db.getRs());
            }

        } catch (SQLException e) {
            logger.error("UNABLE TO GET COLLAGE BY ID");
        }
        db.cleanUp();

        logger.debug("GET COLLAGE WITH ID {} END", collageId);
        return collage;
    }

    public static Collage getMostRecentCollage() {
        logger.debug("GET MOST RECENT COLLAGE START");
        Collage collage = new Collage();

        MySQL db = new MySQL();

        String sql = "SELECT * FROM collage ORDER BY created DESC LIMIT 1";
        try {
            db.setpStmt(db.getConn().prepareStatement(sql));

            db.setRs(db.getpStmt().executeQuery());

            while (db.getRs().next()) {
                collage = ObjectMapper.collage(db.getRs());
            }

        } catch (SQLException e) {
            logger.error("UNABLE TO GET MOST RECENT COLLAGE");
        }
        db.cleanUp();

        logger.debug("GET MOST RECENT COLLAGE END");
        return collage;
    }

    public static List<Collage> getCollages(int userId) {
        logger.debug("GET COLLAGES BY USER ID {} START", userId);
        List<Collage> collages = new ArrayList<>();

        MySQL db = new MySQL();

        StringBuilder sql = new StringBuilder().append("SELECT * FROM collage ");
        if (userId >= 1) {
            sql.append("WHERE application_user_id = ?");
        }
        try {
            db.setpStmt(db.getConn().prepareStatement(sql.toString()));
            if (userId >= 1) {
                db.getpStmt().setInt(1, userId);
            }

            db.setRs(db.getpStmt().executeQuery());
            while (db.getRs().next()) {
                collages.add(ObjectMapper.collage(db.getRs()));
            }

        } catch (SQLException e) {
            logger.error("UNABLE TO GET COLLAGE BY USER ID");
        }
        db.cleanUp();

        logger.debug("GET COLLAGE BY USER ID {} END", userId);
        return collages;
    }

    public static Collage updateCollageTitle(Collage in) {
        logger.debug("UPDATE COLLAGE WITH ID {} START", in.getId());

        MySQL db = new MySQL();

        String sql = "UPDATE collage SET title = ? WHERE id = ?";

        try {
            db.setpStmt(db.getConn().prepareStatement(sql));

            db.getpStmt().setString(1, in.getTitle());
            db.getpStmt().setInt(2, in.getId());
            db.getpStmt().executeUpdate();

        } catch (SQLException e) {
            logger.error("UNABLE TO UPDATE COLLAGE", e);
        }
        db.cleanUp();

        logger.debug("UPDATE COLLAGE WITH ID {} END", in.getId());
        return getCollage(in.getId());
    }

    public static void deleteCollage(int collageId) {
        logger.debug("DELETE COLLAGE WITH ID {} START", collageId);

        MySQL db = new MySQL();

        String sql = "DELETE FROM collage WHERE id = ?";

        try {
            db.setpStmt(db.getConn().prepareStatement(sql));

            db.getpStmt().setInt(1, collageId);
            db.getpStmt().executeUpdate();

        } catch (SQLException e) {
            logger.error("UNABLE TO DELETE COLLAGE", e);
        }
        logger.debug("DELETE COLLAGE WITH ID {} END", collageId);
    }

}
