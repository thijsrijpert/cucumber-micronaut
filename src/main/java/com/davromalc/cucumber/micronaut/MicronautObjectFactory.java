package com.davromalc.cucumber.micronaut;

import io.cucumber.core.backend.ObjectFactory;
import io.cucumber.core.exception.CucumberException;
import io.micronaut.context.ApplicationContext;
import io.micronaut.inject.qualifiers.Qualifiers;

import javax.inject.Named;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Util class to allow micronaut to be used with cucumber
 */
public final class MicronautObjectFactory implements ObjectFactory {

    ApplicationContext applicationContext;

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        applicationContext = ApplicationContext.run("acceptance", "test");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        if (applicationContext != null) {
            applicationContext.stop();
        }
    }

    /**
     * {@inheritDoc}
     *  @param  glueClass type of instance to be created.
     *  @param  <T>       type of Glue class
     *  @return           new instance of type T
     */
    @Override
    public <T> T getInstance(Class<T> glueClass) {
        final Constructor<T> constructor = (Constructor<T>) glueClass.getConstructors()[0];
        final Object[] parameters = new Object[constructor.getParameterCount()];
        try {
            for (int index = 0; index < constructor.getParameters().length; index++) {
                final Type parameterizedType = constructor.getParameters()[index].getParameterizedType();
                if (hasNamedAnnotation(constructor.getParameters()[index])) {
                    final Named[] beanNames = constructor.getParameters()[index].getAnnotationsByType(Named.class);
                    parameters[index] = applicationContext.getBean(constructor.getParameterTypes()[index],
                        Qualifiers.byName(beanNames[0].value()));
                } else if (hasParameterType(parameterizedType)) {
                    parameters[index] = getParameterizedBean(constructor, index, (ParameterizedType) parameterizedType);
                } else {
                    parameters[index] = applicationContext.getBean(constructor.getParameterTypes()[index]);
                }
            }
            return constructor.newInstance(parameters);
        } catch (InstantiationException | IllegalStateException | InvocationTargetException
                | IllegalAccessException e) {
            throw new CucumberException(String.format("Failed to instantiate %s", glueClass), e);
        }
    }

    private <T> Object getParameterizedBean(
            Constructor<T> constructor, int index, ParameterizedType parameterizedType
    ) {
        return applicationContext.getBean(constructor.getParameterTypes()[index],
            Qualifiers.byTypeArgumentsClosest(getTypes(parameterizedType)));
    }

    private Class<?>[] getTypes(ParameterizedType type) {
        final Type[] types = type.getActualTypeArguments();
        final Class<?>[] typesClass = new Class[types.length];
        for (int indexType = 0; indexType < types.length; indexType++) {
            if (types[indexType] instanceof Class) {
                typesClass[indexType] = (Class<?>) types[indexType];
            } else if (types[indexType] instanceof ParameterizedType) {
                typesClass[indexType] = (Class<?>) ((ParameterizedType) types[indexType]).getRawType();
            }
        }
        return typesClass;
    }

    /**
     * {@inheritDoc}
     *
     * @param  glueClass unused
     * @return should always return true, should be ignored.
     */
    @Override
    public boolean addClass(Class<?> glueClass) {
        return true;
    }

    private boolean hasParameterType(Type type) {
        return type instanceof ParameterizedType;
    }

    private boolean hasNamedAnnotation(Parameter parameter) {
        final Named[] named = parameter.getAnnotationsByType(Named.class);
        return named != null && named.length > 0;
    }
}
