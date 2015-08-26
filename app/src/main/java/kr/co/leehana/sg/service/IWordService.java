package kr.co.leehana.sg.service;

import java.util.List;

import kr.co.leehana.sg.model.Words;
import kr.co.leehana.sg.type.GenreType;
import kr.co.leehana.sg.type.WordType;

/**
 * Created by Hana Lee on 2015-08-13 03:19
 *
 * @author Hana Lee
 * @since 2015-08-13 03:19
 */
public interface IWordService {

	List<Words> getWords(WordType type, GenreType genreType);

	List<Words> getNoBackupWords();

	int getCount(WordType type, GenreType genreType);

	void insert(Words word);

	void insert(List<Words> wordsList);

	void delete(Words word);

	void delete(int id);

	void delete(List<Integer> ids);

	int update(Words word);
}
