# Shodhai

# Shodhai (ContestHub)

Shodhai is a coding contest platform where users can browse contests, view problems, and submit code for automated evaluation. It consists of a Spring Boot backend and a Next.js frontend.

---

## 🏗️ Project Structure
Shodhai/
├─ backend/ # Spring Boot API + Judge + H2 Database
└─ frontend/ # Next.js Web UI


---

## ⚙️ Backend Setup (Spring Boot)

### Navigate to backend folder
```bash
cd backend
mvn spring-boot:run
http://localhost:8080
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:contestdb
User: sa
Password: (leave blank)
cd frontend
npm install
NEXT_PUBLIC_API=http://localhost:8080
npm run dev
http://localhost:3000

POST http://localhost:8080/api/submissions
{
  "contestId": 100,
  "problemId": 200,
  "userId": 1,
  "sourceCode": "public class Main{public static void main(String[]a){System.out.println(2+2);}}"
}
GET http://localhost:8080/api/submissions/1
