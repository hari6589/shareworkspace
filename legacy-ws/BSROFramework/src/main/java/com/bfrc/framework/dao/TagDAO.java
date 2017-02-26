package com.bfrc.framework.dao;

import java.util.List;

import com.bfrc.pojo.tagging.TagKey;
import com.bfrc.pojo.tagging.TagVariable;


public interface TagDAO {


	public TagKey fetchTagKeyById(Long tagKeyId);
	public List<TagKey> fetchAllTagKeys();
	public int fetchTagKeysCount();
	public TagKey fetchTagKeyByUrl(String url);
	public TagKey saveTagKey(TagKey tagKey);
	public void deleteTagKey(Long tagKeyId);
	public TagKey updateTagKey(TagKey tagKey);
	

	public List<TagVariable> fetchTagVariablesByTagKey(Long tagKeyId);
	public TagVariable saveTagVariable(TagVariable tagVariable);
	public void deleteTagVariable(Long tagVariableId);
	public TagVariable updateTagVariable(TagVariable tagVariable);
	public TagVariable fetchTagVariableById(Long tagKeyId);
	public int fetchTagVariableCountByTagKey(Long tagKeyId);
}
