// IMPORTS
// These are some classes that may be useful for completing the project.
// You may have to add others.

import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 * The main class for Program100. Program100 constructs the JavaFX window and
 * handles interactions with the dynamic components contained therein.
 */
public class Program100 extends Application {
    // INSTANCE VARIABLES
    // These variables are included to get you started.
    private Stage stage = null;
    private BorderPane borderPane = new BorderPane( );

	private WebEngine webEngine = new WebEngine();
    private TextField statusbar = null;

    private Stage help = null;


    // HELPER METHODS
    /**
     * Retrieves the value of a command line argument specified by the index.
     *
     * @param index - position of the argument in the args list.
     * @return The value of the command line argument.
     */
    private String getParameter( int index ) {
        Parameters params = getParameters();
        List<String> parameters = params.getRaw( );
        return !parameters.isEmpty( ) ? parameters.get(index) : "https://google.com";
    }

    /**
     * Creates a WebView which handles mouse and some keyboard events, and
     * manages scrolling automatically, so there's no need to put it into a ScrollPane.
     * The associated WebEngine is created automatically at construction time.
     *
     * @return browser - a WebView container for the WebEngine.
     */
    private WebView makeHtmlView( ) {
		WebView view = new WebView();
        webEngine = view.getEngine( );
        return view;
    }

    /**
     * Generates the status bar layout and text field.
     *
     * @return statusbarPane - the HBox layout that contains the statusbar.
     */
    private HBox makeStatusBar( ) {
        HBox statusbarPane = new HBox( );
        statusbarPane.setPadding(new Insets( 5, 4, 5, 4 ) );
        statusbarPane.setSpacing( 10 );
        statusbarPane.setStyle( "-fx-background-color: #336699;" );
        statusbar = new TextField( );
        HBox.setHgrow( statusbar, Priority.ALWAYS );
        statusbarPane.getChildren().addAll( statusbar );
        return statusbarPane;
    }

    // REQUIRED METHODS
    /**
     * The main entry point for all JavaFX applications. The start method is
     * called after the init method has returned, and after the system is ready
     * for the application to begin running.
     *
     * NOTE: This method is called on the JavaFX Application Thread.
     *
     * @param primaryStage - the primary stage for this application, onto which
     * the application scene can be set.
     */
    @Override
    public void start( Stage primaryStage ) {
		Scene scene = new Scene( borderPane , 800, 800);
		primaryStage.setScene( scene );
		primaryStage.show( );
		createHelpWindow();

		borderPane.setCenter( makeHtmlView( ) );
    	borderPane.setTop( getHBoxTop( ) );
    	borderPane.setBottom( makeStatusBar( ) );

    	webEngine.load(getParameter(0));


    	webEngine.setOnStatusChanged( e -> {
    		if ( e.getData() != null ) {
    			statusbar.setText(e.getData());
			}

			if ( webEngine.getTitle() != null) {
    			primaryStage.setTitle(webEngine.getTitle());
			} else {
    			primaryStage.setTitle(webEngine.getLocation());
			}
		});





    }

    private HBox getHBoxTop() {
    	HBox hBoxTop  = new HBox( 15  );
    	hBoxTop.setPadding( new Insets ( 5,4,5,4 ) );

    	// text field
    	TextField address = new TextField( "enter website URL" );
    	address.setEditable( true );
    	HBox.setHgrow( address, Priority.ALWAYS );
    	address.setOnAction( e -> webEngine.load("https://" + address.getText( ) ) );

    	// buttons
    	Button btBack = new Button( "<" );
    	Button btForward = new Button( ">" );
    	Button btHelp = new Button ( "?" );

    	// button actions
		btBack.setOnAction( e -> {
			if (webEngine.getHistory().getCurrentIndex() > 0) {
				webEngine.getHistory().go(-1);
			}

		});
		btForward.setOnAction( e -> {
			if (webEngine.getHistory().getCurrentIndex() < webEngine.getHistory().getEntries().size() - 1) {
				webEngine.getHistory().go(1);
			}
		} );
		btHelp.setOnAction( e -> help.show() );

    	// content of HBox
    	hBoxTop.getChildren().add( btBack );
		hBoxTop.getChildren().add( btForward );
    	hBoxTop.getChildren().add( address );
		hBoxTop.getChildren().add( btHelp );

    	return hBoxTop;
	}
	private void createHelpWindow () {

    	VBox vBox = new VBox();
		Text url = new Text( " please enter a url without https//");
		vBox.getChildren().add(url);
		Text back = new Text( "Back Button < : will take you to your previous page ");
		vBox.getChildren().add(back);
		Text next = new Text( "Next Button > : will take you to the page you had just retuned from");
		vBox.getChildren().add(next);
		Text statusBar = new Text( " displays the URL of the link your mouse is hovering over");
		vBox.getChildren().add(statusBar);

		help = new Stage();
		help.setTitle("Help");
		help.setScene(new Scene(vBox, 400, 400));
	}


    /**
     * The main( ) method is ignored in JavaFX applications.
     * main( ) serves only as fallback in case the application is launched
     * as a regular Java application, e.g., in IDEs with limited FX
     * support.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
