
package com.bfrc.dataaccess.model.appointment.email;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "masterId",
    "attributes"
})
public class SubscriberMessage {

    @JsonProperty("masterId")
    private Integer masterId;
    @JsonProperty("attributes")
    private Attributes_ attributes;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("masterId")
    public Integer getMasterId() {
        return masterId;
    }

    @JsonProperty("masterId")
    public void setMasterId(Integer masterId) {
        this.masterId = masterId;
    }

    @JsonProperty("attributes")
    public Attributes_ getAttributes() {
        return attributes;
    }

    @JsonProperty("attributes")
    public void setAttributes(Attributes_ attributes) {
        this.attributes = attributes;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
