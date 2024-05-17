package views;

import javafx.geometry.HorizontalDirection;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Confirm {
	static boolean answer;

	public static boolean display(String Title, String msg) {
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(Title);
		
		
		//Load the background image
		Image backgroundImage = new Image("areYouSure.png.jpg");

		// Create a BackgroundSize object to fit the window size and center the image
		BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, false);
		BackgroundPosition backgroundPosition = BackgroundPosition.CENTER;

		// Create a BackgroundImage object with the image, repeat option, sizing, and position
		BackgroundImage backgroundImg = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, backgroundPosition, backgroundSize);

		// Create a Background object with the background image
		Background background = new Background(backgroundImg);
		
		Label l = new Label(msg);
		Button yes = new Button("YES");
		yes.setOnAction(e -> {
			answer = true;
			window.close();
		});
		Button no = new Button("NO");
		no.setOnAction(e -> {
			answer = false;
			window.close();
		});
		VBox layout = new VBox(2);
		layout.getChildren().addAll(yes, no);
		layout.setAlignment(Pos.BOTTOM_CENTER);
		layout.setBackground(background); // Set the background for the layout
		Scene alert = new Scene(layout);
		window.setScene(alert);
		window.setMinWidth(500);
		window.setMinHeight(650);
		window.showAndWait();
		return answer;
	}
}
