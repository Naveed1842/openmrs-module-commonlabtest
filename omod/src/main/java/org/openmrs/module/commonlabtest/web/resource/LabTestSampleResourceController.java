package org.openmrs.module.commonlabtest.web.resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.commonlabtest.LabTestSample;
import org.openmrs.module.commonlabtest.api.CommonLabTestService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

@Resource(name = RestConstants.VERSION_1 + "/commonlab/labtestsample", supportedClass = LabTestSample.class, supportedOpenmrsVersions = {"2.0.*,2.1.*"})
public class LabTestSampleResourceController extends DataDelegatingCrudResource<LabTestSample> {

    /**
     * Logger for this class
     */
    protected final Log log = LogFactory.getLog(getClass());
	
	/*	@Autowired
		CommonLabTestService commonLabTestService;*/

    private CommonLabTestService commonLabTestService = Context.getService(CommonLabTestService.class);

    @Override
    public LabTestSample getByUniqueId(String s) {
        return commonLabTestService.getLabTestSampleByUuid(s);
    }

    @Override
    protected void delete(LabTestSample labTestSample, String s, RequestContext requestContext) throws ResponseException {
        commonLabTestService.voidLabTestSample(labTestSample, s);
    }

    @Override
    public LabTestSample newDelegate() {
        return new LabTestSample();
    }

    @Override
    public LabTestSample save(LabTestSample labTestSample) {
        return commonLabTestService.saveLabTestSample(labTestSample);
    }

    @Override
    public void purge(LabTestSample labTestSample, RequestContext requestContext) throws ResponseException {
        throw new ResourceDoesNotSupportOperationException();
    }

    @Override
    public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
        if (representation instanceof DefaultRepresentation) {
            DelegatingResourceDescription description = new DelegatingResourceDescription();
            description.addProperty("uuid");
            description.addProperty("labTestSampleId");
            description.addProperty("labTest");
            description.addProperty("specimenType");
            description.addProperty("specimenSite");
            description.addProperty("collectionDate");
            description.addProperty("collector");
            description.addProperty("quantity");
            description.addProperty("units");
            description.addProperty("expirable");
            description.addProperty("expiryDate");
            description.addProperty("processedDate");
            description.addProperty("status");
            description.addProperty("sampleIdentifier");
            description.addProperty("comments");

            description.addSelfLink();
            description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
            return description;
        } else if (representation instanceof FullRepresentation) {
            DelegatingResourceDescription description = new DelegatingResourceDescription();
            description.addProperty("uuid");
            description.addProperty("labTestSampleId");
            description.addProperty("labTest");
            description.addProperty("specimenType");
            description.addProperty("specimenSite");
            description.addProperty("collectionDate");
            description.addProperty("collector");
            description.addProperty("quantity");
            description.addProperty("units");
            description.addProperty("expirable");
            description.addProperty("expiryDate");
            description.addProperty("processedDate");
            description.addProperty("status");
            description.addProperty("sampleIdentifier");
            description.addProperty("comments");

            description.addProperty("creator");
            description.addProperty("dateCreated");

            description.addProperty("changedBy");
            description.addProperty("dateChanged");

            description.addProperty("voided");
            description.addProperty("dateVoided");
            description.addProperty("voidedBy");
            description.addProperty("voidReason");
            return description;
        }
        return null;
    }
}
