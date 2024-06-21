package javafx.assets;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

public class NewsBox extends VBox {

    private final Random rand = new Random();
    private final String standardText = "Aktuell gibt es keine Neuigkeiten. Schaue später wieder vorbei!";
    private int clickCounter = 0;

    public NewsBox() {
        build();
    }

    private VBox newsSurroundingBox;
    private VBox newsBox;
    private Label newsBoxLabel;
    private Text newsText;

    private void build() {
        createNodes();
    }

    private void createNodes() {
        this.newsBoxLabel = buildLabel("dashboard.newsbox.label", "news-box-label");
        this.newsText = buildNewsText();
        this.newsBox = buildNewsBox(newsBoxLabel, newsText);

        this.newsSurroundingBox = buildSurroundBox(newsBox);
        getChildren().add(newsSurroundingBox);
    }

    private Label buildLabel(String key, String... styleClasses) {
        Label label = new Label();
        bind(label.textProperty(), key);
        label.getStyleClass().addAll(styleClasses);
        return label;
    }

    private Text buildNewsText() {
        Text text = new Text(getRandomNewsText());
        text.setWrappingWidth(690);
        text.getStyleClass().add("news-text");
        return text;
    }

    private String getRandomNewsText() {
        String nextString;
        ArrayList<String> newsList = new ArrayList<>();
        File file = new File("src/main/resources/assets/articles.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((nextString = br.readLine()) != null) {
                newsList.add(nextString);
            }
        } catch (Exception ignored) {
            return standardText;
        }
        return newsList.get(rand.nextInt((newsList.size() - 1) - 1) + 1);
    }

    private VBox buildSurroundBox(VBox box1) {
        VBox box = new VBox(box1);
        box1.setAlignment(Pos.CENTER_LEFT);
        box.getStyleClass().addAll("news-surround-box");
        addBoxListener(box);
        return box;
    }

    private void addBoxListener(VBox box) {
        box.setOnMouseClicked(event ->{
            if (clickCounter > 10) {
                clickCounter = 0;
                newsText.textProperty().set(standardText);
                return;
            }
            newsText.textProperty().set(getRandomNewsText());
            clickCounter++;
        });
    }

    private VBox buildNewsBox(Label label, Text text) {
        VBox box = new VBox(label, text);
        box.getStyleClass().add("news-box");
        return box;
    }

    private void bind(StringProperty text, String key) {
        if (key.isEmpty()) {
            return;
        }
        text.bind(getValueByKey(key));
    }

    StringBinding getValueByKey(String key) {
        return LanguagePack.createStringBinding(key);
    }
}
