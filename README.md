# Todo Summary Assistant

A full-stack application to manage personal to-do items, summarize pending tasks using Cohere LLM, and send the summary to a Slack channel.

## Table of Contents

* [Features](#features)
* [Tech Stack](#tech-stack)
* [Setup Instructions](#setup-instructions)
    * [Prerequisites](#prerequisites)
    * [Backend Setup](#backend-setup)
    * [Frontend Setup](#frontend-setup)
* [LLM (Cohere) Setup](#llm-cohere-setup)
* [Slack Integration Setup](#slack-integration-setup)
* [Design/Architecture Decisions](#designarchitecture-decisions)


## Features

* **Create, Edit, Delete To-Do Items:** Full CRUD operations for personal to-do items.
* **View To-Do List:** Display current to-do items with their status.
* **Summarize Pending To-Dos:** Utilizes Cohere LLM to generate a concise summary of all pending to-do items.
* **Send Summary to Slack:** Automatically posts the generated summary to a configured Slack channel using Incoming Webhooks.
* **Notifications:** Provides success/failure messages for Slack operations.

## Tech Stack

* **Frontend:** HTML, CSS, Javascript, React, Axios(for API calls), 
* **Backend:** Spring Boot (Java 17+), Maven
* **Database:** MySQL (via Spring Data JPA and Hibernate)
* **LLM:** Cohere API
* **Messaging:** Slack Incoming Webhooks
* **HTTP Client:** OkHttp (for Cohere and Slack API calls in backend)

## Setup Instructions

### Prerequisites

* Java Development Kit (JDK) 17 or higher
* Node.js and npm (or yarn)
* MySQL Server running locally or accessible remotely
* A Cohere API Key
* A Slack Workspace and an Incoming Webhook URL

### Backend Setup

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/your-username/todo-summary-assistant.git](https://github.com/your-username/todo-summary-assistant.git)
    cd todo-summary-assistant/backend
    ```
2.  **Configure environment variables:**
    Copy `Backend/todo-summary-assistant/.env.example` to `.env` in the backend directory
    and set your database password, Cohere API key, and Slack webhook URL there.
    The backend loads `.env` automatically. Alternatively, define `SPRING_DATASOURCE_PASSWORD`,
    `COHERE_API_KEY`, and `SLACK_WEBHOOK_URL` in the environment where the backend runs.
    Do not commit `.env`.
3.  **Build and Run:**
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```
    The backend will start on `http://localhost:8080`.

### Frontend Setup

1.  **Navigate to the frontend directory:**
    ```bash
    cd ../frontend
    ```
2.  **Install dependencies:**
    ```bash
    npm install
    # or yarn install
    ```
3.  **Run the React application:**
    ```bash
    npm start
    # or yarn start
    ```
    The frontend will open in your browser at `http://localhost:3000`.

## LLM (Cohere) Setup

1.  **Create a Cohere Account:** Visit [Cohere.ai](https://cohere.ai/) and sign up for a free account.
2.  **Obtain API Key:** Once logged in, navigate to your dashboard or API keys section to find your API key.
3.  **Set `COHERE_API_KEY`:** Add your Cohere API key to the backend `.env` file or environment.

## Slack Integration Setup

1.  **Create a Slack App:**
    * Go to [api.slack.com/apps](https://api.slack.com/apps).
    * Click "Create New App" and choose "From scratch".
    * Give your app a name (e.g., "Todo Summary Bot") and select your Slack workspace.
2.  **Activate Incoming Webhooks:**
    * From your app's settings page, navigate to "Features" -> "Incoming Webhooks".
    * Toggle the "Activate Incoming Webhooks" switch to "On".
    * Scroll down and click the "Add New Webhook to Workspace" button.
    * Select the specific channel where you want the to-do summaries to be posted (e.g., `#general`, `#todos`, or a new channel).
    * Click "Allow".
3.  **Copy Webhook URL:**
    * A unique Webhook URL will be generated. Copy this URL.
4.  **Set `SLACK_WEBHOOK_URL`:** Add this URL to the backend `.env` file or environment.

## Design/Architecture Decisions

* **Separation of Concerns:** The project is cleanly separated into frontend (React) and backend (Spring Boot) directories, allowing independent development and deployment.
* **RESTful API:** The backend exposes standard RESTful endpoints for managing todos, ensuring clear and predictable communication with the frontend.
* **Spring Data JPA:** Leveraged for efficient and simplified database interactions with MySQL, reducing boilerplate code for data access.
* **Service Layer:** Business logic (CRUD operations, LLM calls, Slack calls) is encapsulated within dedicated service classes, promoting modularity and testability.
* **External API Integration:** `OkHttp` was chosen as a lightweight and efficient HTTP client for making external API calls to Cohere and Slack.
* **CORS Configuration:** Explicit CORS configuration in Spring Boot ensures that the React frontend can communicate with the backend.
* **Error Handling:** Basic error handling is implemented on both frontend and backend to provide user feedback and log issues.
* **LLM Prompt Engineering:** A simple prompt is used for Cohere to instruct it on summarizing the list of to-do items. This can be further refined for better results.
* **Notification System:** A simple notification component in React provides immediate feedback to the user about operations.

## Demo Images

![Screenshot (1146)](https://github.com/user-attachments/assets/53fe53e3-b527-4659-9ab6-b462ae034fbd)

![Screenshot (1144)](https://github.com/user-attachments/assets/474b1a46-36c8-4407-8bf9-a46ca911603b)

![Screenshot (1143)](https://github.com/user-attachments/assets/1e9f8783-d0df-42ce-a3f8-ec7ca5e7c078)
