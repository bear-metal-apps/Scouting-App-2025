# Bear Metal Scouting

![Bear Metal Logo](composeApp/src/commonMain/resources/bearmetallogo.jpg)

## Sections
- [Overview](https://github.com/betterbearmetalcode/Scouting-App-2025/edit/main/README.md#overview)
- [Additional Software](https://github.com/betterbearmetalcode/Scouting-App-2025/edit/main/README.md#additional-software) (Provided by Bear Metal)
- [Features to be Implemented](https://github.com/betterbearmetalcode/Scouting-App-2025/edit/main/README.md#features-to-be-implemented)
- [Additional Information](https://github.com/betterbearmetalcode/Scouting-App-2025/edit/main/README.md#additional-information)

## Overview
The Bear Metal Scouting App is an open-source application built using Kotlin and Android Jetpack Compose that enables all FRC teams--no matter their size or budget--to have access to high-quality scouting. Using the app, teams can collect data on other teams during competitions, empowering them to be able to strategically select the best performing teams as partners in later matches, elevating their competitiveness.

Bear Metal has been working diligently on the Bear Metal Scouting App, Scouting Server, Koala. However, some features are still a work in progress. These features are listed in the [Features to be Implemented](https://github.com/betterbearmetalcode/Scouting-App-2025/edit/main/README.md#features-to-be-implemented) section, and will be pushed to this repository ASAP.

**The app is made to be run on Android devices only.** The app changes from year to year to reflect the First Robotics Competition game for each year.

Please note that the Bear Metal Scouting App, Koala, and the Bear Metal Scouting Server is being continuously developed throughout the season by Bear Metal's General Programming Team, and will receieve frequent updates. If you are actively using the Bear Metal Scouting System to collect, transfer, and manage data for your FRC team, it is highly recommended to update/pull from the Bear Metal Scouting App, Koala, and the Bear Metal Scouting Server before each competition.

The Bear Metal Scouting App contains 3 ways of collecting data about teams and alliances: Match Scouting, Strategy Scouting, and Pits Scouting.
### Match Scouting
Bear Metal's Match Scouting collects data on the quantitative match-based data of teams, such as amount of coral scored at each level, algae processed, etc. Match Scouting contains 3 pages that are identical to the 3 stages of the Reefscape game: Auto, Tele, and Endgame.

### Strategy Scouting
Bear Metal's Strategy Scouting collects data on the qualitative data of alliances during matches, ranking alliances from 1 to 3 (1 being the best, 3 being the lowest) on data such as team strategy, driving skill, etc.

### Pits Scouting
Bear Metal's Pits Scouting collects data on other teams' robots themselves, such as drive type, motor type, weight, etc.


## Additional Software
Bear Metal provides additional software that is not present in this repository that enables FRC teams to derive the most value possible out of the Bear Metal Scouting App: Koala, and the Bear Metal Scouting Server.

### Koala
Koala is a data transfer, database manager, and TBA data fetcher rolled into one. Koala enables teams to transfer data from the Bear Metal Scouting App to the Bear Metal Scouting Server with only the need to be connected to the same wifi. No internet needed! For more info on Koala, including how to download and use it with the Bear Metal Scouting App and Scouting Server, visit [this page](https://github.com/betterbearmetalcode/koala).

### Bear Metal Scouting Server
The Bear Metal Scouting Server is an application that processes and visualizes the data delivered to it via Koala from the Bear Metal Scouting App. To learn more about the Scouting Server, including how to download and use it, visit [this page](https://github.com/betterbearmetalcode/scouting-server-kotlin). The Server can be run on Linux and Windows machines.


## Features to be Implemented
Below are the features that are not currently in the Bear Metal Scouting App, but will be committed and pushed to this repository ASAP.

### Transfering Images from App to Server
Transfering the images teams can take in the Pits Scouting page of the app cannot be sent to the server. Once this feature has been implemented, teams will be able to transfer their images through Koala just as they can with all other data. However, the transfer of all other Pits Scouting data can be transfered from the Bear Metal Scouting App to the Scouting Server.


## Additional Information

### Multiplatform info

Currently, Android is the only platform that can run the 2025 version of the Bear Metal Scouting App, but the Scouting App also has multiplatform functionality if needed. This enables other teams to easily make alternative versions of the Bear Metal Scouting App that support the device they desire.

Here is how the current file structure looks like for the Bear Metal Scouting App:

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - `androidMain` is for code that is exclusive to the Android version of the Bear Metal Scouting App.
  - `desktopMain` is for code that is exclusive to the desktop version of the Bear Metal Scouting App. <ins>**This version of the app has not been implemented, and is not intended to be in the future.**</ins>
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
