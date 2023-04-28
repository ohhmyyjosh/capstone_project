# White-Out-Client

## Instructions for Project Setup 
Assuming you have installed Visual Studio Code as well as the Java Extension Pack for Visual Studio Code follow these instructions to prepare the project for launch.
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
    - All of the needed libraries are included in the "javafx-sdk-19.0.2.1" -> "lib" folder. 

![image](https://user-images.githubusercontent.com/70347264/235073637-90dd20df-e5d6-4203-81e0-e7158c4c5ed1.png)
![image](https://user-images.githubusercontent.com/70347264/235073909-f2d4b551-dbee-4393-827d-c149a570018d.png)



## Instructions for Running the Project
