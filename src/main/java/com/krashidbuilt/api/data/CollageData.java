package com.krashidbuilt.api.data;

import com.krashidbuilt.api.helpers.ObjectMapper;
import com.krashidbuilt.api.model.Collage;
import com.krashidbuilt.api.model.ThrowableError;
import com.krashidbuilt.api.service.MySQL;
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

        String sql = "INSERT INTO collage (application_user_id, id, title, created)\n" +
                "VALUES (?, ?, ?, NOW())\n" +
                "ON DUPLICATE KEY UPDATE\n" +
                "   title = VALUES(title)";
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

    public static Collage getCollage(int userId) {
        logger.debug("GET COLLAGE BY USER ID {} START", userId);
        Collage collage = new Collage();

        MySQL db = new MySQL();

        String sql = "SELECT * FROM collage WHERE application_user_id = ?";
        try {
            db.setpStmt(db.getConn().prepareStatement(sql));
            db.getpStmt().setInt(1, userId);

            db.setRs(db.getpStmt().executeQuery());

            while (db.getRs().next()) {
                collage = ObjectMapper.collage(db.getRs());
            }

        } catch (SQLException e) {
            logger.error("UNABLE TO GET COLLAGE BY USER ID");
        }
        db.cleanUp();

        logger.debug("GET COLLAGE BY USER ID {} END", userId);
        return collage;
    }
}
