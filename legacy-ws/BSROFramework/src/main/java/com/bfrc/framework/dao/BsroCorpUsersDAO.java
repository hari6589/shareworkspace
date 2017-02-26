package com.bfrc.framework.dao;

import com.bfrc.pojo.storeadmin.BsroCorpUsers;

public interface BsroCorpUsersDAO {
	BsroCorpUsers getCorpUserByEmail(String email);
}
