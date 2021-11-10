# Customer state service

Service purpose to validate and generate report with help of provided CSV or XML files with transactions.

### How to run locally

Execute from the root in command prompt 

* mvn clean package
* mvn spring-boot:run

### Docker

Dockerfile present in root folder if it's more convenient to run application via Docker after packaging

### API

    /localhost:8080/{fileType}

Where *fileType* is XML or CSV extension for supported files to upload
In addition it's required to provide *Request body* as *form-data* with key "file" and "value" file to upload 

Response consist of report for successful and invalid transactions list. Example:

```json
{
  "uploadedTransferReferenceNumbers": [
    "108366",
    "104254",
    "198725",
    "173553",
    "117057",
    "176709",
    "192105",
    "190018"
  ],
  "invalidTransfers": [
    {
      "transactionReference": "196839",
      "reason": "The end balance is invalid. End balance is not equal to start balance and applied mutation"
    },
    {
      "transactionReference": "108338",
      "reason": "The end balance is invalid. End balance is not equal to start balance and applied mutation"
    }
  ]
}
```

