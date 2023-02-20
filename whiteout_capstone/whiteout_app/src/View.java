public enum View {
    MAIN("./fxml/MainMenu.fxml"),
    SETTINGS("./fxml/SettingsMenu.fxml"),
    CREATE(""),
    JOIN("");

    private String fileName;

    View(String fileName){
        this.fileName = fileName;
    }

    public String getFileName(){
        return fileName;
    }

}