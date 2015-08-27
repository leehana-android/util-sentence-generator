package kr.co.leehana.sg.service;

import java.util.List;

import kr.co.leehana.sg.model.Word;
import kr.co.leehana.sg.type.GenreType;
import kr.co.leehana.sg.type.WordType;

/**
 * Created by Hana Lee on 2015-08-13 03:19
 *
 * @author Hana Lee
 * @since 2015-08-13 03:19
 */
public interface IWordService {

	List<Word> getWords(WordType type, GenreType genreType);

	List<Word> getWords();

	List<Word> getWordsByBackupStatus(boolean isBackup);

	List<Word> getWordsByModifyStatus(boolean isModified);

	int getCount(WordType type, GenreType genreType);

	void insert(Word word);

	void insert(List<Word> wordList);

	void delete(Word word);

	void delete(int id);

	void delete(List<Integer> ids);

	int update(Word word);
}
