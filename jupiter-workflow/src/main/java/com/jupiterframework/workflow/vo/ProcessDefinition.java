package com.jupiterframework.workflow.vo;

import org.activiti.engine.FormService;
import org.activiti.engine.RepositoryService;

import lombok.Data;


@Data
public class ProcessDefinition {

    /** unique identifier */
    private String id;

    /**
     * category name which is derived from the targetNamespace attribute in the
     * definitions element
     */
    private String category;

    /** label used for display purposes */
    private String name;

    /** unique name for all versions this process definitions */
    private String key;

    /** description of this process **/
    private String description;

    /** version of this process definition */
    int version;

    /**
     * name of {@link RepositoryService#getResourceAsStream(String, String) the
     * resource} of this process definition.
     */
    private String resourceName;

    /** The deployment in which this process definition is contained. */
    private String deploymentId;

    /** The resource name in the deployment of the diagram image (if any). */
    private String diagramResourceName;

    /**
     * Does this process definition has a
     * {@link FormService#getStartFormData(String) start form key}.
     */
    private boolean startFormKey;

    /**
     * Does this process definition has a graphical notation defined (such that
     * a diagram can be generated)?
     */
    private boolean graphicalNotationg;

    /** Returns true if the process definition is in suspended state. */
    private boolean suspended;

    /** The tenant identifier of this process definition */
    private String tenantId;

}
