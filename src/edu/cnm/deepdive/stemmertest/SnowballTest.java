package edu.cnm.deepdive.stemmertest;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

public class SnowballTest {//read text, stem every word, write out word

  private static final String[] EXCLUSIONS = {"after",
      "all", "although", "and", "another", "any", "anybody", "anyone", "anything", "aren't", "astride",
      "atop", "bar", "because", "before", "behind", "below", "beneath", "beside", "besides", "between",
      "beyond", "both", "but", "can't", "come", "couldn't", "despite", "didn't", "doesn't", "don't",
      "down", "during", "each", "either", "even", "though", "everybody", "everyone", "everything",
      "except", "few", "for", "from", "hadn't", "hasn't", "haven't", "he'd", "he'll", "he's", "her",
      "hers", "herself", "him", "himself", "his", "i'd", "i'll", "i'm", "i've", "inside", "into", "isn't",
      "its", "itself", "less", "let's", "like", "many", "mine", "mightn't", "minus", "more", "most", "much",
      "mustn't", "myself", "near", "nearer", "nearest", "neither", "nobody", "none", "nor", "nothing",
      "off", "once", "one", "onto", "ontop", "opposite", "other", "others", "our", "ours", "ourselves",
      "out", "outside", "over", "pace", "past", "per", "plus", "post", "pre", "sans", "sauf", "save",
      "several", "shan't", "she", "she'd", "she'll", "she's", "short", "shouldn't", "since", "sithence",
      "some", "somebody", "someone", "something", "than", "that", "that's", "their", "theirs", "them",
      "themselves", "there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", "those",
      "though", "through", "throughout", "thru", "thruout", "til", "till", "toward", "under", "underneath",
      "unless", "unlike", "until", "unto", "upon", "upside", "versus", "via", "vice versa", "we'd",
      "we're", "we've", "weren't", "what", "what'll", "what're", "what's", "what've", "whatever", "when",
      "whenever", "where's", "wherever", "whether", "which", "whichever", "while", "who", "who'll",
      "who're", "who's", "who've", "whoever", "whom", "whomever", "whose", "with", "within", "without",
      "won't", "wouldn't", "yet", "you", "you'd", "you'll", "you're", "you've", "your", "yours",
      "yourself", "yourselves"}; //conjunctions, contractions, preposition, proposition, pronouns

  public static void main(String[] args) throws IOException, URISyntaxException {
    Path path = new File(SnowballTest.class.getClassLoader()
        .getResource("resources/five-orange-pips.txt").toURI()).toPath();
    List<String> words = getStemmedContents(path);
    System.out.println(words);
  }

  private static List<String> getStemmedContents(Path path) throws IOException {
    Pattern splitter = Pattern
        .compile("['\"()\\[\\]\\.,;]*\\s+['\"()\\[\\]\\.,;]*"); //split symbols
    SnowballStemmer stemmer = new englishStemmer();
    List<String> exclusion = Arrays.asList(EXCLUSIONS);
    return Files.lines(path)
        .map(line -> splitter
            .splitAsStream(line))//pipe line of string then convert (split) to stream of words
        .flatMap(Function
            .identity()) //reduce the size with flatmap to single pipeline word. if you have one line of word then you dont need this flatmap, can also put filter after this
        .map(word -> { // map each word
          stemmer.setCurrent(word);
          return stemmer.stem() ? stemmer.getCurrent().toLowerCase() : null; //
        })
        .filter(Objects::nonNull)
        .filter(word -> word.length() > 2)
        .filter(word -> !exclusion.contains(word))
        .collect(
            Collectors.toList()); //turn collection to a list, which also processes the pipe line
  }

}
