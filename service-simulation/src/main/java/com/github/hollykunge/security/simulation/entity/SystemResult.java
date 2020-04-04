package com.github.hollykunge.security.simulation.entity;

import com.github.hollykunge.security.simulation.pojo.ActiveDataTypes;
import com.github.hollykunge.security.simulation.pojo.ActiveModels;
import com.github.hollykunge.security.simulation.pojo.FlowData;
import com.github.hollykunge.security.simulation.pojo.InterfaceList;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.List;

/**
 * @author jihang
 */

@Data
@Document(collection = "SIMU_SYSTEM_RESULT")
public class SystemResult {

    @Id
    @Field("SYSTEM_ID")
    private String systemId;

    @Field("FLOW_DATA")
    private FlowData flowData;

    @Field("ACTIVE_MODELS")
    private List<ActiveModels> activeModels;

    @Field("ACTIVE_MODEL_TYPES")
    private List<ActiveDataTypes> activeDataTypes;

    @Field("INTERFACE_LIST")
    private List<InterfaceList> interfaceList;

}
