package com.bfrc.framework.dao;

import java.util.List;

import com.bfrc.pojo.grandopening.BsroGrandOpening;

public interface GrandOpeningDAO {

    List getAllGrandOpenings();
    BsroGrandOpening findGrandOpening(Object id);
    void createGrandOpening(BsroGrandOpening item);
    void updateGrandOpening(BsroGrandOpening item);
    void deleteGrandOpening(BsroGrandOpening item);
    void deleteGrandOpening(Object id);
    int updateGoApproved(Long eventId, boolean approved);
    int deleteGrandOpenings(Object[] eventIds);
}
