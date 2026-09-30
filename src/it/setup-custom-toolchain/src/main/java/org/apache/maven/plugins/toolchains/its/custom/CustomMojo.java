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

import java.util.Optional;

import org.apache.maven.api.Session;
import org.apache.maven.api.Toolchain;
import org.apache.maven.api.di.Inject;
import org.apache.maven.api.plugin.Log;
import org.apache.maven.api.plugin.Mojo;
import org.apache.maven.api.plugin.MojoException;
import org.apache.maven.api.services.ToolchainManager;

/**
 * A sample Mojo that uses the custom toolchain.
 *
 * @author Hervé Boutemy
 */
@org.apache.maven.api.plugin.annotations.Mojo(name = "tool")
public class CustomMojo implements Mojo {

    @Inject
    private Log log;

    @Inject
    private Session session;

    public void execute() throws MojoException {
        // get the custom toolchain
        Optional<Toolchain> toolchain =
                session.getService(ToolchainManager.class).getToolchainFromBuildContext(session, "custom");

        if (toolchain.isEmpty()) {
            throw new MojoException(
                    "Could not find 'custom' toolchain: please check maven-toolchains-plugin configuration.");
        }

        log.info("Found 'custom' toolchain in build context.");

        // get a tool from the toolchain
        String path = toolchain.get().findTool("tool");

        log.info("Found expected tool named 'tool' at following location: " + path);
    }
}
