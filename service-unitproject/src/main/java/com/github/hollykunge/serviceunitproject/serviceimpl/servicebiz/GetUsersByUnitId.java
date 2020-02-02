
package com.github.hollykunge.serviceunitproject.serviceimpl.servicebiz;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="unitId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isContain" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "unitId",
    "isContain"
})
@XmlRootElement(name = "getUsersByUnitId")
public class GetUsersByUnitId {

    @XmlElementRef(name = "unitId", namespace = "http://impl.webservice.dev.casic.com", type = JAXBElement.class, required = false)
    protected JAXBElement<String> unitId;
    @XmlElementRef(name = "isContain", namespace = "http://impl.webservice.dev.casic.com", type = JAXBElement.class, required = false)
    protected JAXBElement<String> isContain;

    /**
     * 获取unitId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getUnitId() {
        return unitId;
    }

    /**
     * 设置unitId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setUnitId(JAXBElement<String> value) {
        this.unitId = value;
    }

    /**
     * 获取isContain属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getIsContain() {
        return isContain;
    }

    /**
     * 设置isContain属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setIsContain(JAXBElement<String> value) {
        this.isContain = value;
    }

}
