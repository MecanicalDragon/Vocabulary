package net.medrag.vocabulary.model;

/**
 * {@author} Stanislav Tretyakov
 * 13.11.2018
 */
public class VocabularyPair {

    private int id;
    private String word;
    private String translation;
    private boolean toLearn;

    public VocabularyPair(){}

    public VocabularyPair(String word, String translation) {
        this.word = word;
        this.translation = translation;
    }

    public VocabularyPair(int id, String word, String translation, boolean toLearn) {
        this.id = id;
        this.word = word;
        this.translation = translation;
        this.toLearn = toLearn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public boolean isToLearn() {
        return toLearn;
    }

    public void setToLearn(boolean toLearn) {
        this.toLearn = toLearn;
    }

    @Override
    public String toString() {
        return "VocabularyPair{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", translation='" + translation + '\'' +
                ", toLearn=" + toLearn +
                '}';
    }
}
