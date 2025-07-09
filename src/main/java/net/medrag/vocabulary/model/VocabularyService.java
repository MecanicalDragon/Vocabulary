package net.medrag.vocabulary.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * {@author} Stanislav Tretyakov
 * 13.11.2018
 */
@Service
public class VocabularyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VocabularyService.class);
    private static final String SELECT = "SELECT * FROM VOCABULARY ORDER BY RAND() LIMIT ?";
    private static final String LEARN = "SELECT * FROM PAIR WHERE TO_LEARN = TRUE;";
    private static final String LIMIT = "SELECT * FROM PAIR ORDER BY ID LIMIT %s OFFSET %s;";
    private static final String INSERT = "INSERT INTO VOCABULARY (WORD, TRANSLATION) VALUES (?, ?);";
    private static final String UPDATE = "update pair set word = ?, translation = ? where id = ?;";
    private static final String TO_LEARN = "update pair set to_learn = ? where id = ?;";
    private static final String TEMPLATE = "INSERT INTO PAIR (WORD, TRANSLATION ) VALUES ('%s','%s');";

    private final VocProps vocProps;

    @Autowired
    public VocabularyService(VocProps vocProps) {
        this.vocProps = vocProps;
    }

    public List<VocabularyPair> getNewVoc(String range) {
        final var iRange = Math.max(100, Integer.parseInt(range));
        List<VocabularyPair> voc = new ArrayList<>(iRange);

        try (Connection connection = DriverManager.getConnection(vocProps.getDbUrl());
             PreparedStatement statement = connection.prepareStatement(SELECT);
        ) {
            LOGGER.info("Getting vocabulary of range {}...", iRange);
            statement.setInt(1, iRange);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                voc.add(new VocabularyPair(rs.getInt("ID"), rs.getString("LANG2"), rs.getString("LANG1"), true));
            }
            LOGGER.info("Vocabulary size: {}", voc.size());
        } catch (SQLException e) {
            LOGGER.error("Could not get vocabulary of size {}", iRange, e);
        }
        return voc;
    }

    public String saveNewPair(VocabularyPair pair) {

//        try (Connection connection = DriverManager.getConnection(DB_URL)) {
//            try (PreparedStatement statement = connection.prepareStatement(INSERT)) {
//                LOGGER.info("Saving new pair: " + pair);
//                statement.setString(1, pair.getWord());
//                statement.setString(2, pair.getTranslation());
//                statement.executeUpdate();
//                LOGGER.info("Successfully saved");
//                return "Successfully saved pair of '" + pair.getWord() + "' and '" + pair.getTranslation() + "'.";
//            } catch (SQLException e) {
//                LOGGER.error("Not saved. What's happened?");
//            }
//        } catch (SQLException e) {
//            LOGGER.error("Could not set connection");
//        }
        return "Not implemented";
    }

    public String updatePair(VocabularyPair pair) {

//        try (Connection connection = DriverManager.getConnection(DB_URL)) {
//            try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
//                LOGGER.info("Updating pair: " + pair);
//                statement.setString(1, pair.getWord());
//                statement.setString(2, pair.getTranslation());
//                statement.setInt(3, pair.getId());
//                statement.executeUpdate();
//                LOGGER.info("Successfully updated");
//                return "Pair updated: " + pair;
//            } catch (SQLException e) {
//                LOGGER.error("Not saved. What's happened?");
//            }
//        } catch (SQLException e) {
//            LOGGER.error("Could not set connection");
//        }
        return "Not implemented";
    }

    public String learnPair(VocabularyPair pair) {

//        try (Connection connection = DriverManager.getConnection(DB_URL)) {
//            try (PreparedStatement statement = connection.prepareStatement(TO_LEARN)) {
//                LOGGER.info("Learning pair: " + pair);
//                statement.setBoolean(1, pair.isToLearn());
//                statement.setInt(2, pair.getId());
//                statement.executeUpdate();
//                LOGGER.info("Learning status successfully changed: ");
//                return "Learning status successfully changed: " + pair;
//            } catch (SQLException e) {
//                LOGGER.error("Not saved. What's happened?");
//            }
//        } catch (SQLException e) {
//            LOGGER.error("Could not set connection");
//        }
        return "Not implemented";
    }
}
