<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include
	file="/WEB-INF/view/module/commonlabtest/include/localHeader.jsp"%>
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

<!--Note: 
 All the commented code will be remove in the production..
  -->
<body>
 <div class="container">
	<c:set var="testType" scope="session" value="${labTestType}" />
    <fieldset  class="scheduler-border">
		<c:if test="${empty testType.referenceConcept.conceptId}">
			<legend  class="scheduler-border"><spring:message code="commonlabtest.labtesttype.add" /></legend>
		</c:if>
		<c:if test="${not empty testType.referenceConcept.conceptId}">
			<legend  class="scheduler-border"><spring:message code="commonlabtest.labtesttype.edit" /></legend>
		</c:if>
		<form:form commandName="labTestType" id="testTypeForm" onsubmit="return validate()">
			<!-- Concept Reference -->
			 <div class="row" >
			   <div class="col-md-2">
			   		<form:input path="labTestTypeId"  hidden="true" id="labTestTypeId"></form:input>
			        <form:label  class="control-label" path="referenceConcept"><spring:message code="general.referenceConcept" /><span class="text-danger font-weight-bold">*</span></form:label>
			   </div>
			   <div class="col-md-6">
			   		<form:input id="conceptSuggestBox" path="referenceConcept" class="form-control" list="conceptOptions"  placeholder="Search Concept..." ></form:input>
					<datalist class="lowercase" id="conceptOptions"></datalist>
					<span id="referenceconcept" class="text-danger "> </span>
			   </div>
			 </div>
			 <!-- Test Name -->
			 <div class="row">
			   <div class="col-md-2">
			   		<form:label  class="control-label" path="name"><spring:message code="general.testName" /><span class="text-danger font-weight-bold">*</span></form:label>
			   </div>
			   <div class="col-md-6">
			   		<form:input class="form-control" path="name" id="name"  name="name"></form:input>
					<span id="testname" class="text-danger"> </span>
				 </div>
			 </div>
			  <!-- Short Name -->
			 <div class="row">
			   <div class="col-md-2">
					<form:label  class="control-label" path="shortName"><spring:message code="general.shortName" /></form:label>
			   </div>
			   <div class="col-md-6">
			   		<form:input  class="form-control"  path="shortName" id="short_name" ></form:input>
			   </div>
			 </div>
			  <!-- Description -->
			 <div class="row">
			   <div class="col-md-2">
					<form:label class="control-label" path="description"><spring:message code="general.description" /></form:label>
			   </div>
			   <div class="col-md-6">
			   		<form:textarea class="form-control" path="description" id="description" rows="5"></form:textarea>
			   </div>
			 </div>
			  <!-- Test Group -->
			 <div class="row">
			   <div class="col-md-2">
					<form:label  class="control-label" path="testGroup"><spring:message code="general.testGroup" /></form:label>
			   </div>
			   <div class="col-md-6">
			   		<form:select class="form-control" path="testGroup" id="testGroup" >
								<form:options items="${LabTestGroup}" />
								<c:forEach items="${LabTestGroup}">
									<option value="${LabTestGroup}">${LabTestGroup}</option>
								</c:forEach>
					</form:select>
			   </div>
			 </div>
			 <!-- Specimen -->
			 <div class="row">
			   <div class="col-md-2">
					<form:label  class="form-check-label"  path="requiresSpecimen"><spring:message code="general.requiresSpecimen" /></form:label>
			   </div>
			   <div class="col-md-6">
			   		<span style="margin-right: 25px"></span>
			   		<form:radiobutton class="form-check-input" path="requiresSpecimen" value="true" />Yes 
			   		<span style="margin-right: 25px"></span>
					<form:radiobutton class="form-check-input"  path="requiresSpecimen" value="false" />No
			   </div>
			 </div>
			<c:if test="${not empty testType.referenceConcept.conceptId}">
				 <!-- Date Create -->
				 <div class="row">
				   <div class="col-md-2">
						<form:label  class="control-label" path="creator"><spring:message code="general.createdBy" /></form:label>
				   </div>
				   <div class="col-md-6">
				   		<c:out value="${testType.creator.personName}" /> - <c:out value="${testType.dateCreated}" />
				   </div>
				 </div>
				  <!-- UUID -->
				 <div class="row">
				   <div class="col-md-2">
						<font color="#D0D0D0"><sub><spring:message code="general.uuid" /></sub></font>
				   </div>
				   <div class="col-md-6">
				   		<font color="#D0D0D0"><sub><c:out value="${testType.uuid}" /></sub></font>
				   </div>
				 </div>
		    </c:if>
		    <!-- Save -->
			 <div class="row">
			   <div class="col-md-2">
					<input type="submit" value="Save Test Type"  ></input>
			   </div>
			 </div>		 
			
<%-- 			<table>
				 <div class="form-group">
					<tr> 
					     <form:input path="labTestTypeId"  hidden="true" id="labTestTypeId"></form:input>			
					     <td><form:label  class="control-label" path="referenceConcept"><spring:message code="general.referenceConcept" /></form:label></td>
					     <div class="col-md-6">
						    <td><form:input id="conceptSuggestBox" path="referenceConcept" class="form-control" list="conceptOptions" placeholder="Search Concept..." required="required" ></form:input>
								<datalist class="lowercase" id="conceptOptions"></datalist>
						    </td>
						 </div>   
						
						<td><input value="${testType.referenceConcept.conceptId}"  id="reference_concept"></input></td>
						<td><form:input path="referenceConcept"  hidden="true"  id="referenceConcept"></form:input></td>
					</tr>
				 </div>	
				 <div class="form-group">
					<tr>
						<td><form:label  class="control-label" path="name"><spring:message code="general.testName" /></form:label></td>
						<td><form:input class="form-control" path="name" id="name"  ></form:input></td>
					</tr>
				</div>
				 <div class="form-group">
					<tr>
						<td><form:label  class="control-label" path="shortName"><spring:message code="general.shortName" /></form:label></td>
						<td><form:input  class="form-control"  path="shortName" id="short_name" ></form:input></td>
					</tr>
				</div>
				<div class="form-group">
					<tr>
						<td><form:label class="control-label" path="description"><spring:message code="general.description" /></form:label></td>
						<td><form:textarea class="form-control" path="description" id="description" rows="5"></form:textarea></td>
					</tr>
				</div>
				<div class="form-group">
					<tr>
						<td><form:label  class="control-label" path="testGroup"><spring:message code="general.testGroup" /></form:label></td>
						<td><form:select class="form-control" path="testGroup" id="testGroup" required="required">
								<form:options items="${LabTestGroup}" />
								<c:forEach items="${LabTestGroup}">
									<option value="${LabTestGroup}">${LabTestGroup}</option>
								</c:forEach>
							</form:select></td>
					</tr>
				</div>
				<div class="class="form-check"">
					<tr>
						<td><form:label  class="form-check-label"  path="requiresSpecimen"><spring:message code="general.requiresSpecimen" /></form:label></td>
						<td><span style="margin-right: 25px"></span><form:radiobutton class="form-check-input" path="requiresSpecimen" value="true" />Yes <span style="margin-right: 25px"></span>
							<form:radiobutton class="form-check-input"  path="requiresSpecimen" value="false" />No</td>
					</tr>
				</div>

				<c:if test="${not empty testType.shortName}">

					<tr>
						<td><form:label  class="control-label" path="creator"><spring:message code="general.createdBy" /></form:label></td>
						<td><c:out value="${testType.creator.personName}" /> - <c:out
								value="${testType.dateCreated}" /></td>
					</tr>
					<tr>
						<td><font color="#D0D0D0"><sub><spring:message
										code="general.uuid" /></sub></font></td>
						<td><font color="#D0D0D0"><sub><c:out
										value="${testType.uuid}" /></sub></font></td>
					</tr>
				</c:if>

				<tr>
					<td>
						<div id="saveUpdateButton" style="margin-top: 15px">
							<input type="submit" value="Save Test Type"></input>
						</div>
					</td>
				</tr>

			</table> --%>

		</form:form>

    </fieldset>
	<br>
	<c:if test="${not empty testType.referenceConcept.conceptId}">
	
		 <fieldset  class="scheduler-border">
      	   <legend  class="scheduler-border"><spring:message code="general.test.retire" /></legend>
					<form method="post" action="${pageContext.request.contextPath}/module/commonlabtest/retirelabtesttype.form" onsubmit="return retireValidate()">
						 <!-- UUID -->
						 <div class="row">
						   <div class="col-md-2">
								<input value="${labTestType.uuid}" hidden="true"  id="uuid" name="uuid"></input>
								<label  class="control-label" path="retireReason"><spring:message code="general.retireReason" /><span class="required">*</span></label>
						   </div>
						   <div class="col-md-6">
						   		<input class="form-control" value="${labTestType.retireReason}" id="retireReason" name="retireReason" >
						 		 <span id="retirereason" class="text-danger "> </span>
						 
						   </div>
						 </div>
						 <!-- Retire -->
						 <div class="row">
						   <div class="col-md-2" >
						 		 <input type="submit" value="Retire Test Type"></input>
						   </div>
						 </div>
				</form>
        </fieldset>
	</c:if>
	<br>
    <c:if test="${not empty testType.referenceConcept.conceptId}">
		 <fieldset  class="scheduler-border">
      	   <legend  class="scheduler-border"><spring:message code="general.foreverDelete" /></legend>
			<form  method="post" action ="${pageContext.request.contextPath}/module/commonlabtest/deletelabtesttype.form" onsubmit="return confirmDelete()">
				
				 <!-- Delete -->
				 <div class="row">
				   <div class="col-md-2" >
				  		 <input value="${labTestType.uuid}" hidden="true"  id="uuid" name="uuid"></input>
				 		 <input type="submit" value="<spring:message code="general.foreverDelete" />" />
				   </div>
				 </div>
				
				<%-- <table>				
				<tr>
					<td>
						<input value="${labTestType.uuid}" hidden="true"  id="uuid" name="uuid"></input>
						<div id="delete" style="margin-top: 15px">
							<input type="submit" value="<spring:message code="general.foreverDelete" />" />
						</div>
					</td>
				</tr>
				</table> --%>
			</form>
		 </fieldset>
	</c:if>
 
 </div>
	
</body>



<script
	src="${pageContext.request.contextPath}/moduleResources/commonlabtest/bootstrap/js/jquery-3.3.1.min.js"></script>
<script
	src="${pageContext.request.contextPath}/moduleResources/commonlabtest/bootstrap/js/popper.min.js"></script>
<script
	src="${pageContext.request.contextPath}/moduleResources/commonlabtest/bootstrap/js/bootstrap.min.js"></script>
<script
	src="${pageContext.request.contextPath}/moduleResources/commonlabtest/js/jquery-ui.min.js"></script>



<script>
	var local_source;

	jQuery(document).ready(function() {
		
		
		$('#name').on('input', function() {
			//alert("on is work !");
			var input=$(this);
			var is_name=input.val();
			if(is_name){input.removeClass("invalid").addClass("valid");}
			else{input.removeClass("valid").addClass("invalid");}
		});
		
		local_source = getConcepts();
	/* 	 console.log(getConcepts());
	
	        <c:if test="${not empty concepts}">
		        <c:forEach var="concept" items="${concepts}" varStatus="status">
		       /*    var conceptDescriptionContainingNewLine = "'${concept.description}'";
		          var conceptDescription =conceptDescriptionContainingNewLine.replace(/(\r\n|\n|\r)/gm, "");
		          alert(conceptDescription); */
		      /*   local_source.push({id:"${concept.id}",value: '${concept.name}' ,description: '${concept.description}' ,shortName : '${concept.shortName}'});
		        </c:forEach>
	        </c:if>      */
	        
	        var datalist = document.getElementById("conceptOptions");
			var dataListLength = datalist.options.length;
			if(dataListLength > 0 ) {
				jQuery("#conceptOptions option").remove();
			}
			
			if(local_source.length > 0) {
				conceptObject = {};
			
				jQuery(local_source).each(function() {
					var conceptName = toTitleCase(this.name.toLowerCase());
			        conceptOption = "<option value=\"" + this.id + "\">" + conceptName + "</option>";
					jQuery('#conceptOptions').append(conceptOption);
		            conceptId = this.id; 
		           	conceptObject[conceptId] = conceptName;
				});
			}
			
			jQuery('#conceptSuggestBox').on('input', function(){
				//refresh();
				var val = this.value;
				if(jQuery('#conceptOptions option').filter(function(){
			        return this.value === val;        
			    }).length) {
					var datalist = document.getElementById("conceptOptions");
					var options = datalist.options;
				    var conceptId = jQuery(this).val();
				    var concepts = local_source.find(o => o.id == conceptId);
				   jQuery("#name").val(concepts.name.toLowerCase());
				   jQuery("#short_name").val(concepts.shortName.toLowerCase());
				   jQuery("#description").val(concepts.description.toLowerCase()); 
				}
			});
			
		/* 	jQuery('#drugSetList').change(function() {
				alert("change is called");
			}); */
	        
	        
	        
	});
	
	function toTitleCase(str) {
	    return str.replace(/(?:^|\s)\w/g, function(match) {
	        return match.toUpperCase();
	    });
	}
	
	 //get all concepts
	 function getConcepts(){
	    	return JSON.parse(JSON.stringify(${conceptsJson}));
	    }
	
	/* /*autocomplete ...  */
	/* $(function() {
		 $("#reference_concept").autocomplete({
			 source : function(request, response) {
				response($.map(local_source, function(item) {
					console.log(item);
					return {
						id : item.id,
						value : item.name,
						/* shortName: item.shortName,
						description :item.description  
					}
				}))
			},
	   	select : function(event, ui) {
				$(this).val(ui.item.value)
				//document.getElementById('referenceConcept').value = '';
				/* $("#referenceConcept").val(ui.item.id);
				$("#short_name").val(ui.item.shortName);
				$("#name").val(ui.item.value);
				$("#description").val(ui.item.description); */
				/*  event.preventDefault();
			},
			minLength : 3,
			autoFocus : false
		});	     
	});   */
	
	/*  */
	function confirmDelete() {
		//onsubmit="return confirmDelete()"
		if (confirm("Are you sure you want to Delete this Test Type? It will be permanently removed from the system.")) {
			return true;
		} else {
			return false;
		}
	}
  
	/*Form Validation  */
   
	function validate(){
		var testName = document.getElementById('name').value;
		var referenceConcept = document.getElementById('conceptSuggestBox').value;
		console.log(referenceConcept);
		var isValidate =true; 
		/*var confirmpass = document.getElementById('conpass').value;
		var mobileNumber = document.getElementById('mobileNumber').value;
		var emails = document.getElementById('emails').value; */
		if(referenceConcept == ""){
			document.getElementById('referenceconcept').innerHTML ="Please fill the Reference Concept field";
			isValidate = false;
		}
		else if(isNaN(referenceConcept)){
			document.getElementById('referenceconcept').innerHTML ="Only the autosearch reference concept Id is accepted";
			isValidate = false;
		}	
		
		if(testName == ""){
				document.getElementById('testname').innerHTML ="Please fill the Test Name field";
				isValidate = false;
			}
		else if(!isNaN(testName)){
				document.getElementById('testname').innerHTML ="Only characters are allowed";
				isValidate = false;
			}
		
		//console.log(form_data); 
	
		return isValidate;
	}
	
	//Retire Validate 
	
	function retireValidate(){
		var retireReason = document.getElementById('retireReason').value;
		var isValidate= true;
		if(retireReason == ""){
			document.getElementById('retirereason').innerHTML ="Please fill the retire reason field";
			isValidate = false;
		}
		else if(!isNaN(retireReason)){
			document.getElementById('retirereason').innerHTML ="Only characters are allowed";
			isValidate = false;
		}
	
		return isValidate;
	}
	
	
	
</script>

<%@ include file="/WEB-INF/template/footer.jsp"%>


