<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<link type="text/css" rel="stylesheet"
	href="/openmrs/moduleResources/commonlabtest/css/commonlabtest.css" />
<link
	href="/openmrs/moduleResources/commonlabtest/font-awesome/css/font-awesome.min.css"
	rel="stylesheet" />
<link
	href="/openmrs/moduleResources/commonlabtest/css/bootstrap.min.css"
	rel="stylesheet" />

<style>

body {
	font-size: 12px;
}

input[type=submit] {
	background-color: #1aac9b;
	color: white;
	padding: 12px 20px;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	
}
#saveUpdateButton {
    text-align: center;
}
fieldset.scheduler-border {
    border: 1px groove #ddd !important;
    padding: 0 1.4em 1.4em 1.4em !important;
    margin: 0 0 1.5em 0 !important;
    -webkit-box-shadow:  0px 0px 0px 0px #1aac9b;
            box-shadow:  0px 0px 0px 0px #1aac9b;
}

legend.scheduler-border {
        font-size: 1.2em !important;
        font-weight: bold !important;
        text-align: left !important;
        width:auto;
        padding:0 10px;
        border-bottom:none;
    }
.row{
 margin-bottom:15px;
 
 }
</style>
<body>

<div class="container">
	<c:set var="testOrder" scope="session" value="${labTest}" />
    <fieldset  class="scheduler-border">
		<c:if test="${empty labTest.labReferenceNumber}">
			<legend  class="scheduler-border"><spring:message code="commonlabtest.order.add" /></legend>
		</c:if>
		<c:if test="${not empty labTest.labReferenceNumber}">
			<legend  class="scheduler-border"><spring:message code="commonlabtest.order.edit" /></legend>
		</c:if>
		<form:form commandName="labTest" id="form">
		  	<form:input  path="order.patient" hidden="true" value="${patientId}"></form:input>
			<form:input  path="order.concept.conceptId" hidden="true"  id="conceptId"></form:input>	
			<form:input  path="order.orderer.providerId" hidden="true" value="${provider.providerId}"></form:input>	
			<form:input  path="order.orderType.orderTypeId" hidden="true" value="3"></form:input>	
		
		    <div class="row" >
				   <div class="col-md-4">
				        <form:label  class="control-label" path="order.encounter"><spring:message code="general.encounter" /><span class="required">*</span></form:label>
				   </div>
				   <div class="col-md-6">
				  
				   		<form:select class="form-control" path="order.encounter" id="encounter" >
								<form:options  />
								 <c:if test="${not empty encounters}">
										<c:forEach var= "encounter" items="${encounters}">
											<form:option item ="${encounter}" value="${encounter}">${encounter.getEncounterType().getName()}</form:option>
										</c:forEach>
					 			</c:if>
						</form:select>
	
				   </div>
			 </div>
			<!-- Test Type -->
			 <div class="row" >
			   <div class="col-md-4">
			        <form:label  class="control-label" path="labTestType.labTestTypeId"><spring:message code="general.testType" /><span class="required">*</span></form:label>
			   </div>
			   <div class="col-md-6">
			   		<form:select class="form-control" path="labTestType.labTestTypeId" id="testType" >
								<form:options  />
								 <c:if test="${not empty testTypes}">
									<c:forEach var= "testType" items="${testTypes}">
										<form:option item ="${testType.labTestTypeId}" value="${testType.labTestTypeId}">${testType.getName()}</form:option>
									</c:forEach>
								</c:if>
					</form:select>
			   </div>
			 </div>
			 <!-- Lab Reference Number -->
			 <div class="row">
			   <div class="col-md-4">
			   		<form:label  class="control-label" path="labReferenceNumber"><spring:message code="commonlabtest.order.labReferenceNo" /><span class="required">*</span></form:label>
			   </div>
			   <div class="col-md-6">
			   		<form:input class="form-control" path="labReferenceNumber" id="labReferenceNumber"  name="labReferenceNumber" required="required"></form:input>
			   </div>
			 </div>
			  <!-- Care Setting-->
			 <div class="row">
			   <div class="col-md-4">
			   		<form:label  class="control-label" path="order.CareSetting.careSettingId"><spring:message code="general.careSetting" /></form:label>
			   </div>
			   <div class="col-md-6">
			   		<form:radiobutton path="order.CareSetting.careSettingId" value="1"  checked="checked"/>OutPatient 
			   		<span style="margin-right: 25px"></span>
					<form:radiobutton path="order.CareSetting.careSettingId" value="2"/>InPatient 	
<!-- 			   		<form:input class="form-control" path="order.CareSetting" id="careSetting"  name="careSetting"></form:input>
 -->			   </div>
			 </div>
			   <!-- Date Scheduled-->
			 <div class="row">
			   <div class="col-md-4">
			   		<form:label  class="control-label" path="order.scheduledDate"><spring:message code="general.scheduledDate" /></form:label>
			   </div>
			   <div class="col-md-6">
			     	     <openmrs_tag:dateField formFieldName="startDateSet" startValue=""/>
<!-- 			   		<form:input class="form-control" path="order.scheduledDate" id="scheduledDate"  name="scheduledDate"></form:input>
 -->					
			   </div>
			 </div>
		    <!-- Save -->
			 <div class="row">
			   <div class="col-md-4">
					<input type="submit" value="Save Test Order"></input>
			   </div>
			 </div>		 
		</form:form>

    </fieldset>
	<br>
	<c:if test="${not empty testOrder.labReferenceNumber}">
	
		 <fieldset  class="scheduler-border">
      	   <legend  class="scheduler-border"><spring:message code="commonlabtest.order.void" /></legend>
					<form method="post" action="${pageContext.request.contextPath}/module/commonlabtest/retirelabtestorder.form" >
						 <!-- UUID -->
						 <div class="row">
						   <div class="col-md-2">
								<input value="" hidden="true"  id="uuid" name="uuid"></input>
								<label  class="control-label" path="voidReason"><spring:message code="general.reason" /><span class="required">*</span></label>
						   </div>
						   <div class="col-md-6">
						   		<input class="form-control" value="" id="voidReason" name="retireReason" required="required">
						   </div>
						 </div>
						 <!-- Retire -->
						 <div class="row">
						   <div class="col-md-2" >
						 		 <input type="submit" value="Void Test Order"></input>
						   </div>
						 </div>
				</form>
        </fieldset>
	</c:if>
 </div>

</body>

<!--JAVA SCRIPT  -->
<script
	src="${pageContext.request.contextPath}/moduleResources/commonlabtest/bootstrap/js/jquery-3.3.1.min.js"></script>
<script
	src="${pageContext.request.contextPath}/moduleResources/commonlabtest/bootstrap/js/popper.min.js"></script>
<script
	src="${pageContext.request.contextPath}/moduleResources/commonlabtest/bootstrap/js/bootstrap.min.js"></script>
<script
	src="${pageContext.request.contextPath}/moduleResources/commonlabtest/js/jquery-ui.min.js"></script>
<script
	src="${pageContext.request.contextPath}/moduleResources/commonlabtest/js/jquery.dataTables.min.js"></script>
<script
	src="${pageContext.request.contextPath}/moduleResources/commonlabtest/js/dataTables.bootstrap4.min.js"></script>


<script type="text/javascript">
	$(document).ready(function () {
		 $('#testType').change(function(){
			  $('#conceptId').val(document.getElementById("testType").value);
		 });
		
	});
</script>

