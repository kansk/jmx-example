package com.github.darogina.jmx.property;

import java.util.Collection;
import java.util.Properties;

/**
 * User: davidrogina
 * Date: 12/12/13
 */
public interface ManageableProperties {
    public Properties getProperties();
    public Collection getHiddenPropertiesList();
    public Collection getReadOnlyPropertiesList();
}
