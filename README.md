## White-Out
White-Out is a desktop application which generates a connected transparent whiteboard over your desktop. This whiteboard can be shared by many users, and offers useful annotation tools such as drawing.

We recommend running the White-Out-Client and testing the drawing tools using the Offline Canvas for the simplest and easiest experience when testing the product.

If you would like to test a multi-client connection we again recommend the White-Out-Client as well as the White-Out-Server projects for the simplest experience.

The SSL-Client and SSL-Server require installing self-signed certificates and we recommend merely observing the code changes within these projects, as there is no visual change between the normal client. 

### Development Team
Jaren Mills, Davis King, Hunter Bastian, Joshua Patterson

## Important System Setup
All provided instructions and guides for use of this product require the installation of Visual Studio Code as well as the Extension Pack for Java for Visual Studio Code.

Visual Studio Code (VSCode): https://code.visualstudio.com/

Extension Pack for Java for VSCode: https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack

Java 19 JDK: https://www.oracle.com/java/technologies/javase/jdk19-archive-downloads.html

## Submission Structure
There are four separate folders included in this repository.

- White-Out-Client
- White-Out-Server
- White-Out-SSL-Client
- White-Out-SSL-Server

NOTE: Each folder contains a "whiteout_app" folder which should be opened directly in Visual Studio Code for the fastest setup. These folders contain a pre-made ".vscode" folder for easier setup. 

## Instructions for Setting Up Project and Running Project in Video and Written Form
### These instructions apply to each server and client folder/project

Instructions for Setup Video: https://youtu.be/yaPIrc4rljI

User Guide/Walkthrough Video: https://youtu.be/9Pm8AOCNAl0

Assuming you have installed Visual Studio Code as well as the Java Extension Pack for Visual Studio Code follow these instructions to prepare each project for launch.
JavaFX requires specific dependencies which need to be recognized by your local Java environment to run the client projects. 

1. Open the "whiteout_app" folder directly in Visual Studio Code. 
    - The folder contains a pre-made ".vscode" folder which will greatly improve setup time.
    - You should see VSCode acknowledge and build the Java Project within a "Java Projects" tab in the Explorer. 
![image](https://user-images.githubusercontent.com/70347264/235069606-71086f6f-c5fe-40cf-b409-f8dc636d566b.png)

2. Setting up the Java Virtual Machine Environment
    - From the VSCode menu bar select "Run" -> "Add Configuration", then select "Java" from the list of configurations.
    - This should generate a "launch.json" file in the ".vscode" folder. You will then open this file.
![image](https://user-images.githubusercontent.com/70347264/235069542-406314c0-f413-4e97-8e87-b1dc0447dce2.png)
    - Add a comma to the end of the line labeled "projectName", then copy and paste the following Java Virtual machine arguments onto a new line within that same object:
    - "vmArgs": "--module-path \\"${workspaceFolder}/lib/javafx-sdk-19.0.2.1/lib\\" --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics,javafx.media,javafx.swing,javafx.web"
![image](https://user-images.githubusercontent.com/70347264/235070863-96dd1331-e3c4-4930-98fd-7f6cd7b04ca0.png)

3. Setting up the Classpath
    - You will want to add the necessary libraries to the classpath for the project.
    - All of the needed libraries are included in the "lib" -> "javafx-sdk-19.0.2.1" -> "lib" folder. 
![image](https://user-images.githubusercontent.com/70347264/235073637-90dd20df-e5d6-4203-81e0-e7158c4c5ed1.png)
![image](https://user-images.githubusercontent.com/70347264/235077908-e27d9307-a75a-497c-beff-9b64b07c0489.png)

4. After following these instructions you should open App.java and check if there are any warnings about imported libraries not being found.
    - You should see the main method at the bottom of the file and it should not display any errors, you will likely see a run button above the method indicating that the project is recognized as a runnable java project.

![image](https://user-images.githubusercontent.com/70347264/235074904-7ad1ffc5-e7ce-4e5f-b7d3-071a2667a81f.png)

5. Running the Project
    - This can be done by simply clicking the "Run" button above the main method.
    - NOTE: Potential errors at this point likely include "JavaFX Runtime Variables are missing."
    - This error indicates that the JavaFX libraries are not being recognized in your environment and I would recommend following the steps above again for the project.


## SSL Setup

1. Server
	
	1.1 Open Powershell as administrator and navigate to the following directory replacing ${jdk-version} as needed: "C:\\Program Files\\Java\\${jdk-version}\\bin"
	
	1.2 Execute the following command: ./keytool -genkey -alias whiteoutCert -keyalg RSA -storepass whiteout -keystore selfsigned.jks -validity 360
	
	1.3 Within the App.java file of SSL-Server change the directory on line 30 to match your keystore directory (Change the $[jdk-version] if needed)

2. Extracting the certificate
	
	2.1 Open Powershell as administrator on the server machine and navigate to the directory of the created keystore replacing ${jdk-version} as needed: "C:\\Program Files\\Java\\${jdk-version}\\bin"
	
	2.2 Execute the following command: ./keytool -export -alias whiteoutCert -keystore selfsigned.jks -rfc -file public.cer
	
	2.3 Enter the password "whiteout" when prompted
  	
	2.4 Distribute the file public.cert to the client machines

3. Client
	
	3.1 On the client machine, relocate the public cert file to the following directory replacing ${jre-version} as needed: "C:\\Program Files\\Java\\${jre-version}\\bin"
  	
	3.2 Run powershell as admin and enter the above directory.
  	
	3.3 Execute the following command replacing $[jre-version] as needed: ./keytool -import -trustcacerts -alias whiteoutCert -file public.cert -keystore "C:\\Program Files\\Java\\${jre-version}\\lib\\security\\cacerts"
  	
	3.4 Enter the keystore password when prompted. The default password is "changeit"
  	
	3.5 Enter "Yes" to the prompt asking if you trust this certificate
  	
	3.6 Within the App.java file of SSL-Client change the directory on line 38 to match the directory of your trust store. (change the jre version as needed)
  	
	3.7 If your trust store password is not the default "changeit", change the password on line 40 of the App.java file in SSL-Client

