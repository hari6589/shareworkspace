package com.bsro.service.tagging;

import java.util.List;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfrc.framework.dao.TagDAO;
import com.bfrc.pojo.tagging.TagKey;
import com.bfrc.pojo.tagging.TagVariable;

@Service
public class TagServiceImpl implements TagService {

	private Log logger;

	@Autowired
	private TagDAO tagDAO;

	@Override
	public List<TagKey> fetchAllTagKeys() {
		return tagDAO.fetchAllTagKeys();
	}

	@Override
	public List<TagVariable> fetchTagVariablesByTagKey(Long tagKeyId) {
		return tagDAO.fetchTagVariablesByTagKey(tagKeyId);
	}

	@Override
	public TagKey fetchTagKeyById(Long tagKeyId) {
		return tagDAO.fetchTagKeyById(tagKeyId);
	}

	@Override
	public TagKey fetchTagKeyByUrl(String url) {
		return tagDAO.fetchTagKeyByUrl(url);
	}

	@Override
	public TagVariable fetchTagVariableById(Long tagKeyId) {
		return tagDAO.fetchTagVariableById(tagKeyId);
	}

	@Override
	public void saveTagKey(TagKey tagKey) {
		tagDAO.saveTagKey(tagKey);

	}

	@Override
	public void updateTagKey(TagKey tagKey) {
		tagDAO.updateTagKey(tagKey);

	}

	@Override
	public void deleteTagKey(Long tagKeyId) {
		tagDAO.deleteTagKey(tagKeyId);

	}

	@Override
	public void saveTagVariable(TagVariable tagVariable) {
		tagDAO.saveTagVariable(tagVariable);
	}

	@Override
	public void updateTagVariable(TagVariable tagVariable) {
		tagDAO.updateTagVariable(tagVariable);
	}

	@Override
	public void deleteTagVariable(Long tagVariableId) {
		tagDAO.deleteTagVariable(tagVariableId);
	}

	@Override
	public int fetchTagKeyCount() {
		return tagDAO.fetchTagKeysCount();
	}

	@Override
	public int fetchTagVariableCountByTagKey(Long tagKeyId) {
		return tagDAO.fetchTagVariableCountByTagKey(tagKeyId);
	}

}
