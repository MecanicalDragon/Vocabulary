package net.medrag.vocabulary.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Stanislav Tretiakov
 * 16.07.2025
 */
@Data
@AllArgsConstructor
public class PairLearnDto {
    private int id;
    private boolean toLearn;
}
