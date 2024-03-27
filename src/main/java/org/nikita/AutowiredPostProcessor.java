package org.nikita;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Autowired
public class AutowiredPostProcessor {
    private final Map<String, Object> finalBeans;

    private final Map<String, Object> beans;
    private final Map<String, Class> beanDefinitions;

    public AutowiredPostProcessor(Map<String, Object> finalBeans, Map<String, Object> beans, Map<String, Class> beanDefinitions) {
        this.finalBeans = finalBeans;
        this.beans = beans;
        this.beanDefinitions = beanDefinitions;
    }


    public void findBeanWithAutowiredAnnotation() {
        for (Map.Entry<String, Class> bean : beanDefinitions.entrySet()) {
            List<Field> fieldWithAutoWired = Arrays.stream(bean.getValue().getFields())
                    .filter(field -> Arrays.stream(field.getAnnotations()).anyMatch(annotation -> annotation.annotationType().equals(Autowired.class)))
                    .toList();
            Object injectBean = beans.get(fieldWithAutoWired.getDeclaringClass());
            Object targetBean = beans.get(bean.getValue());
        }
    }

}
