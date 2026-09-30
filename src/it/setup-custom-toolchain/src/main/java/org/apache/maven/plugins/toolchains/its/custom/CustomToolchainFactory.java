/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.maven.plugins.toolchains.its.custom;

import javax.inject.Named;
import javax.inject.Singleton;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.maven.api.Toolchain;
import org.apache.maven.api.services.ToolchainFactory;
import org.apache.maven.api.services.ToolchainFactoryException;
import org.apache.maven.api.toolchain.ToolchainModel;
import org.apache.maven.api.xml.XmlNode;
import org.codehaus.plexus.util.FileUtils;

/**
 * Custom toolchain factory, declared as <code>ToolchainFactory</code> component (discovered through the Sisu index) with the
 * <code>custom</code> name.
 *
 * @author Hervé Boutemy
 */
@Singleton
@Named("custom")
public class CustomToolchainFactory implements ToolchainFactory {

    public Toolchain createToolchain(ToolchainModel model) throws ToolchainFactoryException {
        // the provides section: every token is matched exactly
        Map<String, String> provides = new HashMap<>();
        for (Map.Entry<String, String> provide : model.getProvides().entrySet()) {
            if (provide.getValue() == null) {
                throw new ToolchainFactoryException(
                        "Provides token '" + provide.getKey() + "' doesn't have any value configured.");
            }
            provides.put(provide.getKey(), provide.getValue());
        }

        // the configuration section
        XmlNode toolHomeNode = model.getConfiguration() != null
                ? model.getConfiguration().child(CustomToolchainImpl.KEY_TOOLHOME)
                : null;
        String toolHome = toolHomeNode != null ? toolHomeNode.value() : null;
        if (toolHome == null) {
            throw new ToolchainFactoryException(
                    "Custom toolchain without the " + CustomToolchainImpl.KEY_TOOLHOME + " configuration element.");
        }

        toolHome = FileUtils.normalize(toolHome);

        if (!new File(toolHome).exists()) {
            throw new ToolchainFactoryException(
                    "Non-existing tool home configuration at " + new File(toolHome).getAbsolutePath());
        }

        return new CustomToolchainImpl(model, provides, toolHome);
    }

    public Optional<Toolchain> createDefaultToolchain() {
        // not sure it's necessary to provide a default toolchain here.
        return Optional.empty();
    }
}
