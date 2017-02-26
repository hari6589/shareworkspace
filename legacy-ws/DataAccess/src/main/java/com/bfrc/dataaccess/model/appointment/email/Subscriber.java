
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
    "subscriptionState",
    "allowResubscribe",
    "division",
    "attributes"
})
public class Subscriber {

    @JsonProperty("subscriptionState")
    private String subscriptionState;
    @JsonProperty("allowResubscribe")
    private String allowResubscribe;
    @JsonProperty("division")
    private Division division;
    @JsonProperty("attributes")
    private Attributes attributes;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("subscriptionState")
    public String getSubscriptionState() {
        return subscriptionState;
    }

    @JsonProperty("subscriptionState")
    public void setSubscriptionState(String subscriptionState) {
        this.subscriptionState = subscriptionState;
    }

    @JsonProperty("allowResubscribe")
    public String getAllowResubscribe() {
        return allowResubscribe;
    }

    @JsonProperty("allowResubscribe")
    public void setAllowResubscribe(String allowResubscribe) {
        this.allowResubscribe = allowResubscribe;
    }

    @JsonProperty("division")
    public Division getDivision() {
        return division;
    }

    @JsonProperty("division")
    public void setDivision(Division division) {
        this.division = division;
    }

    @JsonProperty("attributes")
    public Attributes getAttributes() {
        return attributes;
    }

    @JsonProperty("attributes")
    public void setAttributes(Attributes attributes) {
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
