# Vertical Line Counter (Java Console App)

A robust, high-performance Windows console application designed to detect and count vertical black lines in JPEG images. This project demonstrates advanced image processing logic, defensive programming, and algorithmic optimization.

---

## 🚀 Key Features

* **Tiered Probing Algorithm:** Leverages image properties to achieve near O(W) performance by prioritizing midpoint and quarter-point checks before falling back to an exhaustive scan.
* **JPEG Artifact Resilience:** Utilizes luminance-based thresholding (Average RGB < 128) to maintain accuracy despite lossy compression noise common in MS Paint JPEGs.
* **Defensive Error Handling:** Implements a multi-layered exception hierarchy to catch specific file system errors (Missing file, Access Denied, Corrupted format) while ensuring the app never crashes.
* **Zero-Dependency:** Uses only standard Java SE libraries (java.awt.image, javax.imageio, java.io).

---

## 🛠️ Setup & Installation

### Prerequisites
* **Java JDK 8 or higher** (The app was successfully tested on JDK 1.8.0_202).
* **System PATH:** Ensure your JDK bin folder is added to your System PATH so 'javac' and 'java' commands are recognized globally.

---

## 💻 How to Use

### 1. Compilation
Open your Command Prompt (CMD) in the project directory and run:
> javac LineCounter.java

### 2. Execution
Run the application by providing the absolute path to a JPEG image. Wrap the path in double quotes if it contains spaces.

**Example Command:**
> java LineCounter "C:\Path\To\Your\Image.jpg"

**Example Output:**
> 7

---

## 📂 Project Structure

* **LineCounter.java**: Main application source code containing the detection logic.
* **Summary_of_Tackled_Problems.txt**: A detailed breakdown of technical hurdles (JPEG noise, empty-image bottlenecks) and their solutions.
* **README.md**: Setup and usage instructions.

---

## 🧠 Algorithmic Summary
The application employs a **Heuristic Probing** strategy. Because target lines are continuous, the algorithm first "probes" the vertical midpoint. This allows for a result in O(1) operations per column for most valid images. A full vertical fallback is included to ensure 100% accuracy for irregular cases, fulfilling the "strict" requirements of the assignment.