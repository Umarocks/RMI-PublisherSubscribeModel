# Currency Trading System [Indirect Communication]

## University of Houston-Clear Lake  
**College of Science and Engineering**  
**Advanced Operating Systems**  
**Author: Umar Abdul Aziz (2310655)**  

---

## Table of Contents
- [Introduction](#introduction)
- [System Overview](#system-overview)
- [Data Structures and Algorithms](#data-structures-and-algorithms)
- [Problem Solving - Dealing Room Server](#problem-solving---dealing-room-server)
- [Problem Solving - Client](#problem-solving---client)
- [System Architecture](#system-architecture)
- [Screenshots](#screenshots)
- [Conclusion](#conclusion)
- [How to Run](#how-to-run)
- [Future Enhancements](#future-enhancements)
- [License](#license)
- [Author](#author)

---

## Introduction
The world of cryptocurrency trading is dynamic and requires real-time information sharing. This project implements a **Publish-Subscribe Cryptocurrency Dealing Room System** using Java to solve the problem of indirect communication in distributed trading systems. The system enables multiple users to **publish** and **subscribe** to cryptocurrency updates in real time, leveraging Java’s advanced APIs for distributed systems.

### Key Features:
- **Publish-Subscribe Communication Model**
- **Real-time Cryptocurrency Updates**
- **Centralized Login System** for security
- **GUI for User-Friendliness**
- **Multi-threaded Architecture** for handling concurrent users

---

## System Overview
The system is based on three main components:
1. **Publisher** - Provides cryptocurrency updates.
2. **Subscriber** - Receives real-time updates on chosen cryptocurrencies.
3. **Dealing Room Server** - Acts as a central bulletin board for message distribution.

The Dealing Room Server ensures that **publishers post updates**, and **subscribers receive only the relevant updates**, allowing for **efficient, real-time, and decoupled communication** in a trading environment.

---

## Data Structures and Algorithms
### Key Data Structures Used:
- **HashMap** - Stores user credentials, subscriptions, and cryptocurrency updates.
- **ConcurrentHashMap** - Ensures thread-safe access to active connections.
- **ArrayList** - Maintains dynamic lists of articles and subscribed users.
- **Set** - Ensures uniqueness in subscriptions and prevents duplicates.

These data structures provide **fast lookups**, **scalability**, and **efficient handling of dynamic user actions**.

---

## Problem Solving - Dealing Room Server
### Challenges and Solutions:
1. **Handling Real-time Crypto Updates:**
   - Solution: A `HashMap<String, ArrayList<CryptoObject>>` structure stores updates and distributes them efficiently.
2. **Managing User Subscriptions:**
   - Solution: A `SubscriptionList` (`HashMap<String, Set<String>>`) maps topics to users, allowing easy addition/removal of subscribers.
3. **Efficient Client Communication:**
   - Solution: A `clientOutputStreams` map keeps track of active connections for instant message delivery.
4. **Persisting Data for Recovery:**
   - Solution: Subscription lists and crypto articles are periodically saved to files to ensure resilience against server restarts.

---

## Problem Solving - Client
### Key Functionalities:
1. **User Authentication:** Secure login system differentiates between publishers and subscribers.
2. **Crypto Article Publishing:** Publishers can input new cryptocurrency news updates.
3. **Subscription Management:** Subscribers can dynamically subscribe/unsubscribe to topics.
4. **GUI Interface:** A user-friendly UI simplifies interactions with the system.
5. **Error Handling:** Supports network reconnections and informs users of connection issues.

---

## System Architecture
[Insert System Architecture Diagram Here]

---

## Screenshots
- **Dealing Room Server Running**
- **User Login Interface**
- **Publisher Dashboard**
- **Subscriber Dashboard**
- **Real-time Article Updates**
[Insert Screenshots Here]

---

## Conclusion
This project successfully implements a **real-time, scalable cryptocurrency trading system** using the **publish-subscribe model**. The **Dealing Room Server** efficiently manages user subscriptions and article distribution, while the **client-side UI** allows for seamless interaction.

Future improvements could include **enhanced security features**, **automatic failover mechanisms**, and **support for additional financial data sources**.

---

## How to Run
### Prerequisites
- **Java 8+** installed
- A **networked environment** for distributed communication

### Steps
1. **Start the Dealing Room Server:**
```sh
java DealingRoomServer
```
2. **Start a CryptoPublisherClient:**
```sh
java CryptoPublisherClient
```
3. **Login/Register** and begin publishing/subscribing to cryptocurrency updates.

---

## Future Enhancements
- **Security Enhancements:** Implement encryption for secure communication.
- **Scalability Improvements:** Optimize message delivery for larger user bases.
- **Advanced Analytics:** Introduce historical trend tracking for better decision-making.

---

## More Info
**For more information on the project, refer to code documentation report**

---

## Author
**Umar Abdul Aziz**

