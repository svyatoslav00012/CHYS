package resources.fonts;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Created by Svyatoslav on 04.12.2016.
 */

public class MyFonts {

    private static ArrayList<Label> fonts = new ArrayList<>();

    public static void loadFonts() {
        //used otf
        loadOTF("Drina"); // Drina						[Interface]
        //used ttf
        loadTTF("HansHand"); // HansHand					[table]
        loadTTF("StudioScriptCTT"); // Studio Script CTT	[text-fields]
        loadTTF("Favorit_Grotesk"); // Favorit Grotesk	[titles]
        loadTTF("Comic_Sans_MS"); // Comic Sans MS		[warning labels]
        loadTTF("Old_Comedy"); // Old Comedy				[settings], [tooltips]
        loadTTF("OlgaCTT"); // OlgaCTT					[accordion]
        loadTTF("Chance"); // Chance						[table-view placeholder]
        loadTTF("Cansellarist"); // Cansellarist			[contextMenu]
        loadTTF("LC_Chalk"); // LC Chalk					[notifications time]!!! Нет украинского !!!

        //unused (reserve)
        //otf
        loadOTF("Amphi_CYR"); // Amphi CYR  				very specific handwritten font
        loadOTF("KapelkaNew"); //KapelkaNew
        //ttf
        loadTTF("Carolina"); // Carolina
        loadTTF("Corrida"); // Corrida

    }

    private static int num = 1;

    private static void loadOTF(String name) {
        Font f = Font.loadFont(MyFonts.class.getResourceAsStream(
                "/resources/fonts/otf/" + name.replaceAll("\\.otf", "") + ".otf"), 30);
        Label label = new Label((num++) + ")" + f.getName() + " буквы ыъїі-ЫЪЇІ");
        label.setFont(f);
        fonts.add(label);
    }

    private static void loadTTF(String name) {
        Font f = Font.loadFont(MyFonts.class.getResourceAsStream(
                "/resources/fonts/ttf/" + name.replaceAll("\\.ttf", "") + ".ttf"), 30);
        Label label = new Label((num++) + ") " + f.getName() + " буквы ыъїі-ЫЪЇІ");
        label.setFont(f);
        fonts.add(label);
    }

    public static void showFonts() {
        Stage fontStage = new Stage();
        VBox vBox = new VBox(10);
        vBox.setStyle("-fx-font-size: 25px;");
        vBox.getChildren().addAll(fonts);
        fontStage.setScene(new Scene(new ScrollPane(vBox), 500, 500));
        fontStage.show();
    }
}

// ШРИФТЫ КОТОРЫЕ НЕ ПОДХОДЯТ
//
//loadOTF("Euroscript_Pro"); // Euroscript Pro							!!! Нет украинского !!!
//loadTTF("m_Brody"); // m_Brody										!!! Нет украинского !!!
//loadTTF("NinaCTT"); // NinaCTT									    !!! Нет украинского !!!
//loadTTF("BetinaScriptC"); // BetinaScriptC							!!! don't work !!!
//loadTTF("Cassandra"); // Cassandra									!!! Нету Заглавных Ы, Ъ !!!
//loadTTF("Classica_One"); // Classica One								!!! Нету Заглавных Ы, Ъ !!!
//loadTTF("Eskal_Font4You"); // Eskal Font4You							!!! Нету заглавного Ъ !!!	panewritten
//loadTTF("PF_DaVinci_Script_Pro_Inked"); // PFDaVinciScriptPro-Inked	!!! Don't work with all !!!
//loadTTF("Prospect"); // Prospect										!!! Нет украинского  !!!	nice Soviet font
//loadTTF("Rupster_Script_Free"); // Rupster Script Free				!!! Нет украинского  !!!	(need size++) +looks good