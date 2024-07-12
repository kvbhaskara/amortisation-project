# Amortisation Service API

## Overview

The Amortisation Service API is a RESTful web service that provides functionalities to create, retrieve, and list amortisation schedules for loan details. It validates the loan details, computes the amortisation schedule, and handles various exceptions effectively.

## Features

- **Create amortisation Schedule**: Create a new amortisation schedule based on loan details.
- **Retrieve amortisation Schedule**: Retrieve a specific amortisation schedule by its ID.
- **List amortisation Schedules**: List all amortisation schedules.

## Technologies Used

- **Java 11**
- **Spring Boot**
- **Spring Data JPA**
- **H2 Database**
- **Spring Web**
- **Spring Validation**
- **Swagger/OpenAPI**
- **JUnit & Mockito**

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6.0 or higher

### Installation

1. Clone the repository:

    ```bash
    git clone https://github.com/your-repository/amortisation-service.git
    cd amortisation-service
    ```

2. Build the project:

    ```bash
    mvn clean install
    ```

3. Run the application:

    ```bash
    mvn spring-boot:run
    ```

4. The application will start on `http://localhost:8080`.

### API Endpoints

- **Create amortisation Schedule**

    - **URL**: `/api/schedules`
    - **Method**: `POST`
    - **Request Body**:

      ```json
      {
        "assetCost": 25000,
        "deposit": 5000,
        "annualInterestRate": 5.5,
        "numberOfPayments": 36,
        "balloonPayment": 1000
      }
      ```

    - **Response**: `200 OK`

      ```json
      {
        "id": 1,
        "loanDetails": {
          "assetCost": 25000,
          "deposit": 5000,
          "annualInterestRate": 5.5,
          "numberOfPayments": 36,
          "balloonPayment": 1000
        },
        "scheduleEntries": [
          {
            "paymentNumber": 1,
            "monthlyRepayment": 599.55,
            "principal": 445.78,
            "interest": 153.77,
            "balance": 20454.22
          },
          ...
        ],
        "monthlyRepayment": 599.55,
        "totalInterestDue": 1360.75,
        "totalPaymentsDue": 23610.75
      }
      ```

- **Retrieve amortisation Schedule**

    - **URL**: `/api/schedules/{id}`
    - **Method**: `GET`
    - **Response**: `200 OK`

      ```json
      {
        "id": 1,
        "loanDetails": {
          "assetCost": 25000,
          "deposit": 5000,
          "annualInterestRate": 5.5,
          "numberOfPayments": 36,
          "balloonPayment": 1000
        },
        "scheduleEntries": [
          {
            "paymentNumber": 1,
            "monthlyRepayment": 599.55,
            "principal": 445.78,
            "interest": 153.77,
            "balance": 20454.22
          },
          ...
        ],
        "monthlyRepayment": 599.55,
        "totalInterestDue": 1360.75,
        "totalPaymentsDue": 23610.75
      }
      ```

- **List amortisation Schedules**

    - **URL**: `/api/schedules`
    - **Method**: `GET`
    - **Response**: `200 OK`

      ```json
      [
        {
          "id": 1,
          "loanDetails": {
            "assetCost": 25000,
            "deposit": 5000,
            "annualInterestRate": 5.5,
            "numberOfPayments": 36,
            "balloonPayment": 1000
          },
          "scheduleEntries": [
            {
              "paymentNumber": 1,
              "monthlyRepayment": 599.55,
              "principal": 445.78,
              "interest": 153.77,
              "balance": 20454.22
            },
            ...
          ],
          "monthlyRepayment": 599.55,
          "totalInterestDue": 1360.75,
          "totalPaymentsDue": 23610.75
        },
        ...
      ]
      ```

## Error Handling

### Custom Exceptions

- **InvalidLoanDetailsException**
    - **Description**: Thrown when loan details are invalid.
    - **Response**: `400 Bad Request`

      ```json
      {
        "timestamp": "2024-07-12T10:15:30",
        "status": 400,
        "error": "Bad Request",
        "message": "Invalid loan details provided: {error message}",
        "path": "/api/schedules"
      }
      ```

- **ResourceNotFoundException**
    - **Description**: Thrown when a requested resource is not found.
    - **Response**: `404 Not Found`

      ```json
      {
        "timestamp": "2024-07-12T10:15:30",
        "status": 404,
        "error": "Not Found",
        "message": "Schedule not found with id {id}",
        "path": "/api/schedules"
      }
      ```

- **General Exception**
    - **Description**: Handles all other exceptions.
    - **Response**: `500 Internal Server Error`

      ```json
      {
        "timestamp": "2024-07-12T10:15:30",
        "status": 500,
        "error": "Internal Server Error",
        "message": "{error message}",
        "path": "/api/schedules"
      }
      ```

## Testing

### Running Tests

- Run unit and integration tests using Maven:

    ```bash
    mvn test
    ```

### Test Cases

- **Positive Test Cases**:
    - Validate successful creation of amortisation schedules.
    - Verify retrieval of existing amortisation schedules.
    - Ensure the listing of all amortisation schedules.

- **Negative Test Cases**:
    - Test creation with invalid loan details.
    - Retrieve a non-existing amortisation schedule.
    - Validate proper error messages and HTTP status codes.

## Contributing

Contributions are welcome! Please create a pull request with detailed information on the changes.

## License

This project is licensed under the MIT License.
