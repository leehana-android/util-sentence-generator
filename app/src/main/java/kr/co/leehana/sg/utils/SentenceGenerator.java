package kr.co.leehana.sg.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import kr.co.leehana.sg.model.Words;
import kr.co.leehana.sg.service.IWordService;
import kr.co.leehana.sg.type.GenreType;
import kr.co.leehana.sg.type.WordType;

/**
 * Created by Hana Lee on 2015-08-12 23:46
 *
 * @author Hana Lee
 * @since 2015-08-12 23:46
 */
public class SentenceGenerator {

	private GenreType genreType;
	private IWordService service;

	private List<Words> nounList;
	private List<Words> verbList;
	private List<Words> adverbList;
	private List<Words> adjectiveList;

	private List<List<Words>> sentenceData = new ArrayList<>(4);

	public SentenceGenerator(GenreType genreType, IWordService service) {
		this.genreType = genreType;
		this.service = service;
		this.nounList = service.getWords(WordType.NOUN, genreType);
		this.verbList = service.getWords(WordType.VERB, genreType);
		this.adverbList = service.getWords(WordType.ADVERB, genreType);
		this.adjectiveList = service.getWords(WordType.ADJECTIVE, genreType);

		this.sentenceData.add(nounList);
		this.sentenceData.add(verbList);
		this.sentenceData.add(adverbList);
		this.sentenceData.add(adjectiveList);
	}

	public List<String> generate() {
		if (nounList.isEmpty() || verbList.isEmpty() || adjectiveList.isEmpty() || adverbList.isEmpty()) {
			return Collections.emptyList();
		}
		List<List<Words>> grammarList = new ArrayList<>(4);
		Random randomWordDataPicker = new Random();

		while (!sentenceData.isEmpty()) {
			int pickIndex = 0;
			for (int i = 0; i < 100; i++) {
				pickIndex = randomWordDataPicker.nextInt(sentenceData.size());
			}
			List<Words> wordsList = sentenceData.get(pickIndex);

			sentenceData.remove(pickIndex);

			grammarList.add(wordsList);
		}

		List<String> grammarData = new ArrayList<>(20);
		for (int i = 0; i < 20; i++) {
			List<Words> generatedGrammar = new ArrayList<>(4);
			for (List<Words> wordsList : grammarList) {
				generatedGrammar.add(getRandomWord(wordsList, randomWordDataPicker));
			}

			if (grammarList.size() == 1) {
				generatedGrammar.add(getRandomWord(grammarList.get(0), randomWordDataPicker));
			}

			List<String> newSentence = new ArrayList<>();
			for (Words words : generatedGrammar) {
				newSentence.add(words.getWord());
			}

			grammarData.add(TextUtils.join(" ", newSentence));
		}

		return grammarData;
	}

	private Words getRandomWord(List<Words> word, Random random) {
		int wordPickIndex = 0;
		for (int i = 0; i < 99; i++) {
			wordPickIndex = random.nextInt(word.size());
		}

		return word.get(wordPickIndex);
	}
}
