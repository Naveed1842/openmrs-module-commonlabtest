/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.commonlabtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.Order;
import org.openmrs.Order.Action;
import org.openmrs.Order.Urgency;
import org.openmrs.api.context.Context;
import org.openmrs.module.commonlabtest.LabTestType.LabTestGroup;
import org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * It is an integration test (extends BaseModuleContextSensitiveTest), which verifies DAO methods
 * against the in-memory H2 database. The database is initially loaded with data from
 * standardTestDataset.xml in openmrs-api. All test methods are executed in transactions, which are
 * rolled back by the end of each test method.
 */
public class CommonLabTestDaoTest extends CommonLabTestBase {
	
	@Autowired
	CommonLabTestDaoImpl dao;
	
	@Before
	public void runBeforeEachTest() throws Exception {
		super.initTestData();
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#getAllLabTestAttributeTypes(boolean)}
	 * .
	 */
	@Test
	public final void testGetAllLabTestAttributeTypes() {
		Context.clearSession();
		List<LabTestAttributeType> list = dao.getAllLabTestAttributeTypes(false);
		assertTrue(list.size() == activeLabTestAttributeTypes.size());
		assertThat(list, Matchers.not(Matchers.hasItems(cad4tbScore, xrayFilmPrinted)));
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#getAllLabTestTypes(boolean)}
	 * .
	 */
	@Test
	public final void testGetAllLabTestTypes() {
		Context.clearSession();
		List<LabTestType> list = dao.getAllLabTestTypes(false);
		assertThat(list, Matchers.hasItems(geneXpert, chestXRay));
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#getLabTestTypes(java.lang.String, java.lang.String, org.openmrs.module.commonlabtest.LabTestType.LabTestGroup, org.openmrs.Concept, boolean)}
	 * .
	 */
	@Test
	public final void testGetLabTestTypesByName() {
		Context.clearSession();
		List<LabTestType> list = dao.getLabTestTypes("GeneXpert Test", null, LabTestGroup.BACTERIOLOGY, null, false);
		assertThat(list, Matchers.hasItem(geneXpert));
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#getLabTest(org.openmrs.Encounter)}
	 * .
	 */
	@Test
	public final void testGetLabTestByOrder() {
		Context.clearSession();
		Order order = Context.getOrderService().getOrder(100);
		LabTest labTest = dao.getLabTest(order);
		assertEquals(labTest, harryGxp);
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#getLabTest(java.lang.Integer)}
	 * .
	 */
	@Test
	public final void testGetLabTestById() {
		Context.clearSession();
		LabTest labTest = dao.getLabTest(100);
		assertEquals(labTest, harryGxp);
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#getLabTestAttribute(java.lang.Integer)}
	 * .
	 */
	@Test
	public final void testGetLabTestAttribute() {
		Context.clearSession();
		LabTestAttribute labTestAttribute = dao.getLabTestAttribute(1);
		assertEquals(labTestAttribute, harryCartridgeId);
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#getLabTestAttributeByUuid(java.lang.String)}
	 * .
	 */
	@Test
	public final void testGetLabTestAttributeByUuid() {
		Context.clearSession();
		LabTestAttribute labTestAttribute = dao.getLabTestAttributeByUuid("2c9737d9-47c2-11e8-943c-40b034c3cfee");
		assertEquals(labTestAttribute, harryCartridgeId);
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#getLabTestAttributes(org.openmrs.module.commonlabtest.LabTestAttributeType, org.openmrs.module.commonlabtest.LabTest, org.openmrs.Patient, java.lang.String, java.util.Date, java.util.Date, boolean)}
	 * .
	 */
	@Test
	public final void testGetLabTestAttributesByType() {
		Context.clearSession();
		List<LabTestAttribute> list = dao.getLabTestAttributes(mtbResult, null, null, null, null, null, true);
		assertThat(list, Matchers.hasItem(harryMtbResult));
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#getLabTestAttributes(org.openmrs.module.commonlabtest.LabTestAttributeType, org.openmrs.module.commonlabtest.LabTest, org.openmrs.Patient, java.lang.String, java.util.Date, java.util.Date, boolean)}
	 * .
	 */
	@Test
	@Ignore
	public final void testGetLabTestAttributesByLabTest() {
		Context.clearSession();
		List<LabTestAttribute> list = dao.getLabTestAttributes(null, harryGxp, null, null, null, null, false);
		assertThat(list, Matchers.hasItem(harryCartridgeId));
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#getLabTestAttributes(org.openmrs.module.commonlabtest.LabTestAttributeType, org.openmrs.module.commonlabtest.LabTest, org.openmrs.Patient, java.lang.String, java.util.Date, java.util.Date, boolean)}
	 * .
	 */
	@Test
	@Ignore
	public final void testGetLabTestAttributesByPatient() {
		Context.clearSession();
		List<LabTestAttribute> list = dao.getLabTestAttributes(null, null, harry, null, null, null, false);
		assertThat(list,
		    Matchers.containsInAnyOrder(harryCartridgeId, harryMtbResult, harryRifResult, harryCxrResult));
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#getLabTestAttributeType(java.lang.Integer)}
	 * .
	 */
	@Test
	public final void testGetLabTestAttributeType() {
		// TODO
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#getLabTestAttributeTypeByUuid(java.lang.String)}
	 * .
	 */
	@Test
	public final void testGetLabTestAttributeTypeByUuid() {
		// TODO
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#getLabTestAttributeTypes(java.lang.String, java.lang.String, boolean)}
	 * .
	 */
	@Test
	public final void testGetLabTestAttributeTypes() {
		// TODO
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#getLabTestByUuid(java.lang.String)}
	 * .
	 */
	@Test
	public final void testGetLabTestByUuid() {
		// TODO
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#getLabTests(org.openmrs.module.commonlabtest.LabTestType, org.openmrs.Patient, java.lang.String, java.lang.String, org.openmrs.Concept, org.openmrs.Provider, java.util.Date, java.util.Date, boolean)}
	 * .
	 */
	@Test
	public final void testGetLabTests() {
		// TODO
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#getLabTestSample(java.lang.Integer)}
	 * .
	 */
	@Test
	public final void testGetLabTestSample() {
		// TODO
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#getLabTestSampleByUuid(java.lang.String)}
	 * .
	 */
	@Test
	public final void testGetLabTestSampleByUuid() {
		// TODO
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#getLabTestSamples(org.openmrs.module.commonlabtest.LabTest, boolean)}
	 * .
	 */
	@Test
	public final void testGetLabTestSamplesByLabTest() {
		// TODO
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#getLabTestSamples(org.openmrs.Patient, boolean)}
	 * .
	 */
	@Test
	public final void testGetLabTestSamplesByPatient() {
		// TODO
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#getLabTestSamples(org.openmrs.Provider, boolean)}
	 * .
	 */
	@Test
	public final void testGetLabTestSamplesByProvider() {
		// TODO
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#getLabTestType(java.lang.Integer)}
	 * .
	 */
	@Test
	public final void testGetLabTestType() {
		// TODO
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#getLabTestTypeByUuid(java.lang.String)}
	 * .
	 */
	@Test
	public final void testGetLabTestTypeByUuid() {
		// TODO
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#getNLabTests(org.openmrs.Patient, int, boolean, boolean, boolean)}
	 * .
	 */
	@Test
	public final void testGetNLabTests() {
		// TODO
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#getNLabTestSamples(org.openmrs.Patient, org.openmrs.module.commonlabtest.LabTestSample.LabTestSampleStatus, int, boolean, boolean, boolean)}
	 * .
	 */
	@Test
	public final void testGetNLabTestSamples() {
		// TODO
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#purgeLabTest(org.openmrs.module.commonlabtest.LabTest)}
	 * .
	 */
	@Test
	public final void testPurgeLabTest() {
		// TODO
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#purgeLabTestAttribute(org.openmrs.module.commonlabtest.LabTestAttribute)}
	 * .
	 */
	@Test
	public final void testPurgeLabTestAttribute() {
		// TODO
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#purgeLabTestAttributeType(org.openmrs.module.commonlabtest.LabTestAttributeType)}
	 * .
	 */
	@Test
	public final void testPurgeLabTestAttributeType() {
		// TODO
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#purgeLabTestSample(org.openmrs.module.commonlabtest.LabTestSample)}
	 * .
	 */
	@Test
	public final void testPurgeLabTestSample() {
		// TODO
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#purgeLabTestType(org.openmrs.module.commonlabtest.LabTestType)}
	 * .
	 */
	@Test
	public final void testPurgeLabTestType() {
		LabTestType testType = dao.getLabTestType(3);
		// Purge object
		dao.purgeLabTestType(testType);
		// Clear cache
		Context.flushSession();
		Context.clearSession();
		// Read again
		LabTestType exists = dao.getLabTestTypeByUuid(testType.getUuid());
		assertNull(exists);
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#saveLabTest(org.openmrs.module.commonlabtest.LabTest)}
	 * .
	 */
	@Test
	public final void testSaveLabTest() {
		// TODO
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#saveLabTestOrder(org.openmrs.Order)}
	 * .
	 */
	@Test
	public final void testSaveLabTestOrder_Create() {
		// Create CXR order for Hermione
		Order order = new Order();
		order.setOrderType(Context.getOrderService().getOrderType(3));
		order.setConcept(Context.getConceptService().getConcept(600));
		order.setOrderer(Context.getProviderService().getProvider(300));
		order.setEncounter(Context.getEncounterService().getEncounter(1000));
		order.setInstructions("PERFORM CXR");
		order.setDateActivated(new Date());
		order.setAction(Action.NEW);
		order.setOrderReasonNonCoded("Testing");
		order.setPatient(hermione);
		order.setUrgency(Urgency.ROUTINE);
		order.setCareSetting(Context.getOrderService().getCareSetting(1));
		Order savedOrder = dao.saveLabTestOrder(order);
		assertThat(savedOrder, Matchers.hasProperty("orderId", org.hamcrest.Matchers.notNullValue()));
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#saveLabTestOrder(org.openmrs.Order)}
	 * .
	 */
	@Test
	public final void testSaveLabTestOrder_Update() {
		Order order = Context.getOrderService().getOrder(1);
		Concept cxrTestConcept = Context.getConceptService().getConcept(800);
		order.setConcept(cxrTestConcept);
		Order savedOrder = dao.saveLabTestOrder(order);
		assertThat(savedOrder, Matchers.hasProperty("orderId", org.hamcrest.Matchers.is(1)));
		assertThat(savedOrder, Matchers.hasProperty("concept", org.hamcrest.Matchers.is(cxrTestConcept)));
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#saveLabTestAttribute(org.openmrs.module.commonlabtest.LabTestAttribute)}
	 * .
	 */
	@Test
	public final void testSaveLabTestAttribute() {
		// TODO
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#saveLabTestAttributeType(org.openmrs.module.commonlabtest.LabTestAttributeType)}
	 * .
	 */
	@Test
	public final void testSaveLabTestAttributeType() {
		// TODO
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#saveLabTestSample(org.openmrs.module.commonlabtest.LabTestSample)}
	 * .
	 */
	@Test
	public final void testSaveLabTestSample() {
		// TODO
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.commonlabtest.api.dao.impl.CommonLabTestDaoImpl#saveLabTestType(org.openmrs.module.commonlabtest.LabTestType)}
	 * .
	 */
	@Test
	public final void testSaveLabTestType() {
		LabTestType testType = new LabTestType();
		testType.setName("AFB Smear Microscopy");
		testType.setShortName("AFB");
		testType.setTestGroup(LabTestGroup.BACTERIOLOGY);
		testType.setRequiresSpecimen(Boolean.TRUE);
		Concept concept = Context.getConceptService().getConcept(500);
		testType.setReferenceConcept(concept);
		// Save object
		testType = dao.saveLabTestType(testType);
		// Clear cache
		//Context.flushSession();
		Context.clearSession();
		// Read again
		LabTestType savedTestType = dao.getLabTestType(testType.getId());
		assertThat(savedTestType, Matchers.hasProperty("uuid", org.hamcrest.Matchers.is(testType.getUuid())));
		assertThat(savedTestType, Matchers.hasProperty("creator", org.hamcrest.Matchers.is(testType.getCreator())));
	}
}
