# E-Learning-Management-System

## Introduction

The **E-Learning Management System** is a comprehensive online learning platform that enables users to browse courses, add them to their learning list, and enroll in courses. The application provides functionalities for user authentication, course management, and enrollment management.

## Features

- **User Authentication:** Secure user login and registration.
- **Course Listing:** Browse courses and can buy courses
- **Mail Service:** User can Mail to Academy,After Complete Registation and Payement Send Success Mail.
- **Enrollment Management:** user enrollments in courses.
- **Payment:** User can pay for a course(payhere payment gateway implemented)
- **Admin Panel:** Administrative functionalities for managing courses, users, and enrollments.

## Tech Stack

- **Frontend:** HTML, CSS, JavaScript, Bootstrap
- **Backend:** Spring Boot (Java)
- **Database:** MySQL
- **Build Tool:** Maven
- **Server:** Embedded Tomcat (within Spring Boot)

## Architecture

The application follows a typical layered architecture for Spring Boot web applications:

- **Entity:** Java entities representing the application's data, managed by Spring Data JPA.
- **View:** HTML,CSS,JS using create UI from FrontEnd Module. 
- **Controller:** Spring MVC Controllers handling user requests and interacting with the service layer.
- **Service:** Spring Services containing business logic.
- **Repository:** Spring Data JPA Repositories for database interaction.

## Installation

### Prerequisites

- Java 17 or higher( Java 21 Perfect for this )
- MySQL 5.7 or higher
- Maven 3.6 or higher

- Create Payhere Sandbox Account

- need SMPT SERVERE 

### Steps

1. **Clone the repository:**

   ```bash
   git clone [https://github.com/manuthkausilu/your-LMS-Repository-URL.git](https://github.com/manuthkausilu/your-LMS-Repository-URL.git)
   cd your-LMS-repository-name
