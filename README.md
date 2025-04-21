# E-Learning Management System

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Open Source Love](https://badges.frapsoft.com/os/v1/open-source.svg?v=103)](https://opensource.org/)

## Introduction

The **E-Learning Management System** is a fully featured online learning environment that allows users to explore a variety of courses, create their own learning collections, and enroll with ease. It's equipped with secure user login systems, effective course management tools, streamlined enrollment processes, and built-in payment capabilities.

## Features

* **User Authentication:** Secure user login and registration functionalities to protect user accounts.
* **Course Listing & Browsing:** Intuitive interface for users to browse the available course catalog and view course details.
* **Course Enrollment:** Enables users to easily add courses to their learning list and complete the enrollment process.
* **Payment Integration:** Implemented with the PayHere payment gateway, allowing users to securely pay for courses.
* **Mail Service:** Integrated email functionality to send automated notifications to users upon successful registration and course payment. Also includes a contact form for users to mail inquiries to the academy.
* **Admin Panel:** A dedicated administrative interface providing functionalities for managing courses, users, and enrollments, ensuring platform control and oversight.

## Tech Stack

* **Frontend:** HTML, CSS, JavaScript, Bootstrap (for responsive and consistent UI)
* **Backend:** Spring Boot (Java) - A powerful framework for building scalable and robust backend applications.
* **Database:** MySQL - A reliable relational database for persistent data storage.
* **Build Tool:** Maven - For efficient project building and dependency management.
* **Server:** Embedded Tomcat (within Spring Boot) - For easy deployment and running of the application.
* **Payment Gateway:** PayHere - Integrated for secure online payment processing.
* **Mail Service:** Utilizes an SMTP server for sending email notifications.

## Architecture

The application adheres to a well-defined layered architecture, typical for Spring Boot web applications, ensuring separation of concerns and maintainability:

* **Entity:** Java entities representing the application's data models, managed seamlessly by Spring Data JPA for database interactions.
* **View:** User interface components built with HTML, CSS, and JavaScript, leveraging Bootstrap for styling and responsiveness, created within the FrontEnd Module.
* **Controller:** Spring MVC Controllers responsible for handling incoming user requests and orchestrating interactions with the underlying service layer.
* **Service:** Spring Services containing the core business logic of the application, performing operations on the data.
* **Repository:** Spring Data JPA Repositories providing a simplified and efficient way to interact with the MySQL database.

## Installation

### Prerequisites

Ensure the following software is installed on your system:

* **Java:** Version 17 or higher is required (Java 21 is highly recommended for optimal performance and features).
* **MySQL:** Version 5.7 or higher.
* **Maven:** Version 3.6 or higher.
* **PayHere Sandbox Account:** You will need to create a sandbox account on PayHere to test the payment gateway.
* **SMTP Server:** You will need access to an SMTP server and its credentials to enable the email service.

### Steps

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/manuthkausilu/your-LMS-Repository-URL.git](https://github.com/manuthkausilu/your-LMS-Repository-URL.git)
    cd your-LMS-repository-name
    ```

2.  **Configure MySQL:**
    * Ensure your MySQL server is running.
    * Create a database for the application (e.g., `sekaiacademy`).
    * Update the database connection properties in the `src/main/resources/application.properties` file. This will include the database URL, username, and password.

3.  **Configure PayHere Sandbox:**
    * Obtain your sandbox Merchant ID and Secret Key from your PayHere sandbox account.
    * Update the PayHere configuration properties in the `src/main/resources/application.properties` file.

4.  **Configure SMTP Server:**
    * Provide the details of your SMTP server (host, port, username, password, sender email address) in the `src/main/resources/application.properties` file.

5.  **Build the backend:**
    ```bash
    mvn clean install
    ```

6.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```
    The backend server will start on the embedded Tomcat server (default port is usually 8080).

7.  **Access the frontend:**
    * Open your web browser and navigate to the application URL (e.g., `http://localhost:8080`). The frontend UI, built with HTML, CSS, and JavaScript, will interact with the backend API.

8.  **Use ngrok (for PayHere testing):**
    * Install ngrok.
    * Run this command in your terminal:
        ```bash
        ngrok http 8080
        ```
    * ngrok will give you a public URL (like `https://e09a-2402-4000-b188-9e85-cd22-f788-c9c3-c003.ngrok-free.app`).
    * Copy this URL.
    * In your course and index.html files, find the PayHere form and put the ngrok URL into the "notify_url" field. This is important for PayHere to send payment confirmations to your website.

## Usage

### Browsing Courses

* Navigate to the home page or the "Courses" section to view the available courses.
* Click on a course to see more details.

### Adding to Learning List & Enrollment

* Users can typically have a feature to add courses to a "learning list" or directly proceed to "enroll now."
* Follow the on-screen instructions to enroll in a selected course.

### Payment

* Upon enrollment in a paid course, users will be redirected to the PayHere payment gateway.
* Follow the PayHere interface to securely complete the payment process using supported payment methods.
* Upon successful payment, users will receive a confirmation email and access to the course.

### Contacting the Academy

* Navigate to the "Contact" page.
* Fill out the contact form with your name, email, and message.
* Submit the form to send an email to the academy's designated email address.

### Admin Panel

* Access the admin panel by logging in with administrator credentials.
* The admin panel provides interfaces for managing:
    * **Courses:** Create, read, update, and delete courses.
    * **Modules:** Create, read, update, and delete modules.
    * **Videos:** add, preview, update, and delete videos.
    * **Users:** View and manage user accounts.
    * **Enrollments:** Track and manage user enrollments in courses.

## Link to the Demo Video

[https://youtu.be/3wzkZrCqbwE?si=ZZFKVeQxtcYlVbFB(me](https://youtu.be/3wzkZrCqbwE?si=ZZFKVeQxtcYlVbFB(me)

## Contributing

[If you want others to contribute, add guidelines here. For example, how to fork the repository, create pull requests, coding standards, etc. If not, you can state that contributions are currently not being accepted.]

## License

This project is licensed under the [MIT](https://opensource.org/licenses/MIT) License.

## Contact

[Manuth Kausilu] - [manuthkausilu20031018@gmail.com] - [https://github.com/manuthkausilu]

---

**Thank you for exploring the E-Learning Management System!**
