package com.bfrc.framework.dao.hibernate3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.bfrc.Config;
import com.bfrc.framework.dao.VehicleDAO;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.framework.util.StringUtils;
import com.bfrc.framework.util.TireCatalogUtils;
import com.bfrc.framework.util.TireSearchUtils;
import com.bfrc.pojo.store.Store;
import com.bfrc.pojo.tire.Fitment;

import com.bfrc.pojo.tire.Tire;
import com.bfrc.security.Encode;
import com.bsro.databean.vehicle.TireVehicle;
import com.bsro.databean.vehicle.TireVehicleMake;
import com.bsro.databean.vehicle.TireVehicleModel;
import com.bsro.databean.vehicle.TireVehicleSubModel;
import com.bsro.databean.vehicle.FriendlyVehicleDataBean;

import static com.bfrc.framework.Operator.ERROR;
import static com.bfrc.framework.Operator.OPERATION;
import static com.bfrc.framework.Operator.RESULT;
import static com.bfrc.framework.Operator.SUCCESS;

public class VehicleDAOImpl extends HibernateDaoSupport implements VehicleDAO {

	public static String ARTICLE_PATTERN = "^\\d+$";
	@SuppressWarnings("rawtypes")
	public List getAirPressures(String siteName, String year, String make, String model, String submodel) {
		//submodel = com.bfrc.framework.util.StringUtils.unescapeHtmlLight(submodel);
		String hql = "select distinct f.frb, f.tireSize, f.speedRating, frontInf, rearInf, f.standardInd "
			+ "from Fitment f where (f.standardInd='S'";
		if(Config.FCAC.equalsIgnoreCase(siteName))
			hql += " or f.standardInd='O'";
		hql += ") and f.modelYear=? and f.makeName=? and f.modelName=? and f.submodel=? "
			+ "order by f.standardInd desc, f.frb, f.tireSize";
		return getHibernateTemplate().find(hql, new Object[]{year, make, model, submodel});
	}
	
	@SuppressWarnings("rawtypes")
	public List getAirPressuresById(String siteName, Object year, Object makeId, Object modelId, String submodel) {
		//submodel = com.bfrc.framework.util.StringUtils.unescapeHtmlLight(submodel);
		String hql = "select distinct f.frb, f.tireSize, f.speedRating, frontInf, rearInf, f.standardInd "
			+ "from Fitment f where (f.standardInd='S'";
		if(Config.FCAC.equalsIgnoreCase(siteName))
			hql += " or f.standardInd='O'";
		hql += ") and f.modelYear=? and f.makeId=? and f.modelId=? and f.submodel=? "
			+ "order by f.standardInd desc, f.frb, f.tireSize";
		return getHibernateTemplate().find(hql, new Object[]{year, makeId, modelId, submodel});
	}
	
	@SuppressWarnings("rawtypes")
	public List getAirPressuresById(String siteName, Object year, Object makeId, Object modelId, Object submodelId) {
		//submodel = com.bfrc.framework.util.StringUtils.unescapeHtmlLight(submodel);
		String hql = "select distinct f.frb, f.tireSize, f.speedRating, frontInf, rearInf, f.standardInd "
			+ "from Fitment f where (f.standardInd='S'";
		if(Config.FCAC.equalsIgnoreCase(siteName))
			hql += " or f.standardInd='O'";
		hql += ") and f.modelYear=? and f.makeId=? and f.modelId=? and f.submodelId=? "
			+ "order by f.standardInd desc, f.frb, f.tireSize";
		return getHibernateTemplate().find(hql, new Object[]{year, makeId, modelId, submodelId});
	}

	@SuppressWarnings("rawtypes")
	public List getYearList() {
		String hql = "select distinct f.modelYear as year from Fitment f order by f.modelYear desc";
		return getHibernateTemplate().find(hql);
	}
	
	@SuppressWarnings("rawtypes")
	public List getMakeList(Object year) {
		String hql = "select distinct f.makeName as make, f.makeId from Fitment f where f.modelYear=? order by f.makeName";

		return getHibernateTemplate().find(hql, new Object[]{year});
	}
	
	public List<TireVehicleMake> getAllMakes() {
		Session session = getSession();
		try {
			String hql = "select distinct f.makeId as makeId, f.makeName as name, fr.friendlyMakeName as friendlyName from Fitment f, FitmentReplica fr where f.makeName=fr.makeName order by f.makeName";
	
			@SuppressWarnings("unchecked")
			List<TireVehicleMake> results = (List<TireVehicleMake>) session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(TireVehicleMake.class)).list();
			
			return results;
		
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
	}
	
	public List<TireVehicleMake> getMakesByYear(String year) {
		Session session = getSession();
		try {
			String hql = "select distinct f.makeName as name, f.makeId as makeId, fr.friendlyMakeName as friendlyName from Fitment f, FitmentReplica fr "+
					"where f.modelYear=? and f.makeName = fr.makeName order by f.makeName";
			Query query = session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(TireVehicleMake.class));
			
			query.setString(0, year);
			
			@SuppressWarnings("unchecked")
			List<TireVehicleMake> results = (List<TireVehicleMake>) query.list();
			
			return results;
		
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
	}
	
	@SuppressWarnings("rawtypes")
	public List getModelList(Object year, Object makeId) {
		String hql = "select distinct f.modelName as model, f.modelId from Fitment f where f.modelYear=? and f.makeId=? order by f.modelName";

		return getHibernateTemplate().find(hql, new Object[]{year,new Long(makeId.toString())});
	}

	public List<TireVehicleModel> getAllModels() {
		Session session = getSession();
		try {
			String hql = "select distinct f.modelName as name, f.modelId as modelId, f.makeId as makeId from Fitment f order by f.modelName";
	
			Query query = session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(TireVehicleModel.class));
			
			@SuppressWarnings("unchecked")
			List<TireVehicleModel> results = (List<TireVehicleModel>) query.list();
			
			return results;
		
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
	}
			
	public List<TireVehicleModel> getModelsByYearAndMakeName(String year, String makeName) {
		Session session = getSession();
		try {
			String hql = "select distinct f.modelName as name, f.modelId as modelId, f.makeId as makeId, fr.friendlyModelName as friendlyName from Fitment f, FitmentReplica fr "+
					"where f.modelYear=? and f.makeName=? and f.makeName=fr.makeName and f.modelName=fr.modelName order by f.modelName";
	
			Query query = session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(TireVehicleModel.class));
			
			query.setString(0, year);
			query.setString(1, makeName);
			
			@SuppressWarnings("unchecked")
			List<TireVehicleModel> results = (List<TireVehicleModel>) query.list();
			
			return results;
		
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
	}		
			
	public List<TireVehicleModel> getModelsByMakeId(Long makeId) {
		Session session = getSession();
		try {
			String hql = "select distinct f.modelName as name, f.modelId as modelId, f.makeId as makeId from Fitment f where f.makeId=? order by f.modelName";
	
			Query query = session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(TireVehicleModel.class));
			
			query.setLong(0, makeId);
			
			@SuppressWarnings("unchecked")
			List<TireVehicleModel> results = (List<TireVehicleModel>) query.list();
			
			return results;
		
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
	}
	
	public List<TireVehicleModel> getModelsByMakeName(String makeName) {
		Session session = getSession();
		try {
			String hql = "select distinct f.modelId as modelId, f.modelName as name, fr.friendlyModelName as friendlyName from Fitment f, FitmentReplica fr "+
					"where fr.friendlyMakeName=? and fr.makeName = f.makeName and fr.modelName=f.modelName and fr.submodel=f.submodel order by f.modelName";
	
			Query query = session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(TireVehicleModel.class));
			
			query.setString(0, makeName);
			
			@SuppressWarnings("unchecked")
			List<TireVehicleModel> results = (List<TireVehicleModel>) query.list();
			
			return results;
		
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
	}
	
	public List<String> getYearOptionsByMakeModel(String makeName, String modelName) {
		String hql = "select distinct f.modelYear as year from Fitment f, FitmentReplica fr "+
					"where fr.friendlyMakeName=? and fr.friendlyModelName=? and fr.makeName = f.makeName and fr.modelName=f.modelName and fr.submodel=f.submodel order by f.modelYear desc";
			
		return getHibernateTemplate().find(hql, new Object[]{makeName,modelName});
	}
	
	public FriendlyVehicleDataBean getFriendlyVehicleDataBean(String friendlyMake) {
		Session session = getSession();
		try {
			String hql = "select distinct f.makeName as makeName, f.makeId as makeId, fr.friendlyMakeName as makeFriendlyName from Fitment f, FitmentReplica fr "+
					"where fr.friendlyMakeName=? and f.makeName = fr.makeName order by f.makeName";
			Query query = session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(FriendlyVehicleDataBean.class));
			
			query.setString(0, friendlyMake);
			
			@SuppressWarnings("unchecked")
			List<FriendlyVehicleDataBean> results = (List<FriendlyVehicleDataBean>) query.list();
			
			if (results != null && !results.isEmpty()) {
				return results.get(0);
			}
			return null;
		
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
	}
	
	public FriendlyVehicleDataBean getFriendlyVehicleDataBean(String friendlyMake, String friendlyModel) {
		Session session = getSession();
		try {
			String hql = "select distinct f.makeName as makeName, f.makeId as makeId, fr.friendlyMakeName as makeFriendlyName, f.modelId as modelId, f.modelName as modelName, "+
						 "fr.friendlyModelName as modelFriendlyName from Fitment f, FitmentReplica fr "+
						 "where fr.friendlyMakeName=? and fr.friendlyModelName=? and fr.makeName = f.makeName and fr.modelName=f.modelName and fr.submodel=f.submodel order by f.modelName";
	
			Query query = session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(FriendlyVehicleDataBean.class));
			
			query.setString(0, friendlyMake);
			query.setString(1, friendlyModel);
			
			@SuppressWarnings("unchecked")
			List<FriendlyVehicleDataBean> results = (List<FriendlyVehicleDataBean>) query.list();
			
			if (results != null && !results.isEmpty()) {
				return results.get(0);
			}
			
			return null;
		
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
	}
	
	public FriendlyVehicleDataBean getFriendlyVehicleDataBean(String friendlyMake, String friendlyModel, String year) {
		Session session = getSession();
		try {
			String hql = "select distinct f.makeName as makeName, f.makeId as makeId, fr.friendlyMakeName as makeFriendlyName, f.modelId as modelId, f.modelName as modelName, "+
						 "fr.friendlyModelName as modelFriendlyName, f.modelYear as year from Fitment f, FitmentReplica fr "+
						 "where fr.friendlyMakeName=? and fr.friendlyModelName=? and f.modelYear=? and fr.makeName = f.makeName and fr.modelName=f.modelName and fr.submodel=f.submodel order by f.modelName";
	
			Query query = session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(FriendlyVehicleDataBean.class));
			
			query.setString(0, friendlyMake);
			query.setString(1, friendlyModel);
			query.setString(2, year);
			
			@SuppressWarnings("unchecked")
			List<FriendlyVehicleDataBean> results = (List<FriendlyVehicleDataBean>) query.list();
			
			if (results != null && !results.isEmpty()) {
				return results.get(0);
			}
			
			return null;
		
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
	}
	
	public FriendlyVehicleDataBean getFriendlyVehicleDataBean(String friendlyMake, String friendlyModel, String year, String friendlySubmodel) {
		Session session = getSession();
		try {
			String hql = "select distinct f.makeName as makeName, f.makeId as makeId, fr.friendlyMakeName as makeFriendlyName, f.modelId as modelId, f.modelName as modelName, "+
						 "fr.friendlyModelName as modelFriendlyName, f.modelYear as year, f.submodel as submodelName, f.submodelId as submodelId, fr.friendlySubmodel as submodelFriendlyName, "+
						 "f.acesVehicleId as acesVehicleId, f.tpmsInd as tpmsInd from Fitment f, FitmentReplica fr "+
						 "where fr.friendlyMakeName=? and fr.friendlyModelName=? and f.modelYear=? and fr.friendlySubmodel=? and fr.makeName = f.makeName and fr.modelName=f.modelName and fr.submodel=f.submodel order by f.modelName";
	
			Query query = session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(FriendlyVehicleDataBean.class));
			
			query.setString(0, friendlyMake);
			query.setString(1, friendlyModel);
			query.setString(2, year);
			query.setString(3, friendlySubmodel);
			
			@SuppressWarnings("unchecked")
			List<FriendlyVehicleDataBean> results = (List<FriendlyVehicleDataBean>) query.list();
			
			if (results != null && !results.isEmpty()) {
				return results.get(0);
			}
			
			return null;
		
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
	}
	
	public FriendlyVehicleDataBean getFriendlyVehicleDataBean(Long acesVehicleId) {
		Session session = getSession();
		try {
			String hql = "select distinct f.makeName as makeName, f.makeId as makeId, fr.friendlyMakeName as makeFriendlyName, f.modelId as modelId, f.modelName as modelName, "+
						 "fr.friendlyModelName as modelFriendlyName, f.modelYear as year, f.submodel as submodelName, f.submodelId as submodelId, fr.friendlySubmodel as submodelFriendlyName, "+
						 "f.acesVehicleId as acesVehicleId, f.tpmsInd as tpmsInd from Fitment f, FitmentReplica fr "+
						 "where f.acesVehicleId=? and fr.makeName = f.makeName and fr.modelName=f.modelName and fr.submodel=f.submodel order by f.modelName";
	
			Query query = session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(FriendlyVehicleDataBean.class));
			
			query.setLong(0, acesVehicleId);
			
			@SuppressWarnings("unchecked")
			List<FriendlyVehicleDataBean> results = (List<FriendlyVehicleDataBean>) query.list();
			
			if (results != null && !results.isEmpty()) {
				return results.get(0);
			}
			
			return null;
		
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
	}
	
	public List<FriendlyVehicleDataBean> getMakeModelsByVehicleType(List<String> vehtype) {
		Session session = getSession();
		try {
			String hql = "select distinct f.makeName as makeName, f.makeId as makeId, fr.friendlyMakeName as makeFriendlyName, f.modelId as modelId, f.modelName as modelName, "+
						 "fr.friendlyModelName as modelFriendlyName from Fitment f, FitmentReplica fr where f.vehtype in (:vehtype) and "+
						 "fr.makeName = f.makeName and fr.modelName=f.modelName and fr.submodel=f.submodel order by f.makeName, f.modelName";
			
			Query query = session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(FriendlyVehicleDataBean.class));
			query.setParameterList("vehtype", vehtype);
			
			@SuppressWarnings("unchecked")
			List<FriendlyVehicleDataBean> results = (List<FriendlyVehicleDataBean>) query.list();
			
			return results;
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
	}
	
	public List<TireVehicleSubModel> getSubModelsByYearAndMakeNameAndModelName(String year, String makeName, String modelName) {
		Session session = getSession();
		try {
			String hql = "select distinct f.submodel as name, f.modelId as modelId from Fitment f where f.modelYear=? and f.makeName=? and f.modelName=? order by f.submodel";
	
			Query query = session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(TireVehicleSubModel.class));
			
			query.setString(0, year);
			query.setString(1, makeName);
			query.setString(2, modelName);
			
			@SuppressWarnings("unchecked")
			List<TireVehicleSubModel> results = (List<TireVehicleSubModel>) query.list();
			
			return results;
		
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
	}
	
	public List<TireVehicleSubModel> getSubModelsByMakeIdAndModelId(Long makeId, Long modelId) {
		Session session = getSession();
		try {
			String hql = "select distinct f.submodel as name, f.modelId as modelId from Fitment f where f.makeId=? and f.modelId=? order by f.submodel";
	
			Query query = session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(TireVehicleSubModel.class));
			
			query.setLong(0, makeId);
			query.setLong(1, modelId);
			
			@SuppressWarnings("unchecked")
			List<TireVehicleSubModel> results = (List<TireVehicleSubModel>) query.list();
			
			return results;
		
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
	}
	
	public List<TireVehicleSubModel> getSubModelsByMakeNameAndModelName(String makeName, String modelName) {
		Session session = getSession();
		try {
			String hql = "select distinct f.submodel as name, f.modelId as modelId from Fitment f where f.makeName=? and f.modelName=? order by f.submodel";
	
			Query query = session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(TireVehicleSubModel.class));
			
			query.setString(0, makeName);
			query.setString(1, modelName);
			
			@SuppressWarnings("unchecked")
			List<TireVehicleSubModel> results = (List<TireVehicleSubModel>) query.list();
			
			return results;
		
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
	}
	
	public List<TireVehicleSubModel> getSubModelsByYearMakeAndModelNames(String year, String makeName, String modelName) {
		Session session = getSession();
		try {
			String hql = "select distinct f.acesVehicleId as acesVehicleId, f.modelId as modelId, "+
					"f.submodelId as submodelId, f.submodel as name, fr.friendlySubmodel as friendlyName, "+
					"f.tpmsInd as tpmsInd from Fitment f, FitmentReplica fr where f.modelYear=? and f.makeName=? and f.modelName=? and "+
					"fr.makeName = f.makeName and fr.modelName=f.modelName and fr.submodel=f.submodel order by f.submodel asc";
	
			Query query = session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(TireVehicleSubModel.class));
			
			query.setString(0, year);
			query.setString(1, makeName);
			query.setString(2, modelName);
			
			@SuppressWarnings("unchecked")
			List<TireVehicleSubModel> results = (List<TireVehicleSubModel>) query.list();
			
			return results;
		
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
	}
	
	public List<TireVehicleSubModel> getTrimByYearMakeAndModelNames(String year, String makeName, String modelName) {
		Session session = getSession();
		try {
			String hql = "select distinct f.acesVehicleId as acesVehicleId, "+
					"f.submodelId as submodelId, f.submodel as name, fr.friendlySubmodel as friendlyName, "+
					"f.tpmsInd as tpmsInd from Fitment f, FitmentReplica fr where f.modelYear=? and fr.friendlyMakeName=? and fr.friendlyModelName=? and "+
					"fr.makeName = f.makeName and fr.modelName=f.modelName and fr.submodel=f.submodel order by f.submodel asc";
	
			Query query = session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(TireVehicleSubModel.class));
			
			query.setString(0, year);
			query.setString(1, makeName);
			query.setString(2, modelName);
			
			@SuppressWarnings("unchecked")
			List<TireVehicleSubModel> results = (List<TireVehicleSubModel>) query.list();
			
			return results;
		
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
	}
	
	public List<TireVehicle> getVehiclesByYearMakeNameModelName(String year, String makeName, String modelName) {
		Session session = getSession();
		try {
			String hql = "select distinct f.acesVehicleId as acesVehicleId, f.makeName as makeName, f.makeId as makeId, f.modelName as modelName, f.modelId as modelId, f.submodel as submodelName, f.modelYear as year, f.tpmsInd as hasTpms from Fitment f where f.modelYear=? and f.makeName=? and f.modelName=? order by f.submodel asc";
	
			Query query = session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(TireVehicle.class));
			
			query.setString(0, year);
			query.setString(1, makeName);
			query.setString(2, modelName);
			
			@SuppressWarnings("unchecked")
			List<TireVehicle> results = (List<TireVehicle>) query.list();
			
			return results;
		
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
	}	
	
	public List<TireVehicle> getVehiclesByMakeNameModelNameSubmodelName(String makeName, String modelName, String submodelName) {
		Session session = getSession();
		try {
			String hql = "select distinct f.acesVehicleId as acesVehicleId, f.makeName as makeName, f.makeId as makeId, f.modelName as modelName, f.modelId as modelId, f.submodel as submodelName, f.modelYear as year, f.tpmsInd as hasTpms from Fitment f where f.makeName=? and f.modelName=? and f.submodel=? order by f.modelYear desc";
	
			Query query = session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(TireVehicle.class));
			
			query.setString(0, makeName);
			query.setString(1, modelName);
			query.setString(2, submodelName);
			
			@SuppressWarnings("unchecked")
			List<TireVehicle> results = (List<TireVehicle>) query.list();
			
			return results;
		
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
	}
	
	public List<TireVehicle> getVehiclesByYearMakeNameModelNameSubmodelName(Integer year, String makeName, String modelName, String submodelName) {
		Session session = getSession();
		try {
			String hql = "select distinct f.acesVehicleId as acesVehicleId, f.makeName as makeName, f.makeId as makeId, f.modelName as modelName, f.modelId as modelId, f.submodel as submodelName, f.modelYear as year, f.tpmsInd as hasTpms from Fitment f where f.modelYear=? and f.makeName=? and f.modelName=? and f.submodel=?";
	
			Query query = session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(TireVehicle.class));

			query.setString(0, year.toString());
			query.setString(1, makeName);
			query.setString(2, modelName);
			query.setString(3, submodelName);
			
			@SuppressWarnings("unchecked")
			List<TireVehicle> results = (List<TireVehicle>) query.list();
			
			return results;
		
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
	}
	
	@Override
	public List<TireVehicle> getVehiclesByYearMakeNameModelNameSubmodelNameCaseInsensitive(Integer year, String makeName, String modelName, String submodelName) {
		Session session = getSession();
		try {
			String hql = "select distinct f.acesVehicleId as acesVehicleId, f.makeName as makeName, f.makeId as makeId, f.modelName as modelName, f.modelId as modelId, f.submodel as submodelName, f.modelYear as year, f.tpmsInd as hasTpms from Fitment f where f.modelYear=? and lower(f.makeName)=lower(?) and lower(f.modelName)=lower(?) and lower(f.submodel)=lower(?)";
	
			Query query = session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(TireVehicle.class));

			query.setString(0, year.toString());
			query.setString(1, makeName);
			query.setString(2, modelName);
			query.setString(3, submodelName);
			
			@SuppressWarnings("unchecked")
			List<TireVehicle> results = (List<TireVehicle>) query.list();
			
			return results;
		
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
	}
	
	public TireVehicle fetchVehicleByAcesVehicleId(Long acesVehicleId) {
		Session session = getSession();
		try {
			String hql = "select distinct f.acesVehicleId as acesVehicleId, f.makeName as makeName, f.makeId as makeId, f.modelName as modelName, f.modelId as modelId, f.submodel as submodelName, f.modelYear as year, f.tpmsInd as hasTpms from Fitment f where f.acesVehicleId=:acesVehicleId";
	
			Query query = session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(TireVehicle.class));

			query.setParameter("acesVehicleId", acesVehicleId);
			
			@SuppressWarnings("unchecked")
			TireVehicle result = (TireVehicle) query.uniqueResult();
			
			return result;
		
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
	}
	
	public List<TireVehicle> getVehiclesByMakeIdModelIdSubmodelName(Long makeId, Long modelId, String submodelName) {
		Session session = getSession();
		try {
			String hql = "select distinct f.acesVehicleId as acesVehicleId, f.makeName as makeName, f.makeId as makeId, f.modelName as modelName, f.modelId as modelId, f.submodel as submodelName, f.modelYear as year, f.tpmsInd as hasTpms from Fitment f where f.makeId=? and f.modelId=? and f.submodel=? order by f.modelYear desc";
	
			Query query = session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(TireVehicle.class));
			
			query.setLong(0, makeId);
			query.setLong(1, modelId);
			query.setString(2, submodelName);
			
			@SuppressWarnings("unchecked")
			List<TireVehicle> results = (List<TireVehicle>) query.list();
			
			return results;
		
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
	}
	
	@SuppressWarnings("rawtypes")
	public List getModelListByName(Object year, String makeName) {
		String hql = "select distinct f.modelName as model, f.modelId from Fitment f where f.modelYear=? and f.makeName=? order by f.modelName";
		return getHibernateTemplate().find(hql, new Object[]{year,makeName});
	}
	
	@Override
	public List<Object[]> getModelListByNameCaseInsensitive(String year, String makeName) {
		String hql = "select distinct f.modelName as model, f.modelId from Fitment f where f.modelYear=? and lower(f.makeName)=lower(?) order by f.modelName";
		return getHibernateTemplate().find(hql, new Object[]{year,makeName});
	}
	
	@SuppressWarnings("rawtypes")
	public List getSubmodelList(Object year, Object makeId,Object modelId) {
		String hql = "select distinct f.submodel as submodel, f.acesVehicleId as acesVehicleId, f.tpmsInd as tpms from Fitment f where f.modelYear=? and f.makeId=? and f.modelId=? order by f.submodel";
		return getHibernateTemplate().find(hql, new Object[]{year,new Long(makeId.toString()),new Long(modelId.toString())});
	}
	
	@SuppressWarnings("rawtypes")
	public List getSubmodelListByName(Object year, String makeName,String modelName) {
		String hql = "select distinct f.submodel as submodel, f.acesVehicleId as acesVehicleId, f.tpmsInd as tpms from Fitment f where f.modelYear=? and f.makeName=? and f.modelName=? order by f.submodel";
		return getHibernateTemplate().find(hql, new Object[]{year,makeName,modelName});
	}

	@Override
	public List<Object[]> getSubmodelListByNameCaseInsensitive(Object year, String makeName,String modelName) {
		String hql = "select distinct f.submodel as submodel, f.acesVehicleId as acesVehicleId, f.tpmsInd as tpms from Fitment f where f.modelYear=? and lower(f.makeName)=lower(?) and lower(f.modelName)=lower(?) order by f.submodel";
		return getHibernateTemplate().find(hql, new Object[]{year,makeName,modelName});
	}
	
	@SuppressWarnings("rawtypes")
	public List getSubmodelListNew(Object year, Object makeId,Object modelId) {
		String hql = "select distinct f.submodel as submodel, f.submodelId as submodelId, f.acesVehicleId as acesVehicleId, f.tpmsInd as tpms from Fitment f where f.modelYear=? and f.makeId=? and f.modelId=? order by f.submodel";
		return getHibernateTemplate().find(hql, new Object[]{year,new Long(makeId.toString()),new Long(modelId.toString())});
	}
	
	@SuppressWarnings("rawtypes")
	public List getSubmodelListByNameNew(Object year, String makeName,String modelName) {
		String hql = "select distinct f.submodel as submodel, f.submodelId as submodelId, f.acesVehicleId as acesVehicleId, f.tpmsInd as tpms from Fitment f where f.modelYear=? and f.makeName=? and f.modelName=? order by f.submodel";
		return getHibernateTemplate().find(hql, new Object[]{year,makeName,modelName});
	}
	
	@SuppressWarnings("rawtypes")
	public List<String> getSubmodelNameListByAcesVehicleId(Long acesVehicleId) {
		String hql = "select distinct f.submodel as submodel from Fitment f where f.acesVehicleId=? order by f.submodel";
		List<String> submodels = new ArrayList<String>();
		List results = getHibernateTemplate().find(hql, new Object[]{acesVehicleId});
		if (results != null && !results.isEmpty()) {
			for (int i = 0; i < results.size(); i++) {
				submodels.add(results.get(i).toString());
			}
		}
		return submodels;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getModelYears(Map param) throws DataAccessException {
		param.put("column", "modelYear");
		return (List)operate(param);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getMakeNames(Map param) throws DataAccessException {
		param.put("column", "makeName");
		param.put("idColumn", "make_id");
		return (List)operate(param);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getModelNames(Map param) throws DataAccessException {
		param.put("column", "modelName");
		param.put("idColumn", "model_id");
		return (List)operate(param);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getSubmodels(Map param) throws DataAccessException {
		param.put("column", "submodel");
		param.put("idColumn", "submodel_id");
		return (List)operate(param);
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object operate(Object o) throws DataAccessException {
		Map param = (Map)o;
/*
System.out.println("MODEL_YEAR=" + param.get("modelYear"));
System.out.println("MAKE_NAME=" + param.get("makeName"));
System.out.println("MODEL_NAME=" + param.get("modelName"));
*/
		String column = (String)param.get("column");
		String idColumn = (String)param.get("idColumn");
		if(column != null) {
			String nativeSql = "select distinct f.";
			String alias = column, nativeColumn = column, where = " where ";
			Fitment values = new Fitment();
			if("tpms".equals(column)) {
				nativeColumn = "tpms_ind";
				where += "aces_vehicle_id=?";
				nativeSql += nativeColumn + " from bfs_tire_selector.fitment f" + where;
				Session s = getSession();
				List l = null;
				try {
					SQLQuery q = s.createSQLQuery(nativeSql);
					q.addScalar("acesVehicleId", Hibernate.STRING);
					q.setLong(1, ((Long)param.get("acesVehicleId")).longValue());
					l = q.list();
				} finally {
					//s.close();
					this.releaseSession(s);
				}
				String tpms = (String)l.get(0);
				return tpms;
			}
			if("modelYear".equals(column)) {
				nativeColumn = "model_year";
				alias = "year";
				where = "";
			} else {
				where += "f.model_year=:modelYear";
				values.setModelYear((String)param.get("modelYear"));
				if("makeName".equals(column)) {
					nativeColumn = "make_name";
					alias = "make";
				} else {
					if(StringUtils.isNullOrEmpty(param.get("makeName"))){
						Long dbid = getMakeIdByModelId(param.get("modelName"));
						values.setMakeId(dbid.longValue());
					}else{
					    values.setMakeId(Long.valueOf((String)param.get("makeName")).longValue());
					}
					where += " and f.make_id=:makeId";
					if("modelName".equals(column)) {
						nativeColumn = "model_name";
						alias = "model";
					} else if("submodel".equals(column)) {
						values.setModelId(Long.valueOf((String)param.get("modelName")).longValue());
						where += " and f.model_id=:modelId"; 
					}
				}
			}
			nativeSql += nativeColumn 
				+ " as " 
				+ alias;
			if(idColumn != null)
				nativeSql += ", f." + idColumn;
			if("submodel".equals(column))
				nativeSql += ", aces_vehicle_id as acesVehicleId, tpms_ind as tpms";
			nativeSql += " from bfs_tire_selector.fitment f" 
				+ where + " order by " + alias;
			if("year".equals(alias))
				nativeSql += " desc";
			Session s = getSession(); 
			List l = null;
			try {
				SQLQuery q = s.createSQLQuery(nativeSql);
				q.addScalar(alias, Hibernate.STRING);
				if(idColumn != null)
					q.addScalar(idColumn);
				if("submodel".equals(column)) {
					q.addScalar("acesVehicleId", Hibernate.STRING);
					q.addScalar("tpms", Hibernate.STRING);
				}
				q.setProperties(values);
				l = q.list();
				if("submodel".equals(column)) {
					param.put("originalresults", l);
					List out = l;
					l = new ArrayList();
					Iterator i = out.iterator();
					while(i.hasNext()) {
						Object[] item = (Object[])i.next();
						l.add(item[0]);
						param.put("tpms", item[3]);
						param.put("acesVehicleId", item[2]);
					}
				}
			} finally {
				//s.close();
				this.releaseSession(s);
			}
			return l;
		}

		String hql = "from Fitment f";
		String modelYear = (String)param.get("modelYear");
		//FitmentId valueId = new FitmentId();
		Fitment valueBean = new Fitment();
		if(modelYear != null) {
			hql += " where f.modelYear=:modelYear";
			valueBean.setModelYear(modelYear);
		}
		String makeName = (String)param.get("makeName");
		if(makeName != null) {
			hql += " and f.makeId=:makeName";
			valueBean.setMakeName(makeName);
		}
		String modelName = (String)param.get("modelName");
		if(modelName != null) {
			hql += " and f.modelId=:modelName";
			valueBean.setModelName(modelName);
		}
		String submodel = (String)param.get("submodel");
		if(submodel != null) {
			//submodel = com.bfrc.framework.util.StringUtils.unescapeHtmlLight(submodel);
			hql += " and f.submodelId=:submodel";
			valueBean.setSubmodel(submodel);
		}
//		hql += " order by f." + column;
		return getHibernateTemplate().findByValueBean(hql, valueBean);
/*
		return null;
*/
	}
	
	@SuppressWarnings("rawtypes")
	public Long getMakeIdByModelId(Object modelId){
		String hql = "select distinct f.makeId "
			+ "from Fitment f where  f.modelId=? ";
		List l = getHibernateTemplate().find(hql, new Object[]{Long.valueOf(modelId.toString())});
		if(l != null && l.size() > 0){
			Object o = (Object)l.iterator().next();
			return Long.valueOf(o.toString());
		}
		return null;
	
	}
    
	@SuppressWarnings("rawtypes")
	public List<Fitment> getFitmentsByNames(Object year, String make, String model, String submodel) {
		Session s = getSession();
		try{
		//submodel = com.bfrc.framework.util.StringUtils.unescapeHtmlLight(submodel);
		StringBuffer sb = new StringBuffer();
		sb.append(TireSearchUtils.getFitmentSelectorFields());
		sb.append(" WHERE \n");
		sb.append("MODEL_YEAR='" + Encode.escapeDb(year.toString() )+"' \n");
		sb.append(" AND MAKE_NAME='" + Encode.escapeDb(make) +"' \n");
		sb.append(" AND MODEL_NAME='" + Encode.escapeDb(model) + "' \n");
		sb.append(" AND SUBMODEL='" + Encode.escapeDb(submodel) + "' \n");
		String sql = sb.toString();
	    ServerUtil.debug(sql);
	    List fitments = null;
	    try{
	     fitments = s
	    .createSQLQuery(sql)
	    .addScalar("speedRating")
	    .addScalar("crossSection")
	    .addScalar("aspect")
	    .addScalar("rimSize")
	    .addScalar("standardInd")
	    .addScalar("frb")
	    .addScalar("vehtype")
	    .addScalar("notes")
	    .addScalar("tireSize")
	    .addScalar("loadIndex")
	    .addScalar("loadRange")
	    .addScalar("frontInf")
	    .addScalar("rearInf")
	    .addScalar("acesVehicleId",Hibernate.LONG)
	    .addScalar("makeId",Hibernate.LONG)
	    .addScalar("modelId",Hibernate.LONG)
	    .addScalar("submodelId",Hibernate.LONG)
	    .addScalar("modelYear",Hibernate.STRING)
	    .addScalar("modelName")
	    .addScalar("submodel")
	    .addScalar("makeName")
	    .addScalar("tpmsInd",Hibernate.INTEGER)
	    .setResultTransformer(Transformers.aliasToBean(Fitment.class)).list();
		}catch(Exception ex){
			System.err.println("Error with getFitmentsByNames: "+sql);
			ex.printStackTrace();
		}
	    fillStandard(fitments);
		return fitments;
		}finally {
			//s.close();
			this.releaseSession(s);
		}
	
	}
	public List getFitmentsLightMakeModelOnly(){
		Session s = getSession();
		try{
		//submodel = com.bfrc.framework.util.StringUtils.unescapeHtmlLight(submodel);
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT DISTINCT MAKE_NAME as makeName, \n");
		sb.append("  MODEL_NAME as modelName \n");
		sb.append("  FROM BFS_TIRE_SELECTOR.FITMENT \n");
		sb.append(" order by makeName, modelName  \n");
		String sql = sb.toString();
	    ServerUtil.debug(sql);
	    List fitments = null;
	    try{
	     fitments = s
	    .createSQLQuery(sql)
	    .addScalar("makeName")
	    .addScalar("modelName")
	    .setResultTransformer(Transformers.aliasToBean(Fitment.class)).list();
		}catch(Exception ex){
			//ex.printStackTrace();
			System.err.println("Error with getFitmentsLightMakeModelOnly: "+sql);
		}
		return fitments;
		}finally {
			//s.close();
			this.releaseSession(s);
		}
	}
	@SuppressWarnings("rawtypes")
	public List<Fitment> getFitmentsByIds(Object year, Object makeId, Object modelId, String submodel) {
		//submodel = com.bfrc.framework.util.StringUtils.unescapeHtmlLight(submodel);
		Session s = getSession();
		try{
		StringBuffer sb = new StringBuffer();
		sb.append(TireSearchUtils.getFitmentSelectorFields());
		sb.append(" WHERE \n");
		sb.append("MODEL_YEAR='" + Encode.escapeDb(year.toString() )+"' \n");
		sb.append(" AND MAKE_ID='" + Encode.escapeDb(makeId.toString()) +"' \n");
		sb.append(" AND MODEL_ID='" + Encode.escapeDb(modelId.toString()) + "' \n");
		sb.append(" AND SUBMODEL='" + Encode.escapeDb(submodel) + "' \n");
	    String sql = sb.toString();
	    ServerUtil.debug(sql);
	    List fitments = null;
	    try{
		    fitments = s
		    .createSQLQuery(sql)
		    .addScalar("speedRating")
		    .addScalar("crossSection")
		    .addScalar("aspect")
		    .addScalar("rimSize")
		    .addScalar("standardInd")
		    .addScalar("frb")
		    .addScalar("vehtype")
		    .addScalar("notes")
		    .addScalar("tireSize")
		    .addScalar("loadIndex")
		    .addScalar("loadRange")
		    .addScalar("frontInf")
		    .addScalar("rearInf")
		    .addScalar("acesVehicleId",Hibernate.LONG)
		    .addScalar("makeId",Hibernate.LONG)
		    .addScalar("modelId",Hibernate.LONG)
		    .addScalar("modelYear",Hibernate.STRING)
		    .addScalar("modelName")
		    .addScalar("submodel")
		    .addScalar("makeName")
		    .addScalar("tpmsInd",Hibernate.INTEGER)
		    .setResultTransformer(Transformers.aliasToBean(Fitment.class)).list();
		}catch(Exception ex){
			//ex.printStackTrace();
			System.err.println("Error with getFitmentsByIds: "+sql);
		}
		fillStandard(fitments);
		return fitments;
		}finally {
			//s.close();
			this.releaseSession(s);
		}
	
	}
	@SuppressWarnings("rawtypes")
	public List<Fitment> getFitmentsByIds(Object year, Object makeId, Object modelId, Object submodelId) {
		//submodel = com.bfrc.framework.util.StringUtils.unescapeHtmlLight(submodel);
		Session s = getSession();
		try{
		StringBuffer sb = new StringBuffer();
		sb.append(TireSearchUtils.getFitmentSelectorFields());
		sb.append(" WHERE \n");
		sb.append("MODEL_YEAR='" + Encode.escapeDb(String.valueOf(year))+"' \n");
		sb.append(" AND MAKE_ID='" + Encode.escapeDb(String.valueOf(makeId)) +"' \n");
		sb.append(" AND MODEL_ID='" + Encode.escapeDb(String.valueOf(modelId)) + "' \n");
		sb.append(" AND SUBMODEL_ID='" + Encode.escapeDb(String.valueOf(submodelId)) + "' \n");
	    String sql = sb.toString();
	    ServerUtil.debug(sql);
	    List fitments = null;
	    try{
		    fitments = s
		    .createSQLQuery(sql)
		    .addScalar("speedRating")
		    .addScalar("crossSection")
		    .addScalar("aspect")
		    .addScalar("rimSize")
		    .addScalar("standardInd")
		    .addScalar("frb")
		    .addScalar("vehtype")
		    .addScalar("notes")
		    .addScalar("tireSize")
		    .addScalar("loadIndex")
		    .addScalar("loadRange")
		    .addScalar("frontInf")
		    .addScalar("rearInf")
		    .addScalar("acesVehicleId",Hibernate.LONG)
		    .addScalar("makeId",Hibernate.LONG)
		    .addScalar("modelId",Hibernate.LONG)
		    .addScalar("submodelId",Hibernate.LONG)
		    .addScalar("modelYear",Hibernate.STRING)
		    .addScalar("modelName")
		    .addScalar("submodel")
		    .addScalar("makeName")
		    .addScalar("tpmsInd",Hibernate.INTEGER)
		    .setResultTransformer(Transformers.aliasToBean(Fitment.class)).list();
		}catch(Exception ex){
			//ex.printStackTrace();
			System.err.println("Error with getFitmentsByIds: "+sql + " exception:"+ex.getMessage());
		}
		fillStandard(fitments);
		return fitments;
		}finally {
			//s.close();
			this.releaseSession(s);
		}
	
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Fitment> getFitments(Object acesVehicleId) {
		//submodel = com.bfrc.framework.util.StringUtils.unescapeHtmlLight(submodel);
		Session s = getSession();
		try{
		StringBuffer sb = new StringBuffer();
	    sb.append(TireSearchUtils.getFitmentSelectorFields());
	    sb.append("  WHERE \n");
	    sb.append("    (FITMENT.ACES_VEHICLE_ID='"+Encode.escapeDb(acesVehicleId.toString())+"') \n");
	    String sql = sb.toString();
	    ServerUtil.debug(sql);
	    List fitments = null;
	    try{
		    fitments = s
		    .createSQLQuery(sql)
		    .addScalar("speedRating")
		    .addScalar("crossSection")
		    .addScalar("aspect")
		    .addScalar("rimSize")
		    .addScalar("standardInd")
		    .addScalar("frb")
		    .addScalar("vehtype")
		    .addScalar("notes")
		    .addScalar("tireSize")
		    .addScalar("loadIndex")
		    .addScalar("loadRange",Hibernate.STRING)
		    .addScalar("frontInf")
		    .addScalar("rearInf")
		    .addScalar("acesVehicleId",Hibernate.LONG)
		    .addScalar("makeId",Hibernate.LONG)
		    .addScalar("modelId",Hibernate.LONG)
		    .addScalar("modelYear",Hibernate.STRING)
		    .addScalar("modelName")
		    .addScalar("submodel")
		    .addScalar("makeName")
		    .addScalar("tpmsInd",Hibernate.INTEGER)
		    .setResultTransformer(Transformers.aliasToBean(Fitment.class)).list();
	    }catch(Exception ex){
			//ex.printStackTrace();
			System.err.println("Error with getFitments: "+sql);
		}
		fillStandard(fitments);
		return fitments;
		}finally {
			//s.close();
			this.releaseSession(s);
		}
	
	}
	
	public void fillStandard(List fitments){
		if(fitments != null){
			Fitment std = null;
			//String standardLoadRange = null;
			String standardLoadIndex = null;
			for(int i=0; i< fitments.size();i++){
				Fitment fitment = (Fitment)fitments.get(i);
				if('S' == fitment.getStandardInd()){
					std = fitment;
					//standardLoadRange = fitment.getLoadRange();
					standardLoadIndex = fitment.getLoadIndex();
					break;
				}
					
			}
			for(int i=0; i< fitments.size();i++){
				Fitment fitment = (Fitment)fitments.get(i);
				String currRange = fitment.getLoadRange();
				if(StringUtils.isNullOrEmpty(currRange)){
					fitment.setLoadRange("SL");
				}
				String currIndex =fitment.getLoadIndex();
				if(StringUtils.isNullOrEmpty(currIndex) || currIndex.equals("0")){
					fitment.setLoadIndex(standardLoadIndex);
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public List<Tire> populateTiresDescription(List articles){
		String str_articles = StringUtils.join(articles, "|");
		return populateTiresDescription(str_articles);
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Tire> populateTiresDescription(String articles){
		Session s = getSession();
		try{
		StringBuffer articlesCons = new StringBuffer();
		List pairedTireFront = new ArrayList(); 
		List pairedTireRear = new ArrayList(); 
		Map front_rear = new HashMap(); 
		if(articles != null){
			List l = new ArrayList();
			String[] tokens = articles.split("\\|");
			 for(String article : tokens){
				if(!StringUtils.isNullOrEmpty(article)){
					String[] strs = article.split("_");
					if (Pattern.matches(ARTICLE_PATTERN, strs[0]) == false)
						return null;
					String fronArticle = strs[0];
					String rearArticle = null;
					if(strs.length == 2){
						if (Pattern.matches(ARTICLE_PATTERN, strs[1]) == false)
							return null;
						rearArticle = strs[1];
						front_rear.put(fronArticle, rearArticle);
						if(!pairedTireFront.contains(fronArticle)){
						    pairedTireFront.add(fronArticle);
						}
						if(!pairedTireRear.contains(rearArticle)){
						    pairedTireRear.add(rearArticle);
						}
					}
					if(!l.contains(fronArticle)){
						if(articlesCons.length() == 0){
							articlesCons.append("'"+Encode.escapeDb(fronArticle)+"'");
						}else{
							articlesCons.append(",'"+Encode.escapeDb(fronArticle)+"'");
						}
						l.add(fronArticle);
					}
					if(!StringUtils.isNullOrEmpty(rearArticle)){
						if(!l.contains(rearArticle)){
							if(articlesCons.length() == 0){
								articlesCons.append("'"+Encode.escapeDb(rearArticle)+"'");
							}else{
								articlesCons.append(",'"+Encode.escapeDb(rearArticle)+"'");
							}
							l.add(rearArticle);
						}
					}
				}
			}
		}
		String tireDescriptionSelectorFields = TireSearchUtils.getDescriptionTireSelectorFields();

        String sql = tireDescriptionSelectorFields + "	     WHERE SKU  in  ("+articlesCons+")  \n";
		ServerUtil.debug(sql);
		List<String> l = new ArrayList<String>();
		List<Tire> tires = null;
		try{
		     tires = s
		    .createSQLQuery(sql)
		    .addScalar("article",Hibernate.LONG)
		    .addScalar("displayId",Hibernate.LONG)
		    .addScalar("generateCatalogPage")
		    .addScalar("loadRange")
		    .addScalar("loadIndex",Hibernate.LONG)
		    .addScalar("tireName")
		    .addScalar("brand")
		    .addScalar("tireGroupId",Hibernate.LONG)
		    .addScalar("tireGroupName")
			.addScalar("tireClassId",Hibernate.LONG)
			.addScalar("tireClassName")
		    .addScalar("tireSize")
		    .addScalar("speedRating")
		    .addScalar("sidewallDescription")
		    .addScalar("mileage")
		    .addScalar("technology")
		    .addScalar("warrantyId",Hibernate.LONG)
		    .addScalar("warrantyName")
		    .addScalar("oemFlag")
		    .addScalar("crossSection")
		    .addScalar("aspect")
		    .addScalar("rimSize")
		    .addScalar("discontinued",Hibernate.STRING)
		    .addScalar("article",Hibernate.LONG)
		    .setResultTransformer(Transformers.aliasToBean(Tire.class)).list();
		}catch(Exception ex){
			//ex.printStackTrace();
			System.err.println("Error with populateTiresDescription: "+sql);
		}
		  if(tires != null){
			//--- set Front, Rear, if passed from parameters or cache --//
            List<Tire> uniqueTires = new ArrayList<Tire>();
		    for(Tire tire: tires){
		    	String article = String.valueOf(tire.getArticle());
		    	if(!l.contains(article)){
			    	if(front_rear.containsKey(article)){
			    		String rearArticle = (String)front_rear.get(article);
			    		tire.setRearArticle(Long.parseLong(rearArticle));
			    	}
			    	if(pairedTireFront.contains(article)){
			    		tire.setFrontRearBoth("F");
			    	}
			    	if(pairedTireRear.contains(article)){
			    		tire.setFrontRearBoth("R");
			    	}
			    	l.add(article);
			    	uniqueTires.add(tire);
		    	}
		    	
		    }
		    return uniqueTires;
		  }
		}finally {
			//s.close();
			this.releaseSession(s);
		}
        return null;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Tire> populateTiresPricing(String siteName, List articles,String storeNumber, List tiresIn){
		String str_articles = StringUtils.join(articles, "|");
		return populateTiresPricing(siteName, str_articles,storeNumber,null,tiresIn);
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Tire> populateTiresPricing(String siteName, String articles,String storeNumber, List tiresIn){
		return populateTiresPricing(siteName, articles,storeNumber,null,tiresIn);
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Tire> populateTiresPricing(String siteName, List articles,String storeNumber,String regularStoreNumber, List tiresIn){
		String str_articles = StringUtils.join(articles, "|");
		return populateTiresPricing(siteName, str_articles,storeNumber,regularStoreNumber,tiresIn);
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Tire> populateTiresPricing(String siteName, String articles,String storeNumber,String regularStoreNumber,List tiresIn){
		if(StringUtils.isNullOrEmpty(storeNumber)){
			return null;
		}
		Session s = getSession();
		try{
		if(!StringUtils.isNullOrEmpty(regularStoreNumber)){
			/**
			 * TiresPlus Only
			 *  - If the store is a licensee it will check if pricing data is available for this store, if yes, use the licensee store pricing data, if not then then use the closest regular store pricing data.
			 **/
			if(siteName.equals(Config.TP) || siteName.equals(Config.FIVESTAR)){
				//Util.debug("xxxxxxxxxxxxxxxxxxx\t\tstoreNumber---------------------------------"+storeNumber);
				//Util.debug("xxxxxxxxxxxxxxxxxxx\t\tregularStoreNumber---------------------------------"+regularStoreNumber);
				List tesTires = testTiresPricing(articles,storeNumber,null, tiresIn);
				//Util.debug("xxxxxxxxxxxxxxxxxxx\t\tsize---------------------------------"+tesTires.size());
				if(tesTires == null || tesTires.size() == 0){
					boolean pricingFlag = true;
					try{
					    Store store = findStoreById(Long.valueOf(storeNumber));
					    pricingFlag = (store.getTirePricingActiveFlag().intValue() == 1);
					}catch(Exception ex){
						pricingFlag = false;
					}
					if(pricingFlag)
					    return populateTiresPricing(siteName, articles,regularStoreNumber,null, tiresIn);
					else 
						return populateTiresPricing(siteName, articles,storeNumber,null, tiresIn);
				}else{
					return populateTiresPricing(siteName, articles,storeNumber,null, tiresIn);
				}
			}
			
		}
		StringBuffer articlesCons = new StringBuffer();
		List<String> pairedTireFront = new ArrayList<String>(); 
		List<String> pairedTireRear = new ArrayList<String>(); 
		Map<String,String> front_rear = new HashMap<String,String>(); 
		List<String> allArticles = new ArrayList<String>();
		if(articles != null){
			List<String> l = new ArrayList<String>();
			String[] tokens = articles.split("\\|");
			for(String article : tokens){
				if(!StringUtils.isNullOrEmpty(article)){
					String[] strs = article.split("_");
					if (Pattern.matches(ARTICLE_PATTERN, strs[0]) == false)
						return null;
					String fronArticle = strs[0];
					String rearArticle = null;
					if(strs.length == 2){
						if (Pattern.matches(ARTICLE_PATTERN, strs[1]) == false)
							return null;
						rearArticle = strs[1];
						front_rear.put(fronArticle, rearArticle);
						if(!pairedTireFront.contains(fronArticle)){
						    pairedTireFront.add(fronArticle);
						}
						if(!pairedTireRear.contains(rearArticle)){
						    pairedTireRear.add(rearArticle);
						}
					}
					if(!allArticles.contains(fronArticle)){
						allArticles.add(fronArticle);
					}
					if(!l.contains(fronArticle)){
						if(articlesCons.length() == 0){
							articlesCons.append("'"+Encode.escapeDb(fronArticle)+"'");
						}else{
							articlesCons.append(",'"+Encode.escapeDb(fronArticle)+"'");
						}
						l.add(fronArticle);
						
					}
					if(!StringUtils.isNullOrEmpty(rearArticle)){
						if(!allArticles.contains(rearArticle)){
							allArticles.add(rearArticle);
						}
						if(!l.contains(rearArticle)){
							if(articlesCons.length() == 0){
								articlesCons.append("'"+Encode.escapeDb(rearArticle)+"'");
							}else{
								articlesCons.append(",'"+Encode.escapeDb(rearArticle)+"'");
							}
							l.add(rearArticle);
						}
					}
						
				}
				if(allArticles.size() > 999)
					break;
			}
		}
		String query = TireSearchUtils.getTireSelectorPriceQuery();
        String sql = query.replace("%%ARTICLES_LIST%%",articlesCons);
        sql = sql.replace("%%STORE_NUMBER%%", Encode.escapeDb(storeNumber));
		ServerUtil.debug(sql);
		List<Tire> prices = null;
		try{
		    prices = s
		    .createSQLQuery(sql)
		    .addScalar("article",Hibernate.LONG)
		    .addScalar("description")
		    .addScalar("line",Hibernate.LONG)
		    .addScalar("retailPrice",Hibernate.DOUBLE)
		    .addScalar("onSale",Hibernate.STRING)
		    .addScalar("endDate")
		    .addScalar("exciseTax",Hibernate.DOUBLE)
		    .addScalar("exciseTaxArticle",Hibernate.STRING)
		    .addScalar("tireFee",Hibernate.DOUBLE)
		    .addScalar("feeDesc")
		    .addScalar("feeArticle",Hibernate.STRING)
		    .addScalar("wheelBalanceWeight",Hibernate.DOUBLE)
		    .addScalar("wheelWgtArticle",Hibernate.STRING)
		    .addScalar("wheelBalanceLabor",Hibernate.DOUBLE)
		    .addScalar("wheelBalArticle",Hibernate.STRING)
		    .addScalar("valveStem",Hibernate.DOUBLE)
		    .addScalar("valveStemArticle",Hibernate.STRING)
		    .addScalar("disposalPrice",Hibernate.DOUBLE)
		    .addScalar("disposalFeeArticle",Hibernate.STRING)
		    .addScalar("tireInstallPrice",Hibernate.DOUBLE)
		    .addScalar("tireInstallArticle",Hibernate.STRING)
		    .setResultTransformer(Transformers.aliasToBean(Tire.class)).list();
		}catch(Exception ex){
			//ex.printStackTrace();
			System.err.println("Error with populateTiresPricing: "+sql);
		}
		
		//-- merge the price info into the tire list --//
		List<Tire> out = new ArrayList<Tire>();
		
		if(prices != null){
			//java.util.Map<String, Tire> m = TireSearchUtils.getMappedTires(tiresIn);
			java.util.Map<String, Tire> mp = TireSearchUtils.getMappedTires(prices);
		    for(int i=0; i< tiresIn.size(); i++){
		    	
		    	Tire tire = (Tire)tiresIn.get(i);
		    	if(mp.get(String.valueOf(tire.getArticle())) != null){
		    		Tire price = mp.get(String.valueOf(tire.getArticle()));
		    		//Util.debug("xxxxxxxxxxxxxxxxxxx\t\tprice.getRetailPrice()"+price.getRetailPrice());
		    		TireSearchUtils.populatePrice(tire, price);
		    	}
		    	out.add(tire);
		    }
		}
		//results.setTires(tires);
        return out;
		}finally {
			//s.close();
			this.releaseSession(s);
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Tire> testTiresPricing(String articles,String storeNumber,String regularStoreNumber,List tiresIn){
		Session s = getSession();
		try{
		StringBuffer articlesCons = new StringBuffer();
		List<String> pairedTireFront = new ArrayList<String>(); 
		List<String> pairedTireRear = new ArrayList<String>(); 
		Map<String,String> front_rear = new HashMap<String,String>(); 
		List<String> allArticles = new ArrayList<String>();
		if(articles != null){
			List<String> l = new ArrayList<String>();
			String[] tokens = articles.split("\\|");
			for(String article : tokens){
				if(!StringUtils.isNullOrEmpty(article)){
					String[] strs = article.split("_");
					if (Pattern.matches(ARTICLE_PATTERN, strs[0]) == false)
						return null;
					String fronArticle = strs[0];
					String rearArticle = null;
					if(strs.length == 2){
						if (Pattern.matches(ARTICLE_PATTERN, strs[1]) == false)
							return null;
						rearArticle = strs[1];
						front_rear.put(fronArticle, rearArticle);
						if(!pairedTireFront.contains(fronArticle)){
						    pairedTireFront.add(fronArticle);
						}
						if(!pairedTireRear.contains(rearArticle)){
						    pairedTireRear.add(rearArticle);
						}
					}
					if(!allArticles.contains(fronArticle)){
						allArticles.add(fronArticle);
					}
					if(!l.contains(fronArticle)){
						if(articlesCons.length() == 0){
							articlesCons.append("'"+Encode.escapeDb(fronArticle)+"'");
						}else{
							articlesCons.append(",'"+Encode.escapeDb(fronArticle)+"'");
						}
						l.add(fronArticle);
						
					}
					if(!StringUtils.isNullOrEmpty(rearArticle)){
						if(!allArticles.contains(rearArticle)){
							allArticles.add(rearArticle);
						}
						if(!l.contains(rearArticle)){
							if(articlesCons.length() == 0){
								articlesCons.append("'"+Encode.escapeDb(rearArticle)+"'");
							}else{
								articlesCons.append(",'"+Encode.escapeDb(rearArticle)+"'");
							}
							l.add(rearArticle);
						}
					}
						
				}
				if(allArticles.size() > 999)
					break;
			}
		}
		String query = TireSearchUtils.getTireSelectorPriceQuery();
        String sql = query.replace("%%ARTICLES_LIST%%",articlesCons);
        sql = sql.replace("%%STORE_NUMBER%%", Encode.escapeDb(storeNumber));
		ServerUtil.debug(sql);
		List<Tire> prices = null;
		try{
		    prices = s
		    .createSQLQuery(sql)
		    .addScalar("article",Hibernate.LONG)
		    .addScalar("description")
		    .addScalar("line",Hibernate.LONG)
		    .addScalar("retailPrice",Hibernate.DOUBLE)
		    .addScalar("onSale",Hibernate.STRING)
		    .addScalar("endDate")
		    .addScalar("exciseTax",Hibernate.DOUBLE)
		    .addScalar("exciseTaxArticle",Hibernate.STRING)
		    .addScalar("tireFee",Hibernate.DOUBLE)
		    .addScalar("feeDesc")
		    .addScalar("feeArticle",Hibernate.STRING)
		    .addScalar("wheelBalanceWeight",Hibernate.DOUBLE)
		    .addScalar("wheelWgtArticle",Hibernate.STRING)
		    .addScalar("wheelBalanceLabor",Hibernate.DOUBLE)
		    .addScalar("wheelBalArticle",Hibernate.STRING)
		    .addScalar("valveStem",Hibernate.DOUBLE)
		    .addScalar("valveStemArticle",Hibernate.STRING)
		    .addScalar("disposalPrice",Hibernate.DOUBLE)
		    .addScalar("disposalFeeArticle",Hibernate.STRING)
		    .addScalar("tireInstallPrice",Hibernate.DOUBLE)
		    .addScalar("tireInstallArticle",Hibernate.STRING)
		    .setResultTransformer(Transformers.aliasToBean(Tire.class)).list();
		}catch(Exception ex){
			//ex.printStackTrace();
			System.err.println("Error with testTiresPricing: "+sql);
		}
		
        return prices;
		}finally {
			//s.close();
			this.releaseSession(s);
		}
	}
	
	public Store findStoreById(Object storeNumber) {
		if(storeNumber == null)
			return null;
		List l = getHibernateTemplate().findByNamedParam("from Store s where s.storeNumber=:storeNumber",
				"storeNumber",
				Long.valueOf(storeNumber.toString()));
		return (Store)l.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public List<Tire> searchTiresByVehicle(String siteName, Object storeNumber,Object acesVehicleId, boolean byAdvanced, String tireGroup, String tireClass){
       List<Fitment> fitments = getFitments(acesVehicleId);
	   return getTiresByFitments(siteName, fitments, storeNumber,null,byAdvanced, tireGroup, tireClass);
	}
	
	@SuppressWarnings("unchecked")
	public List<Tire> searchTiresByVehicle(String siteName, Object storeNumber,Object acesVehicleId){
       List<Fitment> fitments = getFitments(acesVehicleId);
	   return getTiresByFitments(siteName, fitments, storeNumber,null,false, null, null);
	}
	
	public List<Tire> searchTiresByVehicle(String siteName, Object storeNumber,Object regularStoreNumber,Object acesVehicleId, boolean byAdvanced, String tireGroup, String tireClass){
       List<Fitment> fitments = getFitments(acesVehicleId);
	   return getTiresByFitments(siteName, fitments, storeNumber,regularStoreNumber,byAdvanced, tireGroup, tireClass);
	}
	
	public List<Tire> searchTiresByVehicle(String siteName, Object storeNumber,Object regularStoreNumber,Object acesVehicleId){
       List<Fitment> fitments = getFitments(acesVehicleId);
	   return getTiresByFitments(siteName, fitments, storeNumber,regularStoreNumber,false, null, null);
	}
	
	public List<Tire> searchTiresByVehicleIds(String siteName, Object storeNumber,Object year, Object makeId, Object modelId, String submodel, boolean byAdvanced, String tireGroup, String tireClass){
		List<Fitment> fitments = getFitmentsByIds(year,makeId,modelId,submodel);
		return getTiresByFitments(siteName, fitments, storeNumber,null,byAdvanced, tireGroup, tireClass);
	}
	//submodel replaced with submodelId
	public List<Tire> searchTiresByVehicleIds(String siteName, Object storeNumber,Object year, Object makeId, Object modelId, Object submodelId, boolean byAdvanced, String tireGroup, String tireClass){
		List<Fitment> fitments = getFitmentsByIds(year,makeId,modelId,submodelId);
		return getTiresByFitments(siteName, fitments, storeNumber,null,byAdvanced, tireGroup, tireClass);
	}
	
	public List<Tire> searchTiresByVehicleIds(String siteName, Object storeNumber,Object year, Object makeId, Object modelId, String submodel) {
		List<Fitment> fitments = getFitmentsByIds(year,makeId,modelId,submodel);
		return getTiresByFitments(siteName, fitments, storeNumber,null, false, null, null);
	}
	//submodel replaced with submodelId
	public List<Tire> searchTiresByVehicleIds(String siteName, Object storeNumber,Object year, Object makeId, Object modelId, Object submodelId) {
		List<Fitment> fitments = getFitmentsByIds(year,makeId,modelId,submodelId);
		return getTiresByFitments(siteName, fitments, storeNumber,null, false, null, null);
	}
	
	public List<Tire> searchTiresByVehicleIds(String siteName, Object storeNumber,Object regularStoreNumber,Object year, Object makeId, Object modelId, String submodel, boolean byAdvanced, String tireGroup, String tireClass){
		List<Fitment> fitments = getFitmentsByIds(year,makeId,modelId,submodel);
		return getTiresByFitments(siteName, fitments, storeNumber,regularStoreNumber, byAdvanced, tireGroup, tireClass);
	}
	//submodel replaced with submodelId
	public List<Tire> searchTiresByVehicleIds(String siteName, Object storeNumber,Object regularStoreNumber,Object year, Object makeId, Object modelId, Object submodelId, boolean byAdvanced, String tireGroup, String tireClass){
		List<Fitment> fitments = getFitmentsByIds(year,makeId,modelId,submodelId);
		return getTiresByFitments(siteName, fitments, storeNumber,regularStoreNumber, byAdvanced, tireGroup, tireClass);
	}
	
	public List<Tire> searchTiresByVehicleIds(String siteName, Object storeNumber,Object regularStoreNumber,Object year, Object makeId, Object modelId, String submodel) {
		List<Fitment> fitments = getFitmentsByIds(year,makeId,modelId,submodel);
		return getTiresByFitments(siteName, fitments, storeNumber,regularStoreNumber, false, null, null);
	}
	//submodel replaced with submodelId
	public List<Tire> searchTiresByVehicleIds(String siteName, Object storeNumber,Object regularStoreNumber,Object year, Object makeId, Object modelId, Object submodelId) {
		List<Fitment> fitments = getFitmentsByIds(year,makeId,modelId,submodelId);
		return getTiresByFitments(siteName, fitments, storeNumber,regularStoreNumber, false, null, null);
	}
	
	public List<Tire> searchTiresByVehicleNames(String siteName, Object storeNumber, Object year, String make, String model, String submodel){
		List<Fitment> fitments = getFitmentsByNames(year, make, model, submodel);
		return getTiresByFitments(siteName, fitments, storeNumber,null, false, null, null);
	}
	
	public List<Tire> searchTiresByVehicleNames(String siteName, Object storeNumber,Object regularStoreNumber, Object year, String make, String model, String submodel){
		List<Fitment> fitments = getFitmentsByNames(year, make, model, submodel);
		return getTiresByFitments(siteName, fitments, storeNumber,regularStoreNumber, false, null, null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Tire> getTiresByFitments(String siteName, List<Fitment> fitments, Object storeNumber, Object regularStoreNumber){
        return getTiresByFitments(siteName, fitments,storeNumber,regularStoreNumber,false,null,null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Tire> getTiresByFitments(String siteName, List<Fitment> fitments, Object storeNumber, Object regularStoreNumber, boolean byAdvanced, String tireGroup, String tireClass){
		Session s = getSession();
		try{
		Store store = findStoreById(storeNumber);
		String tireSelectorFields = TireSearchUtils.getTireSelectorFields();
		boolean queryTirePrice  = true;
		if(byAdvanced){
			if(StringUtils.isNullOrEmpty(tireGroup) && StringUtils.isNullOrEmpty(tireClass)){
				tireSelectorFields = TireSearchUtils.getTireSelectorGroupFields();
				queryTirePrice  = false;
			}else if(!StringUtils.isNullOrEmpty(tireGroup) && StringUtils.isNullOrEmpty(tireClass)){
				tireSelectorFields = TireSearchUtils.getTireSelectorClassFields();
				queryTirePrice  = false;
			}
		}
		StringBuffer sb = new StringBuffer(); 
			     sb.append("  WHERE \n");
			     sb.append("      (TIRE_WEBSOURCE.WEBSOURCE='"+Config.getStoreWebSource(store)+"') \n");
;
			     //-- If Bridgestone Five Star Show Bridgestone Tires Only--// 
			     if(Config.FIVESTAR.equals(siteName)){
			    	 sb.append("      AND BRAND.ID=241 ");
			     }
		
			     if(fitments != null && fitments.size() > 0){
			    	 Fitment fitment = (Fitment)fitments.get(0);
			    	 sb.append("      AND "+TireSearchUtils.VEHICLE_TABLE + ".ACES_VEHICLE_ID=");
			    	 sb.append(Encode.escapeDb((String.valueOf(fitment.getAcesVehicleId())))+"\n");
	 				 sb.append("      AND "+TireSearchUtils.VEHICLE_TABLE + ".SUBMODEL='");
	 				 sb.append(Encode.escapeDb(fitment.getSubmodel())+"'\n");
		 			 if(!byAdvanced){
			 			 if("PASS".equalsIgnoreCase(fitment.getVehtype())) {
			                sb.append("      AND (TIREGROUP.VALUE<>\'" + TireSearchUtils.TRUCK_CATEGORY + "\') ");
			                sb.append("      AND (SEGMENT.VALUE<>\'" + TireSearchUtils.TRUCK_SNOW_SEGMENT + "\') \n");
			             }
		 			 }else{
		 				if(!StringUtils.isNullOrEmpty(tireGroup)){
		                	sb.append(" AND (TIREGROUP.VALUE=\'" + Encode.escapeDb(tireGroup) + "\') ");
		                }
		                if(!StringUtils.isNullOrEmpty(tireClass)){
		                	sb.append(" AND (CLASS.VALUE=\'" + Encode.escapeDb(tireClass) + "\') ");
		                }
		 			 }
			     }else{
			    	 return new ArrayList();
			     }
			     String sql = tireSelectorFields + sb+ TireSearchUtils.buildFitmentQuery(fitments);
                 if(queryTirePrice)
			         sql += "ORDER BY BRAND.VALUE ASC,DISPLAY.VALUE ASC";
                 else 
                	 sql += "ORDER BY 1";
		         
		ServerUtil.debug(sql);
		List<Tire> tires = null;
		try{
			if(!queryTirePrice){
				if(StringUtils.isNullOrEmpty(tireGroup) && StringUtils.isNullOrEmpty(tireClass)){
					tires = s
					.createSQLQuery(sql)
					.addScalar("tireGroupId",Hibernate.LONG)
					.addScalar("tireGroupName")
					.setResultTransformer(Transformers.aliasToBean(Tire.class)).list();
				}else if(!StringUtils.isNullOrEmpty(tireGroup) && StringUtils.isNullOrEmpty(tireClass)){
					tireSelectorFields = TireSearchUtils.getTireSelectorClassFields();
					tires = s
					.createSQLQuery(sql)
					.addScalar("tireClassId",Hibernate.LONG)
				    .addScalar("tireClassName")
					.setResultTransformer(Transformers.aliasToBean(Tire.class)).list();
				}
			}else{
				try{
				tires = s
				.createSQLQuery(sql)
				.addScalar("standardOptional",Hibernate.STRING)
				.addScalar("frontRearBoth",Hibernate.STRING)
				.addScalar("vehtype",Hibernate.STRING)
				.addScalar("displayId",Hibernate.LONG)
				.addScalar("generateCatalogPage")
				.addScalar("loadRange")
				.addScalar("loadIndex",Hibernate.LONG)
				.addScalar("tireName")
				.addScalar("brand")
				.addScalar("tireGroupId",Hibernate.LONG)
				.addScalar("tireGroupName")
				.addScalar("tireClassId",Hibernate.LONG)
				.addScalar("tireClassName")
				.addScalar("tireSegmentId",Hibernate.LONG)
				.addScalar("tireSegmentName")
				.addScalar("tireSize")
				.addScalar("speedRating")
				.addScalar("sidewallDescription")
				.addScalar("mileage")
				.addScalar("technology")
				.addScalar("warrantyId",Hibernate.LONG)
				.addScalar("warrantyName")
				.addScalar("oemFlag")
				.addScalar("crossSection")
				.addScalar("aspect")
				.addScalar("rimSize")
				.addScalar("discontinued",Hibernate.STRING)
				.addScalar("article",Hibernate.LONG)
				.setResultTransformer(Transformers.aliasToBean(Tire.class)).list();
				}catch(Exception ex){
					ex.printStackTrace();
					System.err.println("Error with getTiresByFitments: "+sql);
				}
			}
			if(queryTirePrice){
				List<Tire> utires =  TireSearchUtils.DedupTires(tires);
				if(utires != null && utires.size() > 0){
				    StringBuffer articles = new StringBuffer("|");
				    for(Tire tire : utires){
				    	articles.append(tire.getArticle()+"|");
				    }
				    String strNumber = storeNumber == null ? null : storeNumber.toString();
				    String strRegNumber = regularStoreNumber == null ? null : regularStoreNumber.toString();
				    List ptires = populateTiresPricing(siteName, articles.toString(),strNumber,strRegNumber,utires);
				    return ptires;
				}
			}else{
				return tires;
			}
		}catch(Exception ex){
			ex.printStackTrace();
			System.err.println("\t\terror processing getTiresByFitments :");
			System.err.println("\t\t"+sql);
			
		}
		return new ArrayList();
		}finally {
			//s.close();
			this.releaseSession(s);
		}
	}
	
	public List<Tire> searchTiresBySize(String siteName, Object storeNumber,Object cross, Object aspect, Object rim) {
		return searchTiresBySize(siteName, storeNumber,null,cross, aspect, rim);
	}
	@SuppressWarnings("unchecked")
	public List<Tire> searchTiresBySize(String siteName, Object storeNumber,Object regularStoreNumber,Object cross, Object aspect, Object rim) {
		Session s = getSession();
		try{
		Store store = findStoreById(storeNumber);
		String tireSelectorFields = TireSearchUtils.getSizeTireSelectorFields();
		String sql = tireSelectorFields+"" +
		"  WHERE \n"+
	     "      (TIRE_WEBSOURCE.WEBSOURCE='"+Config.getStoreWebSource(store)+"') \n";

	    //-- If Bridgestone Five Star Show Bridgestone Tires Only--// 
	     if(Config.FIVESTAR.equals(siteName)) {
	    	 sql  += "      AND BRAND.ID=241 ";
	     }
	     sql += TireSearchUtils.buildSizeQuery(cross, aspect, rim);
	     
		ServerUtil.debug(sql);
		List<Tire> tires = null;
		try{
		     tires =s
		    .createSQLQuery(sql)
		    //.addScalar("standardOptional",Hibernate.STRING)
		    //.addScalar("frontRearBoth",Hibernate.STRING)
		    .addScalar("displayId",Hibernate.LONG)
		    .addScalar("generateCatalogPage")
		    .addScalar("loadRange")
		    .addScalar("loadIndex",Hibernate.LONG)
		    .addScalar("tireName")
		    .addScalar("brand")
		    .addScalar("tireGroupId",Hibernate.LONG)
		    .addScalar("tireGroupName")
			.addScalar("tireClassId",Hibernate.LONG)
			.addScalar("tireClassName")
			.addScalar("tireSegmentId",Hibernate.LONG)
			.addScalar("tireSegmentName")
		    .addScalar("tireSize")
		    .addScalar("sidewallDescription")
		    .addScalar("mileage")
		    .addScalar("technology")
		    .addScalar("warrantyId",Hibernate.LONG)
		    .addScalar("warrantyName")
		    .addScalar("oemFlag")
		    .addScalar("crossSection")
		    .addScalar("aspect")
		    .addScalar("speedRating")
		    .addScalar("rimSize")
		    .addScalar("discontinued",Hibernate.STRING)
		    .addScalar("article",Hibernate.LONG)
		    .setResultTransformer(Transformers.aliasToBean(Tire.class)).list();
		}catch(Exception ex){
			//ex.printStackTrace();
			System.err.println("Error with searchTiresBySize: "+sql);
		}
		List<Tire> utires =  TireSearchUtils.DedupTires(tires);
		if(utires != null && utires.size() > 0){
		    StringBuffer articles = new StringBuffer("|");
		    for(Tire tire : utires){
		    	articles.append(tire.getArticle()+"|");
		    }
		    String strNumber = storeNumber == null ? null : storeNumber.toString();
		    String strRegNumber = regularStoreNumber == null ? null : regularStoreNumber.toString();
		    List ptires = populateTiresPricing(siteName, articles.toString(),strNumber,strRegNumber,utires);
		    return ptires;
		}
		
		return null;
		}finally {
			//s.close();
			this.releaseSession(s);
		}
	}
	
	public List<Tire> populateTiresByArticles(String siteName, String articleNumbersdelimited, String storeNumber){
		List<Tire> l = populateTiresDescription(articleNumbersdelimited);
		return populateTiresPricing(siteName, articleNumbersdelimited,storeNumber,l);
	}
	
	public List<Tire> populateTiresByArticles(String siteName, String articleNumbersdelimited,String storeNumber, String regularStoreNumber){
		List<Tire> l = populateTiresDescription(articleNumbersdelimited);
		return populateTiresPricing(siteName, articleNumbersdelimited,storeNumber,regularStoreNumber,l);
	}
	
	public List<Tire> populateTiresByArticles(String siteName, List articleNumbers, String storeNumber){
		List<Tire> l = populateTiresDescription(articleNumbers);
		return populateTiresPricing(siteName, articleNumbers,storeNumber,l);
	}
	
	public List<Tire> populateTiresByArticles(String siteName, List articleNumbers, String storeNumber, String regularStoreNumber){
		List<Tire> l = populateTiresDescription(articleNumbers);
		return populateTiresPricing(siteName, articleNumbers,storeNumber,regularStoreNumber,l);
	}
	
	@SuppressWarnings("rawtypes")
	public String getMakeNameByMakeId(Object makeId) {
		if(makeId == null) return null;
		String hql = "select distinct f.makeName from Fitment f where f.makeId=?";
		List l = getHibernateTemplate().find(hql, new Object[]{Long.valueOf(makeId.toString())});
		if(l != null && l.size() > 0)
			return l.get(0).toString();
		else
	      return null;
	}
	@SuppressWarnings("rawtypes")
	public String getModelNameByModelId(Object modelId) {
		try{
			if(modelId == null) return null;
			String hql = "select distinct f.modelName from Fitment f where f.modelId=?";
			List l = getHibernateTemplate().find(hql, new Object[]{Long.valueOf(modelId.toString())});
			if(l != null && l.size() > 0)
				return l.get(0).toString();
			else
		      return null;
		}catch(NumberFormatException e){
			return null;
		}
		
	}
	
	@SuppressWarnings("rawtypes")
	public String getSubmodelNameBySubmodelId(Object submodelId) {
		try {
			if (submodelId == null)
				return null;
			String hql = "select distinct f.submodel from Fitment f where f.submodelId=?";
			List l = getHibernateTemplate().find(hql,
					new Object[] { Long.valueOf(submodelId.toString()) });
			if (l != null && l.size() > 0)
				return l.get(0).toString();
			else
				return null;
		} catch (NumberFormatException e) {
			return null;
		}

	}
	
	@SuppressWarnings("rawtypes")
	public List getCrossSectionList() {
		String hql = "select distinct c.crossSection as cross from Configuration2 c order by c.crossSection desc";
		return getHibernateTemplate().find(hql);
	}
	
	@SuppressWarnings("rawtypes")
	public List getAspectList(Object cross) {
		String hql = "select distinct c.aspect as aspect from Configuration2 c where c.crossSection=? order by c.aspect";
		return getHibernateTemplate().find(hql, new Object[]{cross});
	}
	
	@SuppressWarnings("rawtypes")
	public List getRimList(Object cross, Object aspect) {
		String hql = "select distinct c.rimSize as rim from Configuration2 c  where c.crossSection=? and c.aspect=? order by c.rimSize";
		return getHibernateTemplate().find(hql, new Object[]{cross, aspect});
	}
	
	@SuppressWarnings("rawtypes")
	public List getRimList(String siteName) {
		String hql = null;
		List l1 = null;
		if (siteName == null || siteName.isEmpty()) {
			hql = "select distinct c.rimSize as rim from Configuration2 c  order by c.rimSize";
			l1 = getHibernateTemplate().find(hql);
		} else {
			hql = "select distinct c.rimSize as rim from Configuration2 c, TireWebsource2 ws "+
					"where c.sku = ws.id.sku and ws.id.websource=? order by c.rimSize";
			l1 = getHibernateTemplate().find(hql, new Object[]{siteName});
		}		
		return l1;
	}
	
	@SuppressWarnings("rawtypes")
	public List getCrossSectionListByRim(Object rim, Object siteName) {
		if (siteName == null || siteName.toString().isEmpty()) {
			siteName = "%";
		}
		String hql = "select distinct c.crossSection as cross from Configuration2 c, TireWebsource2 ws "+
				"where c.rimSize=? and ws.id.websource like ? and c.sku = ws.id.sku order by c.crossSection";
		return getHibernateTemplate().find(hql, new Object[]{rim, siteName});
	}
	
	@SuppressWarnings("rawtypes")
	public List getAspectListByCrossSectionAndRim(Object cross, Object rim, Object siteName) {
		if (siteName == null || siteName.toString().isEmpty()) {
			siteName = "%";
		}
		String hql = "select distinct c.aspect as aspect from Configuration2 c, TireWebsource2 ws "+
				"where c.crossSection=? and c.rimSize=? and ws.id.websource like ? and c.sku = ws.id.sku order by c.aspect";
		return getHibernateTemplate().find(hql, new Object[]{cross, rim, siteName});
	}
	
	/**
	 * This replaces Object getVehicleSizes(Object o);
	 */
	public Map<String, String> getVehicleCrossSections() {
		List<Object> crossSectionValues = this.getCrossSectionList();
		
		if (crossSectionValues != null) {
			Map<String, String> crossSectionMap = new LinkedHashMap<String, String>();
			
			crossSectionValues = TireCatalogUtils.sortSizes(crossSectionValues, "cross");
			List<String> crossSectionNames = TireCatalogUtils.formatSizes(crossSectionValues, "cross");
			
			for (int i = 0; i < crossSectionValues.size(); i++) {
				crossSectionMap.put(crossSectionValues.get(i).toString(), crossSectionNames.get(i));
			}
			
			return crossSectionMap;
		}
		return null;
	}
	
	/**
	 * This replaces Object getVehicleSizes(Object o);
	 */
	public Map<String, String> getVehicleAspects(String crossSection) {
		List<Object> aspectValues = this.getAspectList(crossSection);
		
		if (aspectValues != null) {
			Map<String, String> aspectMap = new LinkedHashMap<String, String>();
			
			aspectValues = TireCatalogUtils.sortSizes(aspectValues, "aspect");
			List<String> aspectNames = TireCatalogUtils.formatSizes(aspectValues, "aspect");
			
			for (int i = 0; i < aspectValues.size(); i++) {
				aspectMap.put(aspectValues.get(i).toString(), aspectNames.get(i));
			}
			
			return aspectMap;
		}
		return null;
	}
	
	/**
	 * This replaces Object getVehicleSizes(Object o);
	 */
	public Map<String, String> getVehicleRims(String crossSection, String aspect) {
		List<Object> rimValues = this.getRimList(crossSection, aspect);
		
		if (rimValues != null) {
			Map<String, String> rimMap = new LinkedHashMap<String, String>();
			
			rimValues = TireCatalogUtils.sortSizes(rimValues, "rim");
			List<String> rimNames = TireCatalogUtils.formatSizes(rimValues, "rim");
			
			for (int i = 0; i < rimValues.size(); i++) {
				rimMap.put(rimValues.get(i).toString(), rimNames.get(i));
			}
			
			return rimMap;
		}
		return null;
	}
	
	public Map<String, String> getVehicleRims() {
		return getVehicleRims(null);
	}
	
	public Map<String, String> getVehicleRims(String siteName) {
		List<Object> rimValues = this.getRimList(siteName);
		
		if (rimValues != null) {
			Map<String, String> rimMap = new LinkedHashMap<String, String>();
			
			rimValues = TireCatalogUtils.sortSizes(rimValues, "rim");
			List<String> rimNames = TireCatalogUtils.formatSizes(rimValues, "rim");
			
			for (int i = 0; i < rimValues.size(); i++) {
				rimMap.put(rimValues.get(i).toString(), rimNames.get(i));
			}
			
			return rimMap;
		}
		return null;
	}
	
	public Map<String, String> getVehicleSizesByRim(String rim) {
		return getVehicleSizesByRim(rim, null);
	}
	
	public Map<String, String> getVehicleSizesByRim(String rim, String siteName) {
		List<Object> rimValues = new ArrayList<Object>();
		rimValues.add(rim);
		List<String> rimNames = TireCatalogUtils.formatSizes(rimValues, "rim");
		String formattedRimSize = rimNames.get(0).toString();
			
		List<Object> crossSectionValues = this.getCrossSectionListByRim(rim, siteName);
		if (crossSectionValues != null) {
			Map<String, String> vehicleSizeMap = new LinkedHashMap<String, String>();
			
			crossSectionValues = TireCatalogUtils.sortSizes(crossSectionValues, "cross");
			List<String> crossSectionNames = TireCatalogUtils.formatSizes(crossSectionValues, "cross");
			for (int i = 0; i < crossSectionValues.size(); i++) {
				List<Object> aspectValues = this.getAspectListByCrossSectionAndRim(crossSectionValues.get(i).toString(), rim, siteName);
				if (aspectValues != null) {
					aspectValues = TireCatalogUtils.sortSizes(aspectValues, "aspect");
					List<String> aspectNames = TireCatalogUtils.formatSizes(aspectValues, "aspect");
					for (int j = 0; j < aspectValues.size(); j++) {
						String size = crossSectionValues.get(i).toString() + "/" + aspectValues.get(j).toString() + "-" + rim;
						String formattedSize = crossSectionNames.get(i).toString() + "/" + aspectNames.get(j).toString() + "-" + formattedRimSize;
						vehicleSizeMap.put(size, formattedSize);
					}
					aspectNames.clear();
					aspectValues.clear();
				}				
			}
			return vehicleSizeMap;
		}
		
		return null;
	}
	
	public Object getVehicleSizes(Object o) {
		Map m = (Map) o;
		String result = ERROR;
		String operation = (String)m.get(OPERATION);
		String type = (String)m.get("type");
		if("dropdown".equals(operation) && type != null) {
			boolean rim = "rim".equals(type); 
			List l = null;
			if("cross".equals(type))
				l = this.getCrossSectionList();
			else if("aspect".equals(type)) {
				if(m.get("cross")!=null) {
					l = this.getAspectList((String)m.get("cross"));
				}
			}	
			else if(rim) {
				if(m.get("cross")!=null && m.get("aspect")!=null) {
					l = this.getRimList((String)m.get("cross"), (String)m.get("aspect"));
				}
			}
				
			if(l != null) {
				l = TireCatalogUtils.sortSizes(l, type);
				List l2 = TireCatalogUtils.formatSizes(l, type);
				m.put(RESULT, l);
				m.put("formattedResult", l2);
			}
			result = SUCCESS;
		}
		return result;
	}	
	
	public long getMakeIdByMakeName(String makeName){
		if(makeName == null) return -1;
		String hql = "select distinct f.makeId from Fitment f where upper(f.makeName)=upper(?)";
		List l = getHibernateTemplate().find(hql, new Object[]{makeName});
		if(l != null && l.size() > 0)
			return Long.parseLong(l.get(0).toString());
		else
	      return -1;
	}
	public long getModelIdByModelName(String modelName){
		if(modelName == null) return -1;
		String hql = "select distinct f.modelId from Fitment f where upper(f.modelName)=upper(?)";
		List l = getHibernateTemplate().find(hql, new Object[]{modelName});
		if(l != null && l.size() > 0)
			return Long.parseLong(l.get(0).toString());
		else
	      return -1;
	}
	public long getSubmodelIdBySubmodelName(String submodelName){
		if(submodelName == null) return -1;
		String hql = "select distinct f.submodelId from Fitment f where upper(f.submodel)=upper(?)";
		List l = getHibernateTemplate().find(hql, new Object[]{submodelName});
		if(l != null && l.size() > 0)
			return Long.parseLong(l.get(0).toString());
		else
	      return -1;
	}
	public List getMakeModelList(){
		String hql = "select distinct f.makeId as makeId, f.makeName as makeName,  f.modelId as modelId, f.modelName as modelName from Fitment f order by f.makeName";
		return getHibernateTemplate().find(hql);
	}
	public List getYearListByMakeAndModel(Object makeIdOrName, Object modelIdOrName){
		boolean byId = false;
		if(makeIdOrName == null || modelIdOrName == null)
			return null;
		try{
			Long.parseLong(makeIdOrName.toString());
			byId = true;
		}catch(Exception ex){}
		if(byId){
			String hql = "select distinct f.modelYear as year from Fitment f where f.makeId = ? and f.modelId = ? order by f.modelYear desc";
			return getHibernateTemplate().find(hql, new Object[]{Long.valueOf(makeIdOrName.toString()), Long.valueOf(modelIdOrName.toString())});
		}else{
			String hql = "select distinct f.modelYear as year from Fitment f where upper(f.makeName)=upper(?) and upper(f.modelName) = upper(?) order by f.modelYear desc";
			return getHibernateTemplate().find(hql, new Object[]{(String)makeIdOrName, (String)modelIdOrName});
		}
	}

	public Store findStoreById(Long storeNumber) {
		if(storeNumber == null)
			return null;
		List l = getHibernateTemplate().findByNamedParam("from Store s where s.storeNumber=:storeNumber",
				"storeNumber",
				storeNumber);
		if (l == null || l.size() < 1)
			return null;
		return (Store)l.get(0);
	}

	public String findToyoStoreById(Long storeNumber) {
		String result = null;		
		Session session = getSession();
		try {
			SQLQuery query = session.createSQLQuery("SELECT * FROM STORE_TOYO_EXCLUSIONS WHERE STORE_NUMBER='"+storeNumber+"'");
			List l = (List<Long>) query.list();
			if(l != null && l.size() > 0)
				result =  l.get(0).toString();
			else
				result =  null;
			
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			releaseSession(session);
		}
		return result;
	}

}
