package net.medrag.vocabulary.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * {@author} Stanislav Tretyakov
 * 13.11.2018
 */
@Controller
public class VocabularyViewController {

    @GetMapping("/")
    public String getVocabulary(){
        VocabularyController.resetVoc();
        return "vocabulary.html";
    }
}
