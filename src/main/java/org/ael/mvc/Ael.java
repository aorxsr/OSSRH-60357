/* Copyright (c) 2019, aorxsr (aorxsr@163.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.ael.mvc;

import lombok.Data;
import org.ael.mvc.constant.EnvironmentConstant;
import org.ael.mvc.constant.HttpMethodConstant;
import org.ael.mvc.handler.StaticsResourcesHandler;
import org.ael.mvc.http.session.SessionHandler;
import org.ael.mvc.http.session.SessionManager;
import org.ael.mvc.route.RouteFunctionHandler;
import org.ael.mvc.route.RouteHandler;
import org.ael.mvc.server.Server;
import org.ael.mvc.server.netty.InitialHandler;
import org.ael.mvc.server.netty.NettyServer;
import org.ael.mvc.template.AelTemplate;
import org.ael.mvc.template.give.DefaultTemplate;

import java.util.*;

/**
 * @Author: aorxsr
 * @Date: 2019/7/16 18:19
 */
@Data
public class Ael {

    private Class<?> startClass;

    private AelTemplate aelTemplate = new DefaultTemplate();

    private StaticsResourcesHandler staticsResourcesHandler = new StaticsResourcesHandler();

    private Server server = new NettyServer();

    private Environment environment = new Environment();

    private RouteHandler routeHandler = new RouteHandler();

    private SessionManager sessionManager = new SessionManager();

    private SessionHandler sessionHandler = new SessionHandler(sessionManager);

    private Set<Class<?>> scanClass = new LinkedHashSet<>(16);

    private List<InitialHandler> initHandlers = new ArrayList<>();

    public Ael start(Class<?> startClass) {
        this.startClass = startClass;
        server.start(this);
        return this;
    }

    public Ael setSessionKey(String sessionKey) {
        environment.put(EnvironmentConstant.SESSION_KEY, sessionKey);
        return this;
    }

    public void stop() {
        server.stop();
    }

    public Ael get(String url, RouteFunctionHandler handler) {
        routeHandler.addHandler(HttpMethodConstant.GET_UPPER, url, handler);
        return this;
    }

    public Ael post(String url, RouteFunctionHandler handler) {
        routeHandler.addHandler(HttpMethodConstant.POST_UPPER, url, handler);
        return this;
    }

    public Ael setTemplateImpl(Class<?> templateClass) throws IllegalAccessException, InstantiationException {
        aelTemplate = (AelTemplate) templateClass.newInstance();
        return this;
    }

    public Ael addResourcesMapping(String resourcesHandler, String resourcesLocation) {
        staticsResourcesHandler.getResources().put(resourcesHandler, resourcesLocation);
        staticsResourcesHandler.getResourcesHandlers().add(resourcesHandler);
        return this;
    }

    public Ael removeResourcesMapping(String resourcesHandler) {
        if (staticsResourcesHandler.getResources().containsKey(resourcesHandler)) {
            staticsResourcesHandler.getResources().remove(resourcesHandler);
            staticsResourcesHandler.getResourcesHandlers().remove(resourcesHandler);
        }
        return this;
    }

}
