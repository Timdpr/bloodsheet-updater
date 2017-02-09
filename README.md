# ![pageres](res/icon.ico)
# Bloodsheet Updater Tool

An application which automatically updates Quantum Force's 'Bloodsheet' tool with data from gpro.net.

## Installation

No need! Just download from [this link](http://testmediafirelink.com/) and run BloodsheetUpdater.exe

## Usage

* Enter your GPRO username and password, select the Bloodsheet file you wish to update and hit 'Update'.
* The tool will fetch your current driver and car attributes, as well as the weather details for the next race, then update the relevant cells in your Bloodsheet.

### What it doesn't do

It will not update the track name or the Dry/Wet weather selection boxes. Make sure to do this yourself! The former may come in a later update, but the latter does not seem feasible considering the race weather can be a judgement call.

### Login?

You may be understandably uneasy at having to enter your password! All I can say is the source code is ~500 lines long and freely available above, and the application does not store any data.

## Notes

This is my first public application! There's bound to be plenty bugs and things I've overlooked, please let me know of any problems, comments or suggestions via GPRO....

My code is thoroughly commented, and anyone learning Java, JavaFX, Apache POI, jsoup or HtmlUnit would probably find it handy to go through. If any experienced hands happen to go through it I would love to hear any critiques!

## Licence

Created by Tim Russell and licenced under the GNU General Public Licence