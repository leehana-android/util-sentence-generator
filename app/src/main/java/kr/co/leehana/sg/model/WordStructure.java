package kr.co.leehana.sg.model;

/**
 * Created by Hana Lee on 2015-08-13 01:26
 *
 * @author Hana Lee
 * @since 2015-08-13 01:26
 */
public class WordStructure {

	public static Word[] availableWords = {new Noun(), new Verb(), new Adverb(), new Adjective()};
	public static boolean[] wordChecked = {true, true, true, true};
}
