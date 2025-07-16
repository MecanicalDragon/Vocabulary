package net.medrag.vocabulary.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * {@author} Stanislav Tretyakov
 * 13.11.2018
 */
@Data
@AllArgsConstructor
public class VocabularyPair {
    private int id;
    private String word;
    private String translation;
    private String examples;
}
