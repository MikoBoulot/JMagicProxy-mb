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

/**
 * This class represents a Proxy Plugin. Make a class extending it so it can
 * receive requests and modify them.
 * 
 * @author ExplodingBottle
 */
public abstract class ProxyPlugin {

	/**
	 * This represents what to do when a proxy request is received.
	 * 
	 * @param request The initial request, often contains some informations.
	 * @return Can return a modified request in order to change the host or other
	 *         things. If you return {@code null}, you will let another plugin
	 *         modify your request.
	 */
	public abstract ConnectionDirective onReceiveProxyRequest(HttpRequestHeader request);

	/**
	 * This represents what to do when a proxy request is received through SSL.
	 * 
	 * @param request The initial request, often contains some informations.
	 * @return Can return a modified request in order to change the host or other
	 *         things. If you return {@code null}, you will let another plugin
	 *         modify your request.
	 */
	public abstract SSLControlDirective onReceiveProxyRequestSSL(SSLControlInformations request);

	/**
	 * This represents what to do when the proxy is receiving data from the server.
	 * 
	 * @param response This represents the HTTP response line as well as the headers
	 *                 to allow modification.
	 * @return Can return a modified response in order to change the host or other
	 *         things. If you return {@code null}, you will let another plugin
	 *         modify your request.
	 */
	public abstract IncomingTransferDirective onReceiveServerAnswer(HttpResponse response);

	/**
	 * This represents what to do when the proxy is receiving data from the server
	 * through SSL.
	 * 
	 * @param response This represents the HTTP response line as well as the headers
	 *                 to allow modification.
	 * @return Can return a modified response in order to change the host or other
	 *         things. If you return {@code null}, you will let another plugin
	 *         modify your request.
	 */
	public abstract HttpResponse onReceiveServerSSLAnswer(HttpResponse response);

	/**
	 * This function is used to make the plugin display a better name than only the
	 * class name.
	 * 
	 * @return The user-friendly plugin name.
	 */
	public abstract String returnPluginName();

}
