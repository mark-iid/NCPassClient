package edu.psu.its.twofactor;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

public class NCPassProtocolTest {

    private NCPassProtocol ncPassProtocol;

    @BeforeEach
    public void setUp() {
        ncPassProtocol = new NCPassProtocol();
    }

    @Test
    public void testBuildHandShake() throws UnsupportedEncodingException, IOException {
        String appID = "TestApp";
        byte[] handshake = ncPassProtocol.buildHandShake(appID);
        assertNotNull(handshake);
        assertTrue(handshake.length > 0);
    }

    @Test
    public void testBuildRequest() throws UnsupportedEncodingException, IOException {
        String userID = "TestUser";
        String secureID = "123456";
        byte[] request = ncPassProtocol.buildRequest(userID, secureID);
        assertNotNull(request);
        assertTrue(request.length > 0);
    }

    @Test
    public void testDecodeHandshake() throws UnsupportedEncodingException {
        byte[] handshake = new byte[] {
            0, 39,
            (byte)0xD6, (byte)0xE2, // "OS" in EBCDIC
            (byte)0xF0, (byte)0xF0, (byte)0xF0, (byte)0xF0, (byte)0xF0, (byte)0xF1, // "000001" in EBCDIC
            (byte)0xE2, (byte)0xC5, (byte)0xF0, (byte)0xF0, // "SE00" in EBCDIC
            0, 5,
            (byte)0xD5, (byte)0xC3, (byte)0xE3, (byte)0xD3, (byte)0xC9, // "NCTLI" in EBCDIC
            0, 5,
            (byte)0xC3, (byte)0xD7, (byte)0xE4, (byte)0xC9, (byte)0xC4, // "CPUID" in EBCDIC
            0, 6,
            (byte)0xD7, (byte)0xC1, (byte)0xE2, (byte)0xE2, (byte)0xE6, (byte)0xC4, // "PASSWD" in EBCDIC
            0, 1,
            (byte)0xF1 // "1" in EBCDIC
        };
        Hashtable result = ncPassProtocol.decodeHandshake(handshake);
        assertNotNull(result);
        assertEquals("NCTLI", result.get("SystemID"));
        assertEquals("CPUID", result.get("CPUID"));
        assertEquals("PASSWD", result.get("Password"));
        assertEquals("1", result.get("DirectionID"));
    }

    @Test
    public void testDecodeResponse() throws UnsupportedEncodingException {
        byte[] response = new byte[] {
            0, 43,
            (byte)0xD6, (byte)0xE2, // "OS" in EBCDIC
            (byte)0xF0, (byte)0xF0, (byte)0xF0, (byte)0xF0, (byte)0xF0, (byte)0xF1, // "000001" in EBCDIC
            (byte)0xE2, (byte)0xC5, (byte)0xF0, (byte)0xF3, // "SE03" in EBCDIC
            0, 2, 0, 0,
            0, 2, 0, 0,
            0, 7,
            (byte)0xD4, (byte)0xC5, (byte)0xE2, (byte)0xE2, (byte)0xC1, (byte)0xC7, (byte)0xC5, // "MESSAGE" in EBCDIC
            0, 4,
            (byte)0xC8, (byte)0xE4, (byte)0xC9, (byte)0xC4, // "HUID" in EBCDIC
            0, 4,
            (byte)0xD9, (byte)0xE4, (byte)0xC9, (byte)0xC4 // "RUID" in EBCDIC
        };
        Hashtable result = ncPassProtocol.decodeResponse(response);
        assertNotNull(result);
        assertEquals("Validation Successful", result.get("ValidationResult"));
        assertEquals("Authentication Successful", result.get("AuthenticationResult"));
        assertEquals("MESSAGE", result.get("Message"));
        assertEquals("HUID", result.get("HostUserID"));
        assertEquals("RUID", result.get("RemoteUserID"));
    }

    @Test
    public void testAuthenticationCode() {
        assertEquals("Authentication Successful", ncPassProtocol.authenticationCode(0));
        assertEquals("Authentication Failed", ncPassProtocol.authenticationCode(10));
        assertEquals("Unknown Authentication Code", ncPassProtocol.authenticationCode(99));
    }

    @Test
    public void testValidationCode() {
        assertEquals("Validation Successful", ncPassProtocol.validationCode(0));
        assertEquals("Invalid Terminal ID", ncPassProtocol.validationCode(2));
        assertEquals("Unknown Validation Code", ncPassProtocol.validationCode(99));
    }
}