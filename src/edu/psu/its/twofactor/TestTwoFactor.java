package edu.psu.its.twofactor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;

/**
 * 
 */
class PSUAuthenBy2ndFactor {

	/**
	 * server host
	 */
	private final String host;

	/**
	 * server port
	 */
	private final int port;

	/**
	 * @param aHost server host
	 * @param aPort server port
	 */
	public PSUAuthenBy2ndFactor(String aHost, int aPort) {
		host = aHost;
		port = aPort;
	}

	/**
	 * Perform the authentication request
	 * 
	 * @param anAppName Application name
	 * @param aUserID Username
	 * @param securID Securid value
	 * @return Response
	 */
	public PSUResponse doAuthen(String anAppName, String aUserID, String securID) {
		Socket aSocket = null;
		DataOutputStream anOutputStream = null;
		DataInputStream anInputStream = null;
		NCPassProtocol protocolBuilder = new NCPassProtocol();
		byte[] resultbytes = new byte[100];
		// Create a socket & protocol object:
		try {
			aSocket = new Socket(host, port);
		} catch (UnknownHostException ex) {
			return new PSUResponse(-1, ex.getMessage());
		} catch (IOException ex) {
			return new PSUResponse(-1, ex.getMessage());
		} catch (Exception ex) {
			return new PSUResponse(-1, ex.getMessage());
		}
		// Send handshake:
		try {
			anOutputStream = new DataOutputStream(aSocket.getOutputStream());
			anOutputStream.write(protocolBuilder.buildHandShake(anAppName));
			anOutputStream.flush();
		} catch (IOException ex) {
			return new PSUResponse(-1, ex.getMessage());
		} catch (Exception ex) {
			return new PSUResponse(-1, ex.getMessage());
		}
		// Receive handshake response:
		try {
			anInputStream = new DataInputStream(aSocket.getInputStream());
			anInputStream.read(resultbytes);
			System.out.println("received header");
			Hashtable handshakeTable = protocolBuilder.decodeHandshake(resultbytes);
			System.out.println(handshakeTable.toString());
		} catch (IOException ex) {
			return new PSUResponse(-1, ex.getMessage());
		} catch (Exception ex) {
			return new PSUResponse(-1, ex.getMessage());
		}
		// Send request
		try {
			anOutputStream.write(protocolBuilder.buildRequest(aUserID, securID));
			anOutputStream.flush();
		} catch (IOException ex) {
			return new PSUResponse(-1, ex.getMessage());
		} catch (Exception ex) {
			return new PSUResponse(-1, ex.getMessage());
		}
		// Receive request response
		try {
			anInputStream = new DataInputStream(aSocket.getInputStream());
			anInputStream.read(resultbytes);
			System.out.println("received response");
			Hashtable responseTable = protocolBuilder.decodeResponse(resultbytes);
			System.out.println(responseTable.toString());
		} catch (IOException ex) {
			return new PSUResponse(-1, ex.getMessage());
		} catch (Exception ex) {
			return new PSUResponse(-1, ex.getMessage());
		}
		// Close everything:
		try {
			anOutputStream.close();
			// aBufferedReader.close();
			aSocket.close();
		} catch (Exception ex) {
			return new PSUResponse(-1, ex.getMessage());
		}
		// Return an error object with code=0 to signal OK:
		return new PSUResponse(0, "");
	}
}

/**
 * 
 */
class PSUResponse {

	/**
	 * User or System error
	 */
	private String category = "";

	/**
	 * error code
	 */
	private int code = 0;

	/**
	 * technical error message
	 */
	private String techMessage = "";

	/**
	 * Editing, network, or database error
	 */
	private String type = "";

	/**
	 * Message that the user sees
	 */
	private String userMessage = "";

	/**
	 * Constructor with code and user message
	 * 
	 * @param aCode error code
	 * @param aUserMessage user error message
	 */
	public PSUResponse(int aCode, String aUserMessage) {
		code = aCode;
		userMessage = aUserMessage;
	}

	/**
	 * Constructor with code, user, and technical message
	 * 
	 * @param aCode error code
	 * @param aUserMessage user error message
	 * @param aTechMessage technical error message
	 */
	public PSUResponse(int aCode, String aUserMessage, String aTechMessage) {
		code = aCode;
		userMessage = aUserMessage;
		techMessage = aTechMessage;
	}

	/**
	 * Constructor with code, type, category, user, and tech message
	 * 
	 * @param aCode Error code
	 * @param aType Error type
	 * @param aCategory Error category
	 * @param aUserMessage user error message
	 * @param aTechMessage technical error message
	 */
	public PSUResponse(int aCode, String aType, String aCategory, String aUserMessage, String aTechMessage) {
		code = aCode;
		type = aType;
		category = aCategory;
		userMessage = aUserMessage;
		techMessage = aTechMessage;
	}

	/**
	 * category getter
	 * 
	 * @return category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * code getter
	 * 
	 * @return code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * tech message getter
	 * 
	 * @return tech message
	 */
	public String getTechMessage() {
		return techMessage;
	}

	/**
	 * type getter
	 * 
	 * @return type
	 */
	public String getType() {
		return type;
	}

	/**
	 * user message getter
	 * 
	 * @return user message
	 */
	public String getUserMessage() {
		return userMessage;
	}

	/**
	 * checks if response has error
	 * 
	 * @return error status
	 */
	public boolean hasError() {
		return !isSuccessful();
	}

	/**
	 * checks if response was successful
	 * 
	 * @return success status
	 */
	public boolean isSuccessful() {
		return code == 0;
	}

	/**
	 * category setter
	 * 
	 * @param aCategory category
	 */
	public void setCategory(String aCategory) {
		category = aCategory;
	}

	/**
	 * code setter
	 * 
	 * @param aCode code
	 */
	public void setCode(int aCode) {
		code = aCode;
	}

	/**
	 * tech message setter
	 * 
	 * @param aTechMessage tech message
	 */
	public void setTechMessage(String aTechMessage) {
		techMessage = aTechMessage;
	}

	/**
	 * type setter
	 * 
	 * @param aType type
	 */
	public void setType(String aType) {
		type = aType;
	}

	/**
	 * user message setter
	 * 
	 * @param aUserMessage user message
	 */
	public void setUserMessage(String aUserMessage) {
		userMessage = aUserMessage;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Err code= " + code + ", Type= " + type + ", Category= " + category + ", User messag= " + userMessage + ", Tech message= " + techMessage;
	}
}

/**
 * Two factor test program
 */
public class TestTwoFactor {

	/**
	 * @param args unused
	 */
	public static void main(String[] args) {
		// Instantiate a PSUAuthenBy2ndFactor object:
		PSUAuthenBy2ndFactor aTwoFactor = new PSUAuthenBy2ndFactor("ncpass.server", 0);
		PSUResponse aResponse = aTwoFactor.doAuthen("marktest", "mxe20", "000000");
		if (aResponse.isSuccessful()) {
			System.out.println("Authen Result: Successful");
		} else {
			System.out.println("Authen Result: Failed; Error Code=" + aResponse.getCode() + "; ErrorMsg=" + aResponse.getUserMessage());
		}
	}
}