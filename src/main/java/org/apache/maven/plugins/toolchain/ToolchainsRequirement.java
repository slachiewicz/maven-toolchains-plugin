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
package org.apache.maven.plugins.toolchain;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.maven.api.xml.XmlNode;

/**
 * The toolchain requirements of the {@code toolchain} goal, one entry per toolchain type.
 */
public final class ToolchainsRequirement {
    private final Map<String, Map<String, String>> toolchains;

    private ToolchainsRequirement(Map<String, Map<String, String>> toolchains) {
        this.toolchains = toolchains;
    }

    /**
     * Reads the requirements from the {@code toolchains} configuration element: each child is a toolchain type
     * and each grand-child a required parameter.
     */
    public static ToolchainsRequirement fromXmlNode(XmlNode configuration) {
        Map<String, Map<String, String>> map = new LinkedHashMap<>();
        for (XmlNode tool : configuration.children()) {
            Map<String, String> parameters = new LinkedHashMap<>();
            for (XmlNode param : tool.children()) {
                parameters.put(param.name(), param.value());
            }
            map.put(tool.name(), parameters);
        }
        return new ToolchainsRequirement(map);
    }

    public Map<String, Map<String, String>> getToolchains() {
        return Collections.unmodifiableMap(toolchains);
    }

    public Set<String> getToolchainsTypes() {
        return Collections.unmodifiableSet(toolchains.keySet());
    }

    public Map<String, String> getParams(String type) {
        Map<String, String> params = toolchains.get(type);
        return params == null ? Collections.emptyMap() : Collections.unmodifiableMap(params);
    }

    @Override
    public String toString() {
        return "ToolchainsRequirement{toolchains=" + toolchains + '}';
    }
}
