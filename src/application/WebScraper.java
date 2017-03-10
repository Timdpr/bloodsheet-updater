package application;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class WebScraper {
	private final WebClient WEB_CLIENT = new WebClient(BrowserVersion.CHROME);
	private final String username;
    private final String password;
    
    WebScraper(String username, String password) {
    	this.username = username;
    	this.password = password;
    	// Retrieves our WebClient's cookie manager and enables cookies,
    	// this is what allows our login session to persist:
    	WEB_CLIENT.getCookieManager().setCookiesEnabled(true);
    }
    
    public void login() {
    	String loginURL = "http://gpro.net/gb/gpro.asp";
    	try {
    		// Create an HtmlPage and get the login page.
            HtmlPage loginPage = WEB_CLIENT.getPage(loginURL);
            // Create an HtmlForm by locating the form that pertains to logging in.
            HtmlForm loginForm = loginPage.getFirstByXPath("//form[@id='Form1']");
            // Modify the form. getInputByName method looks for <input> tag with some name attribute
            // We then set the (text field) value to be our username/password.
            loginForm.getInputByName("textLogin").setValueAttribute(this.username);
            loginForm.getInputByName("textPassword").setValueAttribute(this.password);
            // Find submit button, click it.
            loginForm.getInputByName("LogonFake").click();
    	} catch (FailingHttpStatusCodeException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * @return HTML response for some URL
     * Used to navigate to page after login
     */
    public String get(String URL) {
        try {
            return WEB_CLIENT.getPage(URL).getWebResponse().getContentAsString();
        } catch (FailingHttpStatusCodeException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } return null;
    }
    
    /**
     * @return Array of the 10 temps and 10 hums (in that order) for next race
     */
    public ArrayList<Integer> getTempsAndHumidity(WebScraper client) {
    	try {
    	   	// Navigate to Qualify page:
        	String page = client.get("http://gpro.net/gb/Qualify.asp");
        	// Select all <td class="center"> elements:
    		Elements e = Jsoup.parse(page).select("td.center");
        	// Search for elements within tds above containing "Temp" (& humidity),
        	// add them to relevantElements string:
        	String relevantElements = "";
        	for (Element part : e) {
        		String temp = part.getElementsContainingOwnText("Temp").toString();
        		relevantElements += temp;
        	}
    		return getOrderedTempsArrayFromString(relevantElements);
    		
    	} catch (IllegalArgumentException e) {
    		throw new IllegalArgumentException();
    	}
    }
    
    /**
     * @return Array of the 12 driver values
     */
    public ArrayList<Integer> getDriverValues(WebScraper client) {
    	String page = client.get("http://gpro.net/gb/TrainingSession.asp");
    	// Find td elements within table.squashed element:
    	Elements e = Jsoup.parse(page).select("table.squashed");
    	e = e.select("td");
    	// Build relevantElements string only with td elements containing the
    	// driver attributes we want via (complex!) regex:
    	String relevantElements = "";
    	for (Element part : e) {
    		String temp = part.toString();
    		if (temp.matches("<td>\\d\\d\\d<\\/td>|<td>\\d\\d<\\/td>|"
    				       + "<td>\\d<\\/td>|<td>\\d\\d\\d&nbsp;<\\/td>|"
    				       + "<td>\\d\\d&nbsp;<\\/td>|<td>\\d&nbsp;<\\/td>")) {
    			relevantElements += temp;
    		}
    	} return getOrderedDriverArrayFromString(relevantElements);
    }
    
    /**
     * @return Array of the 11 car level and 11 car wear values (in that order)
     */
    public ArrayList<Integer> getCarValues(WebScraper client) {
    	String page = client.get("http://gpro.net/gb/UpdateCar.asp");
    	Elements e = Jsoup.parse(page).select("table.styled");
    	e = e.select("td[align=center]");
    	e = e.select("*:not([id])"); // Select everything that doesn't have an id
    	
    	String relevantElements = "";
    	for (Element part : e) {
    		String temp = part.toString();
    		if ( temp.contains("td align") && (!(temp.contains("width"))) ) {
    			relevantElements += temp;
    		}
    	} return getOrderedCarArrayFromString(relevantElements);
    }
    
    public ArrayList<Integer> getOrderedTempsArrayFromString(String input) {
    	// Gets rid of rain probabilities:
		input = input.replaceAll("probability: \\d\\d%-\\d\\d%|probability: \\d%-\\d\\d%|"
				               + "probability: \\d%-\\d%|probability(...)%", "");
        // Gets rid of all but required integers, ° and %:
		input = input.replaceAll("[a-zA-Z=<>\\\"\\/ :.-]", "");
		
        String[] items = input.split("°|%");
        ArrayList<Integer> tempArray = new ArrayList<Integer>();
        tempArray = stringListToIntegerArray(items);
        
        try { // Sorts array into temps followed by hums:
        	ArrayList<Integer> orderedArray = new ArrayList<Integer>(Arrays.asList(
        	    // Temperatures:
        	    tempArray.get(0), tempArray.get(2),
        	    tempArray.get(4), tempArray.get(5),
        	    tempArray.get(8), tempArray.get(9),
        	    tempArray.get(12), tempArray.get(13),
        	    tempArray.get(16), tempArray.get(17),
        	    // Humidities:
        	    tempArray.get(1), tempArray.get(3),
        	    tempArray.get(6), tempArray.get(7),
        	    tempArray.get(10), tempArray.get(11),
        	    tempArray.get(14), tempArray.get(15),
        	    tempArray.get(18), tempArray.get(19)
        	    )); // Silly but quick solution!
        	
        	return orderedArray;
        	
        } catch (IndexOutOfBoundsException e) {
        	e.printStackTrace();
        	throw new IndexOutOfBoundsException();
        }
    }
    
    public ArrayList<Integer> getOrderedDriverArrayFromString(String input) {
    	// Put driver values only into 'items' list:
    	input = input.replaceAll("[nbsp&;]|</td>", "");
    	String[] items = input.split("<td>");
        // Convert String[] to ArrayList<Integer>:
    	ArrayList<Integer> driverArray = new ArrayList<Integer>();
        driverArray = stringListToIntegerArray(items);
        // Should remove only stray integer, 'Races Left':
        driverArray.remove(12);
        
        return driverArray;
    }
    
    public ArrayList<Integer> getOrderedCarArrayFromString(String input) {
    	// Put car values only into 'items' list:
    	input = input.replaceAll("<td align=\"center\">|%|<font color=\"orange\">|</font>", "");
    	String[] items = input.split("</td>");
    	
    	ArrayList<Integer> carArray = new ArrayList<Integer>();
        carArray = stringListToIntegerArray(items);
        carArray.remove(0); // Remove car character points values
        carArray.remove(0);
        carArray.remove(0);
        
        // Sort array into levels first then wear, 1-2-1-2 to 1-1-2-2
        ArrayList<Integer> tempArray = new ArrayList<Integer>();
        for (int x = 1; x < 12; x++) {
        	tempArray.add(carArray.remove(x));
        } carArray.addAll(tempArray);
        
    	return carArray;
    }
    
    /**
     * @param String[]
     * @return Input as ArrayList<Integer>
     */
    public ArrayList<Integer> stringListToIntegerArray(String[] list) {
    	ArrayList<Integer> tempArray = new ArrayList<Integer>();
        for (String s : list) {
        	if (!(s.isEmpty())) {
        	    tempArray.add(Integer.parseInt(s));
        	}
        } return tempArray;
    }
}