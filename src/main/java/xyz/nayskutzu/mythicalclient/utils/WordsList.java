package xyz.nayskutzu.mythicalclient.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class WordsList {

    /**
     * List of all bad words
     */
    private String[] words = new String[]{
            "shit",
            "fuck",
            "trash",
            "u suck",
            "you suck",
            "asshole",
            "bitch",
            "bastard",
            "cunt",
            "dick",
            "peasant",
            "kys",
            "pussy",
            "slut",
            "fag",
            "piss",
            "douche",
            "dickhead",
            "nigga",
            "nigger",
            "pula",
            "muie",
            "ez",
            "ezz",
            "kiddo",
            "skid",
            "pizda",
            "sugeo",
            "idiot",
            "cretin",
            "dobitoc",
            "curva",
    };


    /**
     * Bad words as an array list (so we can stream it)
     *
     * @return ArrayList of bad words
     */
    protected List<String> getWords() {
        List<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(words));
        return list;
    }

}