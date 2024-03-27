package org.nikita;

import com.google.common.reflect.ClassPath;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
public class BeanFactory {
    Map<String, Class> beanDefinitions = new HashMap<>();

    public Map<String, Class> createBeanDefinitions() throws IOException {
        return ClassPath.from(ClassLoader.getSystemClassLoader())
                .getAllClasses().stream().map(ClassPath.ClassInfo::load)
                .filter(aClass -> aClass.getAnnotation(MyComponent.class) != null)
                .collect(Collectors.toMap(Class::getName, aClass -> aClass));
    }

    public Map<String, Object> findBeans(String[] args) throws IOException {
        return ClassPath.from(ClassLoader.getSystemClassLoader())
                .getAllClasses().stream().map(clazz -> clazz.load())
                .filter(aClass -> aClass.getAnnotation(MyComponent.class) != null)
                .collect(Collectors.toMap(Class::getName, aClass -> {
                    try {
                        return aClass.getDeclaredConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                             NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }));
    }

}