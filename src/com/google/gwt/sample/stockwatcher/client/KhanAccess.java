package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.core.client.JavaScriptObject;

class KhanAccess extends JavaScriptObject {                              // [1]
  // Overlay types always have protected, zero argument constructors.
  protected KhanAccess() {}                                              // [2]

  // JSNI methods to get stock data.
  public final native String getDisplayName() /*-{ return this.title; }-*/; // [3]
  public final native String getCreationDate() /*-{ return this.creation_date; }-*/;

}
