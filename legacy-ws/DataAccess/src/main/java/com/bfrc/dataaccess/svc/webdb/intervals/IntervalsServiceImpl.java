package com.bfrc.dataaccess.svc.webdb.intervals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.Resource;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfrc.dataaccess.core.util.hibernate.HibernateUtil;
import com.bfrc.dataaccess.dao.generic.IntervalsDAO;
import com.bfrc.dataaccess.model.maintenance.Components;
import com.bfrc.dataaccess.model.maintenance.Intervals;
import com.bfrc.dataaccess.model.maintenance.comparator.ComponentTextComparator;
import com.bfrc.dataaccess.model.maintenance.comparator.MileageComparator;
import com.bfrc.dataaccess.model.maintenance.comparator.OperationTypeComparator;
import com.bfrc.dataaccess.model.maintenance.comparator.ServiceTextComparator;
import com.bfrc.dataaccess.model.maintenance.comparator.TimeIntervalComparator;
import com.bfrc.dataaccess.svc.webdb.IntervalsService;

@Service
public class IntervalsServiceImpl implements IntervalsService {

	private Logger log = Logger.getLogger(getClass().getName());
	
	@Autowired
	private IntervalsDAO intervalsDao;
	@Autowired
	private HibernateUtil hibernateUtil;
	@Resource(name="requiredMaintenanceList")
	private List<String> requiredMaintenanceList;
	@Resource(name="additionalPeriodicMaintenanceList")
	private List<String> additionalPeriodicMaintenanceList;
	@Resource(name="serviceChecksList")
	private List<String> serviceChecksList;
	@Resource(name="maintenanceMilestonesList")
	private List<String> maintenanceMilestonesList;
	
	public List<String> getDistinctServiceTypes(Long acesVehicleId) {
		return new ArrayList<String>(intervalsDao.findDistinctServiceTypesByVehicle(acesVehicleId));
	}
	
	public List<String> getDistinctServiceTypesInSvcType(Long acesVehicleId,
			List<String> serviceTypes) {

		List<Intervals> intervals = getIntervalsInSvcType(acesVehicleId, serviceTypes);
		if(intervals == null || intervals.size() == 0)
			return null;
		
		Set<String> loValues = new HashSet<String>();
		for(Intervals i : intervals) {
			Components cs = i.getComponents();
			if(cs != null && cs.getComponentId() > 0)
				loValues.add(i.getId().getServiceType());
		}
		
		return new ArrayList<String>(loValues);
	}
	
	public List<String> getDistinctMileageValues(Long acesVehicleId, String serviceType) {
		return new ArrayList<String>(intervalsDao.findDistinctMileageByVehicle(acesVehicleId, serviceType));
	}
	
	private List<Intervals> getIntervalsInSvcType(Long acesVehicleId, List<String> serviceTypes) {
		Session session = hibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Intervals.class);
		criteria.add(Restrictions.eq("id.acesVehicleId", acesVehicleId));
		criteria.add(Restrictions.in("id.serviceType", serviceTypes));
		List<Intervals> loResult = new ArrayList<Intervals>();;
		try {
			List<Intervals> tmp = criteria.list();
			if(tmp == null) return loResult;
			for(Intervals i : tmp) {
				if(i.getComponents() != null && i.getComponents().getComponentId() > 0)
					loResult.add(i);
			}
		}catch(Exception E) {
			log.severe(E.getMessage());
		}
		
		return loResult;
	}
	
	public List<Intervals> listRequiredMaintenance(Long acesVehicleId) {
		List<Intervals> loTempResult = getIntervalsInSvcType(acesVehicleId, requiredMaintenanceList);
		
		ComparatorChain chain = new ComparatorChain();
		chain.addComparator(new ServiceTextComparator());
		chain.addComparator(new OperationTypeComparator());
		chain.addComparator(new ComponentTextComparator());
		Collections.sort(loTempResult, chain);
		
		List<Intervals> loResult = new ArrayList<Intervals>();
		Intervals prior = null;
		for(Intervals i : loTempResult) {
//			StringBuilder b = new StringBuilder();
//			b.append(i.getId().getServiceType()).append(" : ");
//			b.append(i.getOperationTypes().getOperationType()).append(" : ");
//			b.append(i.getComponents().getComponent());
//			
//			System.out.println(b.toString());
			if(prior == null) {
				loResult.add(i);
			} else {
				if(!prior.equals(i))
					loResult.add(i);
			}
			prior = i;
			
		}
		return loResult;
	}

	public List<Intervals> listPeriodicMaintenance(Long acesVehicleId) {
		List<Intervals> loResult = getIntervalsInSvcType(acesVehicleId, additionalPeriodicMaintenanceList);
		
		ComparatorChain chain = new ComparatorChain();
		chain.addComparator(new TimeIntervalComparator());
		chain.addComparator(new OperationTypeComparator());
		chain.addComparator(new ComponentTextComparator());
		Collections.sort(loResult, chain);
		return loResult;
	}

	public List<Intervals> listIntervalMaintenance(Long acesVehicleId) {
		List<Intervals> loResult = getIntervalsInSvcType(acesVehicleId, serviceChecksList);
		
		ComparatorChain chain = new ComparatorChain();
		chain.addComparator(new ServiceTextComparator());
		chain.addComparator(new OperationTypeComparator());
		chain.addComparator(new ComponentTextComparator());
		Collections.sort(loResult, chain);
		return loResult;
	}

	public List<Intervals> listMaintenanceMilestones(Long acesVehicleId, String serviceType) {
		List<Intervals> loResult = new ArrayList<Intervals>();
		if("ALL".equalsIgnoreCase(serviceType))
			loResult = new ArrayList<Intervals>(intervalsDao.findByVehicleWithMileage(acesVehicleId));
		else
			loResult = new ArrayList<Intervals>(intervalsDao.findByVehicleAndServiceTypeWithMileage(acesVehicleId, serviceType));
		
		ComparatorChain chain = new ComparatorChain();
		chain.addComparator(new MileageComparator());
		chain.addComparator(new OperationTypeComparator());
		chain.addComparator(new ComponentTextComparator());
		Collections.sort(loResult, chain);
		return loResult;
	}
	
	public List<Intervals> listMaintenanceMilestones(Long acesVehicleId) {
		List<Intervals> loResult = getIntervalsInSvcType(acesVehicleId, maintenanceMilestonesList);
		
		ComparatorChain chain = new ComparatorChain();
		chain.addComparator(new MileageComparator());
		chain.addComparator(new OperationTypeComparator());
		chain.addComparator(new ComponentTextComparator());
		Collections.sort(loResult, chain);
		return loResult;
	}
	
}
