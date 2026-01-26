# SmartFinance Backend

**SmartFinance Backend** – A secure and scalable backend for managing income and expenses.  
Built with **Spring Boot**, **MySQL**, and **Spring Security (JWT Authentication)**, deployed on **Render**.

---

## 🚀 Live Deployment

- Backend is live on Render: [SmartFinance Backend](https://smartfinance-backend-1.onrender.com/)  
*(Replace with your actual Render URL)*

---

## 📌 Key Features

- User Registration and Login with **JWT Authentication**
- Manage Income and Expenses with validation
- Category CRUD (Create, Read, Update, Delete)
- Upload profile pictures to **Cloudinary**
- Download transaction reports as **Excel**
- Send daily email reminders & notifications
- Dashboard analytics with filtering
- Secure and role-based endpoints

---

## 🛠️ Tech Stack

- **Backend Framework:** Spring Boot  
- **Database:** MySQL  
- **Security:** Spring Security with JWT  
- **Build Tool:** Maven  
- **Deployment:** Docker, Render  

---

## ⚙️ Docker Deployment

```dockerfile
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/moneymanger-0.0.1-SNAPSHOT.jar moneymanager-v1.0.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "moneymanager-v1.0.jar"]
```

## ⚙️ Build and run locally
- docker build -t smartfinance-backend .
- docker run -p 9090:9090 smartfinance-backend

## ⚙️ Clone the repo
- git clone https://github.com/TanvirApon/SmartFinance-Backend.git
- cd SmartFinance-Backend

## ⚙️ Build project
- mvn clean package

## ⚙️ Run backend
- java -jar target/moneymanger-0.0.1-SNAPSHOT.jar
