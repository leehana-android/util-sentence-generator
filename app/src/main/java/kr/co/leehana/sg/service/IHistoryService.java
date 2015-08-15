package kr.co.leehana.sg.service;

import java.util.List;

import kr.co.leehana.sg.model.History;
import kr.co.leehana.sg.type.GenreType;

/**
 * Created by Hana Lee on 2015-08-13 03:19
 *
 * @author Hana Lee
 * @since 2015-08-13 03:19
 */
public interface IHistoryService {

	List<History> getHistories();

	List<History> getHistories(GenreType genreType);

	void insert(List<History> histories);

	void insert(History history);

	void delete(History history);
}
