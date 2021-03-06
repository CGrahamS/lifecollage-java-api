package com.krashidbuilt.api.data;

import com.krashidbuilt.api.helpers.ObjectMapper;
import com.krashidbuilt.api.model.Collage;
import com.krashidbuilt.api.model.CollageLatestPic;
import com.krashidbuilt.api.service.MySQL;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CGrahamS on 2/20/17.
 */

@SuppressFBWarnings("OBL_UNSATISFIED_OBLIGATION")
public class PublicCollageData {
    private static Logger logger = LogManager.getLogger();

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

    public static List<CollageLatestPic> getCollageLatestPic(int userId) {
        logger.debug("GET COLLAGES WITH THE NEWEST PIC BY USER ID {} START", userId);
        List<CollageLatestPic> list = new ArrayList<>();

        MySQL db = new MySQL();

        StringBuilder sql = new StringBuilder().append("" +
                "SELECT  " +
                "  c.id                  AS collage_id,  " +
                "  c.title               AS collage_title,  " +
                "  c.created             AS collage_created,  " +
                "  c.application_user_id AS collage_application_user_id,  " +
                "  " +
                "  p.id                  AS picture_id,  " +
                "  p.location            AS picture_location,  " +
                "  MAX(p.created)        AS picture_created  " +
                "  " +
                "FROM  " +
                "  collage AS c  " +
                "  LEFT JOIN picture AS p ON c.id = p.collage_id  " +
                "  ");

        if (userId >= 1) {
            sql.append(" WHERE application_user_id = ? ");
        }

        sql.append("" +
                "GROUP BY  " +
                "  c.id  " +
                "  " +
                "ORDER BY  " +
                "  picture_created DESC");

        try {
            db.setpStmt(db.getConn().prepareStatement(sql.toString()));
            if (userId >= 1) {
                db.getpStmt().setInt(1, userId);
            }

            db.setRs(db.getpStmt().executeQuery());
            while (db.getRs().next()) {
                CollageLatestPic clp = new CollageLatestPic();
                clp.setCollage(ObjectMapper.latestCollage(db.getRs()));
                clp.setCollagePic(ObjectMapper.latestCollagePic(db.getRs()));
                list.add(clp);
            }

        } catch (SQLException e) {
            logger.error("UNABLE TO GET COLLAGES WITH THE NEWEST PIC BY USER ID", e);
        }
        db.cleanUp();

        logger.debug("GET COLLAGES WITH THE NEWEST PIC BY USER ID {} END", userId);
        return list;
    }

}
