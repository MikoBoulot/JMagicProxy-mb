/*
 *   JMagic Proxy - A HTTP and HTTPS Proxy
 *   Copyright (C) 2023  ExplodingBottle
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.github.explodingbottle.jmagicproxy.api;

import java.util.ArrayList;

import io.github.explodingbottle.jmagicproxy.ProxyMain;
import io.github.explodingbottle.jmagicproxy.logging.LoggingLevel;
import io.github.explodingbottle.jmagicproxy.logging.ProxyLogger;

/**
 * This class handles plugins loading an dispatching.
 * 
 * @author ExplodingBottle
 *
 */
public class PluginsManager {

	private ProxyLogger logger;
	private String pluginsToLoad;
	private ArrayList<ProxyPlugin> plugins;

	/**
	 * Instantiates a PluginsManager. It is important to keep track of it as it will
	 * be used everywhere.
	 * 
	 * @param pluginsToLoad The list of plugins to load separated by semicolons.
	 */
	public PluginsManager(String pluginsToLoad) {
		logger = ProxyMain.getLoggerProvider().createLogger();
		this.pluginsToLoad = pluginsToLoad;
	}

	/**
	 * Gives you a Connection Directive appropriate to the Request Header.
	 * 
	 * @param reqHeader The request header.
	 * @return A connection directive linked to the request header provided by a
	 *         plugin.
	 */
	public ConnectionDirective getInitialDirectiveByPlugins(HttpRequestHeader reqHeader) {
		ConnectionDirective finalDirective = null;
		for (ProxyPlugin plugin : plugins) {
			ConnectionDirective dir = plugin.onReceiveProxyRequest(reqHeader);
			if (dir != null) {
				finalDirective = dir;
				logger.log(LoggingLevel.INFO, "The plugin \"" + plugin.returnPluginName()
						+ "\" returned the first a connection directive, its directive will be used.");
				break;
			}
		}
		if (finalDirective == null) {
			logger.log(LoggingLevel.WARN,
					"The connection directive was null, this could be due to a misconfiguration, like a removal of the BasicProxy plugin.");
		}
		return finalDirective;
	}

	/**
	 * Gives you an appropriate incoming transfer directive which will be used to
	 * modify answer coming from the server.
	 * 
	 * @param response The response provided by the server.
	 * @return An incoming transfer directive.
	 */
	public IncomingTransferDirective getIncomingTransferDirective(HttpResponse response) {
		IncomingTransferDirective finalDirective = null;
		for (ProxyPlugin plugin : plugins) {
			IncomingTransferDirective dir = plugin.onReceiveServerAnswer(response);
			if (dir != null) {
				finalDirective = dir;
				logger.log(LoggingLevel.INFO, "The plugin \"" + plugin.returnPluginName()
						+ "\" returned the first an incoming transfer directive, its directive will be used.");
				break;
			}
		}
		if (finalDirective == null) {
			logger.log(LoggingLevel.WARN,
					"The transfer directive was null, this could be due to a misconfiguration, like a removal of the BasicProxy plugin.");
		}
		return finalDirective;
	}

	/**
	 * Gives you an appropriate SSL response which will be used to modify answer
	 * coming from the server.
	 * 
	 * @param response The response provided by the server.
	 * @return The same or the modified response to send.
	 */
	public HttpResponse getModifiedSSLResponse(HttpResponse response) {
		HttpResponse finalDirective = null;
		for (ProxyPlugin plugin : plugins) {
			HttpResponse dir = plugin.onReceiveServerSSLAnswer(response);
			if (dir != null) {
				finalDirective = dir;
				logger.log(LoggingLevel.INFO, "The plugin \"" + plugin.returnPluginName()
						+ "\" returned the first a SSL Http Response, its response will be used.");
				break;
			}
		}
		if (finalDirective == null) {
			logger.log(LoggingLevel.WARN,
					"The transfer directive was null, this could be due to a misconfiguration, like a removal of the BasicProxy plugin.");
		}
		return finalDirective;
	}

	/**
	 * Gives you an appropriate SSL Control Directive which will be used to modify
	 * the data you send to the server has well as possibly the host.
	 * 
	 * @param informations The data sent by the client.
	 * @return A SSL Control Directive.
	 */
	public SSLControlDirective getSSLControlDirective(SSLControlInformations informations) {
		SSLControlDirective finalDirective = null;
		for (ProxyPlugin plugin : plugins) {
			SSLControlDirective dir = plugin.onReceiveProxyRequestSSL(informations);
			if (dir != null) {
				finalDirective = dir;
				logger.log(LoggingLevel.INFO, "The plugin \"" + plugin.returnPluginName()
						+ "\" returned the first an SSL control directive, its directive will be used.");
				break;
			}
		}
		if (finalDirective == null) {
			logger.log(LoggingLevel.WARN,
					"The transfer directive was null, this could be due to a misconfiguration, like a removal of the BasicProxy plugin.");
		}
		return finalDirective;
	}

	/**
	 * This function will load all the plugins.
	 */
	public void loadPlugins() {
		logger.log(LoggingLevel.INFO, "Loading plugins...");
		if (plugins != null) {
			logger.log(LoggingLevel.WARN, "Something tried to load plugins more than once.");
			return;
		}
		plugins = new ArrayList<ProxyPlugin>();
		String[] pluginsTL = pluginsToLoad.split(";");
		for (String plugin : pluginsTL) {
			try {
				Class<?> pluginClass = Class.forName(plugin);
				if (ProxyPlugin.class.isAssignableFrom(pluginClass)) {
					try {
						ProxyPlugin pluginInstance = (ProxyPlugin) pluginClass.newInstance();
						plugins.add(pluginInstance);
						logger.log(LoggingLevel.INFO, "The plugin \"" + pluginInstance.returnPluginName() + "\"("
								+ plugin + ") has been loaded.");
					} catch (Exception e) {
						logger.log(LoggingLevel.WARN, "Failed to instantiate the plugin " + plugin + ".");
					}
				} else {
					logger.log(LoggingLevel.WARN,
							"The plugin " + plugin + " doesn't extends ProxyPlugin. Cannot load.");
				}
			} catch (ClassNotFoundException e) {
				logger.log(LoggingLevel.WARN,
						"A plugin is referenced but its class doesn't exists. Try reviewing configuration. Missing plugin class: "
								+ plugin,
						e);
			}
		}
		logger.log(LoggingLevel.INFO, "A total of " + plugins.size() + " were loaded !");
	}

}
