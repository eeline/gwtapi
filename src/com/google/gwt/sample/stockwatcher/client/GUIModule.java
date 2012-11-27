package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GUIModule {
	  private static int EXERCISE_NAME_ROW = 0;
	  private static int CREATION_DATE_ROW = 1;
	  private static int NOT_SURE_ROW =2;
	  private static int REMOVE_BUTTON_ROW = 3;

	  private VerticalPanel mainPanel = new VerticalPanel();
	  private FlexTable stocksFlexTable = new FlexTable();
	  private HorizontalPanel addPanel = new HorizontalPanel();
	  private Button addStockButton = new Button("Add");
	  private Label lastUpdatedLabel = new Label();
	  private ArrayList<String> stocks = new ArrayList<String>();
	  private Label errorMessageLabel = new Label();
	  private Label errorMessageLabel2 = new Label();
	
	public void moduleLoad(final StockWatcher watcher){
		 // Create table for stock data.
	    stocksFlexTable.setText(0, EXERCISE_NAME_ROW, "Symbol");
	    stocksFlexTable.setText(0, CREATION_DATE_ROW, "Price");
	    stocksFlexTable.setText(0, NOT_SURE_ROW, "null");
	    stocksFlexTable.setText(0, REMOVE_BUTTON_ROW, "Remove");

	    // Add styles to elements in the stock list table.
	    stocksFlexTable.setCellPadding(6);
	    stocksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
	    stocksFlexTable.addStyleName("watchList");
	    stocksFlexTable.getCellFormatter().addStyleName(0, CREATION_DATE_ROW, "watchListNumericColumn");
	    stocksFlexTable.getCellFormatter().addStyleName(0, NOT_SURE_ROW, "watchListNumericColumn");
	    stocksFlexTable.getCellFormatter().addStyleName(0, REMOVE_BUTTON_ROW, "watchListRemoveColumn");

	    // Assemble Add Stock panel.
	    addPanel.add(addStockButton);
	    addPanel.addStyleName("addPanel");

	    // Assemble Main panel.
	    errorMessageLabel.setStyleName("errorMessage");
	    errorMessageLabel.setVisible(false);
	    this.errorMessageLabel2.setStyleName("errorMessage");
	    this.errorMessageLabel2.setVisible(false);

	    mainPanel.add(errorMessageLabel);
	    mainPanel.add(stocksFlexTable);
	    mainPanel.add(addPanel);
	    mainPanel.add(lastUpdatedLabel);

	    // Associate the Main panel with the HTML host page.
	    RootPanel.get("stockList").add(mainPanel);
	    
	    addStockButton.addClickHandler(new ClickHandler() {
	        public void onClick(ClickEvent event) {
	      	  watcher.refreshWatchList();
	        }
	      });
	}
	
	public void errorDisplay(String error, boolean bool){
		if(bool) {
			errorMessageLabel.setText("Error: " + error);
	    	errorMessageLabel.setVisible(true);
		}
		else {
			errorMessageLabel2.setText("Error: " + error);
	    	errorMessageLabel2.setVisible(true);
		}
	}
	
	public void toggleError(boolean bool, boolean boole){
		if(boole)
			errorMessageLabel.setVisible(bool);
		else
			this.errorMessageLabel2.setVisible(bool);
	}
	
	public void timestampUpdate(){
		lastUpdatedLabel.setText("Last update : " + DateTimeFormat.getMediumDateTimeFormat().format(new Date()));
	}
	
	public void addEntity(final String symbol, String date){
		int row = stocksFlexTable.getRowCount();
	    stocks.add(symbol);
	    stocksFlexTable.setText(row, EXERCISE_NAME_ROW, symbol);
	    stocksFlexTable.setText(row, CREATION_DATE_ROW, date);
	    stocksFlexTable.setWidget(row, NOT_SURE_ROW, new Label());
	    stocksFlexTable.getCellFormatter().addStyleName(row, CREATION_DATE_ROW, "watchListNumericColumn");
	    stocksFlexTable.getCellFormatter().addStyleName(row, NOT_SURE_ROW, "watchListNumericColumn");
	    stocksFlexTable.getCellFormatter().addStyleName(row, REMOVE_BUTTON_ROW, "watchListRemoveColumn");

	    // Add a button to remove this stock from the table.
	    Button removeStockButton = new Button("x");
	    removeStockButton.addStyleDependentName("remove");
	    removeStockButton.addClickHandler(new ClickHandler() {
	      public void onClick(ClickEvent event) {
	        int removedIndex = stocks.indexOf(symbol);
	        stocks.remove(removedIndex);
	        stocksFlexTable.removeRow(removedIndex + 1);
	      }
	    });
	    stocksFlexTable.setWidget(row, REMOVE_BUTTON_ROW, removeStockButton);
	}
}
