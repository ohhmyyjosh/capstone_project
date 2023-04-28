# White-Out-Client

## Instructions for Project Setup
Assuming you have installed Visual Studio Code as well as the Java Extension Pack for Visual Studio Code follow these instructions to prepare the project for launch.

1. Open the "whiteout_app" folder directly in Visual Studio Code. 
    - The folder contains a pre-made ".vscode" folder which will greatly improve setup time.
    - You should see VSCode acknowledge and build the Java Project within a "Java Projects" tab in the Explorer. 
![image](https://user-images.githubusercontent.com/70347264/235069606-71086f6f-c5fe-40cf-b409-f8dc636d566b.png)

2. You will need to generate a launch.json file and add the proper Java Virtual Machine arguments to it.
    - From the VSCode menu bar select "Run" -> "Add Configuration", then select "Java" from the list of configurations.
    - This should generate a "launch.json" file in the ".vscode" folder. You will then open this file.
![image](https://user-images.githubusercontent.com/70347264/235069542-406314c0-f413-4e97-8e87-b1dc0447dce2.png)
    - Add a comma to the end of the line labeled "projectName", then copy and paste the following Java Virtual machine arguments onto a new line within that same object:
    - "vmArgs": "--module-path \\"${workspaceFolder}/lib/javafx-sdk-19.0.2.1/lib\\" --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics,javafx.media,javafx.swing,javafx.web"

![image](https://user-images.githubusercontent.com/70347264/235069441-932e8a32-c450-44e8-a4be-1b4ee7e889ce.png)



## Instructions for Running the Project
