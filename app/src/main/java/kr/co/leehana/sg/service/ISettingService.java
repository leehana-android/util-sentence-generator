package kr.co.leehana.sg.service;

import kr.co.leehana.sg.model.Setting;

/**
 * Created by Hana Lee on 2015-08-17 00:11
 *
 * @author Hana Lee
 * @since 2015-08-17 00:11
 */
public interface ISettingService {

	Setting getSetting();

	Setting getNoBackupSetting();

	void update(Setting setting);
}
