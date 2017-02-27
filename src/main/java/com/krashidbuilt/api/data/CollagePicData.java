package com.cgrahams.api.data;

import com.cgrahams.api.helpers.ObjectMapper;
import com.cgrahams.api.model.CollagePic;
import com.cgrahams.api.service.MySQL;
import com.cgrahams.api.model.ThrowableError;
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

    public static CollagePic getPic(int picId) {
        logger.debug("GET PIC WITH ID MATCHING {}", picId);
        CollagePic picture = new CollagePic();

        MySQL db = new MySQL();

        String sql = "SELECT * FROM picture WHERE id = ? LIMIT 1";

        try {
            db.setpStmt(db.getConn().prepareStatement(sql));
            db.getpStmt().setInt(1, picId);

            db.setRs(db.getpStmt().executeQuery());

            while (db.getRs().next()) {
                picture = ObjectMapper.collagePic(db.getRs());
            }

        } catch (SQLException ex) {
            logger.error("UNABLE TO GET PICTURE", ex);
        }

        db.cleanUp();

        logger.debug("GET PICTURE WITH ID {} END", picId);
        return picture;
    }

    public static CollagePic getRecentPic(int collageId) {
        logger.debug("GET MOST RECENT PICTURE OF COLLAGE WITH ID {}", collageId);
        CollagePic picture = new CollagePic();

        MySQL db = new MySQL();

        String sql = "SELECT * FROM picture WHERE collage_id = ? ORDER BY created DESC LIMIT 1";

        try {
            db.setpStmt(db.getConn().prepareStatement(sql));
            db.getpStmt().setInt(1, collageId);

            db.setRs(db.getpStmt().executeQuery());

            while (db.getRs().next()) {
                picture = ObjectMapper.collagePic(db.getRs());
            }

        } catch (SQLException e) {
            logger.error("UNABLE TO GET PICTURE", e);
        }

        db.cleanUp();

        logger.debug("GET MOST RECENT PICTURE OF COLLAGE WITH ID {} END", collageId);
        return picture;
    }
}
