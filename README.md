# Backend App

A Jakarta EE application with Keycloak authentication and authorization integration.

## Overview

This project is a backend application built with Jakarta EE that uses Keycloak for identity and access management. The application is configured to work with a custom Keycloak realm and provides role-based access control.

## Prerequisites

- Java SDK 23
- Maven 3.x
- Docker and Docker Compose (for running Keycloak)

## Project Structure

```
backend-app/
├── keycloak/
│   └── realm-export.json    # Keycloak realm configuration
├── src/                     # Application source code
├── docker-compose.yml       # Docker services configuration
└── pom.xml                 # Maven dependencies
```


## Keycloak Configuration

### Realm Details
- **Realm Name**: `myrealm`
- **Protocol**: OpenID Connect

### Pre-configured Users

| Username | Password | Roles |
|----------|----------|-------|
| demo | password | default-roles-myrealm, my-role, offline_access, uma_authorization |
| igor | password | default-roles-myrealm, offline_access, uma_authorization |

### Client Configuration
- **Client ID**: `frontend-app`
- **Type**: Public client
- **Redirect URIs**: `http://localhost:8080/*`
- **Web Origins**: `http://localhost:8080`

## Getting Started

### 1. Start Keycloak

```shell script
docker-compose up -d
```


### 2. Build the Application

```shell script
./mvnw clean install
```


### 3. Run the Application

```shell script
./mvnw clean package
# Deploy the generated WAR file to your Jakarta EE server
```

## Security

The application uses role-based access control (RBAC) with the following custom role:
- **my-role**: Provides access to frontend and calendar services

## Development

This project uses:
- **Jakarta EE** with jakarta imports
- **Lombok** for reducing boilerplate code
- **Java SDK 23**
