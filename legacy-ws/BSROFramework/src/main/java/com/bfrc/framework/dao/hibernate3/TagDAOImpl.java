package com.bfrc.framework.dao.hibernate3;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.bfrc.framework.dao.TagDAO;
import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.framework.util.Util;
import com.bfrc.pojo.tagging.TagKey;
import com.bfrc.pojo.tagging.TagVariable;

public class TagDAOImpl extends HibernateDAOImpl implements TagDAO {
	
	/*
	 * 
	 * (non-Javadoc)
	 * @see com.bfrc.framework.dao.TagDAO#fetchTagKeysByPage(int)
	 * Pass page numbers starting at page #1, subtracts one and multiplies by resultsPerPage to find first result
	 */
	@Override
	public List<TagKey> fetchAllTagKeys() {
		String hql = "select distinct tk from TagKey tk order by tk.siteName asc, tk.key asc";
		List<TagKey> l = null;
		Session session = getSession();
		try{
			Query hqlQuery = session.createQuery(hql);				
			l = (List<TagKey>) getHibernateTemplate().find(hql);
						
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				try {
					this.releaseSession(session);
				} catch (HibernateException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		if(l!=null&&l.size()>0)
			return l;
		return null;
	}
	@Override
	public int fetchTagKeysCount() {
		String hql = "select count(tk.tagKeyId) from TagKey tk";
		int count = 0;
		Session session = getSession();
		try{
				Query hqlQuery = session.createQuery(hql);
				List<Object> l  = hqlQuery.list();
				if(l!=null&&l.size()>0)
					count = ((Long)l.get(0)).intValue();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				try {
					this.releaseSession(session);
				} catch (HibernateException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		return count;
	}
	@Override
	public TagKey fetchTagKeyByUrl(String url) {
		String hql = "from TagKey tk where tk.key = :key";
		List<TagKey> l = (List<TagKey>) getHibernateTemplate().findByNamedParam(hql, "key", url);
		
		if(l!=null&&l.size()>0)
			return l.get(0);
		return null;
	}
	@Override
	public TagKey fetchTagKeyById(Long tagKeyId) {
		String hql = "from TagKey tk where tk.tagKeyId = :tagKeyId";
		List<TagKey> l = (List<TagKey>) getHibernateTemplate().findByNamedParam(hql, "tagKeyId", tagKeyId);
		
		if(l!=null&&l.size()>0)
			return l.get(0);
		return null;
	}

	@Override
	public TagKey saveTagKey(TagKey tagKey) {
		getHibernateTemplate().save(tagKey);
		return tagKey;
	}

	@Override
	public void deleteTagKey(Long tagKeyId) {
		getHibernateTemplate().delete(getHibernateTemplate().get(TagKey.class,tagKeyId));
	}

	@Override
	public TagKey updateTagKey(TagKey tagKey) {

		getHibernateTemplate().update(tagKey);
		return tagKey;
	}

	@Override
	public List<TagVariable> fetchTagVariablesByTagKey(Long tagKeyId) {
		String hql = "from TagVariable tv where tv.tagKey.tagKeyId = :tagKeyId";
		List<TagVariable> l = (List<TagVariable>) getHibernateTemplate().findByNamedParam(hql, "tagKeyId", tagKeyId);
		
		return l;
	}

	@Override
	public TagVariable saveTagVariable(TagVariable tagVariable) {

		Util.debug("private Long tagVariableId;"+tagVariable.getTagVariableId());
		Util.debug("private TagKey tagKey;"+tagVariable.getTagKey().getTagKeyId());
		Util.debug("private String type;"+tagVariable.getType());
		Util.debug("private String requestAttributeName;"+tagVariable.getRequestAttributeName());
		Util.debug("private String variableValueName;"+tagVariable.getVariableValueName());
		Util.debug("private Date createdDate;"+tagVariable.getCreatedDate());
		Util.debug("private Date updatedDate;"+tagVariable.getUpdatedDate());
		getHibernateTemplate().save(tagVariable);
		return tagVariable;
	}

	@Override
	public void deleteTagVariable(Long tagVariableId) {
		getHibernateTemplate().delete(getHibernateTemplate().get(TagVariable.class,tagVariableId));
	}

	@Override
	public TagVariable updateTagVariable(TagVariable tagVariable) {

		getHibernateTemplate().update(tagVariable);
		return tagVariable;
	}
	@Override
	public TagVariable fetchTagVariableById(Long tagVariableId) {
		String hql = "from TagVariable tv where tv.tagVariableId = :tagVariableId";
		List<TagVariable> l = (List<TagVariable>) getHibernateTemplate().findByNamedParam(hql, "tagVariableId", tagVariableId);
		
		if(l!=null&&l.size()>0)
			return l.get(0);
		return null;
	}
	@Override
	public int fetchTagVariableCountByTagKey(Long tagKeyId) {
		String hql = "select count(distinct tv.tagVariableId) from TagVariable tv where tv.tagKey.tagKeyId = :tagKeyId";
		int count = 0;
		Session session = getSession();
		try{
				Query hqlQuery = session.createQuery(hql);
				hqlQuery.setParameter("tagKeyId", tagKeyId);
				List<Object> l  = hqlQuery.list();
				if(l!=null&&l.size()>0)
					count = ((Long)l.get(0)).intValue();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				try {
					this.releaseSession(session);
				} catch (HibernateException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		return count;
	}


}
