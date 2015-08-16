package kr.co.leehana.sg.utils;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import kr.co.leehana.sg.context.AppContext;
import kr.co.leehana.sg.converter.TypeConverter;
import kr.co.leehana.sg.model.Setting;
import kr.co.leehana.sg.model.Words;
import kr.co.leehana.sg.service.IWordService;
import kr.co.leehana.sg.type.GenreType;
import kr.co.leehana.sg.type.SentenceGenerateType;
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

	private List<List<Words>> sentenceData = new ArrayList<>();

	private Setting setting;

	public SentenceGenerator(GenreType genreType, IWordService service) {
		this.genreType = genreType;
		this.service = service;
		this.setting = AppContext.getInstance().getSetting();

		makeSentenceBaseData();
	}

	private void makeSentenceBaseData() {
		List<WordType> tempList = new ArrayList<>(4);
		tempList.add(WordType.NOUN);
		tempList.add(WordType.VERB);
		tempList.add(WordType.ADVERB);
		tempList.add(WordType.ADJECTIVE);

		List<Words> firstWordList = null;
		List<Words> secondWordList = null;
		List<Words> thirdWordList = null;
		List<Words> fourthWordList = null;

		SentenceGenerateType sentenceGenerateType = setting.getFirstWordType();
		switch (sentenceGenerateType) {
			case NOUN:
			case VERB:
			case ADVERB:
			case ADJECTIVE:
				WordType selectedWordType = TypeConverter.intToWordType(sentenceGenerateType.getIndexCode());
				firstWordList = service.getWords(selectedWordType, genreType);
				tempList.remove(selectedWordType);
				break;
			case RANDOM:
				int randomIndex = makeRandomIndex(tempList.size());
				firstWordList = service.getWords(tempList.get(randomIndex), genreType);
				tempList.remove(randomIndex);
				break;
			case NONE:
				break;
		}

		sentenceGenerateType = setting.getSecondWordType();
		switch (sentenceGenerateType) {
			case NOUN:
			case VERB:
			case ADVERB:
			case ADJECTIVE:
				WordType selectedWordType = TypeConverter.intToWordType(sentenceGenerateType.getIndexCode());
				secondWordList = service.getWords(selectedWordType, genreType);
				tempList.remove(selectedWordType);
				break;
			case RANDOM:
				int randomIndex = makeRandomIndex(tempList.size());
				secondWordList = service.getWords(tempList.get(randomIndex), genreType);
				tempList.remove(randomIndex);
				break;
			case NONE:
				break;
		}

		sentenceGenerateType = setting.getThirdWordType();
		switch (sentenceGenerateType) {
			case NOUN:
			case VERB:
			case ADVERB:
			case ADJECTIVE:
				WordType selectedWordType = TypeConverter.intToWordType(sentenceGenerateType.getIndexCode());
				thirdWordList = service.getWords(selectedWordType, genreType);
				tempList.remove(selectedWordType);
				break;
			case RANDOM:
				int randomIndex = makeRandomIndex(tempList.size());
				thirdWordList = service.getWords(tempList.get(randomIndex), genreType);
				tempList.remove(randomIndex);
				break;
			case NONE:
				break;
		}

		sentenceGenerateType = setting.getFourthWordType();
		switch (sentenceGenerateType) {
			case NOUN:
			case VERB:
			case ADVERB:
			case ADJECTIVE:
				WordType selectedWordType = TypeConverter.intToWordType(sentenceGenerateType.getIndexCode());
				fourthWordList = service.getWords(selectedWordType, genreType);
				tempList.remove(selectedWordType);
				break;
			case RANDOM:
				int randomIndex = makeRandomIndex(tempList.size());
				fourthWordList = service.getWords(tempList.get(randomIndex), genreType);
				tempList.remove(randomIndex);
				break;
			case NONE:
				break;
		}

		if (firstWordList != null && !firstWordList.isEmpty()) {
			this.sentenceData.add(firstWordList);
		}
		if (secondWordList != null && !secondWordList.isEmpty()) {
			this.sentenceData.add(secondWordList);
		}
		if (thirdWordList != null && !thirdWordList.isEmpty()) {
			this.sentenceData.add(thirdWordList);
		}
		if (fourthWordList != null && !fourthWordList.isEmpty()) {
			this.sentenceData.add(fourthWordList);
		}
	}

	private int makeRandomIndex(int count) {
		Random randomWordDataPicker = new Random();
		int randomIndex = 0;
		for (int i = 0; i < 100; i++) {
			randomIndex = randomWordDataPicker.nextInt(count);
		}

		return randomIndex;
	}

	public List<Spanned> generate() {
		if (sentenceData == null || sentenceData.isEmpty()) {
			return Collections.emptyList();
		}

		Random randomWordDataPicker = new Random();

		List<Spanned> grammarData = new ArrayList<>(20);
		for (int i = 0; i < setting.getSentenceCount(); i++) {
			List<Words> generatedGrammar = new ArrayList<>(4);
			for (List<Words> wordsList : sentenceData) {
				generatedGrammar.add(getRandomWord(wordsList, randomWordDataPicker));
			}

			if (sentenceData.size() == 1) {
				generatedGrammar.add(getRandomWord(sentenceData.get(0), randomWordDataPicker));
			}

			List<String> newSentence = new ArrayList<>();
			for (Words words : generatedGrammar) {
				String wordValue = words.getWord();
				String colorCode = AppContext.getInstance().getColorCodeForWord(words.getType());
				switch (words.getType()) {
					case NOUN:
						wordValue = "<font color='" + colorCode + "'>" + wordValue + "</font>";
						break;
					case VERB:
						wordValue = "<font color='" + colorCode + "'>" + wordValue + "</font>";
						break;
					case ADVERB:
						wordValue = "<font color='" + colorCode + "'>" + wordValue + "</font>";
						break;
					case ADJECTIVE:
						wordValue = "<font color='" + colorCode + "'>" + wordValue + "</font>";
						break;
				}

				newSentence.add(wordValue);
			}

			grammarData.add(Html.fromHtml(TextUtils.join(" ", newSentence)));
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
