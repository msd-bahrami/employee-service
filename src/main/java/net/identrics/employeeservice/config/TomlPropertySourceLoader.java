package net.identrics.employeeservice.config;

import com.moandjiezana.toml.Toml;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.*;

public class TomlPropertySourceLoader extends PropertiesPropertySourceLoader {

    @Override
    public String[] getFileExtensions() {
        return new String[]{"toml"};
    }

    @Override
    public List<PropertySource<?>> load(String name, Resource resource)
            throws IOException {
        Toml content = new Toml().read(resource.getInputStream());
        Map<String, Object> tomlProperties = content.toMap();

        HashMap<String, Object> properties = toLinearMap(Optional.empty(), tomlProperties);
        List<PropertySource<?>> propertySources = new ArrayList<>(properties.size());
        propertySources.add(new OriginTrackedMapPropertySource(name + "(document #1)",
                Collections.unmodifiableMap((Map) properties), true));

        return propertySources;
    }

    private HashMap<String, Object> toLinearMap(Optional<String> prefix, Map<String, Object> map) {
        HashMap<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Object> e : map.entrySet()) {
            String newPrefix = prefix.orElse("") + "." + e.getKey();
            if (e.getValue() instanceof Map) {
                result.putAll(toLinearMap(Optional.of(newPrefix), (HashMap<String, Object>) e.getValue()));
            } else {
                result.put(newPrefix, e.getValue());
            }
        }
        return result;
    }
}