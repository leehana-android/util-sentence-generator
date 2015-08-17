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
		List<WordType> tempWordTypeList = new ArrayList<>(4);
		tempWordTypeList.add(WordType.NOUN);
		tempWordTypeList.add(WordType.VERB);
		tempWordTypeList.add(WordType.ADVERB);
		tempWordTypeList.add(WordType.ADJECTIVE);

		sentenceData.add(Collections.<Words>emptyList());
		sentenceData.add(Collections.<Words>emptyList());
		sentenceData.add(Collections.<Words>emptyList());
		sentenceData.add(Collections.<Words>emptyList());

		SentenceGenerateType sentenceGenerateType = setting.getFirstWordType();
		int noneCount = 0;
		int randomCount = 0;
		switch (sentenceGenerateType) {
			case NOUN:
			case VERB:
			case ADVERB:
			case ADJECTIVE:
				WordType selectedWordType = TypeConverter.intToWordType(sentenceGenerateType.getIndexCode());
				sentenceData.remove(0);
				sentenceData.add(0, service.getWords(selectedWordType, genreType));
				tempWordTypeList.remove(selectedWordType);
				break;
			case RANDOM:
				randomCount++;
				break;
			case NONE:
				noneCount++;
				break;
		}

		sentenceGenerateType = setting.getSecondWordType();
		switch (sentenceGenerateType) {
			case NOUN:
			case VERB:
			case ADVERB:
			case ADJECTIVE:
				WordType selectedWordType = TypeConverter.intToWordType(sentenceGenerateType.getIndexCode());
				sentenceData.remove(1);
				sentenceData.add(1, service.getWords(selectedWordType, genreType));
				tempWordTypeList.remove(selectedWordType);
				break;
			case RANDOM:
				randomCount++;
				break;
			case NONE:
				noneCount++;
				break;
		}

		sentenceGenerateType = setting.getThirdWordType();
		switch (sentenceGenerateType) {
			case NOUN:
			case VERB:
			case ADVERB:
			case ADJECTIVE:
				WordType selectedWordType = TypeConverter.intToWordType(sentenceGenerateType.getIndexCode());
				sentenceData.remove(2);
				sentenceData.add(2, service.getWords(selectedWordType, genreType));
				tempWordTypeList.remove(selectedWordType);
				break;
			case RANDOM:
				randomCount++;
				break;
			case NONE:
				noneCount++;
				break;
		}

		sentenceGenerateType = setting.getFourthWordType();
		switch (sentenceGenerateType) {
			case NOUN:
			case VERB:
			case ADVERB:
			case ADJECTIVE:
				WordType selectedWordType = TypeConverter.intToWordType(sentenceGenerateType.getIndexCode());
				sentenceData.remove(3);
				sentenceData.add(3, service.getWords(selectedWordType, genreType));
				tempWordTypeList.remove(selectedWordType);
				break;
			case RANDOM:
				randomCount++;
				break;
			case NONE:
				noneCount++;
				break;
		}

		if (randomCount > 0 || noneCount > 0) {
			List<List<Words>> dummyList = new ArrayList<>(sentenceData);
			if (tempWordTypeList.size() == 1 && noneCount == 0) {
				for (List<Words> words : sentenceData) {
					if (words.isEmpty()) {
						int index = sentenceData.indexOf(words);
						dummyList.remove(index);
						dummyList.add(index, service.getWords(tempWordTypeList.get(0), genreType));
						break;
					}
				}
				tempWordTypeList.clear();
			} else if (noneCount < tempWordTypeList.size()) {
				int totalCount = tempWordTypeList.size() - noneCount;
				List<Integer> insertIndexList = new ArrayList<>();
				for (int i = 0; i < totalCount; i++) {
					int randomIndex = 0;
					if (tempWordTypeList.size() > 1) {
						randomIndex = makeRandomIndex(tempWordTypeList.size());
					}
					int count = 0;
					for (List<Words> words : sentenceData) {
						if (words.isEmpty()) {
							if (!insertIndexList.contains(count)) {
								insertIndexList.add(count);
								dummyList.remove(count);
								dummyList.add(count, service.getWords(tempWordTypeList.get(randomIndex), genreType));
								break;
							}
						}
						count++;
					}
					tempWordTypeList.remove(randomIndex);
				}
			}

			sentenceData = new ArrayList<>(dummyList);

			for (List<Words> wordsList : dummyList) {
				if (wordsList.isEmpty()) {
					int index = dummyList.indexOf(wordsList);
					sentenceData.remove(index);
				}
			}
		}
	}

	private int makeRandomIndex(int count) {
		Random randomWordDataPicker = new Random();
		int randomIndex = 0;
		for (int i = 0; i < 10; i++) {
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
				String colorCode = AppContext.getColorCodeForWord(words.getType());
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

			Spanned newSpannedWord = Html.fromHtml(TextUtils.join(" ", newSentence));
			grammarData.add(newSpannedWord);
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
