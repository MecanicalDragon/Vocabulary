package net.medrag.vocabulary.controller;

import net.medrag.vocabulary.model.VocabularyPair;
import net.medrag.vocabulary.model.VocabularyService;
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

    private static List<VocabularyPair> voc;
    private static int pairCounter;

    public static void resetVoc(){
        voc = null;
        pairCounter = 0;
    }

    @GetMapping(value = "/getWordList", produces = "application/json;charset=UTF-8")
    public VocabularyPair checkWord(@RequestParam String range) {
        if (voc == null || pairCounter == voc.size()) {
            voc = vocabularyService.getNewVoc(range);
            pairCounter = 0;
        }
        return voc.get(pairCounter++);
    }

    @PostMapping(value = "/addWord", produces = "text/plain")
    public String addWord(@RequestBody VocabularyPair pair) {
        return vocabularyService.saveNewPair(pair);
    }

    @GetMapping(value = "/getVocSize", produces = "text/plain")
    public String getVocSize() {
        return String.valueOf(voc.size());
    }

    @PostMapping(value = "/editWord", produces = "text/plain")
    public String editWord(@RequestBody VocabularyPair pair) {
        return vocabularyService.updatePair(pair);
    }

    @PostMapping(value = "/learnWord", produces = "text/plain")
    public String learnWord(@RequestBody VocabularyPair pair) {
        return vocabularyService.learnPair(pair);
    }
}
