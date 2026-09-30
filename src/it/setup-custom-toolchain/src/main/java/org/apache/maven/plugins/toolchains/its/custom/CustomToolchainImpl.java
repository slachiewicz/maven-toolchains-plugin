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

import java.io.File;
import java.util.Map;

import org.apache.maven.api.toolchain.ToolchainModel;
import org.codehaus.plexus.util.Os;

/**
 * @author Hervé Boutemy
 */
public class CustomToolchainImpl implements CustomToolchain {
    public static final String KEY_TOOLHOME = "toolHome";

    private final ToolchainModel model;

    private final Map<String, String> provides;

    private final String toolHome;

    public CustomToolchainImpl(ToolchainModel model, Map<String, String> provides, String toolHome) {
        this.model = model;
        this.provides = provides;
        this.toolHome = toolHome;
    }

    public String getToolHome() {
        return toolHome;
    }

    public String getType() {
        return "custom";
    }

    public ToolchainModel getModel() {
        return model;
    }

    public boolean matchesRequirements(Map<String, String> requirements) {
        for (Map.Entry<String, String> requirement : requirements.entrySet()) {
            if (!requirement.getValue().equalsIgnoreCase(provides.get(requirement.getKey()))) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return "Custom[" + getToolHome() + "]";
    }

    public String findTool(String toolName) {
        File tool = findTool(toolName, new File(getToolHome()));

        if (tool != null) {
            return tool.getAbsolutePath();
        }

        return null;
    }

    private static File findTool(String toolName, File installFolder) {
        File bin = new File(installFolder, "bin"); // NOI18N

        if (bin.exists()) {
            File tool = new File(bin, toolName + (Os.isFamily("windows") ? ".exe" : "")); // NOI18N

            if (tool.exists()) {
                return tool;
            }
        }

        return null;
    }
}
