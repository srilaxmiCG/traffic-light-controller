
package com.example.traffic.model;

import java.util.ArrayList;
import java.util.List;

public class Sequence {
    private long version;
    private List<Phase> phases = new ArrayList<>();

    public long getVersion() { return version; }
    public void setVersion(long version) { this.version = version; }

    public List<Phase> getPhases() { return phases; }
    public void setPhases(List<Phase> phases) { this.phases = phases; }
}
