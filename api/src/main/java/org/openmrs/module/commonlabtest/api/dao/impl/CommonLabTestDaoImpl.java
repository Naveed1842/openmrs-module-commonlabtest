/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.commonlabtest.api.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.Provider;
import org.openmrs.module.commonlabtest.LabTest;
import org.openmrs.module.commonlabtest.LabTestAttribute;
import org.openmrs.module.commonlabtest.LabTestAttributeType;
import org.openmrs.module.commonlabtest.LabTestSample;
import org.openmrs.module.commonlabtest.LabTestSample.LabTestSampleStatus;
import org.openmrs.module.commonlabtest.LabTestType;
import org.openmrs.module.commonlabtest.LabTestType.LabTestGroup;
import org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@Component
public class CommonLabTestDaoImpl implements CommonLabTestDao {
	
	private static final int MAX_FETCH_LIMIT = 100;
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * Get session factory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	/**
	 * Set session factory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#getAllLabTestAttributeTypes(boolean)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<LabTestAttributeType> getAllLabTestAttributeTypes(boolean includeRetired) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LabTestAttributeType.class);
		criteria.addOrder(Order.asc("name"));
		if (!includeRetired) {
			criteria.add(Restrictions.eq("retired", false));
		}
		return criteria.list();
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#getAllLabTestTypes(boolean)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<LabTestType> getAllLabTestTypes(boolean includeRetired) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LabTestType.class);
		criteria.addOrder(Order.asc("name"));
		if (!includeRetired) {
			criteria.add(Restrictions.eq("retired", false));
		}
		return criteria.list();
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#getLabTestTypes(java.lang.String,
	 *      java.lang.String, org.openmrs.module.commonlabtest.LabTestType.LabTestGroup,
	 *      org.openmrs.Concept, boolean)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<LabTestType> getLabTestTypes(String name, String shortName, LabTestGroup testGroup, Concept referenceConcept,
	        boolean includeRetired) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LabTestType.class);
		if (name != null) {
			criteria.add(Restrictions.ilike("name", name));
		}
		if (shortName != null) {
			criteria.add(Restrictions.ilike("shortName", name));
		}
		if (testGroup != null) {
			criteria.add(Restrictions.eq("testGroup", testGroup));
		}
		if (referenceConcept != null) {
			criteria.add(Restrictions.ilike("referenceConcept", referenceConcept));
		}
		if (!includeRetired) {
			criteria.add(Restrictions.eq("retired", false));
		}
		criteria.addOrder(Order.asc("name")).addOrder(Order.asc("retired")).list();
		return criteria.list();
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#getLabTest(org.openmrs.Encounter)
	 */
	@Override
	public LabTest getLabTest(Encounter orderEncounter) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LabTest.class);
		criteria.add(Restrictions.eq("order.encounter", orderEncounter));
		return (LabTest) criteria.uniqueResult();
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#getLabTest(java.lang.Integer)
	 */
	@Override
	public LabTest getLabTest(Integer labTestId) {
		return (LabTest) sessionFactory.getCurrentSession().get(LabTest.class, labTestId);
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#getLabTestAttribute(java.lang.Integer)
	 */
	@Override
	public LabTestAttribute getLabTestAttribute(Integer labTestAttributeId) {
		return (LabTestAttribute) sessionFactory.getCurrentSession().get(LabTestAttribute.class, labTestAttributeId);
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#getLabTestAttributeByUuid(java.lang.String)
	 */
	@Override
	public LabTestAttribute getLabTestAttributeByUuid(String uuid) {
		return (LabTestAttribute) sessionFactory.getCurrentSession()
		        .createQuery("from LabTestAttribute l where l.uuid = :uuid").setString("uuid", uuid).uniqueResult();
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#getLabTestAttributes(org.openmrs.module.commonlabtest.LabTestAttributeType,
	 *      org.openmrs.module.commonlabtest.LabTest, org.openmrs.Patient, java.lang.String,
	 *      java.util.Date, java.util.Date, boolean)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<LabTestAttribute> getLabTestAttributes(LabTestAttributeType labTestAttributeType, LabTest labTest,
	        Patient patient, String valueReference, Date from, Date to, boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LabTestAttribute.class);
		if (labTestAttributeType != null) {
			criteria.add(Restrictions.eqOrIsNull("attributeType", labTestAttributeType));
		}
		if (labTest != null) {
			criteria.add(Restrictions.eqOrIsNull("owner.labTestType", labTestAttributeType));
		}
		if (patient != null) {
			criteria.add(Restrictions.eqOrIsNull("owner.order.patient", patient));
		}
		if (valueReference != null) {
			criteria.add(Restrictions.ilike("valueReference", valueReference, MatchMode.START));
		}
		if (from != null && to != null) {
			criteria.add(Restrictions.between("dateCreated", from, to));
		}
		if (!includeVoided) {
			criteria.add(Restrictions.eq("voided", false));
		}
		criteria.addOrder(Order.asc("testOrderId")).addOrder(Order.asc("voided")).list();
		return criteria.list();
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#getLabTestAttributeType(java.lang.Integer)
	 */
	@Override
	public LabTestAttributeType getLabTestAttributeType(Integer labTestAttributeTypeId) {
		return (LabTestAttributeType) sessionFactory.getCurrentSession().get(LabTestAttributeType.class,
		    labTestAttributeTypeId);
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#getLabTestAttributeTypeByUuid(java.lang.String)
	 */
	@Override
	public LabTestAttributeType getLabTestAttributeTypeByUuid(String uuid) {
		return (LabTestAttributeType) sessionFactory.getCurrentSession()
		        .createQuery("from LabTestAttributeType l where l.uuid = :uuid").setString("uuid", uuid).uniqueResult();
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#getLabTestAttributeTypes(java.lang.String,
	 *      java.lang.String, boolean)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<LabTestAttributeType> getLabTestAttributeTypes(String name, String datatypeClassname,
	        boolean includeRetired) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LabTestAttributeType.class);
		if (name != null) {
			criteria.add(Restrictions.ilike("name", name));
		}
		if (datatypeClassname != null) {
			criteria.add(Restrictions.eq("datatypeClassname", datatypeClassname));
		}
		if (!includeRetired) {
			criteria.add(Restrictions.eq("retired", false));
		}
		criteria.addOrder(Order.asc("name")).addOrder(Order.asc("retired")).list();
		return criteria.list();
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#getLabTestByUuid(java.lang.String)
	 */
	@Override
	public LabTest getLabTestByUuid(String uuid) {
		return (LabTest) sessionFactory.getCurrentSession().createQuery("from LabTest l where l.uuid = :uuid")
		        .setString("uuid", uuid).uniqueResult();
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#getLabTests(org.openmrs.module.commonlabtest.LabTestType,
	 *      org.openmrs.Patient, java.lang.String, java.lang.String, org.openmrs.Concept,
	 *      org.openmrs.Provider, java.util.Date, java.util.Date, boolean)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<LabTest> getLabTests(LabTestType labTestType, Patient patient, String orderNumber, String referenceNumber,
	        Concept orderConcept, Provider orderer, Date from, Date to, boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LabTest.class);
		criteria.createAlias("order", "o");
		if (labTestType != null) {
			criteria.add(Restrictions.eq("labTestType", labTestType));
		}
		if (patient != null) {
			criteria.add(Restrictions.eq("o.patient", patient));
		}
		if (orderNumber != null) {
			criteria.add(Restrictions.ilike("o.orderReference", orderNumber, MatchMode.START));
		}
		if (orderConcept != null) {
			criteria.add(Restrictions.eq("o.concept", orderConcept));
		}
		if (orderer != null) {
			criteria.add(Restrictions.eq("o.orderer", orderer));
		}
		if (referenceNumber != null) {
			criteria.add(Restrictions.ilike("referenceNumber", referenceNumber, MatchMode.START));
		}
		if (from != null && to != null) {
			criteria.add(Restrictions.between("dateCreated", from, to));
		}
		if (!includeVoided) {
			criteria.add(Restrictions.eq("voided", false));
		}
		criteria.addOrder(Order.asc("testOrderId")).addOrder(Order.asc("voided")).list();
		return criteria.list();
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#getLabTestSample(java.lang.Integer)
	 */
	@Override
	public LabTestSample getLabTestSample(Integer labTestSampleId) {
		return (LabTestSample) sessionFactory.getCurrentSession().get(LabTestSample.class, labTestSampleId);
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#getLabTestSampleByUuid(java.lang.String)
	 */
	@Override
	public LabTestSample getLabTestSampleByUuid(String uuid) {
		return (LabTestSample) sessionFactory.getCurrentSession().createQuery("from LabTestSample l where l.uuid = :uuid")
		        .setString("uuid", uuid).uniqueResult();
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#getLabTestSamples(org.openmrs.module.commonlabtest.LabTest,
	 *      boolean)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<LabTestSample> getLabTestSamples(LabTest labTest, boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LabTestSample.class);
		criteria.add(Restrictions.eq("labTest", labTest));
		if (!includeVoided) {
			criteria.add(Restrictions.eq("voided", false));
		}
		criteria.addOrder(Order.asc("sampleIdentifier")).addOrder(Order.asc("voided")).list();
		return criteria.list();
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#getLabTestSamples(org.openmrs.Patient,
	 *      boolean)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<LabTestSample> getLabTestSamples(Patient patient, boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LabTestSample.class);
		criteria.add(Restrictions.eq("order.patient", patient));
		if (!includeVoided) {
			criteria.add(Restrictions.eq("voided", false));
		}
		criteria.addOrder(Order.asc("sampleIdentifier")).addOrder(Order.asc("voided")).list();
		return criteria.list();
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#getLabTestSamples(org.openmrs.Patient,
	 *      boolean)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<LabTestSample> getLabTestSamples(Provider collector, boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LabTestSample.class);
		criteria.add(Restrictions.eq("collector", collector));
		if (!includeVoided) {
			criteria.add(Restrictions.eq("voided", false));
		}
		criteria.addOrder(Order.asc("sampleIdentifier")).addOrder(Order.asc("voided")).list();
		return criteria.list();
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#getLabTestType(java.lang.Integer)
	 */
	@Override
	public LabTestType getLabTestType(Integer labTestTypeId) {
		return (LabTestType) sessionFactory.getCurrentSession().get(LabTestType.class, labTestTypeId);
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#getLabTestTypeByUuid(java.lang.String)
	 */
	@Override
	public LabTestType getLabTestTypeByUuid(String uuid) {
		return (LabTestType) sessionFactory.getCurrentSession().createQuery("from LabTestType l where l.uuid = :uuid")
		        .setString("uuid", uuid).uniqueResult();
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#getNLabTests(org.openmrs.Patient,
	 *      int, boolean, boolean, boolean)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<LabTest> getNLabTests(Patient patient, int n, boolean firstNObjects, boolean lastNObjects,
	        boolean includeVoided) {
		// Disallow fetching more than 100 records per query
		if (n > MAX_FETCH_LIMIT) {
			n = MAX_FETCH_LIMIT;
		}
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LabTest.class);
		List<LabTest> firstN = null;
		List<LabTest> lastN = null;
		if (patient != null) {
			criteria.add(Restrictions.eq("order.patient", patient));
		}
		if (!includeVoided) {
			criteria.add(Restrictions.eq("voided", false));
		}
		if (firstNObjects) {
			criteria.addOrder(Order.asc("dateCreated"));
			firstN = criteria.list();
		}
		if (lastNObjects) {
			criteria.addOrder(Order.desc("dateCreated"));
			lastN = criteria.list();
		}
		criteria.setMaxResults(n);
		List<LabTest> list = new ArrayList<>();
		if (firstN != null) {
			list.addAll(firstN);
		}
		if (lastN != null) {
			list.addAll(lastN);
		}
		return list;
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#getNLabTestSamples(org.openmrs.Patient,
	 *      org.openmrs.module.commonlabtest.LabTestSample.LabTestSampleStatus, int, boolean,
	 *      boolean, boolean)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<LabTestSample> getNLabTestSamples(Patient patient, LabTestSampleStatus status, int n, boolean firstNObjects,
	        boolean lastNObjects, boolean includeVoided) {
		// Disallow fetching more than 100 records per query
		if (n > MAX_FETCH_LIMIT) {
			n = MAX_FETCH_LIMIT;
		}
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LabTestSample.class);
		List<LabTestSample> firstN = null;
		List<LabTestSample> lastN = null;
		criteria.add(Restrictions.eq("labTest.order.patient", patient));
		if (status != null) {
			criteria.add(Restrictions.eq("status", status));
		}
		if (!includeVoided) {
			criteria.add(Restrictions.eq("voided", false));
		}
		if (firstNObjects) {
			criteria.addOrder(Order.asc("dateCreated"));
			firstN = criteria.list();
		}
		if (lastNObjects) {
			criteria.addOrder(Order.desc("dateCreated"));
			lastN = criteria.list();
		}
		criteria.setMaxResults(n);
		List<LabTestSample> list = new ArrayList<>();
		if (firstN != null) {
			list.addAll(firstN);
		}
		if (lastN != null) {
			list.addAll(lastN);
		}
		return list;
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#purgeLabTest(org.openmrs.module.commonlabtest.LabTest)
	 */
	@Override
	public void purgeLabTest(LabTest labTest) {
		sessionFactory.getCurrentSession().delete(labTest);
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#purgeLabTestAttribute(org.openmrs.module.commonlabtest.LabTestAttribute)
	 */
	@Override
	public void purgeLabTestAttribute(LabTestAttribute labTestAttribute) {
		sessionFactory.getCurrentSession().delete(labTestAttribute);
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#purgeLabTestAttributeType(org.openmrs.module.commonlabtest.LabTestAttributeType)
	 */
	@Override
	public void purgeLabTestAttributeType(LabTestAttributeType labTestAttributeType) {
		sessionFactory.getCurrentSession().delete(labTestAttributeType);
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#purgeLabTestSample(org.openmrs.module.commonlabtest.LabTestSample)
	 */
	@Override
	public void purgeLabTestSample(LabTestSample labTestSample) {
		sessionFactory.getCurrentSession().delete(labTestSample);
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#purgeLabTestType(org.openmrs.module.commonlabtest.LabTestType)
	 */
	@Override
	public void purgeLabTestType(LabTestType labTestType) {
		sessionFactory.getCurrentSession().delete(labTestType);
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#saveLabTest(org.openmrs.module.commonlabtest.LabTest)
	 */
	@Override
	public LabTest saveLabTest(LabTest labTest) {
		sessionFactory.getCurrentSession().save(labTest);
		return labTest;
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#saveLabTestAttribute(org.openmrs.module.commonlabtest.LabTestAttribute)
	 */
	@Override
	public LabTestAttribute saveLabTestAttribute(LabTestAttribute labTestAttribute) {
		sessionFactory.getCurrentSession().save(labTestAttribute);
		return labTestAttribute;
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#saveLabTestAttributeType(org.openmrs.module.commonlabtest.LabTestAttributeType)
	 */
	@Override
	public LabTestAttributeType saveLabTestAttributeType(LabTestAttributeType labTestAttributeType) {
		sessionFactory.getCurrentSession().save(labTestAttributeType);
		return labTestAttributeType;
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#saveLabTestSample(org.openmrs.module.commonlabtest.LabTestSample)
	 */
	@Override
	public LabTestSample saveLabTestSample(LabTestSample labTestSample) {
		sessionFactory.getCurrentSession().save(labTestSample);
		return labTestSample;
	}
	
	/**
	 * @see org.openmrs.module.commonlabtest.api.dao.CommonLabTestDao#saveLabTestType(org.openmrs.module.commonlabtest.LabTestType)
	 */
	@Override
	public LabTestType saveLabTestType(LabTestType labTestType) {
		sessionFactory.getCurrentSession().save(labTestType);
		return labTestType;
	}
}