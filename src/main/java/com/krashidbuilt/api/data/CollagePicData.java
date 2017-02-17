package com.krashidbuilt.api.data;

import com.krashidbuilt.api.model.CollagePic;
import com.krashidbuilt.api.model.ThrowableError;
import com.krashidbuilt.api.service.MySQL;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

/**
 * Created by CGrahamS on 2/16/17.
 */

@SuppressFBWarnings("OBL_UNSATISFIED_OBLIGATION")
public class CollagePicData {

    private static Logger logger = LogManager.getLogger();

    public static CollagePic create(int collageId, CollagePic in) throws ThrowableError {
        logger.debug("CREATE PICTURE START");

        MySQL db = new MySQL();

        String sql = "INSERT INTO picture (collage_id, id, location)\n" +
                "VALUES(? ,? ,?)";

        try {
            db.setpStmt(db.getConn().prepareStatement(sql));
            db.getpStmt().setInt(1, collageId);
            db.getpStmt().setInt(2, 0);
            db.getpStmt().setString(3, in.getLocation());

            db.getpStmt().executeUpdate();

        } catch (SQLException e) {
            logger.error("UNABLE TO CREATE PICTURE", e);
        }

        db.cleanUp();

        logger.debug("CREATE PICTURE END");

        return in;
    }
}
