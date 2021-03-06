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
package com.github.ghetolay.jwamp.message.output;

import com.github.ghetolay.jwamp.message.WampCallErrorMessage;

/**
 * @author ghetolay
 *
 */
public class OutputWampCallErrorMessage extends WampCallErrorMessage {
	
	public OutputWampCallErrorMessage(){}
	
	public OutputWampCallErrorMessage(String... args){
		super();
		
		try{
			callId = args[0];
			errorUri = args[1];
			errorDesc = args[2];
			errorDetails = args[3];
		}catch(IndexOutOfBoundsException e){}
	}
	
	public void setCallId(String callId) {
		this.callId = callId;
	}
	
	public void setErrorUri(String errorUri) {
		this.errorUri = errorUri;
	}
	
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}
	
}
