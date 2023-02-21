public enum View {
    MAIN("./fxml/MainMenu.fxml"),
    SETTINGS("./fxml/SettingsMenu.fxml"),
    CANVAS("./fxml/Canvas.fxml");

    private String fileName;

    View(String fileName){
        this.fileName = fileName;
    }

    public String getFileName(){
        return fileName;
    }

}