package com.cgrahams.api.data;

import com.cgrahams.api.model.CollagePic;
import com.cgrahams.api.helpers.ObjectMapper;
import com.cgrahams.api.service.MySQL;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CGrahamS on 2/17/17.
 */

@SuppressFBWarnings("OBL_UNSATISFIED_OBLIGATION")
public class PublicCollagePicData {
    private static Logger logger = LogManager.getLogger();

    public static List<CollagePic> getCollagePics(int collageId) {
        logger.debug("GET ALL PICTURES WITH COLLAGE ID {} START");
        List<CollagePic> pictures = new ArrayList<>();

        MySQL db = new MySQL();

        String sql = "SELECT * FROM picture WHERE collage_id =?";

        try {
            db.setpStmt(db.getConn().prepareStatement(sql));

            db.getpStmt().setInt(1, collageId);

            db.setRs(db.getpStmt().executeQuery());
            while (db.getRs().next()) {
                pictures.add(ObjectMapper.collagePic(db.getRs()));
            }
        } catch (SQLException e) {
            logger.error("UNABLE TO FIND PICTURES",  e);
        }

        logger.debug("GET ALL PICTURES WITH COLLAGE ID {} END", collageId);

        db.cleanUp();
        return pictures;
    }
}
