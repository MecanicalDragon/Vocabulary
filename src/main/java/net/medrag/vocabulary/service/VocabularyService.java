package net.medrag.vocabulary.service;

import net.medrag.vocabulary.model.UserProps;
import net.medrag.vocabulary.model.VocProps;
import net.medrag.vocabulary.model.VocabularyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@author} Stanislav Tretyakov
 * 13.11.2018
 */
@Service
public class VocabularyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VocabularyService.class);
    private static final String SELECT = "SELECT * FROM VOCABULARY ORDER BY RAND() LIMIT ?";
    private static final String ADD_LEARN = "INSERT INTO LEARNINGS (SUB_ID, WORD_ID) VALUES (?, ?)";
    private static final String REMOVE_LEARN = "DELETE FROM LEARNINGS WHERE SUB_ID = ? AND WORD_ID = ?";
    private static final String GET_LEARN = "SELECT * FROM VOCABULARY WHERE ID IN (SELECT WORD_ID FROM LEARNINGS WHERE SUB_ID = ?)";

    private final VocProps vocProps;
    private final UserProps userProps;

    @Autowired
    public VocabularyService(VocProps vocProps, UserProps userProps) {
        this.vocProps = vocProps;
        this.userProps = userProps;
    }

    public List<VocabularyPair> getNewVoc(String range) {
        if ("learn".equals(range)) {
            return wordsToLearn();
        }
        int iRange = Math.min(100, Math.abs(Integer.parseInt(range)));
        if (iRange == 0) iRange = 100;
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
        try (Connection connection = DriverManager.getConnection(vocProps.getDbUrl());
             PreparedStatement statement = connection.prepareStatement(pair.isToLearn() ? ADD_LEARN : REMOVE_LEARN);
        ) {
            statement.setInt(1, userProps.getUser());
            statement.setInt(2, pair.getId());
            statement.executeUpdate();
            return pair.isToLearn() ? "Added to the learn list" : "Removed from the learn list";
        } catch (SQLException e) {
            LOGGER.error("Could not set connection");
            return e.getMessage();
        }
    }

    private List<VocabularyPair> wordsToLearn() {
        final List<VocabularyPair> voc = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(vocProps.getDbUrl());
             PreparedStatement statement = connection.prepareStatement(GET_LEARN);
        ) {
            statement.setInt(1, userProps.getUser());
            final ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                voc.add(new VocabularyPair(rs.getInt("ID"), rs.getString("LANG2"), rs.getString("LANG1"), true));
            }
            LOGGER.info("Words to learn: {}", voc.size());
        } catch (SQLException e) {
            LOGGER.error("Could not get vocabulary for learning", e);
        }
        Collections.shuffle(voc);
        return voc;
    }
}
