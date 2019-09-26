package net.medrag.vocabulary.model;

import net.medrag.vocabulary.Vocabulary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * {@author} Stanislav Tretyakov
 * 13.11.2018
 */
@Service
public class VocabularyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VocabularyService.class);
    private static final Logger DB_FILLER = LoggerFactory.getLogger("BaseFiller");
    private static final String dbURL = "jdbc:h2:../src/main/resources/static/db/vocabulary";
    private static final String SELECT = "SELECT * FROM PAIR;";
    private static final String LEARN = "SELECT * FROM PAIR WHERE TO_LEARN = TRUE;";
    private static final String LIMIT = "SELECT * FROM PAIR ORDER BY ID LIMIT %s OFFSET %s;";
    private static final String INSERT = "INSERT INTO PAIR (WORD, TRANSLATION) VALUES (?, ?);";
    private static final String UPDATE = "update pair set word = ?, translation = ? where id = ?;";
    private static final String TO_LEARN = "update pair set to_learn = ? where id = ?;";
    private static final String TEMPLATE = "INSERT INTO PAIR (WORD, TRANSLATION ) VALUES ('%s','%s');";

    /**
     * |---------------------------------   IMPORTANT   -------------------------------------|
     * |    Program launch, that creates new database must be executed with command below:   |
     * |             java -jar -Dfile.encoding=UTF-8 vocabulary-0.0.1-SNAPSHOT.jar           |
     * |-------------------------------------------------------------------------------------|
     */
    @PostConstruct
    public void init() throws SQLException {

        DriverManager.registerDriver(new org.h2.Driver());
        try (Connection connection = DriverManager.getConnection(dbURL)) {

//                  Checking database for existing
            try (Statement statement = connection.createStatement()) {
                LOGGER.info("Checking database...");
                statement.executeQuery(SELECT);
                LOGGER.info("Database already exists.");
            } catch (SQLException e) {

//                    If database doesn't exist... prepare statement.
                LOGGER.info("Database doesn't exist. Creating new database...");
                StringBuilder query = new StringBuilder();
                try (InputStream is = getClass().getResourceAsStream("/static/db/create_db.sql");
                     BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                    while (br.ready()) {
                        query.append(br.readLine());
                    }
                }

//                    Creating new database
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate(query.toString());
                    LOGGER.info("New database was created.");
                } catch (Exception ee) {
                    LOGGER.info("Database wasn't created.", ee);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Now happened something really unexpected. Database wasn't created.", e);
        } catch (SQLException e) {
            LOGGER.error("Could not set connection");
        }
    }

    public List<VocabularyPair> getNewVoc(String range) {

        if (range.equals("shutdown")) {
            Vocabulary.shutDown();
        }

        String vocMode = "mixed";

        if (range.contains("-en") && range.substring(range.length() - 3).equals("-en") ||
                range.contains("-ут") && range.substring(range.length() - 3).equals("-ут")) {
            vocMode = "en-ru";
            range = range.substring(0, range.length() - 3);
        } else if (range.contains("-ru") && range.substring(range.length() - 3).equals("-ru") ||
                range.contains("-кг") && range.substring(range.length() - 3).equals("-кг")) {
            vocMode = "ru-en";
            range = range.substring(0, range.length() - 3);
        }

        String request = range.matches("\\d+\\-\\d+") ? String.format(LIMIT,
                new Integer(range.split("-")[1]) - new Integer(range.split("-")[0]),
                range.split("-")[0]) : range.equalsIgnoreCase("learn") ||
                range.equalsIgnoreCase("дуфкт") ? LEARN : SELECT;

        List<VocabularyPair> voc = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(dbURL)) {
            try (Statement statement = connection.createStatement()) {
                LOGGER.info("Getting vocabulary");
                ResultSet rs = statement.executeQuery(request);
                while (rs.next()) {
                    voc.add(new VocabularyPair(rs.getInt("ID"), rs.getString("WORD"), rs.getString("TRANSLATION"), rs.getBoolean("TO_LEARN")));
                }
                if (voc.size() == 0) {
                    LOGGER.info("Ranging data was incorrect.");
                    return getNewVoc("all");
                }
                LOGGER.info("Vocabulary size: " + voc.size());
            } catch (SQLException e) {
                LOGGER.error("Could not get vocabulary with parameter " + range);

            }
        } catch (SQLException e) {
            LOGGER.error("Could not set connection");
        }
        Collections.shuffle(voc);
        mix(voc, vocMode);
        return voc;
    }

    private void mix(List<VocabularyPair> voc, String vocMode) {
        String temp;
        switch (vocMode) {
            case "ru-en":
                for (VocabularyPair vocabularyPair : voc) {
                    temp = vocabularyPair.word;
                    vocabularyPair.word = vocabularyPair.translation;
                    vocabularyPair.translation = temp;
                }
                break;
            case "mixed":
                int random;
                for (VocabularyPair vocabularyPair : voc) {
                    random = new Random().nextInt();
                    if (random > 0.5) {
                        temp = vocabularyPair.word;
                        vocabularyPair.word = vocabularyPair.translation;
                        vocabularyPair.translation = temp;
                    }
                }
                break;
            default:
        }

    }

    public String saveNewPair(VocabularyPair pair) {

        try (Connection connection = DriverManager.getConnection(dbURL)) {
            try (PreparedStatement statement = connection.prepareStatement(INSERT)) {
                LOGGER.info("Saving new pair: " + pair);
                statement.setString(1, pair.getWord());
                statement.setString(2, pair.getTranslation());
                statement.executeUpdate();
                LOGGER.info("Successfully saved");
                DB_FILLER.info(String.format(TEMPLATE, pair.word, pair.translation));
                return "Successfully saved pair of '" + pair.getWord() + "' and '" + pair.getTranslation() + "'.";
            } catch (SQLException e) {
                LOGGER.error("Not saved. What's happened?");
            }
        } catch (SQLException e) {
            LOGGER.error("Could not set connection");
        }
        return "Not saved. Something went wrong";
    }

    public String updatePair(VocabularyPair pair) {

        try (Connection connection = DriverManager.getConnection(dbURL)) {
            try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
                LOGGER.info("Updating pair: " + pair);
                statement.setString(1, pair.getWord());
                statement.setString(2, pair.getTranslation());
                statement.setInt(3, pair.getId());
                statement.executeUpdate();
                LOGGER.info("Successfully updated");
                return "Pair updated: " + pair;
            } catch (SQLException e) {
                LOGGER.error("Not saved. What's happened?");
            }
        } catch (SQLException e) {
            LOGGER.error("Could not set connection");
        }
        return "Not saved. Something went wrong";
    }

    public String learnPair(VocabularyPair pair) {

        try (Connection connection = DriverManager.getConnection(dbURL)) {
            try (PreparedStatement statement = connection.prepareStatement(TO_LEARN)) {
                LOGGER.info("Learning pair: " + pair);
                statement.setBoolean(1, pair.isToLearn());
                statement.setInt(2, pair.getId());
                statement.executeUpdate();
                LOGGER.info("Learning status successfully changed: ");
                return "Learning status successfully changed: " + pair;
            } catch (SQLException e) {
                LOGGER.error("Not saved. What's happened?");
            }
        } catch (SQLException e) {
            LOGGER.error("Could not set connection");
        }
        return "Not saved. Something went wrong";
    }
}
