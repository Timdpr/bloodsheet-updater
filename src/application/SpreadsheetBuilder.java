package application;

import java.io.FileOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SpreadsheetBuilder {
	@FXML
	private Label statusLabel;
	
	private File chosenFile;
	private ArrayList<Integer> tempsArray;
	private ArrayList<Integer> driverArray;
	private ArrayList<Integer> carArray;
	
	public SpreadsheetBuilder(File chosenFile, ArrayList<Integer> tempsArray,
			                  ArrayList<Integer> driverArray, ArrayList<Integer> carArray) {
		this.chosenFile = new File(chosenFile.getAbsolutePath());
		this.tempsArray = tempsArray;
		this.driverArray = driverArray;
		this.carArray = carArray;
	}
	
	public void updateSpreadsheet() {
		try {
		    FileInputStream bloodsheetInputFile = new FileInputStream(this.chosenFile);
		    XSSFWorkbook bloodsheet = new XSSFWorkbook(bloodsheetInputFile);
		    // Update and close input stream:
		    updateRaceDetails(bloodsheet);
		    updateInput(bloodsheet);			
		    bloodsheetInputFile.close();
            // Open FileOutputStream to write updates; close:
            FileOutputStream output_file = new FileOutputStream(this.chosenFile);
            bloodsheet.write(output_file);
            output_file.close();
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			throw new NullPointerException();
		} catch (IndexOutOfBoundsException ioobe) {
		    ioobe.printStackTrace();
		    throw new IndexOutOfBoundsException();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void updateRaceDetails(XSSFWorkbook bloodsheet) {
		XSSFSheet raceDetailsSheet = bloodsheet.getSheetAt(3);
		Cell cell = null;
		// Update temperatures:
		int index = 0;
		for (int i = 3; i < 13; i++) {
			cell = raceDetailsSheet.getRow(i).getCell(1);
			cell.setCellValue(this.tempsArray.get(index));
			index++;
		} // Update humidities:
		for (int i = 3; i < 13; i++) {
			cell = raceDetailsSheet.getRow(i).getCell(2);
			cell.setCellValue(this.tempsArray.get(index));
			index++;
		}
	}
	
	public void updateInput(XSSFWorkbook bloodsheet) {
		XSSFSheet inputSheet = bloodsheet.getSheetAt(2);
		Cell cell = null;
		// Update driver values:
		int index = 0;
		for (int i = 3; i < 15; i++) {
			cell = inputSheet.getRow(i).getCell(1);
			cell.setCellValue(this.driverArray.get(index));
			index++;
		} // Update car level values:
		index = 0;
		for (int i = 3; i < 14; i++) {
			cell = inputSheet.getRow(i).getCell(6);
			cell.setCellValue(this.carArray.get(index));
			index++;
		} // Update car wear values:
		for (int i = 3; i < 14; i++) {
			cell = inputSheet.getRow(i).getCell(7);
			cell.setCellValue(this.carArray.get(index));
			index++;
		}	
	}
}