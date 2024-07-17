package javafx.assets;

import lombok.Getter;

@Getter
public enum ColorTheme {
    LIGHT("black", "#000000"), DARK("grey", "#ffffff");

    private final String colorPathFragment;
    private final String hex;

    ColorTheme(String pathFragment, String hex) {
        this.colorPathFragment = pathFragment;
        this.hex = hex;
    }

}