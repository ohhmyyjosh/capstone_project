public enum View {
    MAIN("./fxml/MainMenu.fxml"),
    SETTINGS("./fxml/SettingsMenu.fxml"),
    CANVAS("./fxml/Canvas.fxml"),
    CREATESESSION("./fxml/CreateSessionMenu.fxml"),
    JOINSESSION("./fxml/JoinSessionMenu.fxml");

    private String fileName;

    View(String fileName){
        this.fileName = fileName;
    }

    public String getFileName(){
        return fileName;
    }

}