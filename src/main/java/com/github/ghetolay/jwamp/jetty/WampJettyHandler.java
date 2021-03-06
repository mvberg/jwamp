/**
*Copyright [2012] [Ghetolay]
*
*Licensed under the Apache License, Version 2.0 (the "License");
*you may not use this file except in compliance with the License.
*You may obtain a copy of the License at
*
*http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing, software
*distributed under the License is distributed on an "AS IS" BASIS,
*WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*See the License for the specific language governing permissions and
*limitations under the License.
*/
package com.github.ghetolay.jwamp.jetty;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.ghetolay.jwamp.WampConnection;
import com.github.ghetolay.jwamp.WampFactory;
import com.github.ghetolay.jwamp.WampMessageHandler;
import com.github.ghetolay.jwamp.WampParameter;
import com.github.ghetolay.jwamp.WampSerializer;
import com.github.ghetolay.jwamp.WampWebSocket;
import com.github.ghetolay.jwamp.WampWebSocketListener;

public class WampJettyHandler extends WebSocketHandler{
	
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	private WampParameter param;
	private WampSerializer serializer;
	private WampWebSocketListener listener;
	
	private HashMap<JettyServerConnection,HttpServletRequest> requests;
	
	public WampJettyHandler(WampParameter param, WampSerializer serializer, WampWebSocketListener newConnectionListener){
		this.param = param;
		this.serializer = serializer;
		this.listener = newConnectionListener;
		
		if(listener != null)
			requests = new HashMap<JettyServerConnection, HttpServletRequest>();
		
		getWebSocketFactory().setMaxIdleTime(-1);
	}
	
	public WebSocket doWebSocketConnect(HttpServletRequest request, final String subprotocol) {
		if(subprotocol != null && WampFactory.getProtocolName().equals(subprotocol.toUpperCase())){
			
			if(log.isTraceEnabled())
				log.trace("New Wamp Connection from " + request.getRemoteAddr());
			
			JettyServerConnection connection = new JettyServerConnection(serializer,param.getHandlers());
			
			if(requests != null)
				requests.put(connection,request);
			
			return connection;
		}
		else
			return new WebSocket(){

				public void onOpen(Connection connection) {
					//Not sure 1002 is the appropriate close code 
					connection.close(1002, "Unsupported subprotocol : " + subprotocol);
				}

				public void onClose(int closeCode, String message) {}
				
			};
	}
	
	public void setMaxIdleTime(int idleTimeout){
		getWebSocketFactory().setMaxIdleTime(idleTimeout);
	}
	
	public WampConnection getWampConnection(String uuid){
		return getWampConnection(uuid);
	}
	
	private class JettyServerConnection extends JettyConnection{
		
		public JettyServerConnection(WampSerializer serializer, Collection<WampMessageHandler> handlers) {
			super(null,serializer, handlers, null);
		}

		@Override
		public void onOpen(Connection connection){
			super.onOpen(connection);
			newClientConnection();
			if(listener != null)
				listener.newWampWebSocket(requests.remove(this), new WampWebSocket(this));
		}
		
		@Override
		public void onClose(){
			if(listener != null)
				listener.closedWampWebSocket(getSessionId());
		}
	}
}
