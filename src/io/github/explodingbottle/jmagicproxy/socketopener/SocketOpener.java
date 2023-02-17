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
package io.github.explodingbottle.jmagicproxy.socketopener;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * This class represents a Socket opener.
 * 
 * @author ExplodingBottle
 *
 */
public interface SocketOpener {

	/**
	 * Opens a socket using parameters.
	 * 
	 * @param host The host to connect to.
	 * @param port The port to connect to.
	 * @return The opened socket.
	 * @throws IOException If there was any issues while opening the socket.
	 */
	public Socket openedSocket(InetAddress host, int port) throws IOException;

}
