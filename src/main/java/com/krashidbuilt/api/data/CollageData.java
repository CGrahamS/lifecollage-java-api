package com.cgrahams.api.data;

import com.cgrahams.api.model.Collage;
import com.cgrahams.api.model.ThrowableError;
import com.cgrahams.api.service.MySQL;
import com.mysql.jdbc.PreparedStatement;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

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
            db.setpStmt(db.getConn().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS));

            db.getpStmt().setInt(1, userId);
            db.getpStmt().setInt(2, 0);
            db.getpStmt().setString(3, in.getTitle());

            in.setId(db.executeUpdateGetLastInsertedId());

        } catch (SQLException e) {
            logger.error("UNABLE TO CREATE COLLAGE", e);
        }

        db.cleanUp();

        logger.debug("CREATE COLLAGE END");

        return in;
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
        return PublicCollageData.getCollage(in.getId());
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
        db.cleanUp();
    }

    public static Collage updateCollageOwner(int collageId, int userId) {
        logger.debug("UPDATE OWNER OF COLLAGE WITH ID {} START");
        MySQL db = new MySQL();

        String sql = "UPDATE collage SET application_user_id = ? WHERE id = ?";

        try {
            db.setpStmt(db.getConn().prepareStatement(sql));

            db.getpStmt().setInt(1, userId);
            db.getpStmt().setInt(2, collageId);
            db.getpStmt().executeUpdate();

        } catch (SQLException e) {
            logger.error("UNABLE TO UPDATE COLLAGE OWNER", e);
        }
        logger.debug("UPDATE OWNER OF COLLAGE WITH ID {} END", collageId);
        db.cleanUp();
        return PublicCollageData.getCollage(collageId);
    }

}
