package org.openmrs.module.commonlabtest.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.CareSetting;
import org.openmrs.CareSetting.CareSettingType;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Order;
import org.openmrs.Order.Action;
import org.openmrs.api.context.Context;
import org.openmrs.module.commonlabtest.LabTest;
import org.openmrs.module.commonlabtest.LabTestType;
import org.openmrs.module.commonlabtest.LabTestType.LabTestGroup;
import org.openmrs.module.commonlabtest.api.CommonLabTestService;
import org.openmrs.web.WebConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Controller
public class LabTestRequestController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	private final String SUCCESS_ADD_FORM_VIEW = "/module/commonlabtest/addLabTestRequest";
	
	CommonLabTestService commonLabTestService;
	
	@Autowired
	public LabTestRequestController(CommonLabTestService commonLabTestService) {
		this.commonLabTestService = commonLabTestService;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/module/commonlabtest/addLabTestRequest.form")
	public String showForm(HttpServletRequest request, @RequestParam(required = false) String error,
	        @RequestParam(required = false) Integer patientId, ModelMap model) {
		
		JsonArray testParentArray = new JsonArray();
		for (LabTestGroup labTestGroup : LabTestGroup.values()) {
			JsonObject labTestGroupObj = new JsonObject();
			JsonArray jsonChildArray = new JsonArray();
			if (labTestGroup.equals(LabTestGroup.OTHER)) {
				continue;
			}
			List<LabTestType> labTestTypeList = commonLabTestService.getLabTestTypes(null, null, labTestGroup, null, null,
			    Boolean.FALSE);
			if (labTestTypeList.equals("") || labTestTypeList.isEmpty()) {
				continue; //skip the current iteration.
			}
			labTestGroupObj.addProperty("testGroup", labTestGroup.name());
			for (LabTestType labTestType : labTestTypeList) {
				JsonObject labTestTyeChild = new JsonObject();
				labTestTyeChild.addProperty("testTypeId", labTestType.getId());
				labTestTyeChild.addProperty("testTypeName", labTestType.getName());
				jsonChildArray.add(labTestTyeChild);
			}
			labTestGroupObj.add("testType", jsonChildArray);
			testParentArray.add(labTestGroupObj);
		}
		List<Encounter> encounterList = Context.getEncounterService().getEncountersByPatientId(patientId);
		if (encounterList.size() > 0) {
			Collections.sort(encounterList, new Comparator<Encounter>() {
				
				@Override
				public int compare(Encounter o1, Encounter o2) {
					return o2.getEncounterDatetime().compareTo(o1.getEncounterDatetime());
				}
			});
		}
		
		if (encounterList.size() > 10) {
			model.addAttribute("encounters", encounterList.subList(0, encounterList.size() - 1));
		} else {
			model.addAttribute("encounters", encounterList);
		}
		model.addAttribute("labTestTypes", testParentArray);
		model.addAttribute("patientId", patientId);
		
		return SUCCESS_ADD_FORM_VIEW;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/module/commonlabtest/addLabTestRequest.form")
	@ResponseBody
	public boolean onSubmit(ModelMap model, HttpSession httpSession, HttpServletRequest request, @RequestBody String json,
	        @RequestParam(required = false) Integer patientId) {
		
		String status = "";
		boolean boolStatus = Boolean.TRUE;
		try {
			System.out.println(json);
			JsonArray arry = (JsonArray) new JsonParser().parse(json);
			List<LabTest> labTestArray = new ArrayList<LabTest>();
			for (int i = 0; i < arry.size(); i++) {
				LabTest labTest = new LabTest();
				JsonObject jsonObject = arry.get(i).getAsJsonObject();
				Order order = new Order();
				order.setCareSetting(Context.getOrderService().getCareSetting(1));
				Encounter encounter = Context.getEncounterService().getEncounter(jsonObject.get("encounterId").getAsInt());
				order.setEncounter(encounter);
				order.setAction(Action.NEW);
				order.setOrderer(Context.getProviderService()
				        .getProvidersByPerson(Context.getAuthenticatedUser().getPerson(), false).iterator().next());
				order.setOrderType(Context.getOrderService().getOrderType(3));
				order.setDateActivated(new Date());
				order.setPatient(Context.getPatientService().getPatient(patientId));
				Concept concept = Context.getConceptService().getConcept(jsonObject.get("testTypeId").getAsInt());
				order.setConcept(concept);
				labTest.setOrder(order);
				labTest.setLabInstructions(jsonObject.get("labInstructions").getAsString());
				labTest.setLabReferenceNumber(jsonObject.get("labReferenceNumber").getAsString());
				LabTestType labTestType = commonLabTestService.getLabTestType(jsonObject.get("testTypeId").getAsInt());
				labTest.setLabTestType(labTestType);
				labTestArray.add(labTest);
			}
			for (LabTest labTest : labTestArray) {
				commonLabTestService.saveLabTest(labTest);
			}
			StringBuilder sb = new StringBuilder();
			sb.append("Lab Test Order with Uuid :");
			sb.append(" is  saved!");
			status = sb.toString();
		}
		catch (Exception e) {
			status = "could not save Lab Test Request";
			e.printStackTrace();
			model.addAttribute("error", status);
			boolStatus = Boolean.FALSE;
		}
		if (boolStatus) {
			request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Test Request saved successfully");
		}
		//model.addAttribute("status", status);
		return boolStatus; //"redirect:../../patientDashboard.form?patientId=" + patientId;
	}
	
}