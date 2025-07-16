package net.medrag.vocabulary.controller;

import net.medrag.vocabulary.model.PairLearnDto;
import net.medrag.vocabulary.model.VocabularyPair;
import net.medrag.vocabulary.service.VocabularyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * {@author} Stanislav Tretyakov
 * 13.11.2018
 */
@RestController
public class VocabularyController {

    private final VocabularyService vocabularyService;

    @Autowired
    public VocabularyController(VocabularyService vocabularyService) {
        this.vocabularyService = vocabularyService;
    }

    @GetMapping(value = "/getWordList", produces = "application/json;charset=UTF-8")
    public List<VocabularyPair> checkWord(@RequestParam String range) {
        return vocabularyService.getNewVoc(range);
    }

    @PostMapping(value = "/addWord", produces = "text/plain")
    public String addWord(@RequestBody VocabularyPair pair) {
        return vocabularyService.saveNewPair(pair);
    }

    @PostMapping(value = "/editWord", produces = "text/plain")
    public String editWord(@RequestBody VocabularyPair pair) {
        return vocabularyService.updatePair(pair);
    }

    @PostMapping(value = "/learnWord", produces = "text/plain")
    public String learnWord(@RequestBody PairLearnDto pair) {
        return vocabularyService.learnPair(pair);
    }
}
