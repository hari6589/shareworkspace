
package com.bfrc.dataaccess.model.appointment.email;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    "tables"
})
public class SideTable {

    @JsonProperty("tables")
    private List<Table> tables = new ArrayList<Table>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("tables")
    public List<Table> getTables() {
        return tables;
    }

    @JsonProperty("tables")
    public void setTables(List<Table> tables) {
        this.tables = tables;
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
