# Openshift AI Release Plan SmartSheet Automation

## Overview
Openshift AI Release Plan SmartSheet Automation is created to make planning the next six month's releases plan easier.

## Features
- **Data Retrieval**: Fetches the current release end date from SmartSheet provided.
- **Data Manipulation**: Processes date and calculated next six month's release plan.
- **Data Output**: Writes processed data in provided smartsheet.

## Installation
To use this project, follow these steps:(Assuming you are using linux OS, for other operating system follow OS level installation.)
1. Install JDK 11 `sudo yum install java-11-openjdk-devel`
2. Install Maven 3 `sudo yum install maven`
3. Fork and Clone the repository: `git clone git@github.com:red-hat-data-services/product-pages-generator.git`
4. `cd product-pages-generator`
5. Install dependencies: `mvn clean install`
6. To run Product page API you need to install python3: `sudo dnf install python3` 

## Configuration
Before running the project, ensure you have configured the following:
- You need to add API key/access token for SmartSheet API (details in `config.properties`).

## Usage
To use this project, run the following commands:
1. `cd product-pages-generator`
2. `java -jar target/smartsheet-automation-0.0.1-SNAPSHOT.jar`

## Code Structure
The project structure is as follows:
- `src/`: Contains the source code.
- `target/`: Generated output directory.
- `config.properties`: Configuration file.
- `python/ProductPageAPI.py` : Is a product page API which will update the smartsheet information to the product page.

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
- https://smartsheet.github.io/smartsheet-java-sdk/apidocs/
- https://github.com/smartsheet-platform/smartsheet-java-sdk
