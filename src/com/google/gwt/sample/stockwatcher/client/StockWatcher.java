package com.google.gwt.sample.stockwatcher.client;

import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Timer;


public class StockWatcher implements EntryPoint {

  private static final int REFRESH_INTERVAL = 5000; // ms
  private static final String BASE_URL = "http://www.khanacademy.org";
  private static String CALLBACK_URL = "&callback=";
  Logger log = Logger.getLogger(StockWatcher.class.toString());
  
  private GUIModule moduleLoad = new GUIModule();

  private int jsonRequestID = 0;
  
  public void onModuleLoad() {
	this.moduleLoad.moduleLoad(this);
    // Setup timer to refresh list automatically.
    Timer refreshTimer = new Timer() {
      @Override
      public void run() {
        refreshWatchList();
      }
    };
    refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
    // Listen for mouse events on the Add button.
  }

  /**
   * Add stock to FlexTable. Executed when the user clicks the addStockButton or
   * presses enter in the newSymbolTextBox.
 * @param date 
   */
  private void addStock(final String symbol, String date) {
	  	this.moduleLoad.addEntity(symbol, date);
	    refreshWatchList();
  }

  /**
   * Generate random stock prices.
   */
  void refreshWatchList() {
    String url = BASE_URL;
    url += KhanAPICommandList.TOPIC_TREE;
    url = URL.encode(url) +  CALLBACK_URL;
    this.moduleLoad.errorDisplay("URL before sending to getJSON: " + url, false);
    getJSON(this.jsonRequestID++, url, this);
  }

  public void handleJsonResponse(JavaScriptObject jso) {
    if (jso == null) {
      displayError("Couldn't retrieve JSON in handleJsonResponse.");
      return;
    }
    updateTable(asArrayOfStockData (jso));
  }
  
/**
   * Update the Price and Change fields all the rows in the stock table.
   *
   * @param strings Stock data for all rows.
   */
  private void updateTable(JsArray<KhanAccess> strings) {
    for (int i = 0; i < strings.length(); i++) {
      updateTable(strings.get(i));
    }
    this.moduleLoad.timestampUpdate();

    // Clear any errors.
    this.moduleLoad.toggleError(false, false);
  }

  /**
   * Update a single row in the stock table.
   *
   * @param entry Stock data for a single row.
   */
  private void updateTable(KhanAccess entry) {
    // Format the data in the Price and Change fields.
    String name = entry.getDisplayName();
    String date = entry.getCreationDate();
    // Populate the Price and Change fields with new data.
    addStock(name, date);
  }

  /**
   * If can't get JSON, display error message.
   * @param error
   */
  private void displayError(String error) {
	  moduleLoad.errorDisplay(error, true);
  }

/*
 * JNI beyond here
 */


/**
 * Convert the string of JSON into JavaScript object.
 */
	private final native JsArray<KhanAccess> asArrayOfStockData(JavaScriptObject jso) /*-{
	  return jso;
	}-*/;
	
	public native static void getJSON(int requestID, String url, StockWatcher handler) /*-{
		var callback = "callback" + requestID; 
		var script = document.createElement("script");
		script.setAttribute("src", url+callback);
		script.setAttribute("type", "text/javascript");
		
		window[callback] = function(jsonObj){
			handler.@com.google.gwt.sample.stockwatcher.client.StockWatcher::handleJsonResponse(Lcom/google/gwt/core/client/JavaScriptObject;)(jsonObj);
			window[callback + "done"] = true;
		}
		
	setTimeout(function() {
	 if (!window[callback + "done"]) {
	   handler.@com.google.gwt.sample.stockwatcher.client.StockWatcher::handleJsonResponse(Lcom/google/gwt/core/client/JavaScriptObject;)(null);
	 }
	 document.body.removeChild(script);
	 delete window[callback];
	 delete window[callback + "done"];
	}, 1000);
	
	document.body.appendChild(script);
	}-*/;
}