package com.github.hollykunge.security.simulation.biz;

import com.github.hollykunge.security.simulation.pojo.ActiveDataTypes;
import com.github.hollykunge.security.simulation.pojo.ActiveModels;
import com.github.hollykunge.security.simulation.pojo.Content;
import com.github.hollykunge.security.simulation.pojo.InterfaceList;
import com.github.hollykunge.security.simulation.vo.SystemInfoVo;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class AssembleResultBiz {

    static final String ACQUIRE_READY_STATE = "acquire_ready_state";
    static final String NODE_READY = "node_ready";
    static final String INITIAL_FEDERATE = "initial_federate";
    static final String ADVANCE_REQUEST = "advance_request";
    static final String ADVANCE_GRANT = "advance_grant";
    static final String SIMULATION_RUN = "simulation_run";
    static final String SIMULATION_END = "simulation_end";
    static final String ENGINE_NAME = "SIMUengine777";

    static final List<String> allTopics = new ArrayList<String>(Arrays.asList(
            ACQUIRE_READY_STATE, NODE_READY, INITIAL_FEDERATE,
            ADVANCE_REQUEST, ADVANCE_GRANT, SIMULATION_RUN,
            SIMULATION_END));

    static final List<String> engPubs = new ArrayList<String>(Arrays.asList(
            ACQUIRE_READY_STATE, INITIAL_FEDERATE,
            ADVANCE_GRANT, SIMULATION_RUN,
            SIMULATION_END));

    static final List<String> engSubs = new ArrayList<String>(Arrays.asList(
            ADVANCE_REQUEST, NODE_READY));

    // 如果不进行格式化的话，生成的xml文件将会是很长的一行
    public Format FormatXML() {
        Format format = Format.getCompactFormat();
        format.setEncoding("UTF-8");
        format.setIndent(" ");
        return format;
    }

    //生成整个文档
    public void generateDocument(
            org.jdom2.Document rootDocument, SystemInfoVo entity) {
        Element militaryScenarioElement = new Element("MilitaryScenario");
        rootDocument.addContent(militaryScenarioElement);

        //创建二级节点ScenarioInfo:name/Id/Node
        this.generateScenarioInfo(
                entity.getId(), entity.getSystemName(), entity.getModelName(),
                militaryScenarioElement);

        //创建二级节点TypeDefine/Type
        List<ActiveDataTypes> activeDataTypes =
                entity.getSystemResult().getActiveDataTypes();
        this.generateTypeDefine(activeDataTypes, militaryScenarioElement);

        //创建二级节点Topics
        Element topicsElement = new Element("Topics");
        militaryScenarioElement.addContent(topicsElement);

        //创建预留主题区
        this.generateStaticTopic(topicsElement);
        //创建用户主题区
        List<InterfaceList> interfaceLists =
                entity.getSystemResult().getInterfaceList();
        this.generateUserTopic(interfaceLists, topicsElement);

        //创建二级节点models
        Element modelsElement = new Element("Models");
        militaryScenarioElement.addContent(modelsElement);

        this.generateModels(
                entity.getSystemResult().getActiveModels(),
                entity.getSystemResult().getInterfaceList(),
                modelsElement);
    }

    // 1
    private void generateScenarioInfo(
            String id, String systemName, String modelName,
            Element militaryScenarioElement) {
        Element scenarioInfoElement = new Element("ScenarioInfo");
        militaryScenarioElement.addContent(scenarioInfoElement);

        Element nameElement = new Element("Name");
        nameElement.setText(systemName);
        scenarioInfoElement.addContent(nameElement);

        Element idElement = new Element("Id");
        idElement.setText(id);
        scenarioInfoElement.addContent(idElement);

        Element nodeElement = new Element("Node");
        nodeElement.setText(modelName);
        scenarioInfoElement.addContent(nodeElement);
    }

    // 2
    private void generateTypeDefine(
            List<ActiveDataTypes> activeDataTypes,
            Element militaryScenarioElement) {
        Element typeDefineElement = new Element("TypeDefine");
        militaryScenarioElement.addContent(typeDefineElement);

        for (ActiveDataTypes oneDataType : activeDataTypes) {
            generateTypeElement(oneDataType, typeDefineElement);
        }
    }

    // 2.1
    private void generateTypeElement(
            ActiveDataTypes oneDataType, Element typeDefineElement) {
        String name = oneDataType.getName();
        String description = oneDataType.getDescription();
        List<Content> content = oneDataType.getContent();

        Element typeElement = new Element("Type");
        setValue(typeElement, "name", name);
        setValue(typeElement, "description", description);
        typeDefineElement.addContent(typeElement);
        for (Content obj : content) {
            String typeName = obj.getName();
            String type = obj.getType();
            Element parameterElement = new Element("Parameter");
            parameterElement.setAttribute("name", typeName);
            parameterElement.setAttribute("type", type);
            typeElement.addContent(parameterElement);
        }
    }

    // 2.2
    private void setValue(Element element, String name, String value) {
        element.setAttribute(name, value == null ? "" : value);
    }

    // 3.1
    private void generateStaticTopic(Element topicsElement) {
        for (String topicString : allTopics) {
            Element topicElement = new Element("Topic");
            topicElement.setAttribute("name", topicString);
            topicsElement.addContent(topicElement);
        }
    }

    // 3.2
    private void generateUserTopic(
            List<InterfaceList> interfaceLists, Element topicsElement) {
        Map<String, String> topicMap = new HashMap<>();
        for (InterfaceList interfaceObj : interfaceLists) {
            String nameString = interfaceObj.getName();
            String typeString = interfaceObj.getType();
            if (!topicMap.containsKey(nameString)) {
                topicMap.put(nameString, typeString);
            }
        }
        for (String name : topicMap.keySet()) {
            Element topicElement = new Element("Topic");
            topicElement.setAttribute("name", name);
            topicElement.setAttribute("type", topicMap.get(name));
            topicsElement.addContent(topicElement);
        }
    }

    // 4.1
    private void generateModels(
            List<ActiveModels> activeModels, List<InterfaceList> interfaceLists,
            Element modelsElement) {
        Element modelElement = new Element("Model");
        modelElement.setAttribute("name", ENGINE_NAME);
        modelsElement.addContent(modelElement);

        Element publishElement = new Element("Publish");
        modelElement.addContent(publishElement);
        Element subscribeElement = new Element("Subscribe");
        modelElement.addContent(subscribeElement);

        this.generateStaticModel(publishElement, subscribeElement);

        this.generateUserPublish(interfaceLists, subscribeElement);

        this.generateUserModel(
                activeModels, interfaceLists,
                modelsElement);
    }

    // 4.2
    private void generateStaticModel(
            Element publishElement, Element subscribeElement) {
        addChildrenElement(publishElement, engPubs);
        addChildrenElement(subscribeElement, engSubs);
    }

    // 4.2.1
    private void addChildrenElement(Element Element, List<String> topics) {
        for (String topicName : topics) {
            Element topicElement = new Element("Topic");
            topicElement.setAttribute("topicName", topicName);
            Element.addContent(topicElement);
        }
    }

    // 4.3
    private void generateUserPublish(
            List<InterfaceList> interfaceLists, Element subscribeElement) {
        for (InterfaceList interfaceObj : interfaceLists) {
            String category = interfaceObj.getCategory();
            if (category.equals("output")) {
                Element topicElement = new Element("Topic");
                topicElement.setAttribute("topicName", interfaceObj.getName());
                subscribeElement.addContent(topicElement);
            }
        }
    }

    // 4.4
    private void generateUserModel(
            List<ActiveModels> activeModels, List<InterfaceList> interfaceLists,
            Element modelsElement) {
        for (ActiveModels oneModel : activeModels) {

            Element modelElement2 = new Element("Model");
            String modelNameString = oneModel.getModelName();
            modelElement2.setAttribute("name", modelNameString);
            modelsElement.addContent(modelElement2);

            Element publishElement2 = new Element("Publish");
            modelElement2.addContent(publishElement2);

            Element subscribeElement2 = new Element("Subscribe");
            modelElement2.addContent(subscribeElement2);

            generateStaticUserModel(publishElement2, subscribeElement2);
            generateUserPublishSubscribe(
                    interfaceLists,
                    modelNameString, publishElement2, subscribeElement2);
        }
    }

    // 4.4.1
    private void generateStaticUserModel(
            Element publishElement, Element subscribeElement) {
        addChildrenElement(publishElement, engSubs);
        addChildrenElement(subscribeElement, engPubs);
    }

    // 4.4.2
    private void generateUserPublishSubscribe(
            List<InterfaceList> interfaceList,
            String modelNameString,
            Element publishElement2, Element subscribeElement2) {

        for (InterfaceList interfaceObj : interfaceList) {
            String modelName = interfaceObj.getModelName();
            String name = interfaceObj.getName();
            String type = interfaceObj.getCategory();
            Element topicElement = new Element("Topic");
            topicElement.setAttribute("topicName", name);

            if (modelName.equals(modelNameString)) {
                if (type.equals("output")) {
                    publishElement2.addContent(topicElement);
                }
                if (type.equals("input")) {
                    subscribeElement2.addContent(topicElement);
                }
            }
        }
    }
}
