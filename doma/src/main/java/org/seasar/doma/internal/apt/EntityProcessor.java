/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.apt;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import org.seasar.doma.internal.apt.meta.EntityMeta;
import org.seasar.doma.internal.apt.meta.EntityMetaFactory;
import org.seasar.doma.internal.apt.meta.EntityPropertyMetaFactory;
import org.seasar.doma.internal.message.Message;
import org.seasar.doma.internal.util.IOUtil;

/**
 * @author taedium
 * 
 */
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes( { "org.seasar.doma.Entity" })
@SupportedOptions( { Options.TEST, Options.DEBUG })
public class EntityProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
            RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return true;
        }
        for (TypeElement a : annotations) {
            EntityMetaFactory entityMetaFactory = createEntityMetaFactory();
            for (TypeElement entityElement : ElementFilter.typesIn(roundEnv
                    .getElementsAnnotatedWith(a))) {
                if (Options.isDebugEnabled(processingEnv)) {
                    Notifier.debug(processingEnv, Message.DOMA4090, getClass()
                            .getName(), entityElement.getQualifiedName());
                }
                try {
                    EntityMeta entityMeta = entityMetaFactory
                            .createEntityMeta(entityElement);
                    if (!entityMeta.isError()) {
                        generateEntity(entityElement, entityMeta);
                    }
                } catch (AptException e) {
                    Notifier.notify(processingEnv, e);
                } catch (AptIllegalStateException e) {
                    Notifier.notify(processingEnv, Kind.ERROR,
                            Message.DOMA4039, entityElement);
                    throw e;
                } catch (RuntimeException e) {
                    Notifier.notify(processingEnv, Kind.ERROR,
                            Message.DOMA4016, entityElement);
                    throw e;
                }
                if (Options.isDebugEnabled(processingEnv)) {
                    Notifier.debug(processingEnv, Message.DOMA4091, getClass()
                            .getName(), entityElement.getQualifiedName());
                }
            }
        }
        return true;
    }

    protected EntityMetaFactory createEntityMetaFactory() {
        EntityPropertyMetaFactory propertyMetaFactory = createEntityPropertyMetaFactory();
        return new EntityMetaFactory(processingEnv, propertyMetaFactory);
    }

    protected EntityPropertyMetaFactory createEntityPropertyMetaFactory() {
        return new EntityPropertyMetaFactory(processingEnv);
    }

    protected void generateEntity(TypeElement entityElement,
            EntityMeta entityMeta) {
        EntityTypeGenerator generator = null;
        try {
            generator = createEntityMetaFactoryGenerator(entityElement,
                    entityMeta);
            generator.generate();
        } catch (IOException e) {
            throw new AptException(Message.DOMA4011, processingEnv,
                    entityElement, e, entityElement.getQualifiedName(), e);
        } finally {
            IOUtil.close(generator);
        }
    }

    protected EntityTypeGenerator createEntityMetaFactoryGenerator(
            TypeElement entityElement, EntityMeta entityMeta)
            throws IOException {
        return new EntityTypeGenerator(processingEnv, entityElement, entityMeta);
    }

}
