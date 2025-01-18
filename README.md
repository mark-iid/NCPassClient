[![Java CI with Maven](https://github.com/mark-iid/NCPassClient/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/mark-iid/NCPassClient/actions/workflows/maven.yml)


# NCPassClient

NCPassClient (PSUTwoFactor) is a simple java libray for validating SecurID tokens using the mainframe based NCPass application.

### Installation

To install PSUTwoFactor, download the latest release from the [releases page](https://github.com/mark-iid/psutwofactor/releases).

### Build Instructions

To build PSUTwoFactor from source, follow these steps:

1. Clone the repository:
    ```sh
    git clone https://github.com/mark-iid/psutwofactor.git
    ```
2. Navigate to the project directory:
    ```sh
    cd psutwofactor
    ```
3. Compile the project using Maven:
    ```sh
    mvn clean install
    ```

This will generate the `psutwofactor-x.y.z.jar` file in the `target` directory, where `x.y.z` is the version number.

### Usage

Here is a basic example of how to use PSUTwoFactor:

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

Contributions are welcome! Please open an issue or submit a pull request.

## License

This project is licensed under the MIT License.
