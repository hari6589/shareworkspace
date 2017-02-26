package com.bsro.service.tagging;

import java.util.List;

import com.bfrc.pojo.tagging.TagKey;
import com.bfrc.pojo.tagging.TagVariable;


public interface TagService {

	public int fetchTagKeyCount();
	public List<TagKey> fetchAllTagKeys();
	public List<TagVariable> fetchTagVariablesByTagKey(Long tagKeyId);
	public TagKey fetchTagKeyById(Long tagKeyId);
	public TagKey fetchTagKeyByUrl(String url);
	public TagVariable fetchTagVariableById(Long tagVariableId);
	public void saveTagKey(TagKey tagKey);
	public void updateTagKey(TagKey tagKey);
	public void deleteTagKey(Long tagKeyId);
	public void saveTagVariable(TagVariable tagVariable);
	public void updateTagVariable(TagVariable tagVariable);
	public void deleteTagVariable(Long tagVariableId);
	public int fetchTagVariableCountByTagKey(Long tagKeyId);
	
}
