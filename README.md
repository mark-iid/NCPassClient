[![Java CI with Maven](https://github.com/mark-iid/NCPassClient/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/mark-iid/NCPassClient/actions/workflows/maven.yml)


# NCPassClient

NCPassClient is a simple Java library for validating SecurID tokens using the mainframe based NCPass application.

### Installation

Download the latest release from the [releases page](https://github.com/mark-iid/NCPassClient/releases).

### Build Instructions

To build NCPassClient from source, follow these steps:

1. Clone the repository:
    ```sh
    git clone https://github.com/mark-iid/NCPassClient.git
    ```
2. Navigate to the project directory:
    ```sh
    cd NCPassClient
    ```
3. Compile the project using Maven:
    ```sh
    mvn clean install
    ```

The Maven coordinates are `edu.psu.its:twofactor`, so this generates `twofactor-1.0-SNAPSHOT.jar` in the `target` directory.

### Usage

`NCPassProtocol` is the public API. It builds and decodes the NCPass handshake and request frames:

```java
NCPassProtocol protocol = new NCPassProtocol();
byte[] handshake = protocol.buildHandShake("testapp");
byte[] request   = protocol.buildRequest("username", "000000");
Hashtable response = protocol.decodeResponse(responseBytes);
```

`TestTwoFactor.java` shows the full socket exchange wrapped up as `PSUAuthenBy2ndFactor`:

```java
package edu.psu.its.twofactor;

public class TestTwoFactor {

    public static void main(String[] args) {
        // Instantiate a PSUAuthenBy2ndFactor object:
        PSUAuthenBy2ndFactor aTwoFactor = new PSUAuthenBy2ndFactor("ncpass.server", 0);
        PSUResponse aResponse = aTwoFactor.doAuthen("testapp", "username", "000000");
        if (aResponse.isSuccessful()) {
            System.out.println("Authen Result: Successful");
        } else {
            System.out.println("Authen Result: Failed; Error Code=" + aResponse.getCode() + "; ErrorMsg=" + aResponse.getUserMessage());
        }
    }
}
```

Note that `PSUAuthenBy2ndFactor` and `PSUResponse` are package-private, so this example only
compiles from inside `edu.psu.its.twofactor`. Callers outside that package should use
`NCPassProtocol` directly, or copy the socket handling from `TestTwoFactor`.

Contributions are welcome! Please open an issue or submit a pull request.

## License

This project is licensed under the Apache License 2.0. See [LICENSE](LICENSE).
