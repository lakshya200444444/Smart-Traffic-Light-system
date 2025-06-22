Traffic Management System

This project is a comprehensive Traffic Management System built using Kotlin Multiplatform, Ktor, and Python. It provides a desktop application for monitoring and managing traffic, a server for handling data and requests, a shared module for common code, a detection module for analyzing video feeds, and documentation.

The project is structured into several key components:

*   **`/composeApp`**: This module contains the Compose Multiplatform desktop application. It provides the user interface for interacting with the traffic management system, displaying real-time data, analytics, and controls.

*   **`/server`**: This module houses the Ktor server application. The server acts as the backend for the system, handling requests from the desktop application, processing data, and potentially communicating with the detection module. It is responsible for managing system status, network information, and other relevant data.

*   **`/shared`**: This module contains code that is shared between the `composeApp` and `server` modules. This includes common data models (e.g., `LatencyPing`, `ModelSystemStatus`, `NetworkInfo`), constants, and potentially utility functions used by both the frontend and the backend.

*   **`/detection`**: This module contains the Python code responsible for vehicle detection. It utilizes models like YOLO (You Only Look Once) to analyze video streams and identify vehicles. This module is designed to process video data and likely sends relevant information (e.g., vehicle counts, traffic flow) to the server.

*   **`/docs`**: This directory contains the project's documentation, including HTML files that likely describe the system's features, provide a demo, and offer information about the project.

## Project Structure Overview

The project leverages the benefits of Kotlin Multiplatform to share code between the desktop application and the server. The `/shared` module facilitates this code sharing. The `/composeApp` provides the user-facing interface, communicating with the `/server` to retrieve and display traffic data. The `/server` acts as the central hub, potentially receiving data from the `/detection` module, which performs the actual vehicle analysis on video feeds. The `/docs` provide necessary information for understanding and using the system.

## Getting Started

To get started with the project, you will need to set up the different components. More detailed instructions for building and running each module can be found within their respective directories.

## Contributing

We welcome contributions to the Traffic Management System. Please refer to the contribution guidelines (if available) for more information.