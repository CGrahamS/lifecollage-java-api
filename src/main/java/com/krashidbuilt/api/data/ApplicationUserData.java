package com.krashidbuilt.api.data;

import com.krashidbuilt.api.helpers.ObjectMapper;
import com.krashidbuilt.api.model.*;
import com.krashidbuilt.api.model.Error;
import com.krashidbuilt.api.service.Encryption;
import com.krashidbuilt.api.service.MySQL;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben Kauffman on 1/15/2017.
 */
@SuppressFBWarnings("OBL_UNSATISFIED_OBLIGATION")
public final class ApplicationUserData {

    private static Logger logger = LogManager.getLogger();

    public static ApplicationUser create(ApplicationUser in) throws ThrowableError {
        logger.debug("CREATE USER START");

        MySQL db = new MySQL();

        String sql = "INSERT INTO application_user (id, email, password, first_name, last_name, username)\n" +
                "VALUES (?, ?, ?, ?, ?, ?)\n";
        try {

            db.setpStmt(db.getConn().prepareStatement(sql));

            db.getpStmt().setInt(1, 0);
            db.getpStmt().setString(2, in.getEmail());
            db.getpStmt().setString(3, Encryption.createHash(in.getPassword()));
            db.getpStmt().setString(4, in.getFirstName());
            db.getpStmt().setString(5, in.getLastName());
            db.getpStmt().setString(6, in.getUsername());

            db.getpStmt().executeUpdate();

        } catch (SQLException e) {
            logger.error("UNABLE TO CREATE USER", e);
            if(e.getMessage().contains("Duplicate entry")){
                String problem = e.getMessage().split("\'")[3];
                throw new ThrowableError(Error.duplicate(problem));
            }
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            logger.error("UNABLE TO CREATE USER, PASSWORD ENCRYPTION ERROR");
        }

        db.cleanUp();

        logger.debug("CREATE USER END");

        return AuthenticationData.login(in.getEmail(), in.getPassword());
    }

    public static ApplicationUser update(ApplicationUser in) {
        logger.debug("UPDATE USER WITH ID {} START", in.getId());

        MySQL db = new MySQL();

        String sql = "UPDATE application_user SET email = ? WHERE id = ?";

        try {

            db.setpStmt(db.getConn().prepareStatement(sql));
            db.getpStmt().setString(1, in.getEmail());
            db.getpStmt().setInt(2, in.getId());
            db.getpStmt().executeUpdate();

        } catch (SQLException e) {
            logger.error("UNABLE TO GET USER BY ID", e);
        }
        db.cleanUp();

        logger.debug("UPDATE USER WITH ID {} END", in.getId());
        return getById(in.getId());
    }

    public static void delete(int userId) {
        logger.debug("DELETE USER WITH ID {} START", userId);

        MySQL db = new MySQL();

        String sql = "DELETE FROM application_user WHERE id = ?";

        try {
            db.setpStmt(db.getConn().prepareStatement(sql));
            db.getpStmt().setInt(1, userId);
            db.getpStmt().executeUpdate();

        } catch (SQLException e) {
            logger.error("UNABLE TO DELETE USER  BY ID", e);
        }
        db.cleanUp();

        logger.debug("DELETE USER WITH ID {} END", userId);
    }

    public static ApplicationUser getById(int userId) {
        logger.debug("GET USER {} BY ID", userId);
        ApplicationUser user = new ApplicationUser();
        MySQL db = new MySQL();

        String sql = "SELECT * FROM application_user WHERE id = ? LIMIT 1";
        try {
            db.setpStmt(db.getConn().prepareStatement(sql));
            db.getpStmt().setInt(1, userId);

            db.setRs(db.getpStmt().executeQuery());

            while (db.getRs().next()) {
                user = ObjectMapper.applicationUser(db.getRs());
            }

        } catch (SQLException e) {
            logger.error("UNABLE TO GET USER BY ID", e);
        }

        db.cleanUp();

        logger.debug("GET USER {} BY ID END", userId);
        return user;
    }

    public static List<PublicApplicationUser> getByUsername(String queryString) {
        logger.debug("GET USER WITH USERNAME MATCHING QUERY STRING {}", queryString);
        List<PublicApplicationUser> users = new ArrayList<>();
        MySQL db = new MySQL();

        String sql = "SELECT * FROM application_user WHERE username LIKE ?";
        try {
            db.setpStmt(db.getConn().prepareStatement(sql));
            db.getpStmt().setString(1, "%" + queryString + "%");

            db.setRs(db.getpStmt().executeQuery());
            while (db.getRs().next()){
                users.add(ObjectMapper.publicApplicationUser(db.getRs()));
            }


        } catch (SQLException e) {
            logger.error("UNABLE TO GET USERS MATCHING QUERY STRING", e);
        }

        db.cleanUp();

        logger.debug("GET USER WITH USERNAME MATCHING QUERY STRING {} END", queryString);
        return users;
    }

    public static ApplicationUser getByEmail(String email) {
        logger.debug("GET USER {} BY EMAIL", email);
        ApplicationUser user = new ApplicationUser();
        MySQL db = new MySQL();

        String sql = "SELECT * FROM application_user WHERE email = ? LIMIT 1";
        try {

            db.setpStmt(db.getConn().prepareStatement(sql));
            db.getpStmt().setString(1, email);

            db.setRs(db.getpStmt().executeQuery());

            while (db.getRs().next()) {
                user = ObjectMapper.applicationUser(db.getRs());
            }

        } catch (SQLException e) {
            logger.error("UNABLE TO GET USER BY EMAIL", e);
        }

        db.cleanUp();

        logger.debug("GET USER {} BY EMAIL", email);
        return user;
    }

}
