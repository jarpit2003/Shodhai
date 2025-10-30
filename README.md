# 🏆 Shodhai (ContestHub)

A full-stack coding contest platform where users can browse programming contests, view problems, and submit code for automated evaluation.

## 📋 Table of Contents
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Prerequisites](#-prerequisites)
- [Installation & Setup](#-installation--setup)
- [API Usage](#-api-usage)
- [Contributing](#-contributing)
- [License](#-license)

## ✨ Features

- 🎯 Browse and participate in coding contests
- 📝 View problem statements with test cases
- 💻 Submit code solutions in multiple languages
- ⚡ Automated code evaluation and judging
- 📊 Real-time submission results
- 🗄️ In-memory H2 database for development

## 🛠️ Tech Stack

**Backend:**
- Spring Boot (Java)
- H2 Database (In-Memory)
- Maven
- RESTful API

**Frontend:**
- Next.js (React)
- JavaScript/TypeScript
- TailwindCSS (optional)

## 📁 Project Structure

```
Shodhai/
├── backend/              # Spring Boot API + Judge + H2 Database
│   ├── src/
│   ├── pom.xml
│   └── ...
└── frontend/             # Next.js Web UI
    ├── src/
    ├── public/
    ├── package.json
    └── ...
```

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

- **Java 17+** ([Download](https://www.oracle.com/java/technologies/downloads/))
- **Maven 3.6+** ([Download](https://maven.apache.org/download.cgi))
- **Node.js 18+** ([Download](https://nodejs.org/))
- **npm** or **yarn**

## 🚀 Installation & Setup

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/yourusername/shodhai.git
cd shodhai
```

### 2️⃣ Backend Setup (Spring Boot)

Navigate to the backend directory and start the Spring Boot application:

```bash
cd backend
mvn spring-boot:run
```

The backend API will be available at: **http://localhost:8080**

#### 🗄️ Access H2 Database Console

Navigate to: **http://localhost:8080/h2-console**

Use the following credentials:
- **JDBC URL:** `jdbc:h2:mem:contestdb`
- **Username:** `sa`
- **Password:** *(leave blank)*

### 3️⃣ Frontend Setup (Next.js)

Open a new terminal, navigate to the frontend directory, and install dependencies:

```bash
cd frontend
npm install
```

Create a `.env.local` file in the frontend directory:

```env
NEXT_PUBLIC_API_URL=http://localhost:8080
```

Start the development server:

```bash
npm run dev
```

The frontend will be available at: **http://localhost:3000**

## 🔌 API Usage

### Submit a Solution

**Endpoint:** `POST /api/submissions`

**Request Body:**
```json
{
  "contestId": 100,
  "problemId": 200,
  "userId": 1,
  "sourceCode": "public class Main{public static void main(String[]a){System.out.println(2+2);}}"
}
```

**Example using cURL:**
```bash
curl -X POST http://localhost:8080/api/submissions \
  -H "Content-Type: application/json" \
  -d '{
    "contestId": 100,
    "problemId": 200,
    "userId": 1,
    "sourceCode": "public class Main{public static void main(String[]a){System.out.println(2+2);}}"
  }'
```

### Get Submission Result

**Endpoint:** `GET /api/submissions/{submissionId}`

**Example:**
```bash
curl http://localhost:8080/api/submissions/1
```

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a new branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 📞 Contact

For questions or support, please open an issue on GitHub.

**Happy Coding! 🚀**
