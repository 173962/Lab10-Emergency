package it.polito.tdp.emergency;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.emergency.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class EmergencyController {

	Model model;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button btn_aggiungi;

	@FXML
	private Button btn_start;

	@FXML
	private Label lbl_add;

	@FXML
	private TextField txt_dottore;

	@FXML
	private RadioButton radio_assistente;

	@FXML
	private RadioButton radio_dottore;

	@FXML
	private ToggleGroup tipo;

	@FXML
	private TextArea txt_output;

	@FXML
	private TextField txt_sfasamento;

	@FXML
	void aggiungiDottore(ActionEvent event) {
		
		lbl_add.setText("");
		try {
			if (radio_dottore.isSelected()) {
				model.addDottore(txt_dottore.getText(), Integer.parseInt(txt_sfasamento.getText()));
			} else {
				model.addAssistente(txt_dottore.getText(), Integer.parseInt(txt_sfasamento.getText()));
			}
			
			lbl_add.setText(txt_dottore.getText() + " aggiunto");
			txt_dottore.setText("");
			
		} catch (NumberFormatException n) {
			txt_output.setText("Error, Number Format Exception: " + n.getMessage());
		}
	}

	@FXML
	void startSimulation(ActionEvent event) {
		txt_output.clear();
		String out = model.startSimulation();
		txt_output.appendText(out);
	}

	@FXML
	void initialize() {
		assert btn_aggiungi != null : "fx:id=\"btn_aggiungi\" was not injected: check your FXML file 'es12.1.fxml'.";
		assert btn_start != null : "fx:id=\"btn_start\" was not injected: check your FXML file 'es12.1.fxml'.";
		assert lbl_add != null : "fx:id=\"lbl_add\" was not injected: check your FXML file 'es12.1.fxml'.";
		assert radio_assistente != null : "fx:id=\"radio_assistente\" was not injected: check your FXML file 'es12.1.fxml'.";
		assert radio_dottore != null : "fx:id=\"radio_dottore\" was not injected: check your FXML file 'es12.1.fxml'.";
		assert tipo != null : "fx:id=\"tipo\" was not injected: check your FXML file 'es12.1.fxml'.";
		assert txt_dottore != null : "fx:id=\"txt_dottore\" was not injected: check your FXML file 'es12.1.fxml'.";
		assert txt_output != null : "fx:id=\"txt_output\" was not injected: check your FXML file 'es12.1.fxml'.";
		assert txt_sfasamento != null : "fx:id=\"txt_sfasamento\" was not injected: check your FXML file 'es12.1.fxml'.";
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

}
