package project.gui;

import project.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiElementBox {
    Image imageUp, imageUpLeft, imageDown, imageUpRight, imageDownRight, imageDownLeft, imageRight, imageLeft, imageCarrot;

    private void loadImages() {
        try {
            this.imageUp = new Image(new FileInputStream("src/main/resources/up.png"));
            this.imageDown = new Image(new FileInputStream("src/main/resources/down.png"));
            this.imageRight = new Image(new FileInputStream("src/main/resources/right.png"));
            this.imageLeft = new Image(new FileInputStream("src/main/resources/left.png"));
            this.imageDownLeft = new Image(new FileInputStream("src/main/resources/down-left.png"));
            this.imageUpLeft = new Image(new FileInputStream("src/main/resources/up-left.png"));
            this.imageDownRight = new Image(new FileInputStream("src/main/resources/down-right.png"));
            this.imageUpRight = new Image(new FileInputStream("src/main/resources/up-right.png"));
            this.imageCarrot = new Image(new FileInputStream("src/main/resources/carrot.png"));
        } catch (FileNotFoundException ex) {
            System.out.println("File doesn't exist.");
        }
    }

    public GuiElementBox(){
        loadImages();
    }

    public VBox setImages(IMapElement element) {

        ImageView elementView;

        if (element instanceof Animal) {
            elementView = switch (((Animal)element).getDirection()) {
                case NORTH -> new ImageView(imageUp);
                case EAST -> new ImageView(imageRight);
                case WEST -> new ImageView(imageLeft);
                case SOUTH -> new ImageView(imageDown);
                case NORTH_EAST -> new ImageView(imageUpRight);
                case NORTH_WEST ->  new ImageView(imageUpLeft);
                case SOUTH_EAST ->  new ImageView(imageDownRight);
                case SOUTH_WEST ->  new ImageView(imageDownLeft);
            };

        } else {
            elementView = new ImageView(imageCarrot);
        }
        elementView.setFitWidth(40);
        elementView.setFitHeight(40);
        VBox elementVBox = new VBox();
        elementVBox.getChildren().addAll(elementView);
        elementVBox.setAlignment(Pos.CENTER);

        return elementVBox;

    }
}

