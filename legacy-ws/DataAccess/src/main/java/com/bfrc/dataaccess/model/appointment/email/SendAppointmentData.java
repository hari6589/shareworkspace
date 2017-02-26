
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
    "subscriber",
    "sideTable",
    "subscriberMessage"
})
public class SendAppointmentData {

    @JsonProperty("subscriber")
    private Subscriber subscriber;
    @JsonProperty("sideTable")
    private SideTable sideTable;
    @JsonProperty("subscriberMessage")
    private SubscriberMessage subscriberMessage;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("subscriber")
    public Subscriber getSubscriber() {
        return subscriber;
    }

    @JsonProperty("subscriber")
    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    @JsonProperty("sideTable")
    public SideTable getSideTable() {
        return sideTable;
    }

    @JsonProperty("sideTable")
    public void setSideTable(SideTable sideTable) {
        this.sideTable = sideTable;
    }

    @JsonProperty("subscriberMessage")
    public SubscriberMessage getSubscriberMessage() {
        return subscriberMessage;
    }

    @JsonProperty("subscriberMessage")
    public void setSubscriberMessage(SubscriberMessage subscriberMessage) {
        this.subscriberMessage = subscriberMessage;
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
