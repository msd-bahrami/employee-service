package net.identrics.employeeservice.config;

import org.ini4j.Ini;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IniPropertySourceLoader
        extends PropertiesPropertySourceLoader {

    @Override
    public String[] getFileExtensions() {
        return new String[]{"ini"};
    }

    @Override
    public List<PropertySource<?>> load(String name, Resource resource)
            throws IOException {
        Ini content = new Ini(resource.getFile());
        Map<String, String> properties = new HashMap<>();

        content.entrySet().forEach(stringSectionEntry -> stringSectionEntry.getValue()
                .forEach((key, value) -> properties.put(stringSectionEntry.getKey() + "." + key, value)));

        List<PropertySource<?>> propertySources = new ArrayList<>(properties.size());

        propertySources.add(new OriginTrackedMapPropertySource(name + "(document #1)",
                Collections.unmodifiableMap((Map) properties), true));

        return propertySources;
    }
}