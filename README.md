# Openshift AI Release Plan SmartSheet Automation

## Overview
Openshift AI Release Plan SmartSheet Automation is created to make planning the next six month's releases plan easier.

## Features
- **Data Retrieval**: Fetches the current release end date from SmartSheet provided.
- **Data Manipulation**: Processes date and calculated next six month's release plan.
- **Data Output**: Writes processed data in provided smartsheet.

## Installation
To use this project, follow these steps:
1. Fork and Clone the repository: `git clone https://github.com/moulalis/smartsheet-automation.git`
2. Install dependencies: `mvn clean install`

## Configuration
Before running the project, ensure you have configured the following:
- API key for SmartSheet API (details in `config.properties`).

## Usage
To use this project, run the following commands:
1. `cd smartsheet-automation`
2. `java -jar target/smartsheet-automation-0.0.1-SNAPSHOT.jar`

## Code Structure
The project structure is as follows:
- `src/`: Contains the source code.
- `target/`: Generated output directory.
- `config.properties`: Configuration file.

## APIs/External Services Used
This project uses the SmartSheet API/Java SDK for data retrieval and manipulation.

## Troubleshooting
### Issue: API key authentication fails
- **Solution**: Check if the API key is correctly configured in `config.properties` and check for the log in the github actions.

## Contributions
Contributions are welcome! Follow these steps to contribute:
1. Fork the repository.
2. Create a new branch: `git checkout -b feature/your-feature`.
3. Commit your changes: `git commit -am 'Add new feature'`.
4. Push to the branch: `git push origin feature/your-feature`.
5. Submit a pull request.


## References
- SmartSheet API Documentation: [link-to-docs](https://docs.google.com/document/d/1APw7ehpiE35ZuIiMq6sLbt0JxROpt0Vn6spN7gSvaKg/edit#heading=h.8jjfikvvcg5u)