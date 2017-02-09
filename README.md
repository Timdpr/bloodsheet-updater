# ![pageres](res/icon.ico)
# Bloodsheet Updater Tool

An application which automatically updates Quantum Force's 'Bloodsheet' tool with data from gpro.net.

## Installation

No need! Just download from **[this link](http://testmediafirelink.com/)** and run BloodsheetUpdater.exe

## Usage

* Enter your GPRO username and password, select the Bloodsheet file you wish to update and hit 'Update'.
* The tool will fetch your current driver and car attributes, as well as the weather details for the next race, then update the relevant cells in your Bloodsheet.

### What it doesn't do

It will not update the track name or the Dry/Wet weather selection boxes. Make sure to do this yourself! The former may come in a later update, but the latter does not seem feasible considering the race weather can be a judgement call.

It also assumes you have not placed any new sheets before 'Race Details', and the driver, car and weather cells are all in their usual places. (Correct order is [UNSHEET] - Welcome - Input - Race Details)

### Login?

You may be understandably uneasy at having to enter your password! All I can say is the source code is ~500 lines long and freely available above, and the application does not store any data.

## Notes

* This is my first public application! There's bound to be plenty bugs and things I've overlooked, please let me know of any problems, comments or suggestions via GPRO....

* As such, I recommend making a backup of your bloodsheet before using this tool, and checking it is giving you the correct values - though it is much more likely to fail completely than to update incorrectly.

* My code is thoroughly commented, and anyone learning Java, JavaFX, Apache POI, jsoup or HtmlUnit would probably find it handy to go through. If any experienced hands happen to go through it I would love to hear any critiques!

## Built with

* [JavaFX](docs.oracle.com/javafx/)
* [HtmlUnit](http://htmlunit.sourceforge.net/)
* [jsoup](https://jsoup.org/)
* [Apache POI](https://poi.apache.org/)
* [Launch4j](http://launch4j.sourceforge.net/)

## Licence

Created by Tim Russell and licenced under the GNU General Public Licence